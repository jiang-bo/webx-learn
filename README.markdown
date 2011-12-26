# 从零搭建webx环境
-- 姜博 <jiang.bo.hit@gmail.com>

## 一、创建maven工程
### 1.1 创建工程
创建一个名为webx-demo的mvn-webapp工程， -Pexternal指的是激活external配置，默认公司内部仓库没有archtype-webapp,详情参考maven指南
	mvn archetype:create -DgroupId=org.coderj.webx -DartifactId=webx-demo \
	-DarchetypeArtifactId=maven-archetype-webapp -Pexternal
这样会创建一个空的maven web工程，目录结构如下：

	webx-demo $ tree
	.
	|__.classpath
	|__.project
	|__pom.xml
	|__src
	| |__main
	| | |__resources
	| | |__webapp
	| | | |__index.jsp
	| | | |__WEB-INF
	| | | | |__web.xml

通常都是在eclipse中进行开发，因此需要将项目导入到eclipse
	$mvn eclipse:eclipse
在eclipse中将webx-demo导入。
### 1.2 添加jetty插件运行
通常在开发过程中使用jetty插件能够大大提升开发效率，因此在pom.xml中加入jetty插件：

	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	  <modelVersion>4.0.0</modelVersion>
	  <groupId>org.coderj.webx</groupId>
	  <artifactId>webx-demo</artifactId>
	  <packaging>war</packaging>
	  <version>1.0-SNAPSHOT</version>
	  <name>webx-demo Maven Webapp</name>
	  <url>http://maven.apache.org</url>
	  <dependencies>
	  </dependencies>
	  <build>
	    <finalName>webx-demo</finalName>
	    <plugins>        
    	  <plugin>
        	<groupId>org.mortbay.jetty</groupId>
	        <artifactId>jetty-maven-plugin</artifactId>
    	  </plugin>          
	      </plugins>
	  </build>
	</project>

此时就可以通过mvn jetty:run的方式启动应用：

	$mvn jetty:run
	. . .
	[INFO] Starting jetty 7.0.0.pre5 ...
	2011-12-22 19:13:49.052::INFO:  jetty-7.0.0.pre5
	2011-12-22 19:13:49.294::INFO:  No Transaction manager found - if your webapp 	requires one, please configure one.
	2011-12-22 19:13:50.789::INFO:  Started SelectChannelConnector@0.0.0.0:8080
	[INFO] Started Jetty Server

通过<http://localhost:8080/webx-demo>可以访问一个hello world页面

## 二、加入Webx配置(2.0版)
### 2.1 加入webx的maven依赖
在pom.xml中加入webx的相关依赖：

	<dependencies>
		<dependency>
			<groupId>com.alibaba.toolkit</groupId>
			<artifactId>toolkit-webx-all-in-one</artifactId>
			<version>2.0</version>
		</dependency>
	</dependencies>
	
(如果出现缺少依赖可以尝试替换相关依赖包的版本号)
### 2.2 修改web.xml
在src/main/webapp/WEB-INF/web.xml中加入WebxContollerServlet映射，使所有的*.htm *.do请求都映射到WebxControllerServlet上，即通过webx框架处理

	<web-app>
	...
	<servlet>
		<servlet-name>WebxController</servlet-name>
		<servlet-class>com.alibaba.webx.WebxControllerServlet</servlet-class>
		<init-param>
			<param-name>initAllServices</param-name>
			<param-value>true</param-value>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>WebxController</servlet-name>
		<url-pattern>*.htm</url-pattern>
	</servlet-mapping>
	<servlet-mapping>
		<servlet-name>WebxController</servlet-name>
		<url-pattern>*.do</url-pattern>
	</servlet-mapping>
	...
	</web-app>

