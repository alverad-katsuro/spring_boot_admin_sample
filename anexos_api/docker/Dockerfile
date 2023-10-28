FROM eclipse-temurin:20.0.2_9-jdk-jammy AS builder

WORKDIR /app

COPY . .

RUN ./mvnw install

FROM eclipse-temurin:20.0.2_9-jre-jammy AS runner

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ARG profile

ENTRYPOINT java -jar -Dspring.profiles.active=$profile app.jar
