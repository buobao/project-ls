package com.vocinno.centanet.home;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.keymanage.KeyGetInActivity;
import com.vocinno.centanet.keymanage.KeyManageActivity;
import com.vocinno.centanet.remind.MessageListActivity;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.zbar.lib.CaptureActivity;

public class LeftMenuFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout fuJinChuShou,fuJinChuZu, yueKanFangYuan,
            woDeChuShou,woDeChuZu,yaoShiGuanLi, woDeKeYuan, qiangGongShou,
            qiangGongZu, qiangGongKe, shuPINMa, saoYiSao,woDeTiXing;
    private View view;
    private Activity mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_slid_menu, null);
        mContext=getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setClickListener();
    }

    private void setClickListener() {
        fuJinChuShou=(RelativeLayout)view.findViewById(R.id.rlyt_sell_house_main_page_slid_menus);
        fuJinChuShou.setOnClickListener(this);

        fuJinChuZu = (RelativeLayout) view.findViewById(R.id.rlyt_rent_house_main_page_slid_menus);
        fuJinChuZu.setOnClickListener(this);

        yueKanFangYuan = (RelativeLayout) view.findViewById(R.id.rlyt_see_house_main_page_slid_menus);
        yueKanFangYuan.setOnClickListener(this);

        woDeChuShou = (RelativeLayout) view.findViewById(R.id.rlyt_my_house_main_page_slid_menus);
        woDeChuShou.setOnClickListener(this);

        woDeChuZu = (RelativeLayout) view.findViewById(R.id.rlyt_my_house_main_page_slid_menus2);
        woDeChuZu.setOnClickListener(this);

        yaoShiGuanLi = (RelativeLayout) view.findViewById(R.id.rlyt_key_house_main_page_slid_menus);
        yaoShiGuanLi.setOnClickListener(this);


        woDeKeYuan = (RelativeLayout) view.findViewById(R.id.rlyt_my_customer_main_page_slid_menus);
        woDeKeYuan.setOnClickListener(this);

        qiangGongShou = (RelativeLayout) view.findViewById(R.id.rlyt_grab_house_main_page_slid_menus);
        qiangGongShou.setOnClickListener(this);

        qiangGongZu = (RelativeLayout) view.findViewById(R.id.rlyt_grab_house_main_page_slid_menus2);
        qiangGongZu.setOnClickListener(this);

        qiangGongKe = (RelativeLayout) view.findViewById(R.id.rlyt_grab_customer_main_page_slid_menus);
        qiangGongKe.setOnClickListener(this);

        shuPINMa = (RelativeLayout) view.findViewById(R.id.rlyt_password_main_page_slid_menus);
        shuPINMa.setOnClickListener(this);

        saoYiSao = (RelativeLayout) view.findViewById(R.id.rlyt_sacn_customer_main_page_slid_menus);
        saoYiSao.setOnClickListener(this);

        woDeTiXing = (RelativeLayout) view.findViewById(R.id.rlyt_remind_customer_main_page_slid_menus);
        woDeTiXing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //附近出售
            case R.id.rlyt_sell_house_main_page_slid_menus:
                HouseManageActivity.mArrayHouseItemList[0]=null;
                MethodsDeliverData.mIntHouseType = HouseType.CHU_SHOU;
                MethodsExtra.startActivity(mContext, HouseManageActivity.class);
                break;
            //附近出租
            case R.id.rlyt_rent_house_main_page_slid_menus:
                HouseManageActivity.mArrayHouseItemList[1]=null;
                MethodsDeliverData.mIntHouseType = HouseType.CHU_ZU;
                MethodsExtra.startActivity(mContext, HouseManageActivity.class);
                break;
            //约看房源
            case R.id.rlyt_see_house_main_page_slid_menus:
                MethodsDeliverData.mIntHouseType = HouseType.YUE_KAN;
                MethodsExtra.startActivity(mContext, HouseManageActivity.class);
                break;
            //我的出售
            case R.id.rlyt_my_house_main_page_slid_menus:
                HouseManageActivity.mArrayHouseItemList[3]=null;
                MethodsDeliverData.mIntHouseType = HouseType.WO_DE;
                MethodsExtra.startActivity(mContext, HouseManageActivity.class);
                break;
            //我的出租
            case R.id.rlyt_my_house_main_page_slid_menus2:
                HouseManageActivity.mArrayHouseItemList[4]=null;
                CST_JS.setZOrS("r");
                HouseManageActivity.zOrS=false;
                MethodsDeliverData.mIntHouseType = HouseType.WO_DEZU2;
                MethodsExtra.startActivity(mContext, HouseManageActivity.class);
                break;
            //钥匙
            case R.id.rlyt_key_house_main_page_slid_menus:
                MethodsExtra.startActivity(mContext, KeyManageActivity.class);
                break;
            //客源
            case R.id.rlyt_my_customer_main_page_slid_menus:
                MethodsDeliverData.keYuanOrGongKe=1;
                MethodsDeliverData.isMyCustomer = true;
                MethodsExtra.startActivity(mContext,
                        CustomerManageActivity.class);
                break;
            //抢公售
            case R.id.rlyt_grab_house_main_page_slid_menus:
                MethodsDeliverData.flag = 1;
                MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
                MethodsExtra.startActivity(mContext, HouseManageActivity.class);
                break;
            //抢公租
            case R.id.rlyt_grab_house_main_page_slid_menus2:
                MethodsDeliverData.flag = 1;
                MethodsDeliverData.mIntHouseType = HouseType.GONG_FANGZU;
                MethodsExtra.startActivity(mContext, HouseManageActivity.class);
                break;
            //公客
            case R.id.rlyt_grab_customer_main_page_slid_menus:
                MethodsDeliverData.keYuanOrGongKe=0;
                MethodsDeliverData.flag = 1;
                MethodsDeliverData.isMyCustomer = false;
                MethodsExtra.startActivity(mContext,
                        CustomerManageActivity.class);
                break;
            //PIN码
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
        }
    }
}
