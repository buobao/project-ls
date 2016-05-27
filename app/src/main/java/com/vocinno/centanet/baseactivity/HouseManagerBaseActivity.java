package com.vocinno.centanet.baseactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.vocinno.centanet.R;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.myinterface.HttpInterface;

public abstract class HouseManagerBaseActivity extends FragmentActivity implements View.OnClickListener {
    public Activity mContext = null;
    public Handler mHander = null;
    public ModelDialog modelDialog;
    private DrawerLayout drawer_layout;
    private View leftMenuView;
    public String URL;
    public Intent intent;
    public HttpInterface hif;
    public abstract int setContentLayoutId();    /*******************抽象方法***************************/
    public abstract void initView();
    public abstract void initData();
    public  String TAG = null;
    public static String VPI="viewPageIndex";
//    public abstract void notifCallBack(final String name,final String className, final Object data);
    public abstract Handler setHandler();
    public View baseView=null;
    private RelativeLayout fuJinChuShou,fuJinChuZu, yueKanFangYuan,
            woDeChuShou,woDeChuZu,yaoShiGuanLi, woDeKeYuan,woDeQianKe, qiangGongShou,
            qiangGongZu, qiangGongKe, shuPINMa, saoYiSao,woDeTiXing,ry_exit,rl_my_collection,rl_dian_collection;
    /********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        intent=new Intent();
        hif=(HttpInterface)this;
        MyUtils.addActivityToAllList(this);
        TAG=this.getClass().getName();
        int layoutId=setContentLayoutId();
        baseView=getLayoutInflater().inflate(layoutId,null);
        setContentView(baseView);
        drawer_layout=(DrawerLayout)findViewById(R.id.drawer_layout);
        initView();
        mHander = setHandler();
        setClickListener();
        initData();
    }
    private void setClickListener() {
        leftMenuView=findViewById(R.id.left_menu_housemanager);
        fuJinChuShou=(RelativeLayout)findViewById(R.id.rlyt_sell_house_main_page_slid_menus);
        fuJinChuShou.setOnClickListener(this);

        fuJinChuZu = (RelativeLayout) findViewById(R.id.rlyt_rent_house_main_page_slid_menus);
        fuJinChuZu.setOnClickListener(this);

        yueKanFangYuan = (RelativeLayout) findViewById(R.id.rlyt_see_house_main_page_slid_menus);
        yueKanFangYuan.setOnClickListener(this);

        woDeChuShou = (RelativeLayout) findViewById(R.id.rlyt_my_house_main_page_slid_menus);
        woDeChuShou.setOnClickListener(this);

//        woDeChuZu = (RelativeLayout) findViewById(R.id.rlyt_my_house_main_page_slid_menus2);
//        woDeChuZu.setOnClickListener(this);

        yaoShiGuanLi = (RelativeLayout) findViewById(R.id.rlyt_key_house_main_page_slid_menus);
        yaoShiGuanLi.setOnClickListener(this);


        woDeKeYuan = (RelativeLayout) findViewById(R.id.rlyt_my_customer_main_page_slid_menus);
        woDeKeYuan.setOnClickListener(this);

        woDeQianKe = (RelativeLayout) findViewById(R.id.rlyt_my_potential_customer_main_page_slid_menus);
        woDeQianKe.setOnClickListener(this);

        qiangGongShou = (RelativeLayout) findViewById(R.id.rlyt_grab_house_main_page_slid_menus);
        qiangGongShou.setOnClickListener(this);

        /*qiangGongZu = (RelativeLayout) findViewById(R.id.rlyt_grab_house_main_page_slid_menus2);
        qiangGongZu.setOnClickListener(this);*/

        qiangGongKe = (RelativeLayout) findViewById(R.id.rlyt_grab_customer_main_page_slid_menus);
        qiangGongKe.setOnClickListener(this);

        shuPINMa = (RelativeLayout) findViewById(R.id.rlyt_password_main_page_slid_menus);
        shuPINMa.setOnClickListener(this);

        saoYiSao = (RelativeLayout) findViewById(R.id.rlyt_sacn_customer_main_page_slid_menus);
        saoYiSao.setOnClickListener(this);

        woDeTiXing = (RelativeLayout)findViewById(R.id.rlyt_remind_customer_main_page_slid_menus);
        woDeTiXing.setOnClickListener(this);

        ry_exit = (RelativeLayout)findViewById(R.id.ry_exit);
        ry_exit.setOnClickListener(this);

        rl_my_collection = (RelativeLayout)findViewById(R.id.rl_my_collection);
        rl_my_collection.setOnClickListener(this);

        rl_dian_collection = (RelativeLayout)findViewById(R.id.rl_dian_collection);
        rl_dian_collection.setOnClickListener(this);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(drawer_layout.isDrawerOpen(leftMenuView)){
            drawer_layout.closeDrawer(leftMenuView);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyUtils.allActList.remove(this);
    }
}
