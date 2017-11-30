package de.novatecgmbh.camunda.bpm.prototype.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;


@Service("closeBranchAdapter")
public class GitCloseDelegate implements JavaDelegate {

  public void execute(DelegateExecution execution) throws Exception {
    String branch_name = (String) execution.getVariable("branch_name");
	  
	System.out.println("\n\n\n######\n\n\n");
	System.out.println("NOW WE WOULD CLOSE THE GIT BRANCH " + branch_name);
	System.out.println("\n\n\n######\n\n\n");
  }

}