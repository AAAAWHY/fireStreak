FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /streak-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests


FROM eclipse-temurin:25-jre
RUN groupadd --system streak-service-group && useradd --system --gid streak-service-group streak-service-user
WORKDIR /streak-service
COPY --from=build /streak-service/target/*.jar streak-service.jar
USER streak-service-user
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "streak-service.jar"]
