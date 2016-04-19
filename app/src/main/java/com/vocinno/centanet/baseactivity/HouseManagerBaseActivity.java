package com.vocinno.centanet.baseactivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.ModelDialog;

public abstract class HouseManagerBaseActivity extends FragmentActivity implements View.OnClickListener{
    public Activity mContext = null;
    public Handler mHander = null;
    public ModelDialog modelDialog;
    public static SlidingMenu menu;
    /*******************抽象方法***************************/
    public abstract int setContentLayoutId();
    public abstract void initView();
    public abstract void initData();
    public  String TAG = null;
//    public abstract void notifCallBack(final String name,final String className, final Object data);
    public abstract Handler setHandler();
    public View baseView=null;
    private View leftMenuView;
    private RelativeLayout fuJinChuShou,fuJinChuZu, yueKanFangYuan,
            woDeChuShou,woDeChuZu,yaoShiGuanLi, woDeKeYuan, qiangGongShou,
            qiangGongZu, qiangGongKe, shuPINMa, saoYiSao;
    /********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        TAG=this.getClass().getName();
        int layoutId=setContentLayoutId();
        baseView=getLayoutInflater().inflate(layoutId,null);
        setContentView(baseView);
        initView();
        mHander = setHandler();
        setSlidingMenu();
        setClickListener();
        initData();
    }
    private void setClickListener() {
        fuJinChuShou=(RelativeLayout)findViewById(R.id.rlyt_sell_house_main_page_slid_menus);
        fuJinChuShou.setOnClickListener(this);

        fuJinChuZu = (RelativeLayout) findViewById(R.id.rlyt_rent_house_main_page_slid_menus);
        fuJinChuZu.setOnClickListener(this);

        yueKanFangYuan = (RelativeLayout) findViewById(R.id.rlyt_see_house_main_page_slid_menus);
        yueKanFangYuan.setOnClickListener(this);

        woDeChuShou = (RelativeLayout) findViewById(R.id.rlyt_my_house_main_page_slid_menus);
        woDeChuShou.setOnClickListener(this);

        woDeChuZu = (RelativeLayout) findViewById(R.id.rlyt_my_house_main_page_slid_menus2);
        woDeChuZu.setOnClickListener(this);

        yaoShiGuanLi = (RelativeLayout) findViewById(R.id.rlyt_key_house_main_page_slid_menus);
        yaoShiGuanLi.setOnClickListener(this);


        woDeKeYuan = (RelativeLayout) findViewById(R.id.rlyt_my_customer_main_page_slid_menus);
        woDeKeYuan.setOnClickListener(this);

        qiangGongShou = (RelativeLayout) findViewById(R.id.rlyt_grab_house_main_page_slid_menus);
        qiangGongShou.setOnClickListener(this);

        qiangGongZu = (RelativeLayout) findViewById(R.id.rlyt_grab_house_main_page_slid_menus2);
        qiangGongZu.setOnClickListener(this);

        qiangGongKe = (RelativeLayout) findViewById(R.id.rlyt_grab_customer_main_page_slid_menus);
        qiangGongKe.setOnClickListener(this);

        shuPINMa = (RelativeLayout) findViewById(R.id.rlyt_password_main_page_slid_menus);
        shuPINMa.setOnClickListener(this);

        saoYiSao = (RelativeLayout) findViewById(R.id.rlyt_sacn_customer_main_page_slid_menus);
        saoYiSao.setOnClickListener(this);
    }

    private void setSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);//setSecondaryMenu
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
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
        leftMenuView=getLayoutInflater().inflate(R.layout.activity_slid_menu, null);
        menu.setMenu(leftMenuView);
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
    public void dismissDialog(boolean backDismiss){
        if(!backDismiss){
            if(this.modelDialog!=null&&this.modelDialog.isShowing()){
                this.modelDialog.dismiss();
            }
        }else{

        }

    }
}
