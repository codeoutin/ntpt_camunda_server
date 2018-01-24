# NT Prototype Server
NTPT Prototype Server is a Camunda Server, with sample BPMN & DMN Models and Java Microservices Classes to maintain Software Components. Its surrounded with Spring Boot. You can run this Server standalone, but its recommended to setup the NTPT Environment
* [NTPT Frontend](https://github.com/stegerpa/ntpt_frontend_react)
* [NTPT Docker Containers](https://github.com/stegerpa/ntpt_docker_compose)

<img width="200" alt="github_architecture" src="https://user-images.githubusercontent.com/18348827/35306966-c96738ca-00a0-11e8-8c0e-c8e3e097222d.png">


## Usage
1. Build the jar using Maven
2. Run the jar file `java -jar target/*.jar`
2. Server can be accessed on http://localhost:8080

## API Reference
### General
Method|HTTP Request|Description
---|---|---
GET|/hello|Hello World Test
GET|/status|Return `up` when server is running

### Camunda Rest Api
You have full access to [Camundas Rest API](https://docs.camunda.org/manual/latest/reference/rest/). 

Method|HTTP Request|Description
---|---|---
GET|/rest/|No Authentication required
