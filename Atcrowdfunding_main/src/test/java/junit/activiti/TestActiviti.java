package junit.activiti;

import com.atguigu.atcrowdfunding.activiti.listener.NoListener;
import com.atguigu.atcrowdfunding.activiti.listener.YesListener;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;

public class TestActiviti {

    ApplicationContext ioc = new ClassPathXmlApplicationContext("spring/spring-*.xml");
    ProcessEngine processEngine = (ProcessEngine) ioc.getBean("processEngine");

    //12.测试流程监听器
    @Test
    public void test12(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        HashMap<String, Object> varibales = new HashMap<>();
        varibales.put("yesListener",new YesListener());
        varibales.put("noListener",new NoListener());

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),varibales);
        System.out.println("processInstance="+processInstance);
    }

    @Test
    public void test121(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        TaskService taskService = processEngine.getTaskService();

        TaskQuery taskQuery = taskService.createTaskQuery();

        List<Task> list = taskQuery.taskAssignee("zhangsan").list();

        for (Task task : list){
            taskService.setVariable(task.getId(),"flag","true");
            taskService.complete(task.getId());
        }
    }

    //9.网关 - 包含网关
    @Test
    public void test111(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        TaskService taskService = processEngine.getTaskService();

        TaskQuery taskQuery = taskService.createTaskQuery();

        List<Task> list = taskQuery.taskAssignee("lisi").list();

        for (Task task : list){

            taskService.complete(task.getId());

        }
    }

    //11.网关 - 包含网关（排他+并行）
    @Test
    public void test11(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        HashMap<String, Object> varibales = new HashMap<>();
        varibales.put("days","5");
        varibales.put("cost","8000");

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),varibales);
        System.out.println("processInstance="+processInstance);
    }

    //9.网关 - 并行网关（会签） - 项目经理和财务经理都审批后，流程结束；如果只有一个经理审批，流程需要等待
    @Test
    public void test101(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        TaskService taskService = processEngine.getTaskService();

        TaskQuery taskQuery = taskService.createTaskQuery();

        List<Task> list = taskQuery.taskAssignee("zhangsan").list();

        for (Task task : list){

            taskService.complete(task.getId());

        }
    }

    //10.网关 - 并行网关（会签）
    @Test
    public void test10(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.out.println("processInstance="+processInstance);
    }

    //2.查询部署流程定义
    @Test
    public void test03(){
        System.out.println("processEngine"+processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        ProcessDefinitionQuery processDefinitionQuery
                = repositoryService.createProcessDefinitionQuery();

        List<ProcessDefinition> list = processDefinitionQuery.list();//查询所有的流程定义

        for (ProcessDefinition processDefinition:list){
            System.out.println("ID:"+processDefinition.getId());
            System.out.println("Key:"+processDefinition.getKey());
            System.out.println("Name:"+processDefinition.getName());
            System.out.println("Version:"+processDefinition.getVersion());
            System.out.println("=====================================");
        }

        long count = processDefinitionQuery.count();
        System.out.println("count="+count);
        System.out.println("=====================================");
        //查询最后一次部署的流程定义
        ProcessDefinition processDefinition = processDefinitionQuery.latestVersion().singleResult();
        System.out.println("ID:"+processDefinition.getId());
        System.out.println("Key:"+processDefinition.getKey());
        System.out.println("Name:"+processDefinition.getName());
        System.out.println("Version:"+processDefinition.getVersion());

        //排序查询流程定义，分页查询流程定义。
        ProcessDefinitionQuery definitionQuery = processDefinitionQuery.orderByProcessDefinitionVersion().desc();
        List<ProcessDefinition> listPage = definitionQuery.listPage(0,2);
        for (ProcessDefinition processDefinition1:listPage){
            System.out.println("ID:"+processDefinition1.getId());
            System.out.println("Key:"+processDefinition1.getKey());
            System.out.println("Name:"+processDefinition1.getName());
            System.out.println("Version:"+processDefinition1.getVersion());
            System.out.println("=====================================");
        }
    }

    //2.部署流程定义
    @Test
    public void test02(){
        System.out.println("processEngine"+processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcess9.bpmn").deploy();

        System.out.println("deploy"+deploy);
    }

    //创建流程引擎，创建23张表
    @Test
    public void test01(){

        System.out.println("processEngine"+processEngine);

    }

}
