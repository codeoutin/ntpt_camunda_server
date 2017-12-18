package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.delete_operations;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
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

        // Destroy Test Environment
        if (testEnvCreated) {
            Boolean testEnv = (Boolean) execution.getVariable("test_environment");
            dropTestEnv("/var/home/root/environments", prefix);
            execution.setVariable("test_environment_created", false);
        }
    }

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

    private void dropTestEnv(String url, String name) {
        System.out.println("###\nTest Environment located at " + url + "/" + name + " deleted\n###");
    }

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
            System.out.println("Error while creating a new Git Branch. Task cancelled.");
            throw new BpmnError("CreateError");
        }
    }
}
