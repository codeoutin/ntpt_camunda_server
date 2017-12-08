package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;


@Service("createTestEnvAdapter")
public class TestEnvCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String branch_name = (String) execution.getVariable("branch_name");
        String gitProjectId = (String) execution.getVariable("git_project");
        // ^ to create the prefix

        // Überlegung: Ordner auf Netzlaufwerk oder Dummy Test

        System.out.println("\n######\n");
        System.out.println("NOW WE WOULD CREATE A TEST ENV");
        System.out.println("\n######\n");
    }

}