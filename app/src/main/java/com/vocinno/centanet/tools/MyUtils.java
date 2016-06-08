package com.vocinno.centanet.tools;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.tools.constant.MyConstant;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/4/13.
 */
public  class MyUtils {
    public static List<Activity> actList;
    public static List<Activity> allActList;
    public static final String INTO_FROM_LIST="intoForList";
    public static final String ROB_GONG_FANG="gongFang";
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

    public static void addActivityToAllList(Activity activity){
        if(allActList==null){
            allActList=new ArrayList<Activity>();
        }
        allActList.add(activity);
    }
    public static void removeActivityFromAllList(){
        if(allActList!=null){
            Iterator it = allActList.iterator();
            while (it.hasNext()) {
                ((Activity)it.next()).finish();
            }
            allActList.clear();
            if(actList!=null){
                actList.clear();
            }
        }
    }

    public static int compareDate(Date date,Date date2){
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(date2);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return Math.abs(day2 - day1);
    }
    public static int compareNowDate(Date date){
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(new Date());
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return Math.abs(day2 - day1);
    }
    public static int compareNowDate(String date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar aCalendar = Calendar.getInstance();
        try {
            aCalendar.setTime(sdf.parse(date));
            int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
            aCalendar.setTime(new Date());
            int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
            return Math.abs(day2 - day1);
        } catch (ParseException e) {
            e.printStackTrace();
            return 99;
        }
    }
    public static String dateFormat(String date,String formatString){
        SimpleDateFormat sdf1=new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat sdf2=new SimpleDateFormat(formatString);
        try {
            String format=sdf2.format(sdf1.parse(date));
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
    public static String dateFormat(String date){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        try {
            String format=sdf.format(sdf.parse(date));
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
    public static String getFormatDate(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            String format=sdf.format(date);
            return format;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void showDialog(final Intent intent,final Context ctx,final Class clazz){
        MyDialog.Builder builder=new MyDialog.Builder(ctx);
        builder.setTitle("提示");
        builder.setMessage("确认退出该账户吗?");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                intent.setClass(ctx,clazz);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MyConstant.isExit, true);
                ctx.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static int getHeight(Context context){
        WindowManager wm = ((Activity)context).getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }
    public static int getWidth(Context context){
        WindowManager wm = ((Activity)context).getWindowManager();
        return wm.getDefaultDisplay().getWidth();
    }
    public static double division(double d1,
                             double d2,int len) {// 进行除法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static double division(String d1,String d2,int len) {// 进行除法运算
        double dl1=Double.parseDouble(d1);
        double dl2=Double.parseDouble(d2);
        return division(dl1, dl2, 2);
    }
    public static double division(String d1,String d2) {// 进行除法运算
        return division(d1,d2,2);
    }
    public static void LogI(String d1,String d2) {
        Log.i("request==log-key["+d1,"]########["+d2+"]");
    }
    public static void showToast(Context context,String msg){
        if(context!=null){
            if(msg!=null&&msg.length()>0){
                Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
            }
        }
    };
    public static String newImgUrl(String imgUrl){
        String newUrl;
        if(imgUrl!=null&&imgUrl.trim().length()>0){
            if(imgUrl.indexOf(".")>=0){
                String[]splitString=imgUrl.split(".");
                newUrl=splitString[0]+"/400_300_f"+"."+splitString[1];
                return newUrl;
            }else{
                return imgUrl;
            }
        }
        return imgUrl;
    };
}
