FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn -DskipTests package

FROM eclipse-temurin:17-alpine
WORKDIR /app

COPY --from=build /app/target/app.jar /app/app.jar

EXPOSE 7007
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
