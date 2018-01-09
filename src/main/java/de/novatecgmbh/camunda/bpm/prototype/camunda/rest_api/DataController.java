package de.novatecgmbh.camunda.bpm.prototype.camunda.rest_api;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

/**
 * Creates some classes for another REST-Api that will be used to check if the application respons
 * @author Patrick Steger
 */
@RestController
public class DataController {

    /**
     * Maps GET /hello to the Api
     * @return Hello
     */
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

    /**
     * Returns a Status that can be used to check if server is running
     * @return "up" to proove that server is running
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
    public String returnHelloWorld() {
        return "{\"status\":\"up\"}";
    }
}
