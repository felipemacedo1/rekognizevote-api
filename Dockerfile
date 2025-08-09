# Multi-stage build for optimized production image
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app
COPY pom.xml ./
COPY src/ src/

RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
COPY --chown=appuser:appgroup --from=builder /app/build/libs/*.jar app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]