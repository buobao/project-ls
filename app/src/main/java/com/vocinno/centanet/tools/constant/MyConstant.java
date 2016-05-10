package com.vocinno.centanet.tools.constant;

import com.vocinno.centanet.apputils.AppApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/9.
 */
public class MyConstant {
    private static AppApplication myApp;
    public static void setMyApp(AppApplication application){
        myApp=application;
    }
    public static Map getToken(){
        if(myApp.getToken()!=null){
            Map<String,String> map=new HashMap<String,String>();
            map.put(NetWorkConstant.empId,myApp.getEmpId());
            map.put(NetWorkConstant.token,myApp.getToken());
            return map;
        }
        return null;
    }
}
