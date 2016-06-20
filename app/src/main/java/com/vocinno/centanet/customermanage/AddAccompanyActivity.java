package com.vocinno.centanet.customermanage;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.entity.ParamCustlookList;
import com.vocinno.centanet.entity.TCmLook;
import com.vocinno.centanet.entity.TCmLookAccompany;
import com.vocinno.centanet.entity.TCmLookHouse;
import com.vocinno.centanet.housemanage.FirstHandHouseActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hewei26 on 2016/6/16.
 *
 * 添加带看  根据选择类型跳转到 一手 & 二手房源
 */
public class AddAccompanyActivity extends OtherBaseActivity {

    @Bind(R.id.iv_type_first)        //选择一手房源
            ImageView mIvTypeFirst;
    @Bind(R.id.iv_type_second)       //选择二手房源
            ImageView mIvTypeSecond;
    @Bind(R.id.et_confirmNum)        //带看确认书编号
            EditText mEtConfirmNum;
    @Bind(R.id.tv_startTime)         //开始时间
            TextView mTvStartTime;
    @Bind(R.id.iv_startTime)         //选择开始时间
            ImageView mIvStartTime;
    @Bind(R.id.tv_endTime)           //结束时间
            TextView mTvEndTime;
    @Bind(R.id.iv_endTime)           //选择结束时间
            ImageView mIvEndTime;
    @Bind(R.id.cb_write_back)         //是否回显
            CheckBox mCbWriteBack;
    @Bind(R.id.et_desc_house)         //文字描述
            EditText mEtDescHouse;
    @Bind(R.id.tv_addHouse)           //添加房源
            TextView mTvAddHouse;

    private ImageView mBack;
    private TextView mSubmit;
    private String lookType;    //房源类型  一手&二手

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_add_accompany;
    }

    @Override
    public void initView() {
        //禁用侧滑
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置标题栏
        mBack = (ImageView) MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mSubmit = (TextView) MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.add_accompany, null);
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        mIvTypeFirst.setOnClickListener(this);
        mIvTypeSecond.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_type_first:    //选择一手房源
                mIvTypeFirst.setImageResource(R.drawable.c_manage_button_choose);
                mIvTypeSecond.setImageResource(R.drawable.c_manage_button_unselected);
                lookType = "20074002";
                break;
            case R.id.iv_type_second:   //选择二手房源
                mIvTypeSecond.setImageResource(R.drawable.c_manage_button_choose);
                mIvTypeFirst.setImageResource(R.drawable.c_manage_button_unselected);
                lookType = "20074001";
                break;
            case R.id.tv_addHouse:      //添加房源
                if(lookType=="20074002"){
                    intent = new Intent(this,FirstHandHouseActivity.class);   //添加一手
                    startActivityForResult(intent,MyConstant.REQUEST_ADDFIRST);
                }else if(lookType=="20074001"){
                    intent = new Intent(this,HouseManageActivity.class);      //房源列表
                    startActivity(intent);
                }

                break;
            case R.id.img_left_mhead1:
                finish();
                break;
            case R.id.tv_right_mhead1:
                //显示Loading
                Loading.show(this);
                String custCode = getIntent().getStringExtra(MyConstant.custCode);  //客户编码
                String confirmationNumber = mEtConfirmNum.getText().toString(); //带看确认书编号
                String startTime = mTvStartTime.getText().toString();   //开始时间
                String endTime = mTvEndTime.getText().toString();       //结束时间
                String custlookTrackType = mCbWriteBack.isChecked() ? "1" : "0";    //是否回写 0&1
                String remark = mEtDescHouse.getText().toString();       //文字描述

                //页面内容(必传)
                TCmLook tCmLook = new TCmLook();
                tCmLook.setRemark(remark);
                tCmLook.setStartTime(startTime);
                tCmLook.setEndTime(endTime);
                tCmLook.setLookType(lookType);
                tCmLook.setCustCode(custCode);
                tCmLook.setConfirmationNumber(confirmationNumber);
                //房源列表
                List<TCmLookHouse> tCmLookHouses = new ArrayList<>();
                TCmLookHouse tCmLookHouse = new TCmLookHouse();
                tCmLookHouse.setHousedelCode("1111");
                tCmLookHouse.setFilesId("11");
                tCmLookHouse.setHouseId(1000000L);
                tCmLookHouse.setFeedback("aaa");
                tCmLookHouses.add(tCmLookHouse);
                tCmLookHouses.add(tCmLookHouse);
                //陪看人列表
                List<TCmLookAccompany> tCmLookAccompanies = new ArrayList<>();
                TCmLookAccompany tCmLookAccompany = new TCmLookAccompany();
                tCmLookAccompany.setAccompanyPromise("0");
                tCmLookAccompany.setAccompanyName("2222");
                tCmLookAccompany.setAccompanyRole("22222");
                tCmLookAccompany.setAccompanyUser("222222");
                tCmLookAccompany.setAccompanyGroup("222222");
                tCmLookAccompanies.add(tCmLookAccompany);
                tCmLookAccompanies.add(tCmLookAccompany);

                ParamCustlookList paramCustlookList = new ParamCustlookList();
                paramCustlookList.settCmLook(tCmLook);
                paramCustlookList.settCmLookHouseList(tCmLookHouses);
                paramCustlookList.settCmLookAccompanyList(tCmLookAccompanies);
                paramCustlookList.setCustlookTrackType(custlookTrackType);

                URL = NetWorkConstant.PORT_URL + NetWorkMethod.custLookAdd;
                OkHttpClientManager.postJsonAsyn(URL, paramCustlookList, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Loading.dismissLoading();
                    }

                    @Override
                    public void onResponse(String response) {
                        //隐藏Loading
                        Loading.dismissLoading();
                        MyUtils.LogI("------", response.toString());


                        //返回客户信息页面
                        setResult(MyConstant.accompanyCode);
                        finish();
                    }
                });
                break;
        }
    }

    /*************************从添加一手 & 添加二手  返回**************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MyConstant.RESULT_ADDFIRST && resultCode == MyConstant.RESULT_ADDFIRST){
            //添加一手房源


        }
    }
}
