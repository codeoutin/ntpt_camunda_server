package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


@Service("createBPAdapter")
public class BuildpipelineCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String branch_name = (String) execution.getVariable("branch_name");
        String gitProjectId = (String) execution.getVariable("git_project");
        // ^ to create the prefix

        String jenkinsUrl = (String) execution.getVariable("jenkins_url");
        String jenkinsUser = (String) execution.getVariable("jenkins_user");
        String jenkinsPw = (String) execution.getVariable("jenkins_pw"); //maybe besser mit Token

        List<String> dmn_output = (List<String>) execution.getVariable("dmn_output");
        boolean buildPipeline = (Boolean) execution.getVariable("buildPipeline"); //maybe weg? => auch im frontend weg!


        if (buildPipeline) {
            System.out.println("\n######\n");
            System.out.println("NOW WE WOULD CREATE A NEW BUILDPIPELINE");
            System.out.println("PROFILES FOR BP: ");

            dmn_output.forEach(System.out::println);

            System.out.println("\n######\n");
        }
    }

}