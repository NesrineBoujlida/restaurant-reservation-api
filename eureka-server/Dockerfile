FROM eclipse-temurin:17-alpine
EXPOSE 8761
ADD target/*.jar eureka-server.jar
ENTRYPOINT ["java","-jar","eureka-server.jar"]