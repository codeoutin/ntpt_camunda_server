package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;


@Service("createBranchAdapter")
public class BranchCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String branch_name = (String) execution.getVariable("git_branch_name");
        String gitUrl = (String) execution.getVariable("git_url");
        String gitToken = (String) execution.getVariable("git_token");
        String gitProjectId = (String) execution.getVariable("git_project");
        String gitCommitId = (String) execution.getVariable("git_commit");

        gitUrl = "http://localhost:8002";
        gitProjectId = "3";
        gitCommitId = "HEAD";

        String CreateBranchUrl = gitUrl
                + "/api/v4/projects/"
                + gitProjectId
                + "/repository/branches?branch="
                + branch_name
                + "&ref="
                + gitCommitId;

        // output soll ca. so aussehen:
        // http://localhost:8002/api/v4/projects/3/repository/branches?branch=NEUERBRANCH&ref=HEAD

        // Token als POST Header mitsenden: "PRIVATE-TOKEN: wAdcZ3ZnnRcc5dk7Wwyn"


        //InputStream response = new URL(CreateBranchUrl).openStream();
        HttpURLConnection c =  (HttpURLConnection) new URL(CreateBranchUrl).openConnection();
        c.setRequestMethod("POST");
        c.setRequestProperty("PRIVATE-TOKEN", "wAdcZ3ZnnRcc5dk7Wwyn");
        System.out.println(c.getResponseMessage());

        /*System.out.println("\n######\n");
        System.out.println("NOW WE WOULD CREATE A GIT BRANCH");
        System.out.println(response);
        System.out.println("\n######\n");*/
    }
}