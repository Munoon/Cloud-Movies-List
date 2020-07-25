# Cloud Movies List
Spring Cloud learning project.
Service, provided movies list with ability for users to add movies to favorites or watch later and mark movies.

## Used technologies
Java (11 and 8), Maven, 
Spring Boot, Spring Security, Spring Cloud, Spring Actuator, Spring Cloud Config, Spring Data JPA,
Rest API, oAuth 2, oAuth Authorization Server, oAuth Resource Server, 
Netflix Eureka, Netflix Zuul, Hibernate, JUnit 5, RabbitMQ, thymeleaf,
Postgres, JWT,
JavaScript (including ES6), Node JS, npm, Webpack, React, React Hook Form, SWR, Bootstrap (and Bootswatch), babel, sass.

## Microservices
Launch order | Name | Package | Description
------------ | ---- | ------- | -----------
1 | Config service | config-service | Storage all config and provide it to other microservices.
2 | Eureka service | eureka-service | Giving ability for microservices to communicate with each other.
3 | User resource service | user-resource-service | Storage all info about users and provide API to make CRUD operations with them.
4 | Authorization service | auth-service | Giving ability for users to login.
5 | Gateway service  | gateway-service | Service for clients that giving access to communicate with other microservice.

## How to launch
1. For launching, you need Java 11, Maven, Redis and Postgres.
2. Create `movies-users` database in Postgres.
3. Launch Postgres and Redis.
4. Execute `mvn package` in project root folder.
5. Setup configuration in `config` folder, if needed.
6. Launch each microservice in order, that showing in Microservices table using command `java -jar ${jar-name}`. Each jar locate in `${microservice-package}/target` folder.
7. You may open website using url `localhost:8080`.