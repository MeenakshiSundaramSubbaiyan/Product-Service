# Case Study :: Product Service

Product Service API exposes Product Details like Product Name and Price information for the given Product ID. This service also provides an option to update
the price into the database for the given ProductId and request JSON. Product name is fetched from the Redsky api and the price details are stored and retrieved
from embedded MongoDB.

## Technology Stack

* Java
* Spring Boot, Spring Security, Spring Web, Spring Data
* Swagger
* lombok
* slf4j, log4j, logback-access
* Junit, Mockito
* Sonar JaCoCo
* Maven
* Embedded Mongo DB
* Concourse
* Netflix Hystrix 
 
### Prerequisites

```
Java 1.8
Maven 3.3.9
```

### Application Setup

```
Clone the Repository or download the zip folder from GITHub. https://github.com/MeenakshiSundaramSubbaiyan/Product-Service
```

## Execute Test Cases

```
Navigate to application folder and execute 
mvn clean test
```

## Build With Maven and run from Java artifact

```
mvn clean install
java -jar target/product-service-0.0.1-SNAPSHOT.jar
```

## Code Coverage Report

```
Code coverage report will be generated in the below path after the running the command mvn clean install
<ProjectDirectory>\target\site\jacoco\index.html
```


## Start as Spring Boot Application using Maven

```
mvn spring-boot:run (To Start as Spring boot application)
```

## Application Endpoints:

```
* Access the application login endpoint which will return the bearer token
* Pass the bearer token to the application end point for both GET and PUT resquest
* Bad credentials exception will be thrown when accessing the application end point either without or with invalid bearer token

Login Endpoint(POST):  http://localhost:8080/auth/login

Product End point to GET and PUT: http://localhost:8080/api/v1/products/{productId}
```

## Test with curl

```
curl -X POST -d 'username=test&password=test' http://localhost:8080/auth/login

curl -H 'Authorization: Bearer <<<TokenReceived From Login Response>>>' http://localhost:8080/api/v1/products/13860416

curl -X PUT -H 'Authorization: Bearer <<<TokenReceived From Login Response>>>' -H 'Content-Type: application/json' -d '{"id":13860416,"name":"Progressive power yoga:Sedona experie (DVD)","current_price":{"value":6.67,"currency_code":"USD"}}' http://localhost:8080/api/v1/products/13860416

Sample Product Ids: 13860416, 13860418, 13860420, 13860421, 13860424, 13860425
```

## Swagger API reference
```
http://localhost:8080/swagger-ui.html
```
## Postman Document reference for sample request and response
```
https://documenter.getpostman.com/view/6656799/RztsnRaG
```

## Sample JSON for Update Request
```
{
    "id": 13860416,
    "name": "Progressive power yoga:Sedona experie (DVD)",
    "price": {
        "value": 6.67,
        "currency_code": "USD"
    }
}
```

## Security Implementation

* Token Based Security has been implemented using Spring Security
* Login to application using Login Endpoint and copy the response token
* Use the token as the Authorization Bearer token in the response header to access product Endpoints


