package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


@Service("createBPAdapter")
public class BuildpipelineCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String prefix = execution.getVariable("git_project")
                + "_"
                + execution.getVariable("git_branch_name");

        String jenkinsUrl = (String) execution.getVariable("bp_url");
        if (jenkinsUrl != null) {
            try {
                //String jenkinsUser = (String) execution.getVariable("jenkins_user");
                //String jenkinsPw = (String) execution.getVariable("jenkins_pw"); //maybe besser mit Token

                List<String> dmn_output = (List<String>) execution.getVariable("dmn_output");

                execution.setVariable("bp_created", true);
                System.out.println("\n######\n");
                System.out.println("NOW WE WOULD CREATE A NEW BUILDPIPELINE");
                System.out.println("PROFILES FOR BP: ");
                dmn_output.forEach(System.out::println);
                System.out.println("\n######\n");
            } catch (Exception e) {
                execution.setVariable("bp_created", false);
                execution.setVariable("db_created", false);
                execution.setVariable("test_environment_created", false);
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while creating a Build Pipeline. Task cancelled.");
                throw new BpmnError("CreateError");
            }
        }
    }

}