package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.AjaxResult;
import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authCert")
public class AuthCertController {

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    MemberService memberService;

    @Autowired
    CertService certService;

    /**
     * 拒绝申请
     * @param memberid
     * @param taskid
     * @return
     */
    @ResponseBody
    @RequestMapping("/refuse")
    public AjaxResult refuse(Integer memberid,String taskid){

        try {
            //完成后台审批，拒绝申请
            taskService.setVariable(taskid,"flag",false);
            taskService.setVariable(taskid,"memberid",memberid);
            taskService.complete(taskid);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }

    }

    /**
     * 通过申请
     * @param memberid
     * @param taskid
     * @return
     */
    @ResponseBody
    @RequestMapping("/pass")
    public AjaxResult pass(Integer memberid,String taskid){

        try {
            //完成后台审批，通过申请
            taskService.setVariable(taskid,"flag",true);
            taskService.setVariable(taskid,"memberid",memberid);
            taskService.complete(taskid);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    @RequestMapping("/showCertDetail")
    public String showCertDetail(Integer id,Model model){
        Member member = memberService.getMemberById(id);

        List<Map<String,Object>> list = certService.queryCertByMemberid(id);

        model.addAttribute("member",member);
        model.addAttribute("certimgs",list);

        return "/authCert/certDetail";
    }

    @ResponseBody
    @RequestMapping("/loadAuthCerts")
    public AjaxResult loadAuthCerts(@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        try {
            //查询实名认证后台审核任务列表
            PageHelper.startPage(pn,10);
            List<Task> tasks = taskService.createTaskQuery().processDefinitionKey("auth").taskCandidateGroup("backuser").list();

            List<Map<String,Object>> list = new ArrayList<>();

            for (Task task : tasks){
                Map<String,Object> map = new HashMap<>();
                map.put("taskId",task.getId());
                map.put("taskName",task.getName());

                //根据任务查询流程定义
                ProcessDefinition processDefinition = repositoryService
                                                    .createProcessDefinitionQuery()
                                                    .processDefinitionId(task.getProcessDefinitionId())
                                                    .singleResult();
                map.put("procDefName",processDefinition.getName());
                map.put("version",processDefinition.getVersion());

                //根据流程实例id查询会员
                Member member = memberService.getMemberByPiid(task.getProcessInstanceId());

                map.put("member",member);

                list.add(map);
            }

            PageInfo pageInfo = new PageInfo(list,10);

            return AjaxResult.success().add("pageInfo",pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    /**
     * 用户实名认证申请审核主页
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "/authCert/authCert";
    }

}
