package com.vocinno.centanet.apputils;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/4/13.
 */
public  class MyUtils {
    public static List<Activity> actList;
    public static final String INTO_FROM_LIST="intoForList";
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
    public static void addActivityToList(Activity activity){
        if(actList==null){
            actList=new ArrayList<Activity>();
        }
        actList.add(activity);
    }
    public static void removeActivityFromList(){
        if(actList!=null){
            Iterator it = actList.iterator();
            while (it.hasNext()) {
                ((Activity)it.next()).finish();
            }
            /*for (int i = 0; i <actList.size() ; i++) {
                actList.get(i).finish();
            }*/
            actList.clear();
        }
    }
}
