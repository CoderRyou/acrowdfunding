package com.atguigu.atcrowdfunding.bean;

import java.util.HashMap;
import java.util.Map;

public class AjaxResult {

    //请求成功标志
    private boolean success;

    //提示信息
    private String msg;

    //用户要返回给浏览器的数据
    private Map<String,Object> datas = new HashMap<String,Object>();

    public static AjaxResult success(){
        return success("处理成功！");
    }

    public static AjaxResult fail(){
        return fail("处理失败！");
    }

    public static AjaxResult success(String message){
        AjaxResult result = new AjaxResult();
        result.setSuccess(true);
        result.setMsg(message);

        return result;
    }

    public static AjaxResult fail(String message){
        AjaxResult result = new AjaxResult();
        result.setSuccess(false);
        result.setMsg(message);

        return result;
    }

    public AjaxResult add(String key,Object value){
        this.datas.put(key, value);
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }
}
