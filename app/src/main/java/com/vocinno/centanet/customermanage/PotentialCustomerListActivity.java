package com.vocinno.centanet.customermanage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.adapter.PotentialCustormerListAdapter;
import com.vocinno.centanet.housemanage.adapter.SearchAdapter;
import com.vocinno.centanet.model.CustomerItem;
import com.vocinno.centanet.model.CustomerList;
import com.vocinno.centanet.model.EstateSearchItem;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.NoDoubleClickListener;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView;

import java.util.ArrayList;
import java.util.List;

public class PotentialCustomerListActivity extends OtherBaseActivity implements XListView.IXListViewListener {

    private Dialog mMenuDialog,mSearchDialog;
    private XListView mLvCustormers;
    private View mBack, mSubmit;
    private PotentialCustormerListAdapter mListAdapter;
    private int mPageIndex = 1;
    private List<CustomerItem> mListCustomers = new ArrayList<CustomerItem>();
    private List<CustomerItem> mListCustomersLast = new ArrayList<CustomerItem>();
    private boolean isReFreshOrLoadMore=false;
    //	private String sOrZ;//用来判断客源跟进跳转查询出售还是出租的客源列表
    private String delegationType;//用来判断客源跟进跳转查询出售还是出租的客源列表
    private Intent intent;
    @Override
    public Handler setHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {

            }
        };
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
        mLvCustormers = (XListView) findViewById(R.id.xlv_potential_custormer);
        mLvCustormers.setPullLoadEnable(false);
        mLvCustormers.setPullRefreshEnable(true);
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mLvCustormers.setXListViewListener(this);
    }

    @Override
    public void initData() {
        intent=getIntent();
        boolean isGongKe = intent.getBooleanExtra("isGongKe", false);
        mListAdapter = new PotentialCustormerListAdapter((PotentialCustomerListActivity) mContext);
        mListAdapter.setListDatas(null);
        mLvCustormers.setAdapter(mListAdapter);
        // 添加通知
        MethodsJni.addNotificationObserver(
                CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, TAG);
        MethodsJni.addNotificationObserver(
                CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_CUSTOMER_RESULT, TAG);
        // 调用数据
        getDataFromNetwork(mPageIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case MyConstant.REFRESH:
                getDataFromNetwork(mPageIndex);
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
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, TAG);
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_CUSTOMER_RESULT, TAG);
    }
    // 调用数据
    private void getDataFromNetwork(int page) {
        if(isReFreshOrLoadMore){
            isReFreshOrLoadMore=false;
        }else{
            showDialog();
        }
        String  strReq = CST_JS.getJsonStringForCustomerList(CST_JS.JS_CustomerList_Type_Mypotien, page, 8);
        MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_CustomerList,
                CST_JS.JS_Function_CustomerList_getList, strReq);
    }
    public void notifCallBack(String name, String className, Object data) {
    }
    @Override
    public void onRefresh() {
        mPageIndex = 1;
        isReFreshOrLoadMore=true;
        getDataFromNetwork(mPageIndex);
    }

    private boolean isLoading = false;
    @Override
    public void onLoadMore() {
        if (!isLoading) {
            isLoading = true;
            isReFreshOrLoadMore=true;
            getDataFromNetwork(mPageIndex + 1);
        }
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {
        dismissDialog();
        String strJson = (String) data;
        JSReturn jsReturn = MethodsJson.jsonToJsReturn(strJson,
                CustomerList.class);
        if(name.equals(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT)){
            if (jsReturn.isSuccess()) {
                if (jsReturn.getParams().getIsAppend()) {
                    mLvCustormers.stopLoadMore();
                    if(jsReturn.getListDatas()==null|| jsReturn.getListDatas().size()<=0){
                        mLvCustormers.setPullLoadEnable(false);
                    }else{
                        mLvCustormers.setPullLoadEnable(true);
                    }
                    mListAdapter.addListDatas(jsReturn.getListDatas());
                    mListAdapter.notifyDataSetChanged();
                    mPageIndex++;
                } else {
                    mLvCustormers.stopRefresh();
                    mLvCustormers.setPullLoadEnable(true);
                    mListCustomers = jsReturn.getListDatas();
                    mListAdapter.setListDatas(mListCustomers);
                    mListAdapter.notifyDataSetChanged();
                }

            } else {
                if (mPageIndex == 1) {
                    // 刷新失败
                    mHander.sendEmptyMessage(R.id.FINISH_REFRESH);
                } else {
                    // 加载更多失败
                    mHander.sendEmptyMessage(R.id.FINISH_LOAD_MORE);
                    mPageIndex--;
                }
                MethodsExtra.toast(mContext, jsReturn.getMsg());
            }
            isLoading = false;
        }else if (name.equals(CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_CUSTOMER_RESULT)) {
            JSReturn jReturn = MethodsJson.jsonToJsReturn((String) data,
                    EstateSearchItem.class);
            if(jReturn.isSuccess()){
                if(jReturn.getListDatas()!=null){
                    if(jReturn.getListDatas().size()>0){
                        mSearchListData = jReturn.getListDatas();
                        mSearch.setList(mSearchListData);
                        mSearch.notifyDataSetChanged();
//                    mListView.setAdapter(mSearch);
                        setListHeight(mSearch,mListView);
                        mListView.setVisibility(View.VISIBLE);
                    }else{
//					MethodsExtra.toast(mContext,"抱歉没有搜索到房源");
                        //抱歉没有搜索到该房源
                    }
                }
            }else{
                MethodsExtra.toast(mContext, jsReturn.getMsg());
            }

        }
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
    private ListView mListView;
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
        mListView = (ListView) mSearchDialog.findViewById(R.id.lv_historySearch_dialogSearchHouseManage);
        mSearch = new SearchAdapter(mContext,
                new ArrayList<EstateSearchItem>());
        mListView.setAdapter(mSearch);

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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String custCode = mSearchListData.get(arg2).getCustCode();
                String reqparm = CST_JS.getJsonStringForCustomerList(CST_JS.JS_CustomerList_Type_Mypotien,custCode, 1, 20);
                MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
                        CST_JS.JS_Function_CustomerList_getList, reqparm);
                mSearchDialog.dismiss();
                showDialog();
            }

        });
        mSearchDialog.show();
    }
    private void searchKeYuan(String editString) {
        if(editString==null||editString.length()<=0){
//			mSearch.setList(null);
//			mListView.setAdapter(mSearch);
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
            // 在打字期间添加搜索栏数据
            String reqparm = CST_JS
                    .getJsonStringForKeYuanGuanJianZi(CST_JS.JS_CustomerList_Type_Mypotien,editString,paramType, 1, 20);
            MethodsJni.callProxyFun(hif, CST_JS.JS_ProxyName_CustomerList,
                    CST_JS.JS_Function_CustListMobile_Serarch, reqparm);
        }
    }
}
