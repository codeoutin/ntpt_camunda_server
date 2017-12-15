package de.novatecgmbh.camunda.bpm.prototype.camunda.rest_api;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;


@RestController
public class DataController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json")
    public String returnHello() {
        JSONObject json = new JSONObject();
        try {
            json.put("response", "Hello");
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        return json.toString();
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
    public String returnHelloWorld() {
        return "{\"status\":\"up\"}";
    }
}
