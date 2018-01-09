package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Creates a SonarQube Project and assigns the selected Quality Gate to the Project
 * @author Patrick Steger
 */
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
                //Make URL to Create a SQ Project
                String createUrl = sqUrl+ "/api/projects/create";
                String createParams  = "name="+prefix+"&project="+prefix;
                byte[] createData = createParams.getBytes( StandardCharsets.UTF_8 );

                //Create the SQ Project
                HttpURLConnection createConn =  (HttpURLConnection) new URL(createUrl).openConnection();
                createConn.setDoOutput(true);
                createConn.setRequestMethod("POST");
                createConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                createConn.setRequestProperty("charset", "utf-8");
                createConn.setRequestProperty("Content-Length", Integer.toString(createData.length ));
                createConn.setUseCaches(false);
                try(DataOutputStream wr = new DataOutputStream(createConn.getOutputStream())) {
                    wr.write( createData );
                }

                //Make URL to Associate a Project to a Quality Gate
                String assignUrl = sqUrl + "/api/qualitygates/select";
                String assignParams  = "gateId="+sqProfile+"&projectKey="+prefix;
                byte[] assignData = assignParams.getBytes( StandardCharsets.UTF_8 );

                //Assign the Project to the Gate
                HttpURLConnection assignConn =  (HttpURLConnection) new URL(assignUrl).openConnection();
                assignConn.setDoOutput(true);
                assignConn.setRequestMethod("POST");
                assignConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                assignConn.setRequestProperty("charset", "utf-8");
                assignConn.setRequestProperty("Content-Length", Integer.toString(assignData.length ));
                assignConn.setUseCaches(false);
                try(DataOutputStream assignStream = new DataOutputStream(assignConn.getOutputStream())) {
                    assignStream.write( assignData );
                }

                execution.setVariable("sonarqube_created", true);
                System.out.println("\n#####\n SonarQube Create Profile: " + createConn.getResponseCode() + "\n#####\n");
                System.out.println("\n#####\n SonarQube Assign Quality Gate: " + assignConn.getResponseCode() + "\n#####\n");
            } catch (Exception e) {
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while at SonarQube Task. Other Tasks cancelled.");
                throw new BpmnError("CreateError");
            }
        }
    }
}