package de.novatecgmbh.camunda.bpm.prototype.camunda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class DataController {
    @Autowired
    private ServerRepo repository;


    @RequestMapping("/servers/{name}")
    public Server getServer(@PathVariable("name") String name){
        return repository.findByName(name);
    }

    //Returns all servers as List
    @RequestMapping("/servers")
    public Iterable<Server> getServers(){
        return repository.findAll();
    }


    @PostMapping("/servers")
    public ResponseEntity<String> addEntity(@RequestBody Server server){
        repository.save(server);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping("/servers/{name}")
    public ResponseEntity<String> deleteEntity(@PathVariable String name){
        Server s = repository.findByName(name);
        if(s.getName().equals(name)) {
            repository.delete(s);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
