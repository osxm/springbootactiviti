/**
 * @Title: DefaultActivitiController.java
 * @Package com.osxm.springbootactiviti.controller
 * @Description: TODO
 * @author oscarchen
 * @date 2020年9月5日
 * @version V1.0
 */
package com.osxm.springbootactiviti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: DefaultActivitiController
 * @Description: TODO
 * @author oscarchen
 */
@RestController
@RequestMapping(value = "/activiti")
public class DefaultActivitiController {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@RequestMapping(value = "/startProcess")
	@ResponseBody
	public String startProcess() {
		StringBuffer rtnMsgBuf = new StringBuffer();
		String processId = "myTimeoffProcess";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("requester", "oscar");
		map.put("manager", "oscar");
		map.put("hr", "oscar");
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(processId, map);
		rtnMsgBuf.append("流程实例启动成功:<br>");
		rtnMsgBuf.append("流程实例ID:" + instance.getId() + "<br>");
		rtnMsgBuf.append("流程定义ID:" + instance.getProcessDefinitionId());

		return rtnMsgBuf.toString();
	}

	@RequestMapping(value = "/queryProcess")
	@ResponseBody
	public String queryProcess(String processInstanceId) {
		StringBuffer rtnMsgBuf = new StringBuffer();
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		List<ProcessInstance> runningList = processInstanceQuery.processInstanceId(processInstanceId).list();
		rtnMsgBuf.append("查询到的流程数目:" + runningList.size() + "<br>");
		return rtnMsgBuf.toString();
	}

	@RequestMapping(value = "/queryTask")
	@ResponseBody
	public List<Map<String, String>> queryTask() {
		List<Task> taskList = taskService.createTaskQuery().list();
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		for (Task task : taskList) {
			Map<String, String> map = getTaskMap(task);
			resultList.add(map);
		}
		return resultList;
	}
	
	@RequestMapping(value = "/getTask")
	@ResponseBody
	public Map<String, Object> getTask(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		Map<String, Object> paramMap = taskService.getVariables(taskId);
		map.put("task", getTaskMap(task));
		map.put("paramMap", paramMap);
		return map;
	}
	
	
	@RequestMapping(value = "/completeTask")
	@ResponseBody
	public String completeTask(String taskId) {
		taskService.complete(taskId);
		return "Success";

	}
	
	
	
	
	
	private Map<String, String> getTaskMap(Task task){
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskId", task.getId());
		map.put("name", task.getName());
		map.put("createTime", task.getCreateTime().toString());
		map.put("assignee", task.getAssignee());
		map.put("instanceId", task.getProcessInstanceId());
		map.put("executionId", task.getExecutionId());
		map.put("definitionId", task.getProcessDefinitionId());
		return map;
		
	}

}
