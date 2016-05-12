package com.vocinno.centanet.tools.constant;

import com.vocinno.centanet.apputils.AppApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/9.
 */
public class MyConstant {
    private static AppApplication myApp;
    private static Map<String,String> map;
    public static void setMyApp(AppApplication application){
        myApp=application;
    }
    public static Map getToken(){
        if(myApp.getToken()!=null){
            Map<String,String> map=new HashMap<String,String>();
            String empid=myApp.getEmpId();
            String token=myApp.getToken();
            if(empid!=null){
                map.put(NetWorkMethod.empId,empid);
            }
            if(token!=null){
                map.put(NetWorkMethod.token,token);
            }
            return map;
        }
        return null;
    }

    public static Map getPMap(){
        if(map==null){
            map=new HashMap<String,String>();
        }else{
            map.clear();
        }
        return map;
    }
}
