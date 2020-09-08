/**
 * @Title: ActivitiBasicTests.java
 * @Package com.osxm.springbootactiviti
 * @Description: TODO
 * @author oscarchen
 * @date 2020年9月9日
 * @version V1.0
 */
package com.osxm.springbootactiviti;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName: ActivitiBasicTests
 * @Description: TODO
 * @author oscarchen
 */

@SpringBootTest
public class ActivitiBasicTests {

	@Autowired
	private RuntimeService runtimeService;

	@Test
	public void queryProcessInstances() {
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		List<ProcessInstance> processInstanceList = processInstanceQuery.processDefinitionKey("myTimeoffProcess")
				.list();
		System.out.println("========="+processInstanceList.size());
		for(ProcessInstance processInstance:processInstanceList) {
			System.out.println("ID= " +processInstance.getId());
			System.out.println("Name= "+processInstance.getName());
			System.out.println("Definition Name= "+processInstance.getProcessDefinitionName());
			System.out.println("Definition Id= "+processInstance.getProcessDefinitionId());
			System.out.println("Instance ID= "+processInstance.getProcessInstanceId());
		}

	}
}
