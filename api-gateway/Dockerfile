FROM eclipse-temurin:17-jre

WORKDIR /app
COPY build/libs/api-gateway*.jar app.jar

ENV SERVER_PORT=8086
EXPOSE 8086

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Dserver.port=${SERVER_PORT}", \
  "-jar", "app.jar"]