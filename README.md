# Face2Phase
<a href="https://en.wikipedia.org/wiki/Two-phase_commit_protocol">Two Phase Commit</a> over HTTP for Neo4J (Graph Database) implementation using Reactive Streams, developed with Spring Framework for back-end and ReactJS for front-end

Two Phase Commit is a disterbuted databases protocol

# Architecture
Each server runs the same code (symmetrically) with different configuration file (application.properties, replica.properties, etc),
you can define servers ports, neo4j instance and specify replicas in the properites file

#### For example:  
```
server.port=8080

spring.data.neo4j.uri=bolt://localhost:7687

insta2Phase.replicas.urlList=localhost:8081, localhost:8082

logging.file=primary.log

```
#### Creating a new replica with a new configuation file:
In this example the configuation file stored in src/main/resources/replica.properties
You can create as many as you like
```
@SpringBootApplication
public class ReplicaApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ReplicaApplication.class)
                .properties("spring.config.name:replica")
                .build()
                .run(args);
    }
}
```

### Defining New Entities and Two Phase operation
* In this project the main example is crud operations and "follow account" operation for the Account entity
* Each operation should define a Query in the "crudQueries" package and a service thats implements TwoPhaseCrudOperation<R extends Query> interface
