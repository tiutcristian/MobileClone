FROM openjdk:17
ADD target/mobile-clone-0.0.1-SNAPSHOT.jar mobile-clone-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mobile-clone-0.0.1-SNAPSHOT.jar"]