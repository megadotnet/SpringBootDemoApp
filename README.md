# Springboot Demo  [![Build Status](https://travis-ci.org/megadotnet/SpringBootDemoApp.png?branch=master)](https://travis-ci.org/megadotnet/SpringBootDemoApp/)
A Sample application developed with :
  1. Spring Boot REST
  2. Spring Security
  3. Spring Data JPA
  4. Spring Boot Actuator 
  5. Angular JS
  6. Alibaba Druid
  
Application is secured with JWT and Spring security.


## Feature includes:
   1) Standard login and logout
   2) Registration and email verification
   3) Remember me functionality
   4) Forgot password
   5) Multi-language support
   6) RESTful API

## Srping Actuator
   Spring Boot includes an Actuator module,which introduces production-ready non-functional requirements 
to your application. The Spring Boot Actuator module provides monitoring, metrics, and auditing right out 
of box. 

    /actuator/metrics
This endpoint shows the metrics  information   of the current application, where you can determine the how 
much memory it’s using, how much memory is free, the uptime of your application, the size of the heap is 
being used, the number of threads used, and so on. 

## Swagger

   Swagger is the world’s largest framework of API developer tools for the OpenAPI Specification(OAS), enabling development across the entire API lifecycle, from design and documentation, to test and deployment.

   Link

     http://127.0.0.1:7080/swagger-ui.html
     
## Druid     
Druid is one of the best database connection pools written in JAVA. The Web console url goes to:
    
     http://127.0.0.1:7080/druid/index.html
     
## Testing
  JWT http request 
``` POST http://127.0.0.1:7080/api/authenticate HTTP/1.1
  User-Agent: Fiddler
  Host: 127.0.0.1:7080
  Content-Length: 65
  content-type: application/json
  
  {"username": "admin", "password": "admin", "rememberMe": "false"}
```
  http response

## Docker
### Setup
You can specify the base image, entry point, cmd, maintainer and files you want to add to your
image directly in the pom, without needing a separate `Dockerfile`.
If you need `VOLUME` command(or any other not supported dockerfile command), then you will need
to create a `Dockerfile` and use the `dockerDirectory` element.

By default the plugin will try to connect to docker on localhost:2375. Set the DOCKER_HOST 
environment variable to connect elsewhere.

    DOCKER_HOST=tcp://<host>:2375

Other docker-standard environment variables are honored too such as TLS and certificates.
###Build docker image
``` 
  mvn clean package dockerfile:build
```
  if you want to skip unit test:
``` 
  mvn clean package dockerfile:build -DskipTests
```