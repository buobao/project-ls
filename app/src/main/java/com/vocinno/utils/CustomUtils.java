package com.vocinno.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/3/21.
 */
public class CustomUtils {
    public static int getWindowWidth(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
    public static int getWindowHeight(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
}
