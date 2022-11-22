FROM openjdk:17-alpine as builder
WORKDIR /app
COPY . .
RUN ./gradlew build --stacktrace

FROM openjdk:17-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/build/libs/rectle-0.0.1.jar .
CMD java -jar rectle-0.0.1.jar