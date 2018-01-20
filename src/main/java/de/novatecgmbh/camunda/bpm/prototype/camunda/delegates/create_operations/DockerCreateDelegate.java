package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Creates a new Docker Container from a given Image and Runs it
 * @author Patrick Steger
 */
@Service("createTestEnvAdapter")
public class DockerCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String prefix = (String) execution.getVariable("prefix");
        String dockerUrl = (String) execution.getVariable("test_environment_url");
        String dockerImage = (String) execution.getVariable("test_environment_dockerimage");
        String dockerImagePort = (String) execution.getVariable("test_environment_dockerport");
        String camundaUrl = (String) execution.getVariable("camunda_url");

        if (dockerUrl != null && dockerImage != null && dockerImagePort!=null) {
            System.out.println("\n# createTestEnvAdapter @ " + prefix + " #\nTry to create Test Environment...");
            try {
                // Counts Running Camunda Executions to calculate a Port
                RestTemplate restTemplate = new RestTemplate();
                String gitServerUrl = "http://" + camundaUrl + "/rest/process-instance/count";
                ResponseEntity<String> response = restTemplate.getForEntity(gitServerUrl, String.class);
                JsonNode json = new ObjectMapper().readTree(response.getBody());
                int runningInstancesCount = json.get("count").asInt();
                int port = 8010 + runningInstancesCount;
                String hostPort = dockerImagePort + "/tcp";
                String mappedPort = ""+port;

                // Create JSON For Container Creation
                String createContainerJson = Json.createObjectBuilder()
                        .add("Hostname", prefix)
                        .add("HostConfig", Json.createObjectBuilder()
                            .add("PortBindings", Json.createObjectBuilder()
                                .add(hostPort, Json.createArrayBuilder()
                                    .add(Json.createObjectBuilder()
                                        .add("HostIp", "0.0.0.0")
                                        .add("HostPort", mappedPort)))))
                        .add("Image", dockerImage)
                        .build()
                        .toString();

                // Create URL to Pull Image
                String pullUrl = "http://" + dockerUrl+ "/images/create?fromImage=" + dockerImage;
                String pullUrlParameters = "fromImage=" + URLEncoder.encode(dockerImage, "UTF-8");

                //Send POST Request to Pull Image
                HttpURLConnection pullConn =  (HttpURLConnection) new URL(pullUrl).openConnection();
                pullConn.setDoOutput(true); //needed for POST
                pullConn.setDoInput(true);
                pullConn.setRequestMethod("POST");
                pullConn.setRequestProperty("Content-Length", ""+Integer.toString(pullUrlParameters.getBytes().length));
                System.out.println("Try to pull Docker Image...");
                pullConn.connect();
                System.out.println(pullConn.getResponseMessage());
                System.out.println("Image created. HTTP Response Code: " + pullConn.getResponseCode());

                // Create URL to Create Container
                String createUrl = "http://" + dockerUrl+ "/containers/create?name=" + prefix;

                //Send POST Request to Create Container
                HttpURLConnection createConn =  (HttpURLConnection) new URL(createUrl).openConnection();
                createConn.setDoOutput(true); //needed for POST
                createConn.setDoInput(true);
                createConn.setRequestMethod("POST");
                createConn.setRequestProperty("Content-Type", "application/json");
                createConn.setRequestProperty("Content-Length", ""+Integer.toString(createContainerJson.getBytes().length));
                System.out.println("Try to create Docker Container...");
                try(DataOutputStream wr = new DataOutputStream(createConn.getOutputStream())) {
                    wr.write( createContainerJson.getBytes() );
                }
                System.out.println(createConn.getResponseMessage());
                System.out.println("Container created. HTTP Response Code: " + createConn.getResponseCode());

                // read the response (if responsecode = 200)
                //InputStream in = new BufferedInputStream(createConn.getInputStream());
                //String result = in.toString();
                //System.out.println("### RESULT ### " + result);

                // Create URL to start Container
                String startUrl = "http://" + dockerUrl+ "/containers/" + prefix + "/start";

                //Send POST Request to Start Container
                HttpURLConnection startConn =  (HttpURLConnection) new URL(startUrl).openConnection();
                startConn.setDoOutput(true); //needed for POST
                startConn.setDoInput(true);
                startConn.setRequestMethod("POST");
                startConn.setRequestProperty("Content-Length", Integer.toString(startUrl.getBytes().length ));
                System.out.println("Try to start Docker Image...");
                startConn.connect();
                System.out.println(startConn.getResponseMessage());
                System.out.println("Container started. HTTP Response Code: " + startConn.getResponseCode());

                // Log to Console
                System.out.println("Test Environment Created:");
                System.out.println("URL: http://localhost:" + port);
                System.out.println("Image: " + dockerImage);


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