# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

fireStreak is a gamified habit/task tracker, built as a set of independent Spring Boot microservices (no shared parent pom — each service has its own `pom.xml` and Maven wrapper). Users complete tasks, build streaks, earn in-game currency (Flames/Purrs), and spend it on collectible cats via loot boxes. An LLM-based "lie detector" is meant to verify task completions, and push notifications are meant to drive re-engagement.

Services (each is a standalone Maven project under its own directory, Java 25, Spring Boot 3.x):

| Service | Port | Responsibility |
|---|---|---|
| `gateway` | 8080 | Spring Cloud Gateway (WebFlux). Single public entry point, JWT auth (JJWT) on all routes except `/api/users/registration`, `/api/users/login`. |
| `user-service` | 8081 | Registration/login, password hashing, JWT issuance. |
| `task-service` | 8082 | Task CRUD and completion (BOOLEAN and QUANTITATIVE task types, `frequencyDays`). |
| `streak-service` | 8083 | Streak calculation, nightly cron rollover, freeze mechanic. |
| `flame-service` | (no fixed port found in config) | Game economy: wallets, cats, loot boxes, upgrades. |
| `notification-service` | (no fixed port found in config) | Firebase Cloud Messaging push. **Not wired into the event flow — this is a deliberate scope decision, not an oversight**: manual `/api/push/send` only. |
| `ai-service` | (no fixed port found in config) | Spring AI + OpenAI-compatible client (currently pointed at Groq) for LLM-based task-completion verification. Not yet integrated into the completion flow. |

## Commands

Each service is built/run independently with its bundled Maven wrapper from inside that service's directory:

```bash
# from inside e.g. task-service/
./mvnw clean install       # build
./mvnw test                 # run all tests
./mvnw test -Dtest=ClassName#methodName   # run a single test
./mvnw spring-boot:run       # run the service locally
```

There is no root-level build — you must `cd` into each service directory individually. Integration tests (`flame-service`, `user-service`) use Testcontainers (MySQL/MariaDB, RabbitMQ), so Docker must be running for `./mvnw test` to pass there.

Infra dependencies (RabbitMQ, MariaDB/MySQL) are expected to run locally (`localhost:5672`, `localhost:3306`) — there is no `docker-compose.yml` in the repo to bring these up; they must be started manually or via your own compose file.

## Architecture / data flow

Services communicate two ways:
- **Synchronous REST**, routed through `gateway` (only `user-service`, `task-service`, `streak-service` currently have routes configured in `gateway/src/main/resources/application.yaml` — `flame-service`, `notification-service`, `ai-service` have no gateway route yet).
- **Asynchronous events via RabbitMQ** (Spring AMQP) for cross-service side effects:

```
user-service  --CreateWalletEvent-->        flame-service   (provisions wallet on registration)
task-service  --TaskCompletedEvent-->       streak-service  (updates TaskStreak/UserStreak on completion)
streak-service (nightly cron, DailySchedular)
              --DailyStreakEvent (fanout)--> flame-service   (credits Flames/Purrs based on streak length)
```

- `streak-service`'s `DailySchedular` runs nightly (`0 15 0 * * *`), finalizes/expires streaks for the day, then broadcasts one `DailyStreakEvent` per user over a **fanout** exchange (`daily-exchange`).
- Loot box / cat upgrade spending in `flame-service` is synchronous REST only, no messaging.
- `ai-service` (LLM verification) and `notification-service` (push) are not yet consumers of any of these events — they exist as standalone services not currently in the completion/streak pipeline.

Each of `flame-service`, `streak-service`, `task-service`, `user-service` has its own `HeaderAuthFilter` that trusts `X-user-id`/`X-role` headers on incoming requests without verifying they actually originated from the gateway (see Known issues below) — this is the internal-auth model to be aware of when touching request handling in these services.

## Known issues / security findings

These were identified during a code review pass and are tracked here so future work (by Claude or otherwise) doesn't reintroduce them or waste time rediscovering them.

**Trust boundary between services (high priority):**
- `HeaderAuthFilter` in flame/streak/task/user-service trusts `X-user-id`/`X-role` headers verbatim, with no cryptographic verification that they came from the gateway. If any internal service becomes reachable directly (compromise, misconfigured port mapping, SSRF from another service), this is a full auth bypass — anyone can impersonate any user.
- Planned fix direction (discussed, not yet implemented): switch JWT signing from HS256 (shared secret) to RS256 (asymmetric) — issuer keeps the private key, all services get only the public key and verify the JWT themselves instead of trusting a bare header.

**Secrets handling:**
- `ai-service/src/main/resources/application.yaml` currently has a **hardcoded live Groq API key** in `spring.ai.openai.api-key`. Not yet committed to git (untracked file) as of this writing, but must be moved to an env var (`${GROQ_API_KEY}`) before any commit, and the key should be rotated as a precaution.
- `notification-service/src/main/java/.../config/FirebaseConfig.java` hardcodes an absolute local filesystem path to a Firebase service-account JSON — not portable, and implies the credential file itself may not be properly externalized.
- `notification-service`'s `/api/push/**` endpoints are `permitAll()` — no auth required to trigger a push to an arbitrary device token.
- `user-service`/`gateway` use two different Base64 libraries for JWT handling (`org.bouncycastle` vs `java.util.Base64`) — inconsistent, unnecessary extra dependency.

**Concurrency / transactions:**
- No `@Transactional` usage anywhere in the codebase. `flame-service`'s wallet/balance mutations (`FlameService.upgradeCat`, `LootBoxService` purchases) do read-then-write balance checks with no locking — concurrent requests can double-spend currency.
- `streak-service`'s `StreakService.handleCompletedTask` does check-then-act (`existsById` then `save`) without a transaction boundary — duplicate/concurrent `TaskCompletedEvent`s for the same task could corrupt streak counts.
- No idempotency/dedup handling for consumed RabbitMQ events (`TaskCompletedEvent`, `DailyStreakEvent`) — a redelivery (normal RabbitMQ behavior on retry) can double-process an event.

**Error handling:**
- Several places use `Optional.get()` instead of `orElseThrow(...)` (e.g. `flame-service`'s `FlameService`, `LootBoxService`), which surfaces raw `NoSuchElementException` as an unhandled 500.
- `task-service`'s global exception handler returns `e.getMessage()` directly to clients, risking leaking internal details.
- None of the services have a catch-all fallback `@ExceptionHandler(Exception.class)` — only explicitly-typed exceptions are handled.

**Testing:**
- Meaningful test coverage exists only in `user-service` (Mockito unit tests) and `flame-service` (Testcontainers integration test for the RabbitMQ flow). `task-service`, `ai-service`, `notification-service`, `gateway`, `streak-service` currently only have empty `contextLoads()` stub tests — no business-logic coverage.

**Other/WIP indicators:**
- `ai-service`'s REST controller (`controller.java`) is an empty stub; the actual verification logic (`chatService.askVerificationModel`) is only exercised via a `@PostConstruct` call that fires a real LLM request on every app startup — should be removed or guarded before this service is developed further.
- No Eureka server module exists despite `spring-cloud-starter-netflix-eureka-client` being a dependency in several services (and `eureka.client.enabled: false` set in most `application.yaml`s) — service discovery is effectively unused right now; don't assume it's wired up.
- Naming is inconsistent across services and worth normalizing if touched: `RabbitMqConfig` vs `RabbitMqConfiguration`, `Controller.java` vs `TaskController`/`UserController`, `controller.java` (lowercase) in ai-service, `schedular` (typo for "scheduler") in streak-service.
