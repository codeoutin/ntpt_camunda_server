package de.novatecgmbh.camunda.bpm.prototype;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("emptyArtifactsAdapter")
public class OptArtifactsCloseDelegate implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		boolean database = (Boolean) execution.getVariable("database");
		boolean buildPipeline = (Boolean) execution.getVariable("buildPipeline");
		boolean testEnv = (Boolean) execution.getVariable("testEnv");
		String sonarqubeProfile = (String) execution.getVariable("sonarqubeProfile");
		
		if (database) {
	    	System.out.println("\n######\n");
	        System.out.println("NOW WE WOULD EMPTY AND DELETE THE DATABASE");
	        System.out.println("\n######\n");
	    }
		
	    if (buildPipeline) {
	    	System.out.println("\n######\n");
	        System.out.println("NOW WE WOULD CLOSE THE BUILDPIPELINE");
	        System.out.println("\n######\n");
	    }
	    
	    if (testEnv) {
	    	System.out.println("\n######\n");
	    	System.out.println("NOW WE WOULD DELETE THE TEST ENVIRONMENT");
	    	System.out.println("\n######\n");
	    }
	    
	    if (!sonarqubeProfile.equals("no")) {
	    	System.out.println("\n######\n");
	    	System.out.println("NOW WE WOULD CLOSE THE SONARQUBE QA");
	    	System.out.println("\n######\n");
	    }
		
	}

}
