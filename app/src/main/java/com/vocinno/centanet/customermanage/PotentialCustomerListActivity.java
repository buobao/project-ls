package com.vocinno.centanet.customermanage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.adapter.PotentialCustormerListAdapter;
import com.vocinno.centanet.housemanage.adapter.SearchAdapter;
import com.vocinno.centanet.model.CustomerItem;
import com.vocinno.centanet.model.CustomerList;
import com.vocinno.centanet.model.EstateSearchItem;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.NoDoubleClickListener;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotentialCustomerListActivity extends OtherBaseActivity implements XListView.IXListViewListener {

    private Dialog mMenuDialog,mSearchDialog;
    private View mBack, mSubmit;
    private PotentialCustormerListAdapter mListAdapter;
    private int mPageIndex = 1;
    private List<CustomerItem> mListCustomers = new ArrayList<CustomerItem>();
    private Intent intent;
    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_potential_customer;
    }

    @Override
    public void initView() {
        MethodsExtra.findHeadTitle1(mContext, baseView,
                R.string.my_potential_customer, null);
        mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		/*mSubmit = MethodsExtra.findHeadRightView1(mContext, baseView, 0,
				R.drawable.universal_button_add);*/
        mSubmit = MethodsExtra.findHeadRightView1(mContext, baseView, 0, 0);
        mListView = (XListView) findViewById(R.id.xlv_potential_custormer);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mListView.setXListViewListener(this);
    }

    @Override
    public void initData() {
        intent=getIntent();
        mListAdapter = new PotentialCustormerListAdapter((PotentialCustomerListActivity) mContext);
        mListAdapter.setListDatas(null);
        mListView.setAdapter(mListAdapter);
        getCustomerData(null,1, false, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case MyConstant.REFRESH:
                page=2;
                getCustomerData(null,1,false, true);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_search_customer:   //关键词搜索
//			MethodsExtra.startActivity(mContext, AddCustomerActivity.class);
                showSearchDialog();
                mMenuDialog.dismiss();
                break;
            case R.id.ll_add_customer:     //跳转到"添加潜客"
                intent=new Intent();
                intent.setClass(mContext, AddPotentialActivity.class);
                startActivityForResult(intent,10);
                mMenuDialog.dismiss();
                break;
            case R.id.img_left_mhead1:     //标题左侧箭头
                finish();
                break;
            case R.id.img_right_mhead1:    //标题右侧
                showMenuDialog();
                break;
			/*//钥匙管理
			case R.id.rlyt_key_house_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, KeyManageActivity.class);
				break;
			//我的客源
			case R.id.rlyt_my_customer_main_page_slid_menus:
				if(MethodsDeliverData.isMyCustomer){
					drawer_layout.closeDrawer(leftMenuView);
				}else{
					MyUtils.removeActivityFromList();
					MethodsDeliverData.keYuanOrGongKe=1;
					MethodsDeliverData.isMyCustomer = true;
					MethodsExtra.startActivity(mContext,
							CustomerManageActivity.class);
				}
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
				if(MethodsDeliverData.isMyCustomer){
					MyUtils.removeActivityFromList();
					MethodsDeliverData.keYuanOrGongKe=1;
					MethodsDeliverData.isMyCustomer = true;
					MethodsExtra.startActivity(mContext,
							CustomerManageActivity.class);
				}else{
					drawer_layout.closeDrawer(leftMenuView);
				}
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
            case R.id.btn_close_dialogSearchHouseManage:
                mEtSearch.setText("");
                mSearch.setList(null);
                mSearch.notifyDataSetChanged();
                setListHeight(mSearch, listView);
                break;
            default:
                break;
        }
    }
        /*String  strReq = CST_JS.getJsonStringForCustomerList(CST_JS.JS_CustomerList_Type_Mypotien, page, 8);
        MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_CustomerList,
                CST_JS.JS_Function_CustomerList_getList, strReq);*/
    private void getCustomerData() {
        getCustomerData(null,1,true, true);
    }
    private void getLoadCustomerData() {
        getCustomerData(null, page, true, false);
    }
    private void getCustomerData(String customCode,int pageNo,boolean isReFreshOrLoadMore,final boolean isRefresh) {
        if(!isReFreshOrLoadMore){
           Loading.showForExit(this,true);
        }
        URL= NetWorkConstant.PORT_URL+ NetWorkMethod.custlist;
        Map<String,String> map=new HashMap<String,String>();
        if(null!=customCode){
            map.put(NetWorkMethod.custCode,customCode);
        }
        map.put(NetWorkMethod.page, pageNo + "");
        map.put(NetWorkMethod.pageSize, MyConstant.pageSize+"");
        map.put(NetWorkMethod.type, NetWorkMethod.mypotien);
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                stopRefreshOrLoadMore();
            }
            @Override
            public void onResponse(String response) {
                stopRefreshOrLoadMore();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, CustomerList.class);
                if (jsReturn.isSuccess()) {
                    if (jsReturn.getListDatas()!=null&&jsReturn.getListDatas().size() < MyConstant.pageSize) {
                        mListView.setPullLoadEnable(false);
                    } else {
                        mListView.setPullLoadEnable(true);
                    }
                    if (isRefresh) {
                        mListAdapter.setListDatas(jsReturn.getListDatas());
                    } else {
                        page++;
                        mListAdapter.addListDatas(jsReturn.getListDatas());
                    }
                } else {
                    MyToast.showToast(jsReturn.getMsg());
                }
            }
        });
    }
    public void notifCallBack(String name, String className, Object data) {
    }
    @Override
    public void onRefresh() {
        page = 2;
        getCustomerData();
    }

    private boolean isLoading = false;
    @Override
    public void onLoadMore() {
        getLoadCustomerData();
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {

    }
    private void setListHeight(SearchAdapter mSearch,ListView mListView) {
        int totalHeight = 0;
        for (int i = 0, len = mSearch.getCount(); i < len; i++) {
            View listItem = mSearch.getView(i, null, mListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight + (mListView.getDividerHeight() * (mSearch.getCount() - 1));
        mListView.setLayoutParams(params);
    }
    private void showMenuDialog() {
        mMenuDialog = new Dialog(mContext, R.style.Theme_dialog);
        mMenuDialog.setContentView(R.layout.dialog_menu_customer_manage);
        Window win = mMenuDialog.getWindow();
        win.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.RIGHT | Gravity.TOP);
        mMenuDialog.setCanceledOnTouchOutside(true);
        mMenuDialog.show();
        LinearLayout ll_search_customer = (LinearLayout) mMenuDialog
                .findViewById(R.id.ll_search_customer);
        LinearLayout ll_add_customer = (LinearLayout) mMenuDialog
                .findViewById(R.id.ll_add_customer);
        ll_search_customer.setOnClickListener(this);
        ll_add_customer.setOnClickListener(this);
        ll_add_customer.setVisibility(View.VISIBLE);
    }
    private ListView listView;
    public static EditText mEtSearch;
    private List<String> mHistorySearch;
    private SearchAdapter mSearch;
    private List<EstateSearchItem> mSearchListData;
    private void showSearchDialog() {
        mSearchDialog = new Dialog(mContext, R.style.Theme_dialog);
        mSearchDialog.setContentView(R.layout.dialog_search_house_manage);
        Window win = mSearchDialog.getWindow();
        win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.TOP);
        mSearchDialog.setCanceledOnTouchOutside(true);
        listView = (ListView) mSearchDialog.findViewById(R.id.lv_historySearch_dialogSearchHouseManage);
        mSearch = new SearchAdapter(mContext,
                new ArrayList<EstateSearchItem>());
        listView.setAdapter(mSearch);

        mEtSearch = (EditText) mSearchDialog
                .findViewById(R.id.et_search_dialogSearchHouseManage);
        mEtSearch.setHint(getText(R.string.search_cust_hit));
        Button mBtnSearch = (Button) mSearchDialog
                .findViewById(R.id.btn_search_dialogSearchHouseManage);
        Button mBtnClean = (Button) mSearchDialog
                .findViewById(R.id.btn_close_dialogSearchHouseManage);
        mBtnSearch.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                searchKeYuan(mEtSearch.getText().toString().trim());
            }
        });
        mBtnClean.setOnClickListener(this);
        // 根据mEtSearch得到的字符串去请求

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
                Log.d("on text changed", "true");
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
                Log.d("before text changed", "true");
            }
            @Override
            public void afterTextChanged(Editable arg0) {
//                searchKeYuan(arg0.toString().trim());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String custCode = mSearchListData.get(arg2).getCustCode();
                /*String reqparm = CST_JS.getJsonStringForCustomerList(CST_JS.JS_CustomerList_Type_Mypotien, custCode, 1, 20);
                MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
                        CST_JS.JS_Function_CustomerList_getList, reqparm);*/
                mSearchDialog.dismiss();
//                showDialog();
                getCustomerData(custCode,1,false,true);
            }

        });
        mSearchDialog.show();
    }
    private void searchKeYuan(String editString) {
        mSearch.setList(null);
        listView.setAdapter(mSearch);
        setListHeight(mSearch, listView);
        if(editString==null||editString.length()<=0){
            mSearch.setList(null);
            listView.setAdapter(mSearch);
        }else{
            String paramType="text";
            if(MethodsExtra.isNumeric(editString.toString().trim())){
                paramType="character";
                if(!MethodsExtra.isMobileNO(editString.toString().trim())){
                    MethodsExtra.toast(mContext,"请输入正确的手机号码");
                    return;
                }
            }else{
                mSearch.setColorText(editString.toString().trim());
                paramType="text";
            }
            /*// 在打字期间添加搜索栏数据
            String reqparm = CST_JS
                    .getJsonStringForKeYuanGuanJianZi(CST_JS.JS_CustomerList_Type_Mypotien,editString,paramType, 1, 20);
            MethodsJni.callProxyFun(hif, CST_JS.JS_ProxyName_CustomerList,
                    CST_JS.JS_Function_CustListMobile_Serarch, reqparm);*/
            Loading.show(this);
            URL= NetWorkConstant.PORT_URL+ NetWorkMethod.custListMobileSerarch;
            Map<String,String>map=new HashMap<String,String>();
            map.put(NetWorkMethod.page, 1+"");
            map.put(NetWorkMethod.pageSize, MyConstant.pageSize+"");
            map.put(NetWorkMethod.type, NetWorkMethod.mypotien);
            map.put(NetWorkMethod.name, editString);
            map.put(NetWorkMethod.paramType, paramType);
            OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    stopRefreshOrLoadMore();
                }
                @Override
                public void onResponse(String response) {
                    stopRefreshOrLoadMore();
                    JSReturn jReturn = MethodsJson.jsonToJsReturn(response, EstateSearchItem.class);
                    if(jReturn.isSuccess()){
                        if(jReturn.getListDatas()!=null){
                            if(jReturn.getListDatas().size()>0){
                                mSearchListData = jReturn.getListDatas();
                                mSearch.setList(mSearchListData);
                                mSearch.notifyDataSetChanged();
                                setListHeight(mSearch, listView);
                                listView.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        MyToast.showToast(jReturn.getMsg());
                    }
                    /*if (jReturn.isSuccess()) {
                        if (jReturn.getListDatas() != null && jReturn.getListDatas().size() > 0) {
                            mSearchListData = jReturn.getListDatas();
                            mSearch.setList(mSearchListData);
                            mSearch.notifyDataSetChanged();
                            setListHeight(mSearch, listView);
                            listView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        MyToast.showToast(jReturn.getMsg());
                    }*/
                }
            });
        }
    }
}