###2.3 配置log4j.xml
在src/main/webapp/WEB-INF下添加log4j.xml配置文件：

	<?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE log4j:configuration SYSTEM "http://toolkit.alibaba-inc.com/dtd/log4j/log4j.dtd">
	<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/"> <!-- =================================================================== 
			== -->
		<!-- 以下是appender的定义 -->
		<!-- =================================================================== 
			== -->
		<appender name="WORKSHOP" class="org.apache.log4j.FileAppender">
			<param name="file" value="${loggingRoot}/${localHost}/workshop.log" />
			<param name="append" value="false" />
			<param name="encoding" value="GB2312" />
			<layout class="org.apache.log4j.PatternLayout">
				<param name="ConversionPattern"
					value="%d [%X{requestURIWithQueryString}] %-5p %c{2} - %m
	%n" />
			</layout>
		</appender>
		<appender name="WORKSHOP-ERROR" class="org.apache.log4j.ConsoleAppender">
			<param name="target" value="System.err" />
			<param name="encoding" value="GB2312" />
			<param name="threshold" value="warn" />
			<layout class="org.apache.log4j.PatternLayout">
				<param name="ConversionPattern"
					value="%d [%X{requestURIWithQueryString}] %-5p %c{2} - %m %n" />
			</layout>
		</appender>
		<appender name="RESOURCE" class="org.apache.log4j.FileAppender">
			<param name="file" value="${loggingRoot}/${localHost}/workshop-resource.log" />
			<param name="append" value="false" />
			<param name="encoding" value="GB2312" />
			<layout class="org.apache.log4j.PatternLayout">
				<param name="ConversionPattern"
					value="%d [%X{requestURIWithQueryString}] %-5p %c{2} - %m %n" />
			</layout>
		</appender>
		<appender name="FILTER" class="org.apache.log4j.FileAppender">
			<param name="file" value="${loggingRoot}/${localHost}/workshop-filter.log" />
			<param name="append" value="true" />
			<param name="encoding" value="GB2312" />
			<layout class="org.apache.log4j.PatternLayout">
				<param name="ConversionPattern" value="%d %-5p %c{2} - %m%n" />
			</layout>
		</appender>
		<appender name="APACHE" class="org.apache.log4j.FileAppender">
			<param name="file" value="${loggingRoot}/${localHost}/workshop-apache.log" />
			<param name="append" value="true" />
			<param name="encoding" value="GB2312" />
			<layout class="org.apache.log4j.PatternLayout">
				￼￼￼
				<param name="ConversionPattern"
					value="%d [%X{requestURIWithQueryString}] %-5p %c{2} - %m %n" />
			</layout>
		</appender>
		<appender name="VELOCITY" class="org.apache.log4j.FileAppender">
			<param name="file" value="${loggingRoot}/${localHost}/workshop-velocity.log" />
			<param name="append" value="true" />
			<param name="encoding" value="GB2312" />
			<layout class="org.apache.log4j.PatternLayout">
				<param name="ConversionPattern" value="%d %-5p %c{2} - %m%n" />
			</layout>
		</appender>
		<!-- =================================================================== 
			== -->
		<!-- 以下是logger的定义 -->
		<!-- =================================================================== 
			== -->
		<logger name="com.alibaba.webx.filter" additivity="false">
			<level value="WARN" />
			<appender-ref ref="FILTER" />
		</logger>
		<logger name="com.alibaba.service.VelocityService" additivity="false">
			<level value="WARN" />
			<appender-ref ref="VELOCITY" />
		</logger>
		<logger name="com.alibaba.service.resource" additivity="false">
			<level value="DEBUG" />
			<appender-ref ref="RESOURCE" />
		</logger>
		<logger name="com.alibaba.service.ResourceLoaderService"
			additivity="false">
			<level value="DEBUG" />
			<appender-ref ref="RESOURCE" />
		</logger>
		<logger name="org.apache.commons.beanutils">
			<level value="error" />
		</logger>
		<logger name="org.apache.commons.digester">
			<level value="error" />
		</logger>
		<logger name="org.apache" additivity="false">
			<level value="WARN" />
			<appender-ref ref="APACHE" />
		</logger>
		<!-- =================================================================== 
			== -->
		<!-- Rootlogger的定义 -->
		￼
		<!-- =================================================================== 
			== -->
		<root>
			<level value="DEBUG" />
			<appender-ref ref="WORKSHOP" />
			<appender-ref ref="WORKSHOP-ERROR" />
		</root>
	</log4j:configuration>

这个是log4j的日志配置文件，你可以任意修改配置，具体请参考log4j手册

### 2.4 创建webx.xml
每个webx应用中必须有一个webx.xml配置文件，这个配置文件定义了webx中所用到的所有service。在src/main/webapp/WEB-INF/下创建一个webx.xml文件：

	<?xml version="1.0" encoding="GB2312"?>
	<!DOCTYPE configuration PUBLIC "-//ALIBABA//DTD Services Configuration V1.0//EN" "http://toolkit.alibaba-inc.com/dtd/toolkit/service/services.dtd">
	<configuration>
		<services>
			<service name="PipelineService">
				<property name="pipeline.default.descriptor" value="/WEB-INF/pipeline.xml" />
			</service>
		</services>
	</configuration>

当前这个配置文件中只配置了一个服务PipelineService。这个PipelineService是Webx的核心服务，提供了Webx请求执行的流程。

### 2.5 创建pipeline.xml
PipelineService对请求的执行处理流程定义在pipeline.xml里，在src/main/webapp/WEB-INF/下创建一个pipeline.xml文件：

	<pipeline>
		<valve class="com.alibaba.service.pipeline.TryCatchFinallyValve">
			<try>
				<valve class="com.alibaba.turbine.pipeline.SetLoggingContextValve" />
				<valve class="com.alibaba.turbine.pipeline.SetLocaleValve"
					defaultLocale="zh_CN" defaultCharset="GBK" />
				<valve class="com.alibaba.turbine.pipeline.AnalyzeURLValve" />
				<valve class="com.alibaba.turbine.pipeline.ChooseValve" label="processModule">
					<when extension="jsp, vm">
						<valve class="com.alibaba.turbine.pipeline.PerformActionValve"
							actionParam="action" />
						<valve class="com.alibaba.turbine.pipeline.PerformScreenTemplateValve" />
					</when>
					<when extension="do">
						<valve class="com.alibaba.turbine.pipeline.PerformActionValve"
							actionParam="action" />
						<valve class="com.alibaba.turbine.pipeline.PerformScreenValve" />
					</when>
				</valve>
				<valve class="com.alibaba.turbine.pipeline.RedirectTargetValve"
					goto="processModule" />
			</try>
			<catch>
				<valve target="error.vm" class="com.alibaba.turbine.pipeline.SetErrorPageValve" />
				<valve class="com.alibaba.turbine.pipeline.PerformScreenTemplateValve" />
			</catch>
			<finally>
				<valve class="com.alibaba.turbine.pipeline.SetLoggingContextValve"
					action="cleanup" />
			</finally>
		</valve>
	</pipeline>

### 2.6 创建页面模板
在src/webapp目录下创建模板目录：

	src/main/webapp
	|__templates
	| |__control
	| |__layout
	| |__screen

在screen目录中创建一个hello.vm模板文件

	<html> 
	<head>
	<title>Hello!</title> 
	</head>
	<body>
	<h1>hello webx! </h1>
	</body>
	</html>

### 2.7 运行webx
重新执行下mvn jetty:run
访问<http://localhost:8080/webx-demo/hello.htm>就能看到webx运行的效果，页面会显示:
	hello webx!
### 2.8 添加module
上面的页面只是一个静态的展示，先面加入一些交互，当输入
<http://localhost:8080/webx-demo/hello.htm?name=Jack>时，页面上显示 Hello Mr.Jack!
#### 2.8.1 修改hello.vm
修改hello.vm，在页面上展示name值：

	<html> 
	<head>
	<title>Hello!</title> 
	</head>
	<body>
	<h1>hello $name </h1>
	</body>
	</html>

