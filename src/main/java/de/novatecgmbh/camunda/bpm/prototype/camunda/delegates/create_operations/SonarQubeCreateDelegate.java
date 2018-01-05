package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("createSQAdapter")
public class SonarQubeCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String prefix = (String) execution.getVariable("prefix");

        String sqUrl = "http://" + execution.getVariable("sonarqube_url");
        //String sqUser = (String) execution.getVariable("sq_user");
        //String sqPw = (String) execution.getVariable("sq_pw");
        String sqProfile = (String) execution.getVariable("sonarqube_profile");

        if(sqProfile != "no" && sqUrl != null) {
            try {



                execution.setVariable("sonarqube_created", true);
                System.out.println("\n######\n");
                System.out.println("NOW WE WOULD CREATE A SQ GATE WITH THE PROFILE: " + sqProfile);
                System.out.println("\n######\n");
            } catch (Exception e) {
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while at SonarQube Task. Other Tasks cancelled.");
                throw new BpmnError("CreateError");
            }
        }
    }
}