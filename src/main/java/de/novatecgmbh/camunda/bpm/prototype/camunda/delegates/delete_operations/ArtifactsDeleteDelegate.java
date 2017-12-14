package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.delete_operations;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("emptyArtifactsAdapter")
public class ArtifactsDeleteDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        // One Class to Destroy them all 8)
        Boolean dbCreated = (Boolean) execution.getVariable("db_created");
        Boolean bpCreated = (Boolean) execution.getVariable("bp_created");
        Boolean testEnvCreated = (Boolean) execution.getVariable("test_environment_created");


        // Create Prefix
        String prefix = (String) execution.getVariable("prefix");

        // Destroy Database
        if (dbCreated && dbCreated != null) {
            String dbUrl = "http://" + execution.getVariable("db_url"); // MUST be in format http://host:port
            dropDatabase(dbUrl, prefix);
        }

        // Destroy Jenkins
        if (bpCreated && bpCreated != null) {
            String jenkinsUrl = "http://" + execution.getVariable("bp_url");
            dropBP(jenkinsUrl, "jobnummer1");
        }

        // Destroy Test Environment
        if (testEnvCreated && testEnvCreated != null) {
            Boolean testEnv = (Boolean) execution.getVariable("test_environment");
            dropTestEnv("/var/home/herbert/environment", prefix);
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
        System.out.println("###\nJenkins Job " + url + "/" + job + " deleted\n###");
    }
}
