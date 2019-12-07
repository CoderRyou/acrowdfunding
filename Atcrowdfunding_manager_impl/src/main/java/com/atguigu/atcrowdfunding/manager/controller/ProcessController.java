package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.AjaxResult;
import com.atguigu.atcrowdfunding.utils.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping("/showImg")
    public void showImg(String id, HttpServletResponse response) throws IOException {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();

        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());

        ServletOutputStream outputStream = response.getOutputStream();

        IOUtils.copy(resourceAsStream,outputStream);
    }
    
    @RequestMapping("/doDelete")
    @ResponseBody
    public AjaxResult doDelete(String id){ //流程定义id
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();

            repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    @RequestMapping("/deploy")
    @ResponseBody
    public AjaxResult deploy(HttpServletRequest request){
        try {

            MultipartHttpServletRequest multipartHttpServletRequest =
                    (MultipartHttpServletRequest) request;

            MultipartFile multipartFile = multipartHttpServletRequest.getFile("processDefFile");

            repositoryService.createDeployment()
                    .addInputStream(multipartFile.getOriginalFilename(), multipartFile.getInputStream())
                    .deploy();

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    @RequestMapping("/loadProcess")
    @ResponseBody
    public AjaxResult loadProcess(@RequestParam(value = "pn",defaultValue = "1") Integer pn){

        try {

            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

            PageHelper.startPage(pn,10);
            List<ProcessDefinition> list = processDefinitionQuery.list();

            List<Map<String,Object>> listPage = new ArrayList<>();
            for (ProcessDefinition processDefinition:list) {
                Map<String,Object> pd = new HashMap<>();
                pd.put("id",processDefinition.getId());
                pd.put("name",processDefinition.getName());
                pd.put("key",processDefinition.getKey());
                pd.put("version",processDefinition.getVersion());

                listPage.add(pd);
            }

            PageInfo pageInfo = new PageInfo(listPage,10);
            return AjaxResult.success().add("pageInfo",pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    @RequestMapping("/index.htm")
    public String index(){
        return "/process/index";
    }

}
