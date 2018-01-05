package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;


@Service("createTestEnvAdapter")
public class TestEnvCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String prefix = (String) execution.getVariable("prefix");

        if ((Boolean) execution.getVariable("test_environment")) {
            try {
                // Überlegung: Ordner auf Netzlaufwerk oder Dummy Test

                execution.setVariable("test_environment_created", true);
                System.out.println("\n######\n");
                System.out.println("NOW WE WOULD CREATE A TEST ENV");
                System.out.println("\n######\n");
            } catch (Exception e) {
                execution.setVariable("test_environment_created", false);
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while creating a Test Environment. Task cancelled.");
                throw new BpmnError("CreateError");
            }
        } else {
            execution.setVariable("test_environment_created", false);
        }
    }

}