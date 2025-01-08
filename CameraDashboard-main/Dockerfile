#
# Build stage
#
FROM maven:3.6.0-jdk-8 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
COPY --from=build /target/virtualcam-0.0.1-SNAPSHOT.jar virtualcam.jar
# ENV PORT=8888
EXPOSE 8888
ENTRYPOINT ["java","-jar","virtualcam.jar"]