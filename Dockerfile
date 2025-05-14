FROM openjdk:17
ADD ./mobile-clone-0.0.1-SNAPSHOT.jar mobile-clone-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "mobile-clone-0.0.1-SNAPSHOT.jar"]