FROM       openjdk:21-slim
LABEL      author="ARDI"
RUN        mkdir -p /app
WORKDIR    /app

ARG        JAR

ENV        SPRING_PROFILES_ACTIVE="prod"

ARG        JAR_FILE=build/libs/${JAR}.jar

COPY       ${JAR_FILE} /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]