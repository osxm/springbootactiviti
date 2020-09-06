# springbootactiviti
Framework For Spring Boot + Security+Activiti (基于Spring Boot与Activiti 的工作流开发)


## 前言： Java工作流引擎与Activiti
   工作流（Workflow）是业务流程在计算机上的自动化，工作流引擎是工作流作为应用系统的一部分，提供工作流需要的基础设施的解决方案， 工作流引擎包括节点管理、任务管理、流向管理等基本功能。
   
 Java领域的开源工作流引擎就不得不提JBPM了，其全程是Java Business Process Management（Java业务流程管理），JBPM的创始人是Tom Baeyens， 后来该项目被RedHat收购，成为JBoss应用平台的一部分。传闻Tom Baeyens在JBPM的发展上与Redhat存在分歧，Tom Baeyens从JBPM团队出来，加入Alfresco，在JBPM3，JBPM4 的基础发展了Activiti 5。
	
 Activiti5使用Spring进行引擎配置以及各个Bean的管理，综合使用IoC和AOP技术，使用CXF作为Web Services实现的基础，使用MyBatis进行底层数据库ORM的管理，预先提供Bundle化包能较容易地与OSGi进行集成


综合以上， Spring 与 Activiti5 用于搭建工作流框架是不错的选择，Activiti5也提供了Spring Boot的原生整合。


## 框架搭建环境与步骤总览

这里在Windows 10下，使用Eclipse IDE开发，在Eclipse中，可以安装Activiti插件，直接绘制流程图。activiti需要保存保存流程定义信息以及流程实例，所以需要数据库的支持，学习和测试可以直接使用H2内存数据库，这里选用MySQL数据库。具体环境如下：

* 操作系统： Windows 10
* Java 8
* 开发IDE：Eclipse
* MySQL 8
* Spring Boot 2.3.3

框架搭建步骤包含：

1. 创建Spring Boot项目
2. 引入activiti等依赖包
3. 安装activiti designer
4. 绘制流程
5. 编写activiti 工作流代码


这里搭建一个基于REST的流程框架，通过REST API 调用，返回JSON格式的响应数据。


## 创建Spring Boot项目

使用 Spring Boot在线项目生成器创建项目。
在浏览器进入如下地址：[https://start.spring.io/](https://start.spring.io/)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200906111245143.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29zY2FyOTk5,size_16,color_FFFFFF,t_70#pic_center)

以上选项说明如下：
* 使用Maven管理依赖包
* 输入项目的包名、项目名
* Java版本选择 8
* 依赖项包括：
 - Spring Web
 - Spring Security
-  Spring Boot DevTools
- MySQL Driver

注：流程的签核一般需要有用户的参与， 所以activiti默认是需要Spring Security的支持。Spring Web是用来开发Web项目，Spring Boot DevTools则是为了开发遍历，在编写代码的时候会自动更新等。这里使用MySQL数据库， 所以需要MySQL的驱动包。

以上设定完成，点击 GENERATE按钮后会下载一个 .zip的压缩文件。将此压缩文件解压到Eclipse的工作区， 在Eclipse中导入该Maven项目。

## 引入activiti等依赖包
除了上面选择的Spring Web、Spring Security等依赖包，还导入以下activiti等依赖包， 具体包括
* Activiti 相关依赖包
* druid 阿里的数据库连接池：必须
导入的配置如下：
```
<!-- 阿里数据库连接池 -->
		<dependency>
		    <groupId>com.alibaba</groupId>
		    <artifactId>druid</artifactId>
		    <version>1.1.23</version>
		</dependency>
		<!-- Activiti -->
		<dependency>
		    <groupId>org.activiti</groupId>
		    <artifactId>activiti-spring-boot-starter</artifactId>
		    <version>7.1.0.M6</version>
		</dependency>	
		
```
## 安装activiti designer

activiti designer是Eclipse的插件， 用来设计和绘制activiti 流程图，不过该插件自2015年8月之后就没有再更新，版本也定格在Activiti Designer 5.18.0。
在线安装的地址是： https://github.com/Activiti/Activiti-Designer/releases ， 不过总是会安装不成功， 所以建议使用离线安装的方式，先下载插件的压缩文件，在到Eclipse中安装。
下载地址是：
http://www.activiti.org/designer/archived/activiti-designer-5.18.0.zip
下载之后， 在Eclipse的插件安装页面，点击 “Archive”按钮选择下载的.zip 文件。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200906111534191.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29zY2FyOTk5,size_16,color_FFFFFF,t_70#pic_center)


勾选需要安装的组件后点击Finish。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200906111549269.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29zY2FyOTk5,size_16,color_FFFFFF,t_70#pic_center)

接下来会出现同意协议等窗口，直接下一步即可完成安装。
安装完成，重启Eclipse。


## 绘制流程
src/main/resources 目录下的流程图文件会被自动部署到数据库中。

在项目的src/main/resources目录下创建 processes子目录，创建完成的目录结构如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200906111653284.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29zY2FyOTk5,size_16,color_FFFFFF,t_70#pic_center)

在processes下添加一个简单的请假流程图。 

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200906111709131.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29zY2FyOTk5,size_16,color_FFFFFF,t_70#pic_center)


流程图文件的命名为 TimeOff, 在Eclipse中绘制如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020090611171850.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29zY2FyOTk5,size_16,color_FFFFFF,t_70#pic_center)

#### 流程名以及节点的分派人的设定
打开流程图，在下方的Properties视图中可以查看和设置该流程图的Id以及名字等设置。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200906111915265.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29zY2FyOTk5,size_16,color_FFFFFF,t_70#pic_center)
选中某个节点,在Properties中的"Main config"标签页中可以设置该节点的分派人, 也就是这一关是谁来执行。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200906112046363.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29zY2FyOTk5,size_16,color_FFFFFF,t_70#pic_center)

#### 示例流程
这里演示的是一个请假流程， 流程包括三个节点：
|  序号|  节点名|分派人|
|--|--|--|--|
| 1 | 填单 |${requester} |
| 2 |主管审核  |${manager}   |
| 3 |  人事审核| ${hr} |

`${} ` 是占位符，也就是变量，在开始流程实例时传入变量对应的值。

## 编写activiti 工作流代码
在开始activiti代码之前，需要进行数据库，Spring Boot框架以及Spring Security 相关设定。

#### 创建数据库

```
create database activitiflow;
```

#### 配置application.yml
这里使用yml文件格式进行配置。

```xml
spring:     
    datasource:
        url: jdbc:mysql://localhost:3306/activitiflow?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
        username: root
        password: 123456
        drvier-class-name: com.mysql.jdbc.Driver
        type: com.alibaba.druid.pool.DruidDataSource
        
    activiti:
        database-schema-update: true
        history-level: full
        db-history-used: true              
```

这里主要是数据源和activiti的配置。


#### Spring Security配置
在項目的com.osxm.springbootactiviti包下创建子目录config，在该目录下建立Security配置文件SecurityConfig.java，内容如下：

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("oscar")
				.password(new BCryptPasswordEncoder().encode("1")).roles("ADMIN");

	}
	
}
```
这里简单起见，使用内存用户的方式， 创建一个ADMIN角色的用户oscar, 密码为1。
应用启动后， 默认会进入Spring Security的登录页面(地址是http://localhost:8080/login)，效果如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200906114309588.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29zY2FyOTk5,size_16,color_FFFFFF,t_70#pic_center)
#### 主页面响应

输入以上用户名和密码之后， 会跳转到主页面， 主页面的地址是： http://localhost:8080/。在com.osxm.springbootactiviti下建立controller子目录，并创建HomeController.java 用于处理主页面显示，内容如下：

```java
@RestController
public class HomeController {
	@GetMapping("/")
	public String index() {
		return "Index Page";
	}
}
```

#### Activiti 主要服务类
 流程定义和流程实例的概念 与 Java类的定义和类的实例的概念是类似的。
 流程图绘制的是流程的定义，该定义信息会被部署到数据库中。使用这些流程定义可以启动流程实例。
Activiti提供了相关的服务类，主要包括：

 - RepositoryService： 流程定义的服务类
 - RuntimeService： 流程运行的服务类
 - taskService： 任务服务类
 - HistoryService： 流程历史记录服务类

#### Activiti 代码演示
以上准备了那么多， 终于进入Activiti的代码部分， 在controller 目录下建立DefaultActivitiController类，在该类中演示Activiti相关方法。

 1. 启动流程实例


```java
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
```

使用runtimeService的startProcessInstanceByKey()方法启动流程， 启动时可以传入上面在流程绘制时设置的 `${}`变量。这里设置填单人、主管以及HR的账号都是 oscar。

2. 查询流程

```java
	@RequestMapping(value = "/queryProcess")
	@ResponseBody
	public String queryProcess(String processInstanceId) {
		StringBuffer rtnMsgBuf = new StringBuffer();
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		List<ProcessInstance> runningList = processInstanceQuery.processInstanceId(processInstanceId).list();
		rtnMsgBuf.append("查询到的流程数目:" + runningList.size() + "<br>");
		return rtnMsgBuf.toString();
	}
```

3. 查询任务
每一个节点需要进行的工作称为任务，使用taskService可以查询应用中的任务。

```java
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
```

4. 完成任务
某个节点的分派人如果完成了这笔任务，可以签核完成该笔任务。方式时使用taskService的complete()方法。
