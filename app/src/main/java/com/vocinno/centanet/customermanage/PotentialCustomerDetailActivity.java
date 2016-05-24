package com.vocinno.centanet.customermanage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.selfdefineview.ListViewNeedResetHeight;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.adapter.ContentAdapter;
import com.vocinno.centanet.customermanage.adapter.CustomerDetailAdapter;
import com.vocinno.centanet.model.CustomerDetail;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.Requets;
import com.vocinno.centanet.model.Track;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotentialCustomerDetailActivity extends OtherBaseActivity {
    private String mCusterCode = null;
    private View mBackView, mImgViewAddTrack,mSubmit;
    private RelativeLayout mGrabCustomer;
    private TextView mTvCustomerCode, mTvCustomerName, mTvType, mTvAcreage,
            mTvPrice, tv_fangxing_potential,tv_area_potential, tv_xuqiu_qianke;
    private ListViewNeedResetHeight mLvTracks;
    private CustomerDetail mDetail = null;
    private RelativeLayout mImgViewPhone;
    private Drawable drawable;
    private static final int RESET_LISTVIEW_TRACK = 1001;
    private ImageView iv_add_demand_potential;
    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_potential_customer_detail;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.customernews,
                null);
        mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mGrabCustomer = (RelativeLayout) findViewById(R.id.rlyt_seize_qianke);
        mTvCustomerCode = (TextView) findViewById(R.id.tv_customercode_qianke);
        mTvCustomerName = (TextView) findViewById(R.id.tv_customername_qianke);
        mTvType = (TextView) findViewById(R.id.tv_type_qianke);
        mTvAcreage = (TextView) findViewById(R.id.tv_acreage_qianke);
        mTvPrice = (TextView) findViewById(R.id.tv_price_qianke);
        tv_fangxing_potential = (TextView) findViewById(R.id.tv_fangxing_potential);
        tv_area_potential = (TextView) findViewById(R.id.tv_area_potential);
        tv_xuqiu_qianke = (TextView) findViewById(R.id.tv_xuqiu_qianke);
        mLvTracks = (ListViewNeedResetHeight) findViewById(R.id.lv_track_qianke);
        mImgViewAddTrack = findViewById(R.id.imgView_addTrack_qianke);
        mImgViewPhone = (RelativeLayout) findViewById(R.id.imgView_phone_qianke);
        iv_add_demand_potential = (ImageView) findViewById(R.id.iv_add_demand_potential);
        iv_add_demand_potential.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mGrabCustomer.setOnClickListener(this);
        mImgViewAddTrack.setOnClickListener(this);
        mImgViewPhone.setOnClickListener(this);
    }

    @Override
    public void initData() {
        intent=new Intent();
        TAG = this.getClass().getName();
        // 添加通知
        MethodsJni.addNotificationObserver(
                CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
        MethodsJni.addNotificationObserver(
                CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT, TAG);
        MethodsJni.addNotificationObserver(
                CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_CONTACT_RESULT, TAG);
        mCusterCode=getIntent().getStringExtra("custCode");
        showDialog();
        // 调用数据
        MethodsJni.callProxyFun(hif, CST_JS.JS_ProxyName_CustomerList,
                CST_JS.JS_Function_CustomerList_getCustomerInfo,
                CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));
        if (MethodsDeliverData.flag1 == 1) {
            mGrabCustomer.setVisibility(View.VISIBLE);
            mSubmit = MethodsExtra.findHeadRightView1(mContext, baseView, 0,R.drawable.phone_img);
            mSubmit.setOnClickListener(this);
            mImgViewPhone.setVisibility(View.GONE);
        }else{
            mImgViewPhone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_add_demand_potential:
                intent=new Intent(this, AddDemandActivity.class);
                intent.putExtra("custCode", mCusterCode);
                startActivityForResult(intent,101);
                break;
            case R.id.img_right_mhead1:
                if(mDetail.getPhone()==null||mDetail.getPhone().length()<=0){
                    MethodsExtra.toast(mContext,"暂无联系人号码");
                }else{
                    List<CustomerDetail.Content> list=new ArrayList<CustomerDetail.Content>();
                    CustomerDetail.Content content=new CustomerDetail().new Content();
                    content.setName(mDetail.getName());
                    content.setPhone(mDetail.getPhone());
                    list.add(content);
                    showCallCosturmerDialog(list);
                }
                break;
            case R.id.img_left_mhead1:
                finish();
                break;
            case R.id.imgView_addTrack_customerDetailActivity:
                MethodsDeliverData.string = mCusterCode;
                intent.setClass(mContext,
                        AddFollowInCustomerActivity.class);
                MethodsExtra.startActivityForResult(mContext,10,intent);
                break;
            case R.id.imgView_phone_customerDetailActivity:
                showDialog();
                // 调用联系人列表数据
                MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_CustomerList,
                        CST_JS.JS_Function_CustomerList_CustContactList,
                        CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));
                break;
            case R.id.rlyt_seize_customerDetailActivity:
                mGrabCustomer.setClickable(false);
                showDialog();
                // 抢
                MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_CustomerList,CST_JS.JS_Function_CustomerList_claimCustomer,CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));
                mGrabCustomer.setClickable(true);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MyConstant.REFRESH) {
            URL= NetWorkConstant.PORT_URL+ NetWorkMethod.custInfo;
            Map<String,String> map=new HashMap<String,String>();
            map.put(NetWorkMethod.custCode,mCusterCode);
            showDialog();
            OkHttpClientManager.postAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    dismissDialog();
                }

                @Override
                public void onResponse(String response) {
                    dismissDialog();
                    JSReturn jsReturn = MethodsJson.jsonToJsReturn(response,
                            CustomerDetail.class);
                    if(jsReturn.isSuccess()){
                        getPotentialInfo(jsReturn);
                    }else{
                        MethodsExtra.toast(mContext,jsReturn.getMsg());
                    }
                }
            });

        }
    }
    private Dialog mCallCustormerDialog;
    private void showCallCosturmerDialog(List<CustomerDetail.Content> list) {
        mCallCustormerDialog = new Dialog(mContext, R.style.Theme_dialog);
        mCallCustormerDialog
                .setContentView(R.layout.dialog_call_custormer_house_resouse_detial);
        Window win = mCallCustormerDialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        mCallCustormerDialog.setCanceledOnTouchOutside(true);

        win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView mListViewCustormer = (ListView) mCallCustormerDialog
                .findViewById(R.id.lv_custormerPhone_HouseDetailActivity);

        ContentAdapter phoneAdapter = new ContentAdapter(
                mContext, list);
        mListViewCustormer.setAdapter(phoneAdapter);
        mListViewCustormer
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        TextView tvTel = (TextView) arg1
                                .findViewById(R.id.tv_custNothing_CustormerPhoneAdapter);

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:" + tvTel.getText().toString()));
                        mContext.startActivity(intent);
                        mCallCustormerDialog.dismiss();
                    }
                });
        mCallCustormerDialog.show();
    }
    public void notifCallBack(String name, String className, Object data) {

    }

    private CustomerDetail getContent(String strJson) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strJson);
            CustomerDetail customerDetail=(CustomerDetail)new Gson().fromJson(jsonObject.toString(),CustomerDetail.class);
