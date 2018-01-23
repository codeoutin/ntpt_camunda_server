package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import javax.json.Json;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Creates a SonarQube Project and assigns the selected Quality Gate to the Project
 * @author Patrick Steger
 */
@Service("createQAAdapter")
public class SonarQubeCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String sqProjectName = (String) execution.getVariable("prefix");
        String sqUrl = (String) execution.getVariable("sonarqube_url");
        String sqToken = (String) execution.getVariable("sonarqube_token");

        String sqProfile = (String) execution.getVariable("sonarqube_profile");

        //Check if SonarQube not "no", not empty and Url is not null
        if(!sqProfile.equals("no") && !sqProfile.isEmpty() && sqUrl != null) {
            System.out.println("\n# createQAAdapter @ " + sqProjectName + " #");
            try {
                //Make URL to Create a SQ Project
                String createUrl = "http://" + sqUrl+ "/api/projects/create";
                String createParams  = "name="+sqProjectName+"&project="+sqProjectName;
                byte[] createData = createParams.getBytes( StandardCharsets.UTF_8 );

                //Create the SQ Project
                HttpURLConnection createConn =  (HttpURLConnection) new URL(createUrl).openConnection();
                createConn.setDoOutput(true); //needed for POST
                createConn.setRequestMethod("POST");
                createConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                createConn.setRequestProperty("charset", "utf-8");
                createConn.setRequestProperty("Content-Length", Integer.toString(createData.length ));
                createConn.setUseCaches(false);
                System.out.println("Try to create SonarQube Project...");
                try(DataOutputStream wr = new DataOutputStream(createConn.getOutputStream())) {
                    wr.write( createData );
                }

                //Check for Success
                if(createConn.getResponseCode() / 100 == 2) {
                    System.out.println("Profile created. HTTP Response Code: " + createConn.getResponseCode());

                    //Make URL to Associate a Project to a Quality Gate
                    String assignUrl = "http://" + sqUrl + "/api/qualitygates/select";
                    String assignParams  = "gateId="+sqProfile+"&projectKey="+sqProjectName;
                    byte[] assignData = assignParams.getBytes( StandardCharsets.UTF_8 );
                    String basicAuth = Base64.getEncoder().encodeToString((sqToken+":").getBytes(StandardCharsets.UTF_8));

                    //Assign the Project to the Gate
                    HttpURLConnection assignConn =  (HttpURLConnection) new URL(assignUrl).openConnection();
                    assignConn.setDoOutput(true);
                    assignConn.setRequestMethod("POST");
                    assignConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    assignConn.setRequestProperty("charset", "utf-8");
                    assignConn.setRequestProperty("Authorization", "Basic " + basicAuth);
                    assignConn.setRequestProperty("Content-Length", Integer.toString(assignData.length ));
                    assignConn.setUseCaches(false);
                    System.out.println("Try to assign Quality Gate to Project");
                    try(DataOutputStream assignStream = new DataOutputStream(assignConn.getOutputStream())) {
                        assignStream.write( assignData );
                    }
                    if(assignConn.getResponseCode() / 100 == 2) {
                        // Create Return JSON
                        String returnJson = Json.createObjectBuilder()
                                .add("project_name", sqProjectName)
                                .add("path", "http://" + sqUrl + "/dashboard?id=" + sqProjectName)
                                .build()
                                .toString();

                        // Log to Console
                        System.out.println("Quality Gate " + sqProfile + " assigned. HTTP Response Message: " + assignConn.getInputStream());

                        // Set Process Variable for later checks
                        execution.setVariable("sonarqube_created", true);
                        execution.setVariable("return_sonarqube", returnJson);
                    } else {
                        throw new Exception ("Could not assign Profile. Error: " + assignConn.getErrorStream());
                    }
                } else {
                    throw new Exception ("Could not create Project. Error: " + createConn.getErrorStream());
                }
            } catch (Exception e) {
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error at creating SonarQube Artifacts. Error Message: " + e.getMessage());
                throw new BpmnError("CreateError");
            }
        }
    }
}