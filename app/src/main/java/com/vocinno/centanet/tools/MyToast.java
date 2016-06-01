package com.vocinno.centanet.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/10.
 */
public class MyToast {
    private static Context context;
    private static MyToast mInstance;
    public static MyToast getInstance(Context mContext)
    {
        if (mInstance == null)
        {
            synchronized (OkHttpClientManager.class)
            {
                if (mInstance == null)
                {
                    context=mContext;
                    mInstance=new MyToast();
                }
            }
        }
        return mInstance;
    }
    public static void showToast(String str){
        if(context!=null){
            if(str!=null&&str.length()>0){
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static void showToast(Context ctx,String str){
        if(ctx!=null){
            if(str!=null&&str.length()>0){
                Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
