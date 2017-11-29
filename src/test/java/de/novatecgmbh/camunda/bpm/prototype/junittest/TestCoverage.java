package de.novatecgmbh.camunda.bpm.prototype.junittest;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

@Deployment(resources = { "pipeline.bpmn" })
public class TestCoverage {
	@Rule
	@ClassRule
	public static ProcessEngineRule processEngine = TestCoverageProcessEngineRuleBuilder.create().build();
	
	private RuntimeService runtimeService = null;
	@SuppressWarnings("unused")
	private TaskService taskService = null;

//	@Before
//	public void setUp() throws Exception {
//		runtimeService = processEngine.getRuntimeService();
//		taskService = processEngine.getTaskService();
//	}
//
//	@Test
//	public void testHappyPath() {
//		VariableMap variables = Variables.createVariables().putValue("errorCode", 0);
//
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TestBuildPipeline", variables);
//		assertThat(processInstance).isEnded();
//	}
//
//	@Test
//	public void testThrowError() {
//		VariableMap variables = Variables.createVariables().putValue("errorCode", 1000);
//
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TestBuildPipeline", variables);
//		assertThat(processInstance).isEnded();
//	}

}
