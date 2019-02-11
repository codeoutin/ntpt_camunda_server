# NTPT Server Component (Prototype)
This project is part of my Bachelor Thesis project NTPT. It's a Camunda Server based on Spring Boot (Java), with a demo BPMN & DMN Models and Java Microservices Classes to maintain Software Components (create git branches, databases, make API calls to the code quality tool, ...). You can run this Server standalone, but I recommended to use it together with the other parts of the NTPT project:
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