#### 2.8.2 添加Screen类Hello.java
在webx-demo中新建一个src/main/java源码文件加，新建一个org.coderj.webx.demo.web.Hello.java类：
	package org.coderj.webx.demo.web.screen;
	
	import com.alibaba.service.template.TemplateContext;
	import com.alibaba.turbine.module.screen.TemplateScreen;
	import com.alibaba.turbine.service.rundata.RunData;
	
	
	public class Hello extends TemplateScreen {
	
		@Override
		public void execute(RunData rundata, TemplateContext context) {
			String name = (String) rundata.getParameters().get("name");
			name = "Mr." + name;
			context.put("name", name);
		}
	}	

该类继承了TemplateScreen，覆写了execute方法，注意框架中有多个RunData，不要引用错了。
这个execute方法主要完成了从请求中获取参数name的值，并在之前追加一个"Mr."，然后放知道context中，最终渲染到页面。

#### 2.8.3 配置ModuleLoaderService
在webx.xml加入ModuleLoaderService配置：

	<?xml version="1.0" encoding="GB2312"?>
	<!DOCTYPE configuration PUBLIC "-//ALIBABA//DTD Services Configuration V1.0//EN" "http://toolkit.alibaba-inc.com/dtd/toolkit/service/services.dtd">
	<configuration>
		<services>
			...
			<service name="ModuleLoaderService">
				<property name="module.packages">
					<value>org.coderj.webx.demo.web</value>
				</property>
			</service>
		</services>
	</configuration>
该配置告诉ModuleLoaderService到org.coderj.webx.demo.web包下查找module。
#### 2.8.4 重新运行
重新mvn jetty:run启动应用，访问<http://localhost:8080/webx-demo/hello.htm?name=Jack>
页面将会显示：
Hello Mr.Jack!

## 三、加入Webx配置（Webx 2.5版）
Webx2.5是在Webx2.0之上做过优化的框架，目前部分应用使用的是2.5框架，2.5与2.0的框架底层原理基本相同，但是部分配置不同。
### 3.1修改maven依赖
需要在pom.xml中加入2.5的maven依赖：

		<dependency>
			<groupId>com.alibaba.china.shared</groupId>
			<artifactId>apollo.webx</artifactId>
			<version>2.5.0-SNAPSHOT</version>
		</dependency>
如果提示找不到该依赖，需要在pom.xml头部引入parent依赖：

	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
		<modelVersion>4.0.0</modelVersion>
		<!--这段parent配置是由于历史问题为antx仓库到maven仓库转换所用-->
		<parent>
			<groupId>com.alibaba</groupId>
			<artifactId>a2m.china</artifactId>
			<version>2</version>
		</parent>
		<!-- == -->
		<groupId>org.coderj.webx</groupId>
		<artifactId>webx-demo</artifactId>
		<packaging>war</packaging>
		<version>1.0-SNAPSHOT</version>
同样由于引入了parent pom，导致工程目录的配置出现了变化，即公司默认的java源码目录为src/java 配置文件目录为src/conf，web资源文件为src/webroot，这与maven默认不一致，有两种修改方式:

* 一种是修改项目目录结构，使其符合公司的目录结构，同时还需要修改maven jetty插件的配置，使web资源目录调整为src/webroot。                  
* 另一种时修改pom.xml配置，使其符合maven标准。

