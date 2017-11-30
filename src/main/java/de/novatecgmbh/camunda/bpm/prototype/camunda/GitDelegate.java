package de.novatecgmbh.camunda.bpm.prototype.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.io.InputStream;
import java.net.URL;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;


@Service("gitAdapter")
public class GitDelegate implements JavaDelegate {

  public void execute(DelegateExecution execution) throws Exception {
    String branch_name = (String) execution.getVariable("branch_name");
    String url = "https://gturnquist-quoters.cfapps.io/api/random";
    String charset = java.nio.charset.StandardCharsets.UTF_8.name();
    
    InputStream response = new URL(url).openStream();
	  
	System.out.println("\n######\n");
	System.out.println("NOW WE WOULD CREATE A GIT BRANCH");
	System.out.println(response);
	System.out.println("\n######\n");
  }

}