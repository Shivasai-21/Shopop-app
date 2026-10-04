# ---- build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY pom.xml .
RUN mvn -q dependency:go-offline          # cached layer unless pom changes
COPY src ./src
RUN mvn -q package -DskipTests

# ---- runtime stage (small, non-root) ----
FROM eclipse-temurin:17-jre
RUN useradd -r -u 1001 app
USER 1001
WORKDIR /app
COPY --from=build /src/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","app.jar"]
