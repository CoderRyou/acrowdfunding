package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.AccountTypeCert;
import com.atguigu.atcrowdfunding.bean.AjaxResult;
import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import com.atguigu.atcrowdfunding.manager.service.CertTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/certtype")
public class CertTypeController {
    @Autowired
    private CertTypeService certTypeService;

    @Autowired
    private CertService certService;

    @ResponseBody
    @RequestMapping("/deleteCertAccttype")
    public AjaxResult deleteCertAccttype(AccountTypeCert record){
        try {
            int ret = certTypeService.deleteCertAccttype(record);
            if (ret == 1){
                return AjaxResult.success();
            }else{
                return AjaxResult.fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    @ResponseBody
    @RequestMapping("/insertCertAccttype")
    public AjaxResult insertCertAccttype(AccountTypeCert record){
        try {
            int ret = certTypeService.insertCertAccttype(record);
            if (ret == 1){
                return AjaxResult.success();
            }else{
                return AjaxResult.fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    @ResponseBody
    @RequestMapping("/loadCertAccttype")
    public AjaxResult loadCertAccttype(){
        try {
            //查询资质与账户类型之间的关系（表示之前给账户类型分配过资质）
            List<Map<String, Object>> certAccttypeList = certTypeService.queryCertAccttype();
            return AjaxResult.success().add("certAccttype",certAccttypeList);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
    }

    @RequestMapping("/index")
    public String index(Model model){
        //查询所有资质
        List<Cert> certs = certService.queryAllCert();
        model.addAttribute("allCert",certs);

        return "/certType/certType";
    }
}
