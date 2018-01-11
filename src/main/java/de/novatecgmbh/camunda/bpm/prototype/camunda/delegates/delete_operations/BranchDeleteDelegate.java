package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.delete_operations;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to delete a GitLab Branch
 * @author Patrick Steger
 */
@Service("closeBranchAdapter")
public class BranchDeleteDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String gitBranchName = (String) execution.getVariable("git_branch_name");
        String gitUrl = "http://" + execution.getVariable("git_url");
        String gitToken = (String) execution.getVariable("git_token");
        String gitProjectId = (String) execution.getVariable("git_project");

        //Find out if Git was successfully created, maybe its better to test if its really created, by trying to connect to the branch, instead of asking for Variables
        boolean gitBranchCreated;
        gitBranchCreated = execution.getVariable("git_branch_created") != null && (boolean) execution.getVariable("git_branch_created");

        //Create the URL for a REST-Api Call and send it to the GitLab Server
        if (gitBranchCreated) {
            String DeleteBranchUrl = gitUrl
                    + "/api/v4/projects/"
                    + gitProjectId
                    + "/repository/branches/"
                    + gitBranchName;

            HttpURLConnection c = (HttpURLConnection) new URL(DeleteBranchUrl).openConnection();
            c.setRequestMethod("DELETE");
            c.setRequestProperty("PRIVATE-TOKEN", gitToken);

            System.out.println("Git Delete Branch Message: " + c.getResponseMessage());
            execution.setVariable("git_branch_created", false);
        } else {
            System.out.println("Could not delete Branch, please contact an Administrator");
        }

    }

}