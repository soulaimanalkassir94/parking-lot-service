
# ParkinglotService

The Service is responsible for receiving incorrect message sthat are sent to the parkinglot-queue, and then save them for correction and resend to the original exchange.

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

Open the properties.yaml and under parkinglot enter the following information in the correct way:
  parkinglots:
    service_1:
     pakinglot: (queue name)
     servicename: (service name)
     exchange: (exchange name)
     routingkey: (routingkey)
    service_2:
     pakinglot: (queue name)
     servicename: (service name)
     exchange: (exchange name)
     routingkey: (routingkey)
        ....
        
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

* **Soulaiman Alkassir** - [soulaimanalkassir@gamil.com](mailto:soulaimanalkassir@gamil.com)

## Acknowledgments

TBD
=======
