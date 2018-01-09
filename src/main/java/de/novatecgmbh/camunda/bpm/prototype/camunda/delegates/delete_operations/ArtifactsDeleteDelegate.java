package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.delete_operations;

import com.mongodb.MongoClient;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("emptyArtifactsAdapter")
public class ArtifactsDeleteDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        // One Class to Destroy them all 8)
        boolean dbCreated;
        boolean bpCreated;
        boolean testEnvCreated;
        boolean sqCreated;

        //Castings
        if (execution.getVariable("db_created") == null)
            dbCreated = false;
        else
            dbCreated = (boolean) execution.getVariable("db_created");

        if (execution.getVariable("bp_created") == null)
            bpCreated = false;
        else
            bpCreated = (boolean) execution.getVariable("bp_created");

        if (execution.getVariable("test_environment_created") == null)
            testEnvCreated = false;
        else
            testEnvCreated = (boolean) execution.getVariable("test_environment_created");
        if (execution.getVariable("sonarqube_created") == null)
            sqCreated = false;
        else
            sqCreated = (boolean) execution.getVariable("sonarqube_created");

        // Create Prefix
        String prefix = (String) execution.getVariable("prefix");

        // Destroy Database
        if (dbCreated) {
            String dbUrl = "http://" + execution.getVariable("db_url"); // MUST be in format http://host:port
            dropDatabase(dbUrl, prefix);
            execution.setVariable("db_created", false);
        }

        // Destroy Jenkins BuildPipeline
        if (bpCreated) {
            String jenkinsUrl = "http://" + execution.getVariable("bp_url");
            List<String> dmn_output = (List<String>) execution.getVariable("dmn_output");
            for (int i = 0; i < dmn_output.size(); i++) {
                dropBP(jenkinsUrl, prefix + "_" + dmn_output.get(i));
            }
            execution.setVariable("bp_created", false);
        }

        // "Destroy" Test Environment
        if (testEnvCreated) {
            dropTestEnv("/var/home/root/environments", prefix);
            execution.setVariable("test_environment_created", false);
        }

        //Destroy SonarQube Project
        if (sqCreated) {
            String sqUrl = "http://" + execution.getVariable("sonarqube_url");
            dropSonarqubeProject(sqUrl, prefix);
            execution.setVariable("sonarqube_created", false);
        }
    }

    /**
     * Deletes a MongoDB Database
     * @param url MongoDB URL + Port
     * @param database Name of Database
     */
    private void dropDatabase(String url, String database) {
        // Split URL into host + port
        Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?");
        Matcher matcher = pattern.matcher(url);
        matcher.find();

        String domain = matcher.group(2);
        int port = Integer.parseInt(matcher.group(3).replaceAll("\\D+", ""));

        // Connect to MongoDB Driver
        MongoClient mongoClient = new MongoClient(domain, port);
        mongoClient.dropDatabase(database);
        System.out.println("###\nDB " + database + " deleted\n###");
    }

    /**
     * Simulates Erasure of a Test Environment, since we dont create one, we are not able to delete one
     * @param url Test Environment URL
     * @param name Name of Test Environment
     */
    private void dropTestEnv(String url, String name) {
        System.out.println("###\nTest Environment located at " + url + "/" + name + " deleted\n###");
    }

    /**
     * Deletes a Jenkins Job
     * @param url Jenkins URL
     * @param job Jenkins Job Name
     */
    private void dropBP(String url, String job) {
        try {
            String CreateDeleteUrl = url
                    + "/job/"
                    + job
                    + "/doDelete";

            HttpURLConnection c =  (HttpURLConnection) new URL(CreateDeleteUrl).openConnection();
            c.setRequestMethod("POST");

            System.out.println("###\nJenkins Job " + job + " deleted\n###");
            System.out.println("Message: " + c.getResponseMessage() + "\n#####\n");
        } catch (Exception e) {
            // Space for Exception Handling
            // throw new BpmnError("CreateError");
        }
    }

    /**
     * Deletes a SonarQube Project
     * @param url SonarQube URL
     * @param project SonarQube Project Name
     */
    private void dropSonarqubeProject(String url, String project) {
        try {
            //Make URL to Delete a Project using SQ REST Api
            String deleteUrl = url+ "/api/projects/delete";
            String deleteParams  = "project="+project;
            byte[] deleteData = deleteParams.getBytes( StandardCharsets.UTF_8 );
            String basicAuth = Base64.getEncoder().encodeToString(("admin:admin").getBytes(StandardCharsets.UTF_8));

            //Delete the SQ Project
            HttpURLConnection delConn =  (HttpURLConnection) new URL(deleteUrl).openConnection();
            delConn.setDoOutput(true);
            delConn.setRequestMethod("POST");
            delConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            delConn.setRequestProperty("charset", "utf-8");
            delConn.setRequestProperty("Authorization", "Basic " + basicAuth);
            delConn.setRequestProperty("Content-Length", Integer.toString(deleteData.length ));
            delConn.setUseCaches(false);
            try(DataOutputStream d = new DataOutputStream(delConn.getOutputStream())) {
                d.write( deleteData );
            }
            System.out.println("SonarQube Profile deleted. Code " + delConn.getResponseCode());
        } catch (Exception e) {
            // Space for Exception Handling
        }
    }
}
