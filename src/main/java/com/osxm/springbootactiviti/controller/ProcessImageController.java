/**
 * @Title: ProcessImageController.java
 * @Package com.osxm.springbootactiviti.controller
 * @Description: TODO
 * @author oscarchen
 * @date 2020年9月9日
 * @version V1.0
 */
package com.osxm.springbootactiviti.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: ProcessImageController
 * @Description: TODO
 * @author oscarchen
 */
@Controller
public class ProcessImageController {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private ProcessDiagramGenerator processDiagramGenerator;

	@Autowired
	private HistoryService historyService;

	@RequestMapping("/activiti/exportImage")
	public void exportImage(HttpServletResponse response, String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		String processDefinitionId = processInstance.getProcessDefinitionId();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		HistoricActivityInstanceQuery historyInstanceQuery = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId);

		response.setContentType("text/html;charset=utf-8");
		// 查询历史节点
		List<HistoricActivityInstance> historicActivityInstanceList = historyInstanceQuery
				.orderByHistoricActivityInstanceStartTime().asc().list();
		List<String> executedActivityIdList = historicActivityInstanceList.stream().map(item -> item.getActivityId())
				.collect(Collectors.toList());
		InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, executedActivityIdList);
		
		byte[] b = new byte[1024];
		int len;
		try {
			while ((len = imageStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
			response.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { // 流关闭
			try {
				imageStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