本文使用的时后一种，直接修改pom.xml，目录结构不需要调整。调整目录结构的方式参见[从零搭建Webx2.5应用](http://...)
pom主要修改资源目录和打war过程中源码目录和资源文件目录配置，修改之后的pom.xml文件如下：

	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
		<modelVersion>4.0.0</modelVersion>
		<parent>
			<groupId>com.alibaba</groupId>
			<artifactId>a2m.china</artifactId>
			<version>2</version>
		</parent>
		<groupId>org.coderj.webx</groupId>
		<artifactId>webx-demo2.5</artifactId>
		<packaging>war</packaging>
		<version>1.0-SNAPSHOT</version>
		<name>webx-demo2.5 Maven Webapp</name>
		<url>http://maven.apache.org</url>
		<dependencies>
			<dependency>
				<groupId>com.alibaba.china.shared</groupId>
				<artifactId>apollo.webx</artifactId>
				<version>2.5.0-SNAPSHOT</version>
			</dependency>
		</dependencies>
		<build>
			<finalName>webx-demo2.5</finalName>
			<resources>
				<resource>
					<directory>src/main/resources</directory>
					<includes>
						<include>**/*</include>
					</includes>
				</resource>
				<resource>
					<directory>src/main/java</directory>
					<includes>
						<include>**/*</include>
					</includes>
					<excludes>
						<exclude>**/*.java</exclude>
					</excludes>
				</resource>
			</resources>
			<plugins>
				<plugin>
					<artifactId>maven-war-plugin</artifactId>
					<version>2.1-beta-1</version>
					<executions>
						<execution>
							<id>default-war</id>
							<phase>package</phase>
							<goals>
								<goal>war</goal>
							</goals>
							<configuration>
								<warSourceDirectory>src/main/webapp</warSourceDirectory>
							</configuration>
						</execution>
					</executions>
					<configuration>
						<warSourceDirectory>src/main/webapp</warSourceDirectory>
					</configuration>
				</plugin>
				<plugin>
					<groupId>org.mortbay.jetty</groupId>
					<artifactId>jetty-maven-plugin</artifactId>
					<version>7.0.0.pre5</version>
				</plugin>
			</plugins>
		</build>
	</project>
重新mvn clean eclipse:eclipse 生成eclipse工程，导入到eclipse


### 3.2 修改webx.xml
webx2.5的webx.xml配置与2.0有很大的不同，因为webx2.5主要时基于对service framework的扩展完成2.0版本功能的扩展，因此，webx.xml中需要配置2.5的相关service，主要包括

* ModuleLoaderService，需要使用2.5提供的PojoModuleLoaderService，且module.factory需要使用PojoModuleFactory
* BeanFactoryService， 2.5中Pojo Module的依赖注入有Spring来托管，因此即使没有biz层，也必须使用BeanFactoryService
* MappingService，2.5中uri和module的映射规则不同，需要扩展默认的Mapper

修改后的webx.xml如下：

	<?xml version="1.0" encoding="GB2312"?>
	<!DOCTYPE configuration PUBLIC "-//ALIBABA//DTD Services Configuration V1.0//EN" "http://toolkit.alibaba-inc.com/dtd/toolkit/service/services.dtd">
	<configuration>
		<services>
			<service name="PipelineService">
				<property name="pipeline.default.descriptor" value="/WEB-INF/pipeline.xml" />
			</service>
			<service name="ModuleLoaderService" class="com.alibaba.turbine.service.moduleloader.PojoModuleLoaderService">
				<property name="module.factory">
	               <property name="default.class" value="com.alibaba.webx.service.moduleloader.PojoModuleFactory"/>
	            </property>
				<property name="module.packages">
					<value>org.coderj.webx.demo.web</value>
				</property>
			</service>
			<service name="BeanFactoryService" class="com.alibaba.service.spring.SpringxBeanFactoryService">
	            <property name="bean.descriptors">
	                <value>/classpath/applicationContext.xml</value>
	            </property>
	        </service>
			<service name="MappingService" class="com.alibaba.turbine.service.mapping.DefaultMappingService">
	            <property name="mapper.action">
	                <property name="class" value="com.alibaba.webx.service.mapping.mapper.CompatibleActionDirectModuleMapper"/>
	            </property>
	            <property name="mapper.screen.notemplate">
	                <property name="class" value="com.alibaba.webx.service.mapping.mapper.CompatibleScreenDirectModuleMapper"/>
	            </property>
	            <property name="mapper.screen">
	                <property name="class" value="com.alibaba.webx.service.mapping.mapper.CompatibleScreenFallbackModuleMapper"/>
	                <property name="default" value="TemplateScreen"/>
	            </property>
	            <property name="mapper.screen.template">
	                <property name="class" value="com.alibaba.turbine.service.mapping.mapper.DirectTemplateMapper"/>
	                <property name="prefix" value="screen"/>
	            </property>
	            <property name="mapper.layout">
	                <property name="class" value="com.alibaba.webx.service.mapping.mapper.CompatibleLayoutFallbackModuleMapper"/>
	                <property name="default" value="TemplateLayout"/>
	            </property>
	            <property name="mapper.layout.template">
	                <property name="class" value="com.alibaba.turbine.service.mapping.mapper.FallbackTemplateMapper"/>
	                <property name="prefix" value="layout"/>
	            </property>
	            <property name="mapper.control.notemplate">
	                <property name="class" value="com.alibaba.webx.service.mapping.mapper.CompatibleControlDirectModuleMapper"/>
	            </property>
	            <property name="mapper.control">
	                <property name="class" value="com.alibaba.webx.service.mapping.mapper.CompatibleControlFallbackModuleMapper"/>
	                <property name="default" value="TemplateControl"/>
	            </property>
	            <property name="mapper.control.template">
	                <property name="class" value="com.alibaba.turbine.service.mapping.mapper.DirectTemplateMapper"/>
	                <property name="prefix" value="control"/>
	            </property>
	        </service>
		</services>
	</configuration>

### 3.3 修改pipeline.xml
webx2.5和2.0的请求处理流程有所不同，所以需要修改pipeline.xml

	<pipeline>
		<valve class="com.alibaba.service.pipeline.TryCatchFinallyValve">
			<try>
				...
				<valve class="com.alibaba.turbine.pipeline.ChooseValve" label="processModule">
					<when extension="jsp, vm">
						<valve class="com.alibaba.turbine.pipeline.PerformBindableActionValve"
							actionParam="action" />
						<valve class="com.alibaba.turbine.pipeline.PerformBindableScreenTemplateValve" />
					</when>
					<when extension="do">
						<valve class="com.alibaba.turbine.pipeline.PerformBindableActionValve"
							actionParam="action" />
						<valve class="com.alibaba.turbine.pipeline.PerformBindableScreenValve" />
					</when>
				</valve>
				<valve class="com.alibaba.turbine.pipeline.RedirectTargetValve"
					goto="processModule" />
			</try>
			<catch>
				<valve target="error.vm" class="com.alibaba.turbine.pipeline.SetErrorPageValve" />
				<valve class="com.alibaba.turbine.pipeline.PerformBindableScreenTemplateValve" />
			</catch>
			<finally>
				<valve class="com.alibaba.turbine.pipeline.SetLoggingContextValve"
					action="cleanup" />
			</finally>
		</valve>
	</pipeline>
主要变化是调整Moudle处理的valve，由原来的PerformXXXValve改为PerformBindableXXXValve。

### 3.4 添加Action
新建一个HelloAction：

	package org.coderj.webx.demo.web.action;
	
	import com.alibaba.webx.ActionResult;
	import com.alibaba.webx.action.Parameter;
	
	public class HelloAction {
	
		public ActionResult sayHi(@Parameter(name = "name") String name) {
			ActionResult ar = ActionResult.create(this);
			name = "Mr." + name;
			ar.putInContext("name", name);
			return ar;
		}
	}
该Action与2.0版的区别在于不继承任何父类
### 3.5 添加模板
在templates/screen目录下新增一个hello/sayHi.vm模板

	<html> 
	<head>
	<title>Hello!</title> 
	</head>
	<body>
	<h1>hello $name </h1>
	</body>
	</html>
	
### 3.6 运行
执行mvn jetty:run ，运行应用，访问
<http://localhost:8080/webx-demo2.5/hello/sayHi.htm?name=jack>
看看效果吧
	
## 四、参考文档

* 《Hello Apollo!》<http://b2b-doc.alibaba-inc.com/display/ccbu/Hello+Apollo%21>
* 《jetty maven plugin》 	<http://docs.codehaus.org/display/JETTY/Maven+Jetty+Plugin>
* 《新员工开发指南》
