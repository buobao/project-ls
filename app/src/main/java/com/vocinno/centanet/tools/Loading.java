package com.vocinno.centanet.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.vocinno.centanet.R;

public class Loading extends Dialog {
    public static int showTag = 0;
    private static Loading loading;
    private static Context context;
    public String a="1";
    public String b="1";
    public synchronized void setIsShow(int flag) {
        showTag = flag;
    }

    public Loading(Context context, int layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        this.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth() * 3 / 4;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    private static void setLoading(Context ctx) {
        context = ctx;
        loading = new Loading(context, R.layout.loading, R.style.Theme_dialog);
        loading.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loading.showTag = 0;
            }
        });
    }

    public  static void show(Context ctx) {
        if(loading==null||!loading.isShowing()){
            setLoading(ctx);
        }
        if (Loading.showTag == 0 && loading != null) {
            Activity activity = (Activity) ctx;
            if (activity != null && !activity.isFinishing()) {
                Loading.showTag = 1;
                loading.show();
            } else {
                loading.showTag = 0;
            }
        }
    }
    public static void dismissLoading() {
        if (loading != null &&loading.isShowing()) {
            loading.showTag = 0;
            loading.dismiss();
        }
    }

}