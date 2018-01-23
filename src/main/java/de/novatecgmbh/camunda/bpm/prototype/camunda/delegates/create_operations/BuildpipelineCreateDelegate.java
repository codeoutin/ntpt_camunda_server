package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 * Creates Jenkins Jobs based on DMN Output. To create a job we always use the same template and just change the name
 * For further development, you could use a (XML) Template Engine to customize the Job based on your needs.
 * @author Patrick Steger
 */
@Service("createBPAdapter")
public class BuildpipelineCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String prefix = (String) execution.getVariable("prefix");
        String jenkinsUrl = (String) execution.getVariable("bp_url");

        if (jenkinsUrl != null) {
            System.out.println("\n# createBPAdapter @ " + prefix + " #\nTry to create Jenkins Jobs...");
            try {
                //Collect the DMN Output in a new List
                List<String> dmn_output = (List<String>) execution.getVariable("dmn_output");
                int jobCount = 0;
                // Create Return JSON
                JsonArrayBuilder builder = Json.createArrayBuilder();


                while (jobCount < dmn_output.size()) {
                    //Make URL to create a single Jenkins Job
                    String CreateJobUrl = "http://"
                            + jenkinsUrl
                            + "/createItem?name="
                            + prefix
                            + "_"
                            + dmn_output.get(jobCount);

                    //Use config.xml as Template for the Job and post it to Jenkins REST-Api
                    File input = new File("src/main/resources/config.xml");
                    PostMethod post = new PostMethod(CreateJobUrl);
                    post.setRequestEntity(new InputStreamRequestEntity(
                            new FileInputStream(input), input.length()));
                    post.setRequestHeader(
                            "Content-type", "text/xml; charset=ISO-8859-1");
                    HttpClient httpclient = new HttpClient();
                    int result = httpclient.executeMethod(post);

                    if(result / 100 == 2) {
                        //Add to Return JSON
                        builder.add(Json.createObjectBuilder()
                                .add("id", jobCount)
                                .add("value", dmn_output.get(jobCount))
                        );

                        System.out.println(jobCount + ". Created Jenkins Job for " + dmn_output.get(jobCount) + ". Http Status Code: " + result);
                        jobCount++;
                    } else {
                        throw new Exception("Could not create Jenkins Job");
                    }
                }

                //Finish Return JSON
                JsonArray returnJson = builder.build();
                System.out.println(returnJson.toString());

                // Log to Console
                System.out.println("Build Pipeline with " + jobCount + " Jobs created.");

                // Set Process Variable for later checks
                execution.setVariable("bp_created", true);
                execution.setVariable("return_bp", returnJson.toString());
            } catch (Exception e) {
                execution.setVariable("bp_created", false);
                execution.setVariable("db_created", false);
                execution.setVariable("test_environment_created", false);
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while creating a Build Pipeline. Error Message: " + e.getMessage());
                throw new BpmnError("CreateError");
            }
        }
    }

}