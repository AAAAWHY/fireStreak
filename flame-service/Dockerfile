FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /flame-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests


FROM eclipse-temurin:25-jre
RUN groupadd --system flame-service-group && useradd --system --gid flame-service-group flame-service-user
WORKDIR /flame-service
COPY --from=build /flame-service/target/*.jar flame-service.jar
USER flame-service-user
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "flame-service.jar"]
