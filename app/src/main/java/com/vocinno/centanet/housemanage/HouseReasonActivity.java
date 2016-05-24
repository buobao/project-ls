package com.vocinno.centanet.housemanage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.umeng.socialize.utils.Log;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.HouseDetail;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HouseReasonActivity extends OtherBaseActivity {
    private View mBackView, mTitleView;
    private ImageView mSubmit;
    private TextView mTvDate;
    private EditText et_content;
    private String mCustorCode = null;
    private String content;
    private Intent intent;
    private RadioButton rb_reason1,rb_reason2,rb_reason3,rb_reason4;
    @Override
    public int setContentLayoutId() {
        return R.layout.activity_house_reason;
    }

    @Override
    public void initView() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        intent=getIntent();
        mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mSubmit = (ImageView) MethodsExtra.findHeadRightView1(mContext, baseView, 0, R.drawable.universal_button_undone);
        mSubmit.setClickable(false);
        mTitleView = MethodsExtra
                .findHeadTitle1(mContext, baseView, 0, getResources().getString(R.string.lookhousereason));
        mTvDate = (TextView) findViewById(R.id.tv_date_houseReasonActivity);
        et_content = (EditText) findViewById(R.id.et_content_houseReasonActivity);
        rb_reason1 = (RadioButton) findViewById(R.id.rb_reason1);
        rb_reason2 = (RadioButton) findViewById(R.id.rb_reason2);
        rb_reason3 = (RadioButton) findViewById(R.id.rb_reason3);
        rb_reason4 = (RadioButton) findViewById(R.id.rb_reason4);
        rb_reason1.setChecked(true);

        setListener();
    }

    public void setListener() {
        mBackView.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = et_content.getText().toString();
                if (content.trim().length() >= 10) {
                    mSubmit = (ImageView) MethodsExtra.findHeadRightView1(mContext, baseView, 0, R.drawable.universal_button_done);
                    mSubmit.setClickable(true);
                }
            }
        });
    }

    @Override
    public void initData() {
        methodsJni=new MethodsJni();
        methodsJni.setMethodsJni((HttpInterface)this);
       mCustorCode = MethodsDeliverData.string;
        MethodsJni.addNotificationObserver(
                CST_JS.NOTIFY_NATIVE_DOROOMVIEW_RESULT, TAG);
        mTvDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()));
    }

    ModelDialog modelDialog;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_left_mhead1:
                finish();
                break;
            case R.id.img_right_mhead1:
                if(modelDialog==null){
                    modelDialog=new ModelDialog(this,R.layout.loading,R.style.Theme_dialog);
                }
                modelDialog.show();
                String delCode = intent.getStringExtra("delCode");
                String houseId = intent.getStringExtra("houseId");
                String checkContent;
                if(rb_reason1.isChecked()){
                    checkContent="10080001";
                }else if(rb_reason2.isChecked()){
                    checkContent="10080002";
                }else if(rb_reason3.isChecked()){
                    checkContent="10080003";
                }else{
                    checkContent="10080004";
                }
                MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
                        CST_JS.JS_Function_HouseResource_getShiHao, CST_JS
                                .getJsonStringForLookShiHao(content,delCode, houseId, checkContent));
                break;
            default:
                break;
        }
    }

    @Override
    public Handler setHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
            }
        };
    }


    public void notifCallBack(String name, String className, Object data) {
        
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {
        modelDialog.dismiss();
        Log.i("data==",data+"=="+className+"==");
        //看房理由
        if (name.equals(CST_JS.NOTIFY_NATIVE_DOROOMVIEW_RESULT)) {
            String strJson = (String) data;
            JSReturn jReturnHouseDetail = MethodsJson.jsonToJsReturn(strJson,
                    HouseDetail.class);
            HouseDetail mHouseDetail = (HouseDetail) jReturnHouseDetail.getObject();
            if(jReturnHouseDetail.isSuccess()){
                intent.putExtra("roomNo", mHouseDetail.getRoomNO());
                intent.putExtra("buiding", mHouseDetail.getBuiding());
                setResult(101, intent);
                finish();
            }else{
                MethodsExtra.toast(mContext, jReturnHouseDetail.getMsg());
            }
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
