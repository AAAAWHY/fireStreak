FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /task-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests


FROM eclipse-temurin:25-jre
RUN groupadd --system task-service-group && useradd --system --gid task-service-group task-service-user
WORKDIR /task-service
COPY --from=build /task-service/target/*.jar task-service.jar
USER task-service-user
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "task-service.jar"]
