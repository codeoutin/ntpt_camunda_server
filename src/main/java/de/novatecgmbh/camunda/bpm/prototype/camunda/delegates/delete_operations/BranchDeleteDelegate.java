package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.delete_operations;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;


@Service("closeBranchAdapter")
public class BranchDeleteDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String branch_name = (String) execution.getVariable("branch_name");
        String gitUrl = (String) execution.getVariable("git_url");
        String gitToken = (String) execution.getVariable("git_token");
        String gitProjectId = (String) execution.getVariable("git_project");
        String gitCommitId = (String) execution.getVariable("git_commit");

        String CreateBranchUrl = gitUrl
                + "/api/v4/projects/"
                + gitProjectId
                + "/repository/branches/"
                + branch_name;

        // output soll ca. so aussehen:
        // http://localhost:8002/api/v4/projects/3/repository/branches?branch=NEUERBRANCH&ref=HEAD

        // Token als POST Header mitsenden: "PRIVATE-TOKEN: wAdcZ3ZnnRcc5dk7Wwyn"


        InputStream response = new URL(CreateBranchUrl).openStream();

        System.out.println("\n######\n");
        System.out.println("Branch " + branch_name + " deleted");
        System.out.println(response);
        System.out.println("\n######\n");
    }

}