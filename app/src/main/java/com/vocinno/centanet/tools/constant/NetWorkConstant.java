package com.vocinno.centanet.tools.constant;

import com.vocinno.centanet.apputils.AppApplication;
import com.vocinno.utils.LocationUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/9.
 */
public class NetWorkConstant {
    //测试环境
//    public static final String PORT_URL="http://61.152.255.241:8082/sales-web/";//测试环境
    public static final String PORT_URL="http://10.28.11.67:8082/sales-web/";//余敏
//    public static final String PORT_URL="http://10.28.11.77:8082/sales-web/";//谭美玉
//    public static final String PORT_URL="http://a.sh.centanet.com/sales-web/";//正式环境
    public static final int pageSize=20;



    private static AppApplication myApp;
    private static Map<String,String> map;
    public final static int REFRESH=99;
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
            map.put(NetWorkMethod.jingDu, LocationUtil.getLongitude()+"");
            map.put(NetWorkMethod.weiDu, LocationUtil.getLatitude()+"");
            return map;
        }
        return null;
    }
    public static String getStrToken(){
        if(myApp.getToken()!=null){
//            String empid=myApp.getEmpId();
            String token=myApp.getToken();
            if(token!=null){
                return "?"+NetWorkMethod.token+"="+token;
            }
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
