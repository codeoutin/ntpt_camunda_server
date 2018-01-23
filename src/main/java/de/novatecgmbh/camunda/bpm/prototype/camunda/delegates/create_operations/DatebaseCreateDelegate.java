package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import javax.json.Json;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a Database on a MongoDB Server
 * @author Patrick Steger
 */
@Service("createDatabaseAdapter")
public class DatebaseCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String dbName = (String) execution.getVariable("prefix");
        String dbUrl = (String) execution.getVariable("db_url");

        if (dbUrl != null) {
            System.out.println("\n# createDatabaseAdapter @ " + dbName + " #\nTry to create MongoDB-Database...");
            try {
                // Add http to url (dont do this before or if-check doesnt work!
                dbUrl = "http://" + dbUrl;
                // Split URL into host + port
                // Match 1: Protocol, Match 2: Base URL, Match 3: Port, Match 4: Everything else
                Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?");
                Matcher matcher = pattern.matcher(dbUrl);
                matcher.find();

                String domain   = matcher.group(2);
                // Remove all non-digit symbols (:)
                int port     = Integer.parseInt(matcher.group(3).replaceAll("\\D+",""));

                // Connect to MongoDB Driver
                MongoClient mongoClient = new MongoClient(domain, port);
                MongoDatabase db = mongoClient.getDatabase(dbName);

                // Create a sample document to actually create the new database
                Document doc = new Document("name", "MongoDB")
                        .append("type", "database");
                MongoCollection<Document> collection = db.getCollection("doc");
                collection.insertOne(doc);

                // Create Return JSON
                String returnJson = Json.createObjectBuilder()
                        .add("database_name", dbName)
                        .add("database_url", "mongodb://" + domain + ":" + port + "/" + dbName)
                        .build()
                        .toString();

                // Log to Console
                System.out.println("Database " + dbName + " created.");

                // Set Process Variable for later checks
                execution.setVariable("db_created", true);
                execution.setVariable("return_database", returnJson);
            } catch (Exception e) {
                execution.setVariable("db_created", false);
                execution.setVariable("test_environment_created", false);
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while creating a Database. Error Message: " + e.getMessage());
                throw new BpmnError("CreateError", e.getMessage());
            }
        }
    }

}