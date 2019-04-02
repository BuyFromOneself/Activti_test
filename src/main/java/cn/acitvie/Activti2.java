package cn.acitvie;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * purchase
 * 采购申请流程
 */
public class Activti2 {

    /**
     * 基于代码的方式初始化工作流
     */
    @Test
    public void initProcessEngin(){
        //1，创建引擎配置对象
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        //2，如何创建流程引擎对象
        //目标是生成23张表所以要设置数据源 com.mysql.jdbc.Driver
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/activiti_2018");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("root");

        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);

        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println("初始化引擎成功");
        System.out.println(processEngine.getName());


    }

    /**
     * 基于配置文件的方式初始化工作流
     */
    @Test
    public void initProcessEngin2(){
        //1读取配置文件，初始化流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("初始化引擎成功");
        System.out.println(engine.getName());
    }

    /**
     * 部署流程
     */
    @Test
    public void ProcessDeployment(){
        //1 获取流程引擎对象 ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2,基于该对象获取流程部署服务对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3,通过流程部署服务对象，创建流程部署对象，加载资源文件并进行部署
        DeploymentBuilder deployment = repositoryService.createDeployment();
        Deployment deploy =deployment.name("申请请假v3").
                addClasspathResource("Purchase1.bpmn20.xml").
                addClasspathResource("Purchase1.png").
                deploy();
        System.out.println("流程部署成功");
        System.out.println("流程部署id:"+deploy.getId());
        System.out.println("流程部署名字："+deploy.getName());

    }

    /**
     *启动流程
     */
    @Test
    public void startDeployment(){
        //1，获取流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2, 基于该对象获取流程运行服务对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String,Object> map = new HashMap<>();
        map.put("money",10001);
        //3,启动流程，就是创建了一个流程实例对象，一个流程部署对象可以对应多个流程对象
        ProcessInstance  processInstance = runtimeService.startProcessInstanceByKey("myProcess_1",map);



        //4,打印流程实例信息
        System.out.println("流程实例对象id:"+processInstance.getId());
        System.out.println("流程实例对象定义id:"+processInstance.getProcessDefinitionId());

    }

    /**
     * ,查询所有任务节点
     */
    @Test
    public void queryAllTask(){
        //获取流殷勤对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //1,基于该对象获取任务处理对象
        TaskService taskService = processEngine.getTaskService();

                List<Task> list = taskService.createTaskQuery().list();
        System.out.println(list.size());
        for(Task task : list){
            System.out.println("任务id:"+task.getId());
            System.out.println("任务名称："+task.getName());
            System.out.println("任务处理人：" + task.getAssignee());
            System.out.println("任务所属的流程实例id:"+task.getProcessInstanceId());
            System.out.println("任务所属的流程部署服务定义id："+task.getProcessDefinitionId());
        }
    }

    /*执行任务节点*/
    @Test
    public void execute(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //通过任务id，来通过任务节点
        Map<String,Object> map = new HashMap<>();
        map.put("fale",true);
        taskService.complete("70003",map);
        System.out.println("当前任务节点通过，，流程将走到下一个节点");
    }
}
