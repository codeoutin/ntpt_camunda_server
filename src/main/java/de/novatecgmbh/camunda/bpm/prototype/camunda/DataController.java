package de.novatecgmbh.camunda.bpm.prototype.camunda;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.web.bind.annotation.*;


@RestController
public class DataController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json")
    public KeyValue returnHello() {
        KeyValue hello = new KeyValue("name", "hello");

        //Convert Java Object to JSON
        //ObjectMapper mapper = new ObjectMapper();
        return hello;
    }

    //Return the address of a server stored in properties file
    @RequestMapping("/server/{server}")
    @JsonValue
    public String getServerAddress(@PathVariable("server") String server) {
        try {
            PropertiesConfiguration conf = new PropertiesConfiguration("server.properties");
            return (String) conf.getProperty(server);
        } catch (ConfigurationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

//    @RequestMapping("/servers/{name}")
//    public Server getServer(@PathVariable("name") String name){
//        return repository.findByName(name);
//    }
//
//    //Returns all servers as List
//    @RequestMapping("/servers")
//    public Iterable<Server> getServers(){
//        return repository.findAll();
//    }
//
//
//    @PostMapping("/servers")
//    public ResponseEntity<String> addEntity(@RequestBody Server server){
//        repository.save(server);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
//
//    @DeleteMapping("/servers/{name}")
//    public ResponseEntity<String> deleteEntity(@PathVariable String name){
//        Server s = repository.findByName(name);
//        if(s.getName().equals(name)) {
//            repository.delete(s);
//            return new ResponseEntity<>(HttpStatus.ACCEPTED);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }

    class KeyValue {
        private String key;
        private String value;

        KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

}
