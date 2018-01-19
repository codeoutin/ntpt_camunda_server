package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * !Simulates! to create a Test Environment
 * @author Patrick Steger
 */
@Service("createTestEnvAdapter")
public class TestEnvCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String prefix = (String) execution.getVariable("prefix");
        String testEnvUrl = (String) execution.getVariable("test_environment_url");
        String camundaUrl = (String) execution.getVariable("camunda_url");

        if (testEnvUrl != null) {
            System.out.println("\n# createTestEnvAdapter @ " + prefix + " #\nTry to create Test Environment...");
            try {
                // Logic here
                // Ideas:
                // * Create Docker Compose File to setup Environment
                // * Vagrant

                System.out.println("counting executions:");

                RestTemplate restTemplate = new RestTemplate();
                String gitServerUrl = "http://" + camundaUrl + "/rest/process-instance/count";
                ResponseEntity<String> response = restTemplate.getForEntity(gitServerUrl, String.class);
                JsonNode json = new ObjectMapper().readTree(response.getBody());
                int runningInstancesCount = json.get("count").asInt();
                int testServerPort = 8010 + runningInstancesCount;




                // Log to Console
                System.out.println("Test Environment Create Simulation done.");

                // Set Process Variable for later checks
                execution.setVariable("test_environment_created", true);
            } catch (Exception e) {
                execution.setVariable("test_environment_created", false);
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while creating a Test Environment. Error Message: " + e.getMessage());
                throw new BpmnError("CreateError");
            }
        } else {
            execution.setVariable("test_environment_created", false);
        }
    }

}