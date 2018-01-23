package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.net.HttpURLConnection;
import java.net.URL;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.json.Json;
import javax.xml.transform.sax.SAXSource;

/**
 * Create a new GitLab Branch based on an existing Branch in an existing Project
 * @author Patrick Steger
 */
@Service("createBranchAdapter")
public class BranchCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String gitBranchName = (String) execution.getVariable("git_branch_name");
        String gitUrl = (String) execution.getVariable("git_url");
        String gitToken = (String) execution.getVariable("git_token");
        String gitProjectId = (String) execution.getVariable("git_project");
        String gitCommitId = (String) execution.getVariable("git_commit");

        // Check if Variables are NOT empty
        if (!(gitBranchName.isEmpty() && gitToken.isEmpty() && gitProjectId.isEmpty() && gitCommitId.isEmpty())) {
            System.out.println("\n# createBranchAdapter @ " + gitBranchName + " #\nTry to create Git Branch...");
            try {
                //Make URL to Create a Branch using Gitlab REST Api
                String CreateBranchUrl = "http://" + gitUrl
                        + "/api/v4/projects/"
                        + gitProjectId //Attribute = id
                        + "/repository/branches?branch="
                        + gitBranchName //Attribute = branch
                        + "&ref="
                        + gitCommitId; //Attribute = ref

                //Send using POST to GitLab's Api
                HttpURLConnection c =  (HttpURLConnection) new URL(CreateBranchUrl).openConnection();
                c.setRequestMethod("POST");
                c.setRequestProperty("PRIVATE-TOKEN", gitToken);

                // Check for Success
                if(c.getResponseCode() / 100 == 2) {
                    // Log to Console
                    System.out.println("Git Branch Created. Response Message: " + c.getResponseMessage());

                    // Get Project Path
                    RestTemplate restTemplate = new RestTemplate();
                    String getProjectUrl = "http://" + gitUrl + "/api/v4/projects/" + gitProjectId;
                    ResponseEntity<String> response = restTemplate.getForEntity(getProjectUrl, String.class);
                    JsonNode json = new ObjectMapper().readTree(response.getBody());
                    String gitProjectPath = json.get("path_with_namespace").asText();

                    // Create Return JSON
                    String createReturnJson = Json.createObjectBuilder()
                            .add("branch_name", gitBranchName)
                            .add("branch_url", gitUrl + "/" + gitProjectPath + "/tree/" + gitBranchName)
                            .build()
                            .toString();

                    // Set Process Variable for later checks
                    execution.setVariable("git_branch_created", true);
                    execution.setVariable("return_git", createReturnJson);
                } else {
                    throw new Exception("Could not create Git Branch");
                }

            } catch (Exception e) {
                execution.setVariable("git_branch_created", false);
                execution.setVariable("bp_created", false);
                execution.setVariable("db_created", false);
                execution.setVariable("test_environment_created", false);
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while creating a new Git Branch. Error Message: " + e.getMessage());
                throw new BpmnError("CreateError", e.getMessage());
            }
        }
    }
}