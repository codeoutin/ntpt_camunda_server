package de.novatecgmbh.camunda.bpm.prototype.camunda.delegates.create_operations;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Creates Jenkins Jobs based on DMN Output. To create a job we always use the same template and just change the name
 * For further development, you could use a (XML) Template Engine to customize the Job based on your needs.
 * @author Patrick Steger
 */
@Service("createBPAdapter")
public class BuildpipelineCreateDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String prefix = (String) execution.getVariable("prefix");
        String jenkinsUrl = "http://" + execution.getVariable("bp_url");

        if (jenkinsUrl != null) {
            try {
                //Collect the DMN Output in a new List
                List<String> dmn_output = (List<String>) execution.getVariable("dmn_output");

                for (int i = 0; i < dmn_output.size(); i++) {

                    //Make URL to create a single Jenkins Job
                    String CreateJobUrl = jenkinsUrl
                            + "/createItem?name="
                            + prefix
                            + "_"
                            + dmn_output.get(i);

                    //Use config.xml as Template for the Job and post it to Jenkins REST-Api
                    File input = new File("src/main/resources/config.xml");
                    PostMethod post = new PostMethod(CreateJobUrl);
                    post.setRequestEntity(new InputStreamRequestEntity(
                            new FileInputStream(input), input.length()));
                    post.setRequestHeader(
                            "Content-type", "text/xml; charset=ISO-8859-1");
                    HttpClient httpclient = new HttpClient();
                    int result = httpclient.executeMethod(post);

                    System.out.println("\n#####\n Create Jenkins Job for " + dmn_output.get(i) + ". Status Code: " + result + "\n#####\n");
//                    System.out.println("Response body: ");
//                    System.out.println(post.getResponseBodyAsString());
                }

                execution.setVariable("bp_created", true);
            } catch (Exception e) {
                execution.setVariable("bp_created", false);
                execution.setVariable("db_created", false);
                execution.setVariable("test_environment_created", false);
                execution.setVariable("sonarqube_created", false);
                System.out.println("Error while creating a Build Pipeline. Task cancelled.");
                System.out.println(e.getMessage());
                throw new BpmnError("CreateError");
            }
        }
    }

}