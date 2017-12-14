package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;


@Service("createBranchAdapter")
public class BranchCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String gitBranchName = (String) execution.getVariable("git_branch_name");
        String gitUrl = "http://" + execution.getVariable("git_url");
        String gitToken = (String) execution.getVariable("git_token");
        String gitProjectId = (String) execution.getVariable("git_project");
        String gitCommitId = (String) execution.getVariable("git_commit");

        // Check if Variables are NOT empty
        if (!(gitBranchName.isEmpty() && gitToken.isEmpty() && gitProjectId.isEmpty() && gitCommitId.isEmpty())) {
            try {
                // Create URL to Create a Branch using Gitlab REST Api
                String CreateBranchUrl = gitUrl
                        + "/api/v4/projects/"
                        + gitProjectId
                        + "/repository/branches?branch="
                        + gitBranchName
                        + "&ref="
                        + gitCommitId;

                HttpURLConnection c =  (HttpURLConnection) new URL(CreateBranchUrl).openConnection();
                c.setRequestMethod("POST");
                c.setRequestProperty("PRIVATE-TOKEN", gitToken);

                execution.setVariable("git_branch_created", true);
                System.out.println("\n#####\n Git Create Branch Message: " + c.getResponseMessage() + "\n#####\n");
            } catch (Exception e) {
                execution.setVariable("git_branch_created", false);
                execution.setVariable("bp_created", false);
                execution.setVariable("db_created", false);
                execution.setVariable("test_environment_created", false);
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while creating a new Git Branch. Task cancelled.");
                throw new BpmnError("CreateError");
            }
        }
    }
}