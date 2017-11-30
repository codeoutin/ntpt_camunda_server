package de.novatecgmbh.camunda.bpm.prototype.camunda;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface ServerRepo extends CrudRepository<Server, String>{
    public Server findByName(String name);
}
