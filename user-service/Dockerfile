FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /user-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests


FROM eclipse-temurin:25-jre
RUN groupadd --system user-service-group && useradd --system --gid user-service-group user-service-user
WORKDIR /user-service
COPY --from=build /user-service/target/*.jar user-service.jar
USER user-service-user
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "user-service.jar"]
