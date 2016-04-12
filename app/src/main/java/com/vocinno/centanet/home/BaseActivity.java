package com.vocinno.centanet.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.ModelDialog;

import cn.jpush.android.api.JPushInterface;

public abstract class BaseActivity extends Activity implements View.OnClickListener {
    public static String TAG = null;
    public Activity mContext = null;
    public Handler mHander = null;
    public ModelDialog modelDialog;
    public SlidingMenu menu;
    /*******************抽象方法***************************/
    public abstract Handler setHandler();
    public abstract int setContentLayoutId();
    public abstract void initView();
    public abstract void initData();
    public abstract void notifCallBack(final String name,final String className, final Object data);
    public View baseView=null;
    /********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId=setContentLayoutId();
        baseView=getLayoutInflater().inflate(layoutId,null);
        setContentView(layoutId);
        initView();
        setSlidingMenu();
        initData();
    }

    private void setSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);//setSecondaryMenu
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeEnabled(true);
        menu.setFadeDegree(0.35f);
        //把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);

        //为侧滑菜单设置布局
        menu.setMenu(R.layout.leftmenu);
    }


    public void showDialog(){
        if(this.modelDialog==null){
            this.modelDialog=ModelDialog.getModelDialog(this);
        }
        this.modelDialog.show();
    }
    public void dismissDialog(){
        if(this.modelDialog!=null&&this.modelDialog.isShowing()){
            this.modelDialog.dismiss();
        }
    }
    /********************第三方sdk初始化**********************************/
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
