package com.atguigu.atcrowdfunding.potal.controller;

import com.atguigu.atcrowdfunding.bean.*;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import com.atguigu.atcrowdfunding.potal.listener.PassListener;
import com.atguigu.atcrowdfunding.potal.listener.RefuseListener;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.atguigu.atcrowdfunding.potal.service.TicketService;
import com.atguigu.atcrowdfunding.utils.Const;
import com.atguigu.atcrowdfunding.utils.Data;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/member")
public class MenberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CertService certService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;

    @RequestMapping("doCheckCode")
    @ResponseBody()
    public AjaxResult doCheckCode(HttpSession session,String authcode){
        try {
            //获取登录会员信息
            Member member = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            
            //让系统用户完成：验证码审核任务
            Ticket ticket = ticketService.getTicketByMemberidAndStatus(member.getId());
            if(!ticket.getAuthcode().equals(authcode)){
                return AjaxResult.fail("验证码错误，请检查验证码！");
            }

            //完成审核验证码任务
            Task task = taskService.createTaskQuery().processInstanceId(ticket.getPiid()).taskAssignee(member.getLoginacct()).singleResult();
            taskService.complete(task.getId());

            //更新用户申请状态
            member.setAuthstatus("1");
            memberService.updateAuthStatus(member);

            //记录流程步骤
            ticket.setPstep(Const.PSTEP_5);
            ticketService.update(ticket);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }
    
    @RequestMapping("/checkCode")
    public String toCheckCode(){
        return "/member/checkCode";
    }

    @ResponseBody
    @RequestMapping("doCheckEmail")
    public AjaxResult doCheckEmai(HttpSession session,String email){
        try {
            //获取登录会员信息
            Member member = (Member) session.getAttribute(Const.LOGIN_MEMBER);

            //如果用户输入新的邮箱，将旧的邮箱地址替换
            String oldEmail = member.getEmail();
            if(!oldEmail.equals(email)){
                member.setEmail(email);
                memberService.updateEmail(member);
            }

            //启动实名认证流程 - 系统自动发送邮件，生成验证码，验证邮箱地址是否合法
            StringBuilder authcode = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                authcode.append(new Random().nextInt(10));
            }

            Map<String,Object> variables = new HashMap<>();
            variables.put("toEmail",email.toString());
            variables.put("authcode",authcode.toString());
            variables.put("loginacct",member.getLoginacct());
            variables.put("passListener",new PassListener());
            variables.put("refuseListener",new RefuseListener());

            ProcessInstance auth = runtimeService.startProcessInstanceByKey("auth",variables);

            //记录流程步骤
            Ticket ticket = ticketService.getTicketByMemberidAndStatus(member.getId());
            ticket.setPstep(Const.PSTEP_4);
            ticket.setPiid(auth.getId());
            ticket.setAuthcode(authcode.toString());
            ticketService.update(ticket);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    /**
     * 填写邮箱，发送验证码页面
     * @return
     */
    @RequestMapping("checkEmail")
    public String toCheckEmail(){
        return "/member/checkEmail";
    }

    /**
     * 上传资质文件
     * @param session
     * @param data
     * @return
     */
    @ResponseBody
    @RequestMapping("doUploadCert")
    public AjaxResult doUploadCert(HttpSession session, Data data){
        try {
            //获取登录会员信息
            Member member = (Member) session.getAttribute(Const.LOGIN_MEMBER);

            //获取图片文件存放目录真实路径
            String realpath = session.getServletContext().getRealPath("/pics");

            //保存会员与资质关系数据
            List<MemberCert> certimgs = data.getCertimgs();
            for (MemberCert memberCert : certimgs){
                //资质文件上传
                MultipartFile file = memberCert.getFile();

                String OriginalFilename = file.getOriginalFilename();//java.jpg
                String extname = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));//.jpg
                String newName = UUID.randomUUID().toString()+extname;

                String path = realpath+"\\cert\\"+newName;

                System.out.println(path);

                file.transferTo(new File(path));

                memberCert.setMemberid(member.getId());
                memberCert.setIconpath(newName);
            }

            //保存会员与资质数据
            certService.insertMemberCert(certimgs);

            //记录流程步骤
            Ticket ticket = ticketService.getTicketByMemberidAndStatus(member.getId());
            ticket.setPstep(Const.PSTEP_3);
            ticketService.updatePstepById(ticket);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    /**
     * 上传资质界面
     * @return
     */
    @RequestMapping("/uploadCert")
    public String toUploadCert(){
        return "member/uploadCert";
    }

    /**
     * 更新基本信息
     * @param session
     * @param member
     * @return
     */
    @RequestMapping("/updateBasicinfo")
    @ResponseBody
    public AjaxResult updateBasicInfo(HttpSession session,Member member){
        try {
            //获取登录会员信息
            Member loginmember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            loginmember.setRealname(member.getRealname());
            loginmember.setCardnum(member.getCardnum());
            loginmember.setPhonenum(member.getPhonenum());
            //更新账户类型
            memberService.updateBasicinfo(loginmember);

            //记录流程步骤
            Ticket ticket = ticketService.getTicketByMemberidAndStatus(loginmember.getId());
            ticket.setPstep(Const.PSTEP_2);
            ticketService.updatePstepById(ticket);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    /**
     * 用户基本信息页面
     * @return
     */
    @RequestMapping("/basicinfo")
    public String toBasicInfo(){
        return "/member/basicinfo";
    }

    /**
     * 更新账户类型
     * @param session
     * @param accttype
     * @return
     */
    @RequestMapping("/updateAcctType")
    @ResponseBody
    public AjaxResult updateAcctType(HttpSession session,String accttype){
        try {
            //获取登录会员信息
            Member member = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            member.setAccttype(accttype);
            //更新账户类型
            memberService.updateAcctType(member);

            //记录流程步骤
            Ticket ticket = ticketService.getTicketByMemberidAndStatus(member.getId());
            ticket.setPstep(Const.PSTEP_1);
            ticketService.updatePstepById(ticket);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    /**
     * 实名认证选择用户类型界面
     * @return
     */
    @RequestMapping("/accttype")
    public String toAccttype(){
        return "/member/accttype";
    }

    /**
     * 实名认证选择用户类型界面
     * @return
     */
    @RequestMapping("/apply")
    public String apply(HttpSession session){

        Member member = (Member) session.getAttribute(Const.LOGIN_MEMBER);

        //根据memberid查询正在实名认证的审批流程单
        Ticket ticket = ticketService.getTicketByMemberidAndStatus(member.getId());

        if(ticket == null){
            ticket = new Ticket();
            ticket.setMemberid(member.getId());
            ticket.setStatus("0");

            ticketService.insertTicket(ticket);

            return "/member/"+Const.PSTEP_1;
        }else {
            String pstep = ticket.getPstep();

            if(Const.PSTEP_1.equals(pstep)){
                return "/member/"+Const.PSTEP_2;
            }else if(Const.PSTEP_2.equals(pstep)){
                //根据当前用户查询账户类型，然后根据账户类型查找需要上传的资质

                List<Cert> certs = certService.queryCertByAccttype(member.getAccttype());
                session.setAttribute("certs",certs);

                return "/member/"+Const.PSTEP_3;
            }else if (Const.PSTEP_3.equals(pstep)){
                return "/member/"+Const.PSTEP_4;
            }else if (Const.PSTEP_4.equals(pstep)){
                return "/member/"+Const.PSTEP_5;
            }else if (Const.PSTEP_5.equals(pstep)){
                return "redirect:/member/index.htm";
            }
        }
        return "/member/"+Const.PSTEP_1;
    }

    /**
     * 前台登录用户主页
     * @return
     */
    @RequestMapping("/index")
    public String toMember(){
        return "/member/member";
    }

}
