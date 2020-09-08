/**
 * @Title: ActivitiProcessConfig.java
 * @Package com.osxm.springbootactiviti.config
 * @Description: TODO
 * @author oscarchen
 * @date 2020年9月9日
 * @version V1.0
 */
package com.osxm.springbootactiviti.config;

import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ActivitiProcessConfig
 * @Description: TODO
 * @author oscarchen
 */

@Configuration
public class ActivitiProcessConfig {

	@Bean
	public ProcessDiagramGenerator processDiagramGenerator() {
		return new DefaultProcessDiagramGenerator();
	}
}
