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

## Customizing

### DataStore

This project demonstrates a simple custom `DataStore`, the `OperationDataStore`, that allows exposing operations that do not persist any data, for instance for sending an e-mail.

This is done by creating a model, `Mail`, with a `LifeCycleHook` that performs the operation.

This allows exposing the operations through both JSON API and GraphQL.

#### JSON API

`POST /mail`
```json
{
  "data": {
    "type": "mail",
    "attributes": {
      "from": "thomas",
      "to": "henry",
      "content": "Hello world"
    }
  }
}
```

#### GraphQL

```graphql
mutation {
  mail(op: UPSERT, data: {from: "thomas", to: "henry", content: "Hello world"}) {
    edges {
      node {
        id
      }
    }
  }
}
```

### OpenAPI

Elide will only generate the OpenAPI document for the models that are defined in its `EntityDictionary`.

If a consolidated document is required, for instance for documenting a `@RestController` like the `HelloController` example then Elide offers integration with Springdoc which can be accessed at http://localhost:8080/swagger-ui/index.html.

The sample configuration is in `OpenApiConfiguration` which takes into account Elide's API versioning functionality.

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>${springdoc.version}</version>
</dependency>
```

### Native

Elide supports being built into a GraalVM native image by supplying a feature, `yahoo.elide.core.graal.ElideFeature`, and its own native hints in it's libraries, ie. `native-image.properties`, `reflect-config.json` and `resource-config.json`.

Further configuration is typically required, for instance to add project specific resources like `analytics/models/tables/artifactDownloads.hjson`, or for instance if the [GraalVM Reachability Metadata Repository](https://github.com/oracle/graalvm-reachability-metadata) does not contain updated metadata for newly released libraries.

This project has configured additional hints using Spring Boot's `RuntimeHintsRegistrar` in `AppRuntimeHints`. This is configured on `App` using `@ImportRuntimeHints`.


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

#### Async Query

`POST /asyncQuery`

```json
{
  "data": {
    "type": "asyncQuery",
    "id": "ba31ca4e-ed8f-4be0-a0f3-12088fa9263d",
    "attributes": {
      "query": "/group?sort=commonName&fields%5Bgroup%5D=commonName,description",
      "queryType": "JSONAPI_V1_0",
      "status": "QUEUED"
    }
  }
}
```

#### Async Table Export

`POST /tableExport`

```json
{
  "data": {
    "type": "tableExport",
    "id": "ba31ca4e-ed8f-4be0-a0f3-12088fa9263f",
    "attributes": {
      "query": "/group?sort=commonName&fields%5Bgroup%5D=commonName,description",
      "queryType": "JSONAPI_V1_0",
      "status": "QUEUED",
      "resultType": "CSV"
    }
  }
}
```


### GraphQL

#### Query

```graphql
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

```graphql
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

```graphql
subscription OnAddGroup {
  group (topic: ADDED) {
    name
    description
  }
}
```

#### Async Query

```graphql
mutation {
  asyncQuery(
    op: UPSERT
    data: {id: "bb31ca4e-ed8f-4be0-a0f3-12088fb9263e", query: "{\"query\":\"{ group { edges { node { name } } } }\",\"variables\":null}", queryType: GRAPHQL_V1_0, status: QUEUED}
  ) {
    edges {
      node {
        id
        query
        queryType
        status
        result {
          completedOn
          responseBody
          contentLength
          httpStatus
          recordCount
        }
      }
    }
  }
}
```

#### Async Table Export

```graphql
mutation {
  tableExport(
    op: UPSERT
    data: {id: "bb31ca4e-ed8f-4be0-a0f3-12088fb9263d", query: "{\"query\":\"{ group { edges { node { name } } } }\",\"variables\":null}", queryType: GRAPHQL_V1_0, resultType: "CSV", status: QUEUED}
  ) {
    edges {
      node {
        id
        query
        queryType
        resultType
        status
        result {
          completedOn
          url
          message
          httpStatus
          recordCount
        }
      }
    }
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
