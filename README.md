# Cloud Movies List
[![Java Maven Test](https://github.com/Munoon/Cloud-Movies-List/workflows/Java%20Maven%20Test/badge.svg)](https://github.com/Munoon/Cloud-Movies-List/actions?query=workflow%3A%22Java+Maven+Test%22)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bc79bad27f4246948060e7d7df6066bb)](https://app.codacy.com/manual/Munoon/Cloud-Movies-List?utm_source=github.com&utm_medium=referral&utm_content=Munoon/Cloud-Movies-List&utm_campaign=Badge_Grade_Dashboard)

Spring Cloud learning project.
Service, provided movies list with ability for users to add movies to favorites or watch later and mark movies.

## Used technologies
Java 14, Maven, Kotlin,
Spring Boot, Spring Security, Spring Cloud, Spring Actuator, Spring Cloud Config,
Spring Data JPA, Spring Hateoas, Spring Cloud Contract, Spring Cache, Spring Sleuth, Zipkin,
Rest API, GraphQL, oAuth 2, oAuth Authorization Server, oAuth Resource Server, Micrometer,
Netflix Eureka, Netflix Zuul, Netflix Feign, Hibernate, JUnit 5, RabbitMQ, thymeleaf,
PostgreSQL, MongoDB, Redis, JWT, JSON,
JavaScript (including ES6), TypeScript, Node JS, npm, Webpack, React, React Hook Form, Redux, React Redux,
SWR, graphql-request, Bootstrap (and Bootswatch), babel, sass,
Grafana, Graphite, Micrometer, Docker, Docker Compose.

## Microservices
### 1. Config service
Storage all config and provide it to other microservices. \
Profiles: **git** (for getting config files from the git), **native** (for getting config files from local file system) \
Package: **config-service** \
Working port: **8010** \
Requirements: **RabbitMQ** (should be in each microservice, allow sending configuration updates for each microservice), (Optional: Logstash, Graphite) \
Launch order: **ALWAYS FIRST** \
PS: if you use native file system, provide **CONFIG_FILES_PATH** environment property with location to path, where all configuration files placing

### 2. Eureka service
Giving ability for microservices to communicate with each other. \
Package: **eureka-service** \
Working port: **8020** \
Requirements: **RabbitMQ**, (Optional: Logstash, Graphite) \
Launch order: **ALWAYS SECOND**

### 3. User resource service
Storage all info about users and provide API to make CRUD operations with them. \
**Can be launched in multiple instances.** \
Package: **user-resource-service** \
Working port: **random** (you may watch at eureka dashboard) \
Requirements: **RabbitMQ**, **PostgreSQL** (for storing users), **Redis** (for caching users), (Optional: Logstash, Graphite, Zipkin) \
Launch order: **Any time after eureka service**

### 4. Authorization service
Giving ability for users to login. \
Package: **auth-service** \
Working port: **8030** \
Base URL path: **/uaa** \
Requirements: **RabbitMQ**, **PostgreSQL** (for storing users), (Optional: Logstash, Graphite, Zipkin) \
Launch order: **Any time after eureka service**

### 5. Movies list service
Working with movies. Admin can make CRUD with them, users can read them and add to favourite and so on. \
Package: **movies-list-service** \
Working port: **random** (you may watch at eureka dashboard) \
Requirements: **RabbitMQ**, **MongoDB** (for storing movies), (Optional: Logstash, Graphite, Zipkin) \
Launch order: **Any time after eureka service**

### 6. Gateway service
Service for clients that giving access to communicate with other microservices. \
Package: **gateway-service** \
Working port: **8080** \
Requirements: **RabbitMQ**, (Optional: Logstash, Graphite, Zipkin) \
Launch order: **Any time after eureka service**

## Additional requirement
For launching, you may use docker run command or docker-compose. \
**Using docker-compose**: type this command in project root folder (near `docker-compose.yml` file)
```
$ docker-compose up -d
```
Or, run minimal containers:
```
$ docker-compose up -d postgres mongo rabbit redis
```

**Using docker run command**:

1. [PostgreSQL](https://www.postgresql.org/) - users info storage.
    ```
    // Docker run example
    $ docker run --name cloud-movies-postgres -p 5432:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_DB=movies-users -d postgres:13.0
    ```
2. [MongoDB](https://www.mongodb.com/) - movies storage.
    ```
    // Docker run example
    $ docker run --name cloud-movies-mongo -p 27017:27017 -e MONGO_INITDB_DATABASE=movies-list -e MONGO_INITDB_ROOT_USERNAME=mongodb -e MONGO_INITDB_ROOT_PASSWORD=password -d mongo:4.4.0
    ```
3. [RabbitMQ](https://www.rabbitmq.com/) - for sending configuration updates.
    ```
    // Docker run example
    $ docker run -it --name cloud-movies-rabbitmq -p 5672:5672 -p 15672:15672 -d rabbitmq:3-management
    ```
4. [Redis](https://redis.io/) - for caching users.
    ```
    // Docker run example
    $ docker run --name cloud-movies-redis -p 6379:6379 -d redis:6.0
    ```
5. [Zipkin](https://zipkin.io/) - distributed tracing system.
    ```
    // Docker run example
    $ docker run --name cloud-movies-zipkin -p 9411:9411 -d openzipkin/zipkin
    ```
6. [Graphite](https://graphiteapp.org/) - storing metrics. (Uses docker network called `cloud-movies-actuator`)
    ```
    // Docker run example
    $ docker run --name cloud-movies-graphite -p 2003:2003 -p 2004:2004 -p 2023:2023 -p 2024:2024 -p 8125:8125/udp -p 8126:8126 --network cloud-movies-actuator -d graphiteapp/graphite-statsd
    ```
7. [Grafana](https://grafana.com/) - dashboard for displaying metrics. After launching, you may visit it using address `http://localhost:3000` with user **admin** and password **admin**. You may import dashboard using `grafana/Main-dashboard.json` file. If you launch it with docker-compose - it configures datasource's automatically, but, if you run it via docker run command, you should configure it manually. To configure datasource, add Grafana datasource with url `http://cloud-movies-graphite:8080`. (Uses docker network called `cloud-movies-actuator`)
    ```
    // Docker run example
    $ docker run --name cloud-movies-grafana -p 3000:3000 --network cloud-movies-actuator -d grafana/grafana
    ```
8. [Elasticsearch](https://www.elastic.co/) - for storing logs.
    ```
    // Docker run example
    $ docker run --name cloud-movies-elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -d docker.elastic.co/elasticsearch/elasticsearch:7.10.0
    ```
6. [Logstash](https://www.elastic.co/logstash) - for sending logs to the server.
    ```
    // Docker run example
    $ docker run --name cloud-movies-logstash --link cloud-movies-elasticsearch:elasticsearch -v `pwd`/logstash.conf:/usr/share/logstash/pipeline/logstash.conf -v `pwd`/logs:/usr/share/logstash/logs -d docker.elastic.co/logstash/logstash:7.10.0
    ```
7. [Kibana](https://www.elastic.co/kibana) - for managing logs.
    ```
    // Docker run example
    $ docker run --name cloud-movies-kibana --link cloud-movies-elasticsearch:elasticsearch -p 5601:5601 -e "ELASTICSEARCH_HOSTS:http://elasticsearch:9200" -d docker.elastic.co/kibana/kibana:7.10.0
    ```

## How to launch
1. For launching, you need Java 11, Maven and application, indicated in 'Additional requirement'.
2. Create `movies-users` database in Postgres.
3. Launch all applications, indicated in 'Additional requirement'..
4. Execute `mvn package` in project root folder.
5. Setup configuration in `config` folder, if needed.
6. Launch each microservice in order, that showing in Microservices list using command `java -jar ${jar-name}`. Each jar locate in `${microservice-package}/target` folder.
7. You may open website using url `localhost:8080`.
