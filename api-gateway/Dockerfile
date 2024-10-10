FROM eclipse-temurin:17-alpine
EXPOSE 8080
ADD target/*.jar api-gateway.jar
ENTRYPOINT ["java","-jar","/api-gateway.jar"]