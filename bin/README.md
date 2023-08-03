# ConfigDemoService

This service is a demo for cloud config values and has no productive use (just for testing).

Copy the file "config-demo-service.yml" into your cloud-config and call swagger ui for endpoints:
http://localhost:8090/swagger-ui/index.html#/

or directly the 4 endpoints:
http://localhost:8090/config-demo-service/v1/config/value1
http://localhost:8090/config-demo-service/v1/config/value2
http://localhost:8090/config-demo-service/v1/config/value3
http://localhost:8090/config-demo-service/v1/config/value4

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
 See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You need to have installed:

```
Maven
Java 11
RabbitMQ
Consul
```

### Installing

TBD

## Initial setup of a new service

When a new service is being created these steps should be followed:

* Fork *application-seed-java* repository into a new repository with the name of the new service.
* Change \<artifactId>, \<name> and \<description> in pom.xml according to the new service.
* Change *logger.rabbitmq.application.id* in logger.rabbitmq.application.id with the id of the new service.
* Change *spring.application.name* in application.properties with the name of the new service.
* Create under *com.trianel* a package with the name of the service and all other packages should be children of this package.<br/>Example:
 
```
    • com.trianel.<service name>
        ◦ com.trianel.<service name>.configs
        ◦ com.trianel.<service name>.converters
        ◦ com.trianel.<service name>.logging
        ◦ com.trianel.<service name>.models
        ◦ com.trianel.<service name>.rabbitmq
        ◦ com.trianel.<service name>.services
```

## Running from the IDE

TBD

## Running the tests

You can run all the tests using:

```
mvn test
```
### Coding style 

For this project is used the official Upnetix Checkstyle

## Deployment

TBD

## Built With

* [Spring](https://spring.io/docs) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [InteliJ](https://www.jetbrains.com/idea/documentation/) - IDE


## Versioning

For the versions available, see the tags on this repository 

## Usage

Maven build tool is used for the complete build process of the module. There are couple of specific goals configured
in order to build, tag and push a Docker image to a remote Docker registry. As a prerequisite Docker must be installed
on the build machine and the user executing the next command must be added to the Docker group.
To execute the whole process one must execute the following maven commands:

####Clean
*    __mvn clean__
####Compile and prepare executable jar
*    __mvn package__
####Build Docker image
*    __mvn dockerfile:build__
###Tag Docker image
*    __mvn dockerfile:tag@tag-latest dockerfile:tag@tag-version -Ddocker.registry.address={ip:port or URL}__
###Push tags to remote Docker registry
*    __mvn dockerfile:push@tag-latest dockerfile:push@tag-version -Ddocker.registry.address={ip:port or URL}__
###Cleanup local images
*    __mvn exec:exec@images-cleanup__

Build step produces an image in the local registry with tag "latest". "tag-latest" maven goal tags an image with pointing
to a remote Docker registry with a given address and tag "latest". "tag-version" serves a similar function but uses the maven
artifact version as tag. The image carries a name in the form "com.trianel.{articact.id}". The Docker image is built including
a label "name" conaining the image name in order to allow cleanup afterwards. Push step requires the registry Address/URL as well.
Cleanup step will remove all produced images carrying with label "name" equal to the module label.

## Authors

* **Vladimir Bahnev** - [vladimir.bahnev@scalefocus.com](mailto:vladimir.bahnev@scalefocus.com)
* **Nikolay Petrov** - [niki.petrov@scalefocus.com](mailto:niki.petrov@scalefocus.com)

## Acknowledgments

TBD