package com.vocinno.centanet.baseactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.MyUtils;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.housemanage.HouseManageActivity2;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.view.refreshablelistview.XListView;

/**
 * Created by Administrator on 2016/4/20.
 */
public abstract class OtherHomeMenuBaseActivity extends Activity implements HttpInterface,XListView.IXListViewListener,View.OnClickListener{
    public Intent intent;
    public DrawerLayout drawer_layout;
    public View leftMenuView;
    public XListView mListView;
    public View mBackView;
    public MethodsJni methodsJni;
    public Activity mContext = null;
    public Handler mHander = null;
    public ModelDialog modelDialog;//loading
    public MyDialog.Builder myDialog;//自定义窗口
    public HttpInterface hif;
    /*******************抽象方法***************************/
    public abstract int setContentLayoutId();
    public abstract void initView();
    public abstract void initData();
    public  String TAG = null;
    public abstract Handler setHandler();
    public View baseView=null;
    private RelativeLayout fuJinChuShou,fuJinChuZu, yueKanFangYuan,
            woDeChuShou,woDeChuZu,yaoShiGuanLi, woDeKeYuan, qiangGongShou,
            qiangGongZu, qiangGongKe, shuPINMa, saoYiSao,woDeTiXing;
    /********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        hif=(HttpInterface)this;
        MyUtils.addActivityToList(this);
        MyUtils.addActivityToAllList(this);
        TAG=this.getClass().getName();
        int layoutId=setContentLayoutId();
        baseView=getLayoutInflater().inflate(layoutId,null);
        setContentView(baseView);
        initView();
        mHander = setHandler();
        setClickListener();
        initData();
    }
    private void setClickListener() {
        drawer_layout=(DrawerLayout)findViewById(R.id.dl_menu_tixing);
        leftMenuView=findViewById(R.id.left_menu_tixing);

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

        woDeTiXing = (RelativeLayout)findViewById(R.id.rlyt_remind_customer_main_page_slid_menus);
        woDeTiXing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //附近出售
            case R.id.rlyt_sell_house_main_page_slid_menus:
                startIntent(0);
                break;
            //附近出租
            case R.id.rlyt_rent_house_main_page_slid_menus:
                startIntent(1);
                break;
            //约看房源
            case R.id.rlyt_see_house_main_page_slid_menus:
                startIntent(2);
                break;
            //我的出售
            case R.id.rlyt_my_house_main_page_slid_menus:
                startIntent(3);
                break;
            //我的出租
            case R.id.rlyt_my_house_main_page_slid_menus2:
                startIntent(4);
                break;
        }
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
    public void startIntentToGongFangManager(int index){
        if(intent==null){
            intent=new Intent();
        }
        intent.setClass(mContext, HouseManageActivity2.class);
        intent.putExtra("viewPageIndex", index);
        intent.putExtra(MyUtils.ROB_GONG_FANG,true);
        startActivity(intent);
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(drawer_layout.isDrawerOpen(leftMenuView)){
            drawer_layout.closeDrawer(leftMenuView);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startIntent(int index,boolean flag){
        MyUtils.removeActivityFromAllList();
        if(intent==null){
            intent=new Intent();
        }
        intent.setClass(mContext, HouseManageActivity2.class);
        intent.putExtra("viewPageIndex", index);
        intent.putExtra(MyUtils.ROB_GONG_FANG,flag);
        startActivity(intent);
    };
    public void startIntent(int index){
        MyUtils.removeActivityFromAllList();
        if(intent==null){
            intent=new Intent();
        }
        intent.setClass(mContext, HouseManageActivity2.class);
        intent.putExtra("viewPageIndex", index);
        intent.putExtra(MyUtils.ROB_GONG_FANG,false);
        startActivity(intent);
    };
    public void houseDetailReturn(int index,boolean flag){
        if(intent==null){
            intent=new Intent();
        }
        intent.putExtra(HouseManageActivity2.VPI, index);
        intent.putExtra(MyUtils.ROB_GONG_FANG,flag);
        this.setResult(RESULT_OK, intent);
        finish();
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyUtils.actList.remove(this);
        MyUtils.allActList.remove(this);
    }
}
