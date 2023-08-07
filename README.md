# Shorty
## URL Shortener - Java Spring Boot Application

### How to build the application with Maven
* install Maven to your operating system if you don't have it
* go to the main project folder ("shorty")
* run "mvn clean install" in the terminal to generate the "target" folder with the .war file
* go to the newly generated folder "target"
* run "java -jar shorty.war" in the terminal to start the application

### Maven Dependencies
* Spring Web (spring-boot-starter-web)
* Spring Data JPA (spring-boot-starter-data-jpa)
* PostgreSQL (postgresql)
* Lombok (lombok)
* AssertJ (assertj-core)
* Hamcrest (hamcrest)
* Junit Jupiter (junit-jupiter)
* Mockito (mockito-core)
* H2 (h2)

### Database
PostgreSQL - local server (Windows machine)\
Port: 5432 (default)\
Database: dev\
Username: postgres\
Password: postgres

### Tested with
* Java Development Kit 17
* Spring Boot 3.1.1
* PostgreSQL 15