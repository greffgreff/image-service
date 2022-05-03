FROM openjdk:17
ADD target/imageservice.jar imageservice.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/imageservice.jar"]