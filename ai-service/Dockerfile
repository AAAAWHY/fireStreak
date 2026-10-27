FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /ai-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests


FROM eclipse-temurin:25-jre
RUN groupadd --system ai-service-group && useradd --system --gid ai-service-group ai-service-user
WORKDIR /ai-service
COPY --from=build /ai-service/target/*.jar ai-service.jar
USER ai-service-user
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "ai-service.jar"]
