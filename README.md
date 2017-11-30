# NT Prototype Server
Prototype Camunda Server. Includes the process, process-engine, a dmn and a Rest Interface.
The Server is written in SpringBoot and includes the Camunda Engine with BPMN and DMN Models.

## Usage
1. Build the jar using Maven
2. Run the jar file `java -jar target/*.jar`
2. Server can be accessed on http://localhost:8080

## API Reference
### General
Method|HTTP Request|Description
---|---|---
GET|/hello|Hello World Test
GET|/server/`{server}`|Gets the current servers adress
