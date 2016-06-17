package com.vocinno.centanet.customermanage;

import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.entity.ParamCustlookList;
import com.vocinno.centanet.entity.TCmLook;
import com.vocinno.centanet.entity.TCmLookAccompany;
import com.vocinno.centanet.entity.TCmLookHouse;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hewei26 on 2016/6/16.
 */
public class AddAccompanyActivity extends OtherBaseActivity {

    @Bind(R.id.iv_type_first)        //选择一手房源
            ImageView mIvTypeFirst;
    @Bind(R.id.iv_type_second)       //选择二手房源
            ImageView mIvTypeSecond;
    @Bind(R.id.et_confirmNum)        //带看确认书编号
            EditText mEtConfirmNum;
    @Bind(R.id.et_startTime)         //开始时间
            EditText mEtStartTime;
    @Bind(R.id.iv_startTime)         //选择开始时间
            ImageView mIvStartTime;
    @Bind(R.id.et_endTime)           //结束时间
            EditText mEtEndTime;
    @Bind(R.id.iv_endTime)           //选择结束时间
            ImageView mIvEndTime;
    @Bind(R.id.cb_write_back)         //是否回显
            CheckBox mCbWriteBack;
    @Bind(R.id.et_desc_house)         //文字描述
            EditText mEtDescHouse;
    @Bind(R.id.ll_addHouse)           //添加房源
            LinearLayout mLlAddHouse;

    private ImageView mBack;
    private TextView mSubmit;

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_add_accompany;
    }

    @Override
    public void initView() {
        //设置标题栏
        mBack = (ImageView)MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mSubmit = (TextView)MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.add_potential_customer, null);



        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
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
            case R.id.img_left_mhead1:
                finish();
                break;
            case R.id.tv_right_mhead1:
                //显示Loading
                Loading.show(this);

                String custCode = "";       //TODO:客户编码
                String confirmationNumber = mEtConfirmNum.getText().toString(); //带看确认书编号
                String startTime = mEtStartTime.getText().toString();   //开始时间
                String endTime = mEtEndTime.getText().toString();       //结束时间
                boolean isChecked = mCbWriteBack.isChecked();
                String custlookTrackType = isChecked ? "1" : "0";    //是否回写
                String lookType = "";  //带看类型
                String remark = mEtDescHouse.getText().toString();       //文字描述

                ParamCustlookList paramCustlookList = new ParamCustlookList();

                TCmLook tCmLook = new TCmLook();
                tCmLook.setRemark("这是文字描述");
                tCmLook.setStartTime("2011-09-09 12:22");
                tCmLook.setEndTime("2016-09-09 12:22");
                tCmLook.setLookType("20074002");
                tCmLook.setCustCode("12345");
                tCmLook.setConfirmationNumber("123123123");

                List<TCmLookHouse> tCmLookHouses = new ArrayList<>();
                TCmLookHouse tCmLookHouse = new TCmLookHouse();
                tCmLookHouse.setHousedelCode("1111");
                tCmLookHouse.setFilesId("11");
                tCmLookHouse.setHouseId(1000000L);
                tCmLookHouse.setFeedback("aaa");
                tCmLookHouses.add(tCmLookHouse);
                tCmLookHouses.add(tCmLookHouse);

                List<TCmLookAccompany> tCmLookAccompanies = new ArrayList<>();
                TCmLookAccompany tCmLookAccompany = new TCmLookAccompany();
                tCmLookAccompany.setAccompanyPromise("0");
                tCmLookAccompany.setAccompanyName("2222");
                tCmLookAccompany.setAccompanyRole("22222");
                tCmLookAccompany.setAccompanyUser("222222");
                tCmLookAccompany.setAccompanyGroup("222222");
                tCmLookAccompanies.add(tCmLookAccompany);
                tCmLookAccompanies.add(tCmLookAccompany);

                paramCustlookList.settCmLook(tCmLook);
                paramCustlookList.settCmLookHouseList(tCmLookHouses);
                paramCustlookList.settCmLookAccompanyList(tCmLookAccompanies);
                paramCustlookList.setCustlookTrackType("0");

                URL= NetWorkConstant.PORT_URL+ NetWorkMethod.custLookAdd;
                OkHttpClientManager.postJsonAsyn(URL, paramCustlookList, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Loading.dismissLoading();
                    }

                    @Override
                    public void onResponse(String response) {
                        Loading.dismissLoading();
                        MyUtils.LogI("------",response.toString());
                    }
                });
                break;
        }
    }

}
