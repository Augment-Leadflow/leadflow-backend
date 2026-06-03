FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B --no-transfer-progress

COPY src ./src
RUN mvn clean package -DskipTests -B --no-transfer-progress
RUN cp target/*.jar app.jar && rm -f target/*.jar.original

FROM eclipse-temurin:17-jre-alpine AS runtime

RUN addgroup -S leadflow && adduser -S leadflow -G leadflow

WORKDIR /app

COPY --from=builder /app/app.jar app.jar
RUN chown leadflow:leadflow app.jar

ENV MONGO_URL=${MONGO_URL}
ENV REDIS_HOST=${REDIS_HOST}
ENV REDIS_PORT=${REDIS_PORT}
ENV JWT_SECRET_TOKEN=${JWT_SECRET_TOKEN}
ENV TELEGRAM_BOT_TOKEN=${TELEGRAM_BOT_TOKEN}
ENV TELEGRAM_CHAT_ID=${TELEGRAM_CHAT_ID}
ENV MAIL_USERNAME=${MAIL_USERNAME}
ENV MAIL_PASSWORD=${MAIL_PASSWORD}
ENV FRONTEND_URL=${FRONTEND_URL}
ENV twilio_account_sid=${twilio.account.sid}
ENV twilio_auth_token=${twilio.auth.token}
ENV twilio_whatsapp_number=${twilio.whatsapp.number}

USER leadflow

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=45s --retries=3 \
  CMD wget -qO- http://localhost:8080/api/health || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
