# **Popcorn Palace - Setup Guide**

## **API documentation**
API documentation for this assignment can be found in the original Readme.md.

Additional notes by me and error handling documentation can be found in Readme2.md

## **1. Prerequisites**
Ensure you have the following installed:

- **Java 17 or higher**
  ```sh
  java -version
  ```

- **Docker & Docker Compose**
  ```sh
  docker --version
  docker compose version
  ```

## **2. Configuration**

### **Production (`application.yaml`)**
Persistent database using a docker container
Located in `src/main/resources/application.yaml`:

```yaml
server:
  port: 8080

spring:
  application:
    name: popcorn-palace
  datasource:
    url: jdbc:postgresql://db:5432/popcorn-palace  
    username: popcorn-palace
    password: popcorn-palace
    driverClassName: org.postgresql.Driver
  jpa:
    database: POSTGRESQL
    show-sql: true
    hibernate:
      ddl-auto: update 
  sql:
    init:
      mode: always
```

### **Test Environment (`application-test.yaml`)**
For running tests with an H2 in-memory database:

```yaml
server:
  port: 8080

spring:
  application:
    name: popcorn-palace
  datasource:
    url: jdbc:h2:mem:db;MODE=PostgreSQL;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: password
    driverClassName: org.h2.Driver
  sql:
    init:
      platform: mssql
  h2:
    console:
      enabled: true
  jpa:
    database: POSTGRESQL
    show-sql: true
    hibernate:
      ddl-auto: update
```

## **3. Cloning the Repository**
Clone the repository and navigate into the project directory:

```sh
git clone <repository-url>
cd popcorn-palace
```

## **4. Running with Docker Compose**

To start the PostgreSQL database:

```sh
docker compose up -d
```

## **5. Building and Running the Application**



### **With Mvnw script**
Linux:
```sh
./mvnw spring-boot:run
```

Windows:
```sh
mvnw spring-boot:run
```

### **To run the test application**
Linux:
mvnw:
```sh
./mvnw test
```
Windows:
```sh
mvnw test
```


## **6. Testing API Endpoints**
- **Check if the app is running:**
- Open another cmd:
  ```sh
  telnet localhost 8080
  ```