//			customerDetail.getContent();
            return customerDetail;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT, TAG);
    }

    private CustomerDetailAdapter adapter;
    private List<Track> listTracks;

    public TextView getmTvCustomerCode() {
        return mTvCustomerCode;
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {
        dismissDialog();
        String strJson = (String) data;
        JSReturn jsReturn = MethodsJson.jsonToJsReturn(strJson,
                CustomerDetail.class);
        if(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_CONTACT_RESULT.equals(name)){
                CustomerDetail contentList=getContent(strJson);
                if(contentList!=null){
                    List<CustomerDetail.Content> list=contentList.getContent();
                    showCallCosturmerDialog(list);
                }else{
                    MethodsExtra.toast(mContext,jsReturn.getMsg());
                }
        }else if(name.equals(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT)){
            if(jsReturn.isSuccess()){
                getPotentialInfo(jsReturn);
            }else{
                MethodsExtra.toast(mContext,jsReturn.getMsg());
            }
        }else if (name.equals(CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT)) {
            if (jsReturn.isSuccess()) {
                MethodsExtra.toast(mContext, jsReturn.getMsg());
                setResult(MyConstant.REFRESH);
                finish();
            } else {
                if (!jsReturn.isSuccess() || jsReturn.getObject() == null) {
                    MethodsExtra.toast(mContext,jsReturn.getMsg());
                }
            }
        }
    }

    private void getPotentialInfo(JSReturn jsReturn) {
        mDetail = (CustomerDetail) jsReturn.getObject();
        mTvCustomerCode.setText("编号：" + mDetail.getCustCode());
        mTvCustomerName.setText("姓名：" + mDetail.getName());
        // 填充跟踪信息列表
        listTracks = mDetail.getTracks();
        if (listTracks != null && listTracks.size() >= 1) {
            adapter = new CustomerDetailAdapter(mContext, listTracks);
            mLvTracks.setAdapter(adapter);
            mHander.sendEmptyMessageDelayed(RESET_LISTVIEW_TRACK, 50);
        }
        // 填充需求信息
        List<Requets> listReqs = mDetail.getRequets();
        if (listReqs != null && listReqs.size() >= 1) {
            Requets req = listReqs.get(0);
            mTvType.setText(req.getReqType());// 类型
            tv_fangxing_potential.setText( req.getFromToRoom());// 类型
            mTvAcreage.setText(req.getArea());
            mTvPrice.setText( req.getPrice());// 价格
            tv_area_potential.setText( req.getAcreage());
            tv_xuqiu_qianke.setText( req.getSelfDescription());
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
