package de.novatecgmbh.camunda.bpm.prototype.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;


@Service("NotificationAdapter")
public class NotificationDelegate implements JavaDelegate {
	public void execute(DelegateExecution execution) throws Exception {
	    String additionalArtifactsDesc = (String) execution.getVariable("additionalArtifactsDesc");
		
	    //System.out.println(">> current id: " + execution.getCurrentActivityId());

	    if(execution.getCurrentActivityId().equals("RequestArtifactsNotification")) {
	    	System.out.println("\n######\n");
	    	System.out.println("NOW WE WOULD SEND A NOTIFICATION TO SYSADMIN TO CREATE THE ADDITIONAL ARTIFACTS");
	    	System.out.println("To Sys-Admin: \n" + additionalArtifactsDesc);
	    	System.out.println("\n######\n");	    	
	    }
	    
	    
	    if(execution.getCurrentActivityId().equals("CloseTimerNotification")) {
	    	System.out.println("\n######\n");
	    	System.out.println("NOW WE WOULD SEND A REMINDER TO THE DEV TO ASK IF HE WANTS TO DELETE THE ARTIFACTS NOW");
	    	System.out.println("\n######\n");	    	
	    }
	    
	    if(execution.getCurrentActivityId().equals("CloseExtraArtifactsNotification")) {
	    	System.out.println("\n######\n");
	    	System.out.println("NOW WE WOULD SEND A NOTIFICATION TO SYSADMIN TO CLOSE THE ADDITIONAL ARTIFACTS");
	    	System.out.println("\n######\n");	    	
	    }
	}
}
