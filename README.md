# Shorty
## URL Shortener - Java Spring Boot Application

### Technologies used
* Spring Boot (Java)
* Keycloak - authentication
* PostgreSQL - database
* H2 - database (testing only)

### How to build the application with Maven
* install Maven to your operating system if you don't have it
* go to your Keycloak bin folder (e.g. keycloak-22.0.1/bin)
* run the Keycloak server on port 8081 with "kc.bat start-dev --http-port=8081"
* go to the main project folder ("shorty")
* run "mvn clean install" in the terminal to generate the "target" folder with the .war file
* go to the newly generated "shorty/restapi/target" folder
* run "java -jar restapi.war" in the terminal to start the application ("restapi" is the main module)

### Keycloak - configuration
Keycloak - local server (Windows machine)\
Port: 8081\
Realm: shorty\
Client ID: shorty-rest-api

### Database - configuration
PostgreSQL - local server (Windows machine)\
Port: 5432 (default)\
Database: dev\
Username: postgres\
Password: postgres

### Tested with
* Java Development Kit 17
* Spring Boot 3.1.1
* PostgreSQL 15
* Keycloak 22.0.1

### Maven Dependencies
* Spring Web (spring-boot-starter-web)
* Webflux
* Spring Data JPA (spring-boot-starter-data-jpa)
* Tomcat (spring-boot-starter-tomcat)
* PostgreSQL
* Lombok
* AssertJ
* Hamcrest
* Junit Jupiter
* Mockito
* H2
* Thymeleaf (spring-boot-starter-thymeleaf)
* Log4J
* Security (spring-boot-starter-security)
* OAuth2 Resource Server (spring-boot-starter-oauth2-resource-server)
* GSON