package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.delete_operations;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@Service("closeBranchAdapter")
public class BranchDeleteDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String gitBranchName = (String) execution.getVariable("git_branch_name");
        String gitUrl = "http://" + execution.getVariable("git_url");
        String gitToken = (String) execution.getVariable("git_token");
        String gitProjectId = (String) execution.getVariable("git_project");

        boolean gitBranchCreated;
        if (execution.getVariable("git_branch_created") == null)
            gitBranchCreated = false;
        else
            gitBranchCreated = (boolean) execution.getVariable("git_branch_created");

        if (gitBranchCreated) {
            String DeleteBranchUrl = gitUrl
                    + "/api/v4/projects/"
                    + gitProjectId
                    + "/repository/branches/"
                    + gitBranchName;

            HttpURLConnection c =  (HttpURLConnection) new URL(DeleteBranchUrl).openConnection();
            c.setRequestMethod("DELETE");
            c.setRequestProperty("PRIVATE-TOKEN", gitToken);

            System.out.println("Git Delete Branch Message: " + c.getResponseMessage());
            execution.setVariable("git_branch_created", false);
        } else {
            System.out.println("Could not delete Branch, please contact an Administrator");
        }

    }

}