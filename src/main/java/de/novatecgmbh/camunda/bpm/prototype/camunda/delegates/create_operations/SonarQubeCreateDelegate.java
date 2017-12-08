package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;


@Service("createSQAdapter")
public class SonarQubeCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String branch_name = (String) execution.getVariable("branch_name");
        String gitProjectId = (String) execution.getVariable("git_project");
        // ^ to create the prefix

        String sqUrl = (String) execution.getVariable("sq_url");
        String sqUser = (String) execution.getVariable("sq_user");
        String sqPw = (String) execution.getVariable("sq_pw");
        String sqProfile = (String) execution.getVariable("sq_profile");

        //InputStream response = new URL(url).openStream();

        System.out.println("\n######\n");
        System.out.println("NOW WE WOULD CREATE A GIT BRANCH");
        //System.out.println(response);
        System.out.println("\n######\n");
    }

}