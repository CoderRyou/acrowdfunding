package com.atguigu.atcrowdfunding.utils;

public class StringUtil {
    public static boolean isEmpty(String inStr){
        if(inStr == null){
            return true;
        }
        if(inStr.equals("")){
            return true;
        }else {
            return false;
        }
    }
}
