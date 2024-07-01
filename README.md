# Elide Spring Boot Example

An archetype Elide project using Spring Boot.

[![Build Status](https://cd.screwdriver.cd/pipelines/7924/badge)](https://cd.screwdriver.cd/pipelines/7924)

## Background

This project is the sample code for [Elide's Getting Started documentation](https://elide.io/pages/guide/v7/01-start.html).

## Quick Start

### Build and run

#### Java

```shell
mvn clean install
java -jar target/elide-spring-boot-1.0.0.jar
```

#### Native

[Getting Started with GraalVM](https://www.graalvm.org/latest/docs/getting-started/)

```shell
mvn clean -Pnative native:compile
```

### Explore

| Description         | URL
|---------------------|---------------------------------------------
| API Default Version | http://localhost:8080/
| API Version 1       | http://localhost:8080/?path=/v1
| API Version 2       | http://localhost:8080/?path=/v2
| Springdoc           | http://localhost:8080/swagger-ui/index.html

## Docker and Containerize

To containerize and run elide project locally

1. Build Docker Image from Root of the project 
   ```
   docker build -t elide/elide-spring-boot-example .
   ```
2. Run docker container locally
   ```
   docker run -p 8081:8080 -d elide/elide-spring-boot-example
   ```
3. Application should be running in 
   ```
   http://localhost:8081/
   ```
   
3. Check Docker containers running
   ```
   docker ps
   CONTAINER ID   IMAGE                             COMMAND                  CREATED         STATUS         PORTS                     NAMES
   99998a35d377   elide/elide-spring-boot-example   "java -cp app:app/li…"   6 seconds ago   Up 5 seconds   0.0.0.0:8089->8080/tcp    upbeat_lehmann
   ```
4. Check logs of the container
   ```
   docker logs -f 99998a35d377 
   ```

5. If you want to ssh to your docker container for more troubleshooting
   ```
    docker exec -it 99998a35d377 /bin/sh
   ```
# Run from cloud (AWS)


1. ECR 
   1. Create a repository in Elastic Container Registry with name elide-spring-boot-example
   2. aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin xxx.dkr.ecr.us-east-1.amazonaws.com 
   3. docker tag elide-spring-boot-example:latest xxx.dkr.ecr.us-east-1.amazonaws.com/elide-spring-boot-example:latest
   4. docker push xxx.dkr.ecr.us-east-1.amazonaws.com/elide-spring-boot-example
2. Fargate
   1. With the image on AWS ECR, you can now define your deployment on AWS ECS/Fargate.
   2. Make sure to mention ECR details
   3. Port mapping is needed in the task definition. If you open the json definition of the task, you can edit to add this.
   "portMappings": [
         {
         "hostPort": 8080,
         "protocol": "tcp",
         "containerPort": 8080
         }
      ],
    4. The security group created or used for the task , needs to have inbound rule for TCP custom port 8080 , CIDR block 0.0.0.0/0(open to everyone, or you can add your company's CIDR block if its an internal service)
    
## Usage

See [Elide's Getting Started documentation](https://elide.io/pages/guide/v7/01-start.html).

## Queries

### JSON API

#### Mutation

`POST /group/{groupId}/products/{productId}/versions`

|Variable      |Value
|--------------|--------------------
|`groupId`     |`com.yahoo.elide`
|`productId`   |`elide-core`

```json
{
  "data": {
  "type": "version",
  "id": "7.1.0",
  "attributes": {
    "createdAt": "2007-12-03T10:15Z"
    }
  }
}
```

#### Atomic Operations

`POST /operations`

```json
{
  "atomic:operations": [
    {
      "op": "add",
      "href": "/group/com.yahoo.elide/products/elide-core/versions",
      "data": {
        "type": "version",
        "id": "7.1.0",
        "attributes": {
          "createdAt": "2007-12-03T10:15Z"
        }
      }
    }
  ]
}
```

### GraphQL

#### Query

```json
query QueryGroup {
  group {
    edges {
      node {
        name
        description
        products {
          edges {
            node {
              name
              versions {
                edges {
                  node {
                    name
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
```

#### Mutation

```json
mutation UpsertGroup {
  group(
    op: UPSERT
    data: {name: "org.springframework.boot", description: "Spring Boot"}
  ) {
    edges {
      node {
        name
        description
      }
    }
  }
}
```
#### Subscription

```json
subscription OnAddGroup {
  group (topic: ADDED) {
    name
    description
  }
}
```

## Dependencies

This example uses the `elide-spring-boot-starter` which includes most of Elide's modules which may be excluded if not required.

### Async

This enables the async functionality to make `asyncQuery` and `tableExport` for both JSON-API and GraphQL.

```xml
<dependency>
    <groupId>com.yahoo.elide</groupId>
    <artifactId>elide-spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>com.yahoo.elide</groupId>
            <artifactId>elide-async</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### Aggregation Datastore

This enables the functionality for defining analytic models. The example is at `resources/analytics/models/tables/artifactDownloads.hjson.`

```xml
<dependency>
    <groupId>com.yahoo.elide</groupId>
    <artifactId>elide-spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>com.yahoo.elide</groupId>
            <artifactId>elide-datastore-aggregation</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### GraphQL

This enables the functionality for making GraphQL queries, mutations and subscriptions.

```xml
<dependency>
    <groupId>com.yahoo.elide</groupId>
    <artifactId>elide-spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>com.yahoo.elide</groupId>
            <artifactId>elide-graphql</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### Swagger

This enables the functionality for exposing the JSON-API documentation using OpenAPI 3.

```xml
<dependency>
    <groupId>com.yahoo.elide</groupId>
    <artifactId>elide-spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>com.yahoo.elide</groupId>
            <artifactId>elide-swagger</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
## Contribute
Please refer to the [CONTRIBUTING.md](CONTRIBUTING.md) file for information about how to get involved. We welcome issues, questions, and pull requests.

## License
This project is licensed under the terms of the [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) open source license.
Please refer to [LICENSE](LICENSE.txt) for the full terms.
