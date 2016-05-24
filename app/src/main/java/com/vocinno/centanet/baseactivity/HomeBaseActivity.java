package com.vocinno.centanet.baseactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.RelativeLayout;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.customermanage.PotentialCustomerListActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.keymanage.KeyGetInActivity;
import com.vocinno.centanet.keymanage.KeyManageActivity;
import com.vocinno.centanet.remind.MessageListActivity;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.user.UserLoginActivity;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.zbar.lib.CaptureActivity;

import cn.jpush.android.api.JPushInterface;

public abstract class HomeBaseActivity extends Activity implements View.OnClickListener {
    public static String TAG = null;
    public Activity mContext = null;
    public Handler mHander = null;
    public ModelDialog modelDialog;
    public DrawerLayout drawer_layout;
    public View leftMenuView;
    public Intent intent;
    /*******************抽象方法***************************/
    public abstract Handler setHandler();
    public abstract int setContentLayoutId();
    public abstract void initView();
    public abstract void initData();
    public abstract void notifCallBack(final String name,final String className, final Object data);
    public View baseView=null;
    private RelativeLayout fuJinChuShou,fuJinChuZu, yueKanFangYuan,
            woDeChuShou,woDeChuZu,yaoShiGuanLi, woDeQianKe,woDeKeYuan, qiangGongShou,
            qiangGongZu, qiangGongKe, shuPINMa, saoYiSao,woDeTiXing,ry_exit,rl_my_collection,rl_dian_collection;
    /********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtils.addActivityToList(this);
        intent=new Intent();
        mContext = this;
        int layoutId=setContentLayoutId();
        baseView=getLayoutInflater().inflate(layoutId,null);
        setContentView(baseView);
        initView();
        mHander = setHandler();
        setClickListener();
        initData();

    }
    public void startIntentToHouseManager(int index){
        startIntentToHouseManager(0, index);
    };
    public void startIntentToHouseManager(int type,int index){
        if(intent==null){
            intent=new Intent();
        }
        intent.setClass(mContext, HouseManageActivity.class);
        intent.putExtra(MyConstant.listType, type);
        intent.putExtra(MyConstant.menuType, index);
        startActivity(intent);
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
                //附近出售
                case R.id.rlyt_sell_house_main_page_slid_menus:
                    startIntentToHouseManager(0);
                    break;
                //附近出租
                case R.id.rlyt_rent_house_main_page_slid_menus:
                    startIntentToHouseManager(1);
                    break;
                //约看房源
                case R.id.rlyt_see_house_main_page_slid_menus:
                    startIntentToHouseManager(2);
                    break;
                //我的出售
                case R.id.rlyt_my_house_main_page_slid_menus:
                    startIntentToHouseManager(3);
                    break;
                //我的出租
                case R.id.rlyt_my_house_main_page_slid_menus2:
                    startIntentToHouseManager(4);
                    break;
                //我的收藏
                case R.id.rl_my_collection:
                    startIntentToHouseManager(MyConstant.myCollectionHouseList,0);
                    break;
                //店租收藏
                case R.id.rl_dian_collection:
                    startIntentToHouseManager(MyConstant.dianzCollectionHouseList,0);
                    break;
                //钥匙管理
                case R.id.rlyt_key_house_main_page_slid_menus:
                    MethodsExtra.startActivity(mContext, KeyManageActivity.class);
                    break;
                //我的客源
                case R.id.rlyt_my_customer_main_page_slid_menus:
                    MethodsDeliverData.keYuanOrGongKe=1;
                    MethodsDeliverData.isMyCustomer = true;
                    MethodsExtra.startActivity(mContext,
                            CustomerManageActivity.class);
                    break;
                //我的潜客
                case R.id.rlyt_my_potential_customer_main_page_slid_menus:
                    MethodsExtra.startActivity(mContext,
                            PotentialCustomerListActivity.class);
                    break;
                //抢公售
                case R.id.rlyt_grab_house_main_page_slid_menus:
                    /*MethodsDeliverData.flag = 1;
                    MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
                    MethodsExtra.startActivity(mContext, HouseManageActivity.class);*/
                    startIntentToHouseManager(2,0);
                    break;
                //抢公租
                case R.id.rlyt_grab_house_main_page_slid_menus2:
                    /*MethodsDeliverData.flag = 1;
                    MethodsDeliverData.mIntHouseType = HouseType.GONG_FANGZU;
                    MethodsExtra.startActivity(mContext, HouseManageActivity.class);*/
                    startIntentToHouseManager(2, 1);
                    break;
                //抢公客
                case R.id.rlyt_grab_customer_main_page_slid_menus:
                    MethodsDeliverData.keYuanOrGongKe=0;
                    MethodsDeliverData.flag = 1;
                    MethodsDeliverData.isMyCustomer = false;
                    MethodsExtra.startActivity(mContext,
                            CustomerManageActivity.class);
                    break;
                //pin码
                case R.id.rlyt_password_main_page_slid_menus:
                    MethodsExtra.startActivity(mContext, KeyGetInActivity.class);
                    break;
                //扫一扫
                case R.id.rlyt_sacn_customer_main_page_slid_menus:
                    MethodsExtra.startActivity(mContext, CaptureActivity.class);
                    break;
                //我的提醒
                case R.id.rlyt_remind_customer_main_page_slid_menus:
                    MethodsDeliverData.flag = -1;
                    MethodsExtra.startActivity(mContext, MessageListActivity.class);
                    break;
                //退出app
                case R.id.ry_exit:
                    MyUtils.showDialog(intent,this, UserLoginActivity.class);
                    break;
        }

        if(drawer_layout.isDrawerOpen(leftMenuView)){
            drawer_layout.closeDrawer(leftMenuView);
        }
    }

    private void setClickListener() {
        drawer_layout= (DrawerLayout) findViewById(R.id.dl_home_page);
        leftMenuView=findViewById(R.id.left_home_page);
        drawer_layout.closeDrawer(leftMenuView);

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

        woDeQianKe = (RelativeLayout) findViewById(R.id.rlyt_my_potential_customer_main_page_slid_menus);
        woDeQianKe.setOnClickListener(this);

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
