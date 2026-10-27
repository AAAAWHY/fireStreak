FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /gateway
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests


FROM eclipse-temurin:25-jre
RUN groupadd --system gateway-group && useradd --system --gid gateway-group gateway-user
WORKDIR /gateway
COPY --from=build /gateway/target/*.jar gateway.jar
USER gateway-user
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway.jar"]
