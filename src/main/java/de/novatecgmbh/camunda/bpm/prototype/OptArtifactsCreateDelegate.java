package de.novatecgmbh.camunda.bpm.prototype;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("createArtifactsAdapter")
public class OptArtifactsCreateDelegate implements JavaDelegate {

  public void execute(DelegateExecution execution) throws Exception {
	String branch_name = (String) execution.getVariable("branch_name");
	List<String> dmn_output = (List<String>) execution.getVariable("dmn_output");
	boolean database = (Boolean) execution.getVariable("database");
	boolean buildPipeline = (Boolean) execution.getVariable("buildPipeline");
	boolean testEnv = (Boolean) execution.getVariable("testEnv");
	String sonarqubeProfile = (String) execution.getVariable("sonarqubeProfile");
	
	System.out.println(execution.getVariable("dmn_output"));
	
	if (database) {
		System.out.println("\n######\n");
		System.out.println("NOW WE WOULD CREATE A NEW DATABASE TO STORE ALL YOUR INFORMATION");
		System.out.println("NAME OF THE DB: " + branch_name);
		System.out.println("\n######\n");		
	}
	
    if (buildPipeline) {
    	System.out.println("\n######\n");
        System.out.println("NOW WE WOULD CREATE A NEW BUILDPIPELINE");
        System.out.println("PROFILES FOR BP: ");
        
        dmn_output.forEach(System.out::println);
        
        System.out.println("\n######\n");
    }
    
    if (testEnv) {
    	System.out.println("\n######\n");
    	System.out.println("NOW WE WOULD CREATE A TEST ENVIRONMENT");
    	System.out.println("\n######\n");
    }
    
    if (!sonarqubeProfile.equals("no")) {
    	System.out.println("\n######\n");
    	System.out.println("NOW WE WOULD CREATE A NEW QA WITH THE HELP OF SONARQUBE");
    	System.out.println("SELECTED PROFILE: " + sonarqubeProfile);
    	System.out.println("\n######\n");
    }
    
  }
  
}