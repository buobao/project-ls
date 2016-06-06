package com.vocinno.centanet.customermanage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrabCustomerDetailActivity extends OtherBaseActivity {
    private String mCusterCode = null;
    private View mBackView, mImgViewAddTrack, mSubmit;
    private RelativeLayout mGrabCustomer;
    private TextView mTvCustomerCode, mTvCustomerName, mTvType, tv_fangxing_grab, mTvAcreage, tv_quyu_customerDetailActivity,
            mTvPrice, mTvMoney/*, mTvPaymenttype*/;
    private ListViewNeedResetHeight mLvTracks;
    //	private ImageView  mImgViewQQ, mImgWeixin;
    private CustomerDetail mDetail = null;
    private RelativeLayout mImgViewPhone;
    private TextView tv_area_gkdetail, tv_xuqiuzishu_grab;
    private Drawable drawable;
    private static final int RESET_LISTVIEW_TRACK = 1001;
    private boolean isGongKe , robRefresh = true, returnRefresh = true, firstGetContent = true;//防止重复加载数据
    private ImageView iv_add_demand_gongke;

    public GrabCustomerDetailActivity() {
    }

    @Override
    public Handler setHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RESET_LISTVIEW_TRACK:
                        MethodsExtra.resetListHeightBasedOnChildren(mLvTracks);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_grab_customer_detail;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.customernews,
                null);
        mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mGrabCustomer = (RelativeLayout) findViewById(R.id.rlyt_seize_customerDetailActivity);
        mTvCustomerCode = (TextView) findViewById(R.id.tv_customercode_customerDetailActivity);
        mTvCustomerName = (TextView) findViewById(R.id.tv_customername_customerDetailActivity);
        mTvType = (TextView) findViewById(R.id.tv_type_customerDetailActivity);
        tv_fangxing_grab = (TextView) findViewById(R.id.tv_fangxing_grab);
        tv_quyu_customerDetailActivity = (TextView) findViewById(R.id.tv_quyu_customerDetailActivity);
        mTvAcreage = (TextView) findViewById(R.id.tv_acreage_customerDetailActivity);
        mTvPrice = (TextView) findViewById(R.id.tv_price_customerDetailActivity);
        mTvMoney = (TextView) findViewById(R.id.tv_money_customerDetailActivity);
//		mTvPaymenttype = (TextView) findViewById(R.id.tv_paymenttype_customerDetailActivity);
        mLvTracks = (ListViewNeedResetHeight) findViewById(R.id.lv_track_customerDetailActivity);
        mImgViewAddTrack = findViewById(R.id.imgView_addTrack_customerDetailActivity);
        mImgViewPhone = (RelativeLayout) findViewById(R.id.imgView_phone_customerDetailActivity);
        tv_area_gkdetail = (TextView) findViewById(R.id.tv_area_gkdetail);
        tv_xuqiuzishu_grab = (TextView) findViewById(R.id.tv_xuqiuzishu_grab);
        iv_add_demand_gongke = (ImageView) findViewById(R.id.iv_add_demand_gongke);
        iv_add_demand_gongke.setOnClickListener(this);
//		mImgViewQQ = (ImageView) findViewById(R.id.imgView_qq_customerDetailActivity);
//		mImgWeixin = (ImageView) findViewById(R.id.imgView_wx_customerDetailActivity);
        mBackView.setOnClickListener(this);
        mGrabCustomer.setOnClickListener(this);
        mImgViewAddTrack.setOnClickListener(this);
//		mImgViewQQ.setOnClickListener(this);
        mImgViewPhone.setOnClickListener(this);
    }


    @Override
    public void initData() {
        mCusterCode = getIntent().getStringExtra(MyConstant.custCode);
        isGongKe = getIntent().getBooleanExtra(MyConstant.isGongKe,false);
        getData();
        if (isGongKe) {
            mGrabCustomer.setVisibility(View.VISIBLE);
            mSubmit = MethodsExtra.findHeadRightView1(mContext, baseView, 0, R.drawable.phone_img);
            mSubmit.setOnClickListener(this);
            mImgViewPhone.setVisibility(View.GONE);
        } else {
            mImgViewPhone.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        Loading.showForExit(this);
        URL = NetWorkConstant.PORT_URL + NetWorkMethod.custInfo;
        Map<String, String> map = new HashMap<String, String>();
        map.put(NetWorkMethod.custCode, mCusterCode);
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Loading.dismissLoading();
            }

            @Override
            public void onResponse(String response) {
                Loading.dismissLoading();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, CustomerDetail.class);
                if (jsReturn.isSuccess()) {
                    getGrabInfo(jsReturn);
                } else {
                    MethodsExtra.toast(mContext, jsReturn.getMsg());
                }
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_right_mhead1:
                if (mDetail.getPhone() == null || mDetail.getPhone().length() <= 0) {
                    MethodsExtra.toast(mContext, "暂无联系人号码");
                } else {
                    List<CustomerDetail.Content> list = new ArrayList<CustomerDetail.Content>();
                    CustomerDetail.Content content = new CustomerDetail().new Content();
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
                MethodsJni.removeNotificationObserver(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
                MethodsDeliverData.string = mCusterCode;
                // listTracks
                Intent intent = new Intent(mContext,
                        AddFollowInCustomerActivity.class);
                intent.putExtra("custCode", mCusterCode);
//				MethodsExtra.startActivityForResult(mContext,10,intent);
                startActivityForResult(intent, 101);
                break;
            case R.id.imgView_phone_customerDetailActivity:
                firstGetContent = true;
                getCustContactList();
                break;
            case R.id.rlyt_seize_customerDetailActivity:
                mGrabCustomer.setClickable(false);
                // 抢
                robRefresh = true;
//                MethodsJni.callProxyFun(hif, CST_JS.JS_ProxyName_CustomerList, CST_JS.JS_Function_CustomerList_claimCustomer, CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));
                claimCust();
                break;
			/*//钥匙管理
			case R.id.rlyt_key_house_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, KeyManageActivity.class);
				break;
			//我的客源
			case R.id.rlyt_my_customer_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsDeliverData.keYuanOrGongKe=1;
				MethodsDeliverData.isMyCustomer = true;
				MethodsExtra.startActivity(mContext,
						CustomerManageActivity.class);
				break;
			//抢公售
			case R.id.rlyt_grab_house_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
				startIntentToGongFangManager(0);
				break;
			//抢公租
			case R.id.rlyt_grab_house_main_page_slid_menus2:
				MyUtils.removeActivityFromList();
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANGZU;
				startIntentToGongFangManager(1);
				break;
			//抢公客
			case R.id.rlyt_grab_customer_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsDeliverData.keYuanOrGongKe=0;
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.isMyCustomer = false;
				MethodsExtra.startActivity(mContext,
						CustomerManageActivity.class);
				break;
			//pin码
			case R.id.rlyt_password_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, KeyGetInActivity.class);
				break;
			//扫一扫
			case R.id.rlyt_sacn_customer_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, CaptureActivity.class);
				break;
			//我的提醒
			case R.id.rlyt_remind_customer_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, MessageListActivity.class);
				break;*/
            case R.id.iv_add_demand_gongke:
                intent = new Intent(this, AddDemandActivity.class);
                intent.putExtra("custCode", mCusterCode);
                startActivityForResult(intent, 101);
                break;
            default:
                break;
        }
    }

    private void claimCust() {
        Loading.showForExit(this);
        URL = NetWorkConstant.PORT_URL + NetWorkMethod.claimCust;
        Map<String, String> map = new HashMap<String, String>();
        map.put(NetWorkMethod.custCode, mCusterCode);
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                mGrabCustomer.setClickable(true);
                Loading.dismissLoading();
            }
            @Override
            public void onResponse(String response) {
                mGrabCustomer.setClickable(true);
                Loading.dismissLoading();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, CustomerDetail.class);
                if (jsReturn.isSuccess()) {
                    MyToast.showToast(jsReturn.getMsg());
                    setResult(MyConstant.REFRESH);
                    finish();
                } else {
                    MyToast.showToast(jsReturn.getMsg());
                }
            }
        });
    }

    private void getCustContactList() {
        Loading.showForExit(this);
        URL = NetWorkConstant.PORT_URL + NetWorkMethod.custContactList;
        Map<String, String> map = new HashMap<String, String>();
        map.put(NetWorkMethod.custCode, mCusterCode);
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Loading.dismissLoading();
            }
            @Override
            public void onResponse(String response) {
                Loading.dismissLoading();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, CustomerDetail.class);
                if (jsReturn.isSuccess()) {
                    CustomerDetail contentList = getContent(response);
                    if (contentList != null) {
                        List<CustomerDetail.Content> list = contentList.getContent();
                        showCallCosturmerDialog(list);
                    } else {
                        MethodsExtra.toast(mContext, jsReturn.getMsg());
                    }
                } else {
                    MethodsExtra.toast(mContext, jsReturn.getMsg());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
		/*if (data == null) {
			return;
		}*/
        if (resultCode == MyConstant.REFRESH) {
            URL = NetWorkConstant.PORT_URL + NetWorkMethod.custInfo;
            Map<String, String> map = new HashMap<String, String>();
            map.put(NetWorkMethod.custCode, mCusterCode);
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
                    if (jsReturn.isSuccess()) {
                        getGrabInfo(jsReturn);
                    } else {
                        MethodsExtra.toast(mContext, jsReturn.getMsg());
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

    private CustomerDetail getContent(String strJson) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strJson);
            CustomerDetail customerDetail = (CustomerDetail) new Gson().fromJson(jsonObject.toString(), CustomerDetail.class);
//			customerDetail.getContent();
            return customerDetail;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private CustomerDetailAdapter adapter;
    private List<Track> listTracks;

    @Override
    public void netWorkResult(String name, String className, Object data) {
    }

    private void getGrabInfo(JSReturn jsReturn) {
        mDetail = (CustomerDetail) jsReturn.getObject();
        mTvCustomerCode.setText("编号：" + mDetail.getCustCode());
        mTvCustomerName.setText("姓名：" + mDetail.getName());
//		mTvPaymenttype.setText("付款方式：" + mDetail.getPaymentType());
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
            mTvType.setText("类型：" + req.getReqType());// 类型
            tv_fangxing_grab.setText("房型：" + req.getFromToRoom());// 类型
            tv_quyu_customerDetailActivity.setText("城区：" + req.getDistrictCode());
            String customArea = req.getArea();
            if ("0".equals(customArea)) {
                customArea = "不限";
            }
            mTvAcreage.setText("片区：" + customArea);
            mTvPrice.setText("价格：" + req.getPrice());// 价格
            tv_area_gkdetail.setText("面积：" + req.getAcreage());
            tv_xuqiuzishu_grab.setText(req.getSelfDescription());
        }
        // 联系方式
        if (mDetail == null || TextUtils.isEmpty(mDetail.getPhone()) || mDetail.getPhone().equals("null")) {
//					mImgViewPhone.setImageResource(R.drawable.c_manage_icon_contact01);
        }
        if (mDetail == null || TextUtils.isEmpty(mDetail.getQq()) || mDetail.getQq().equals("null")) {
//					mImgViewQQ.setImageResource(R.drawable.c_manage_icon_qq01);
        }
        if (mDetail == null || TextUtils.isEmpty(mDetail.getWechat()) || mDetail.getWechat().equals("null")) {
//					mImgWeixin.setImageResource(R.drawable.c_manage_icon_wechat01);
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
