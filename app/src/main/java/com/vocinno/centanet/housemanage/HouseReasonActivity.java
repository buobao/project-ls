package com.vocinno.centanet.housemanage;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.HouseDetail;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HouseReasonActivity extends OtherBaseActivity {
    private View mBackView, mTitleView;
    private TextView mSubmit;
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
        mSubmit = (TextView) MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
        mSubmit.setClickable(false);
        mSubmit.setEnabled(false);
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
                    mSubmit.setClickable(true);
                    mSubmit.setEnabled(true);
                } else {
                    mSubmit.setClickable(false);
                    mSubmit.setEnabled(false);
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
            case R.id.tv_right_mhead1:
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
                saveReason(content,delCode, houseId, checkContent);
                /*MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
                        CST_JS.JS_Function_HouseResource_getShiHao, CST_JS
                                .getJsonStringForLookShiHao(content,delCode, houseId, checkContent));*/
                break;
            default:
                break;
        }
    }

    private void saveReason(String content, String delCode, String houseId, String checkContent) {
        Loading.show(this);
        URL= NetWorkConstant.PORT_URL+ NetWorkMethod.doRoomview;
        Map<String, String> map=new HashMap<String,String>();
        map.put(NetWorkMethod.reason, content);
        map.put(NetWorkMethod.delCode, delCode);
        map.put(NetWorkMethod.houseId, houseId);
        map.put(NetWorkMethod.type, checkContent);
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Loading.dismissLoading();
            }
            @Override
            public void onResponse(String response) {
                Loading.dismissLoading();
                JSReturn jReturnHouseDetail = MethodsJson.jsonToJsReturn(response,HouseDetail.class);
                HouseDetail mHouseDetail = (HouseDetail) jReturnHouseDetail.getObject();
                if(jReturnHouseDetail.isSuccess()){
                    intent.putExtra(MyConstant.roomNo, mHouseDetail.getRoomNO());
                    intent.putExtra(MyConstant.buiding, mHouseDetail.getBuiding());
                    setResult(101, intent);
                    finish();
                }else{
                    MyToast.showToast(jReturnHouseDetail.getMsg());
                }
            }
        });
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
}
