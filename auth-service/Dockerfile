FROM eclipse-temurin:17-jre

WORKDIR /app
COPY build/libs/auth-service*.jar app.jar

ENV SERVER_PORT=8085
EXPOSE 8085

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Dserver.port=${SERVER_PORT}", \
  "-jar", "app.jar"]