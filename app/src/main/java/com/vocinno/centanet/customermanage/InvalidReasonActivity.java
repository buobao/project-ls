package com.vocinno.centanet.customermanage;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.JSContent;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView.IXListViewListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 客源管理
 *
 * @author Administrator
 */
public class InvalidReasonActivity extends OtherBaseActivity implements
        IXListViewListener {
    private View mBack, mSubmit;
    private EditText et_reason_content;
    private String pkid;
    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_invalid_reason;
    }

    @Override
    public void initView() {
        intent = getIntent();
        MethodsExtra.findHeadTitle1(mContext, baseView,
                R.string.lnvalid_reason, null);
        mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mBack.setOnClickListener(this);

        mSubmit = MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
        mSubmit.setOnClickListener(this);
        mSubmit.setEnabled(false);
        mSubmit.setClickable(false);

        et_reason_content= (EditText) findViewById(R.id.et_reason_content);
        et_reason_content.addTextChangedListener(getWatcher());
    }

    @Override
    public void initData() {
        intent=getIntent();
        pkid= intent.getStringExtra(MyConstant.pkid);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_right_mhead1:
                saveCustInvalid();
                break;
            case R.id.img_left_mhead1:
                finish();
                break;
        }
    }

    private void saveCustInvalid() {
        Loading.show(this);
        String URL= NetWorkConstant.PORT_URL+ NetWorkMethod.importCustInvalid;
        Map<String,String> map=new HashMap<String,String>();
        map.put(NetWorkMethod.pkid,pkid);
        map.put(NetWorkMethod.invalidReason,et_reason_content.getText()+"");
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                stopRefreshOrLoadMore();
            }
            @Override
            public void onResponse(String response) {
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response,JSContent.class);
                if (jsReturn.isSuccess()) {
                    JSContent content = (JSContent) jsReturn.getObject();
                    if (content.isSuccess()) {
                        setResult(MyConstant.REFRESH);
                        finish();
                    }
                    MyToast.showToast(content.getMsg());
                } else {
                    stopRefreshOrLoadMore();
                    MyToast.showToast(jsReturn.getMsg());
                }
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void netWorkResult(String name, String className, Object data) {
    }
    @NonNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null&&s.toString().trim().length()>8){
                    mSubmit.setEnabled(true);
                    mSubmit.setClickable(true);
                }else{
                    mSubmit.setEnabled(false);
                    mSubmit.setClickable(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
