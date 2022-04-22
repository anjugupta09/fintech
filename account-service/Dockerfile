FROM openjdk:15
ADD build/libs/account-service.jar account-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "account-service.jar"]