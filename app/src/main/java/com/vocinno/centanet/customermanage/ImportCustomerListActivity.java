package com.vocinno.centanet.customermanage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.adapter.ImportCustormerAdapter;
import com.vocinno.centanet.housemanage.adapter.SearchAdapter;
import com.vocinno.centanet.model.ImportCustomer;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.ImportCustInterface;
import com.vocinno.centanet.myinterface.NoDoubleClickListener;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView;
import com.vocinno.utils.view.refreshablelistview.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客源管理
 *
 * @author Administrator
 */
public class ImportCustomerListActivity extends OtherBaseActivity implements
        IXListViewListener ,ImportCustInterface {
    private Dialog mMenuDialog, mSearchDialog;
    private View mBack, mSubmit;
    private ImportCustormerAdapter mListAdapter;
    private List<ImportCustomer> customerList;
    private List<String> titleList;
    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_import_custormer;
    }
    @Override
    public void initView() {
        intent = getIntent();
        MethodsExtra.findHeadTitle1(mContext, baseView,
                R.string.customer_daoruke, null);
        mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mSubmit = MethodsExtra.findHeadRightView1(mContext, baseView, 0, R.drawable.import_search);
        mListView = (XListView) findViewById(R.id.xlv_import_listview);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mListView.setXListViewListener(this);
//        setSwipeMenuCreator();
    }

    @Override
    public void initData() {
        mListAdapter = new ImportCustormerAdapter((ImportCustomerListActivity) mContext,(ImportCustInterface)this);
        customerList = new ArrayList<ImportCustomer>();
        mListAdapter.setListData(customerList);
        mListView.setAdapter(mListAdapter);
        getCustomerData();
    }

    private void getCustomerData() {
        getCustomerData(null, 1, true);
    }

    private void getCustomerData(int pageNo, boolean isRefresh) {
        getCustomerData(null, pageNo, isRefresh);
    }

    private void getCustomerData(String phone, int pageNo, final boolean isRefresh) {
        if (isReFreshOrLoadMore) {
            isReFreshOrLoadMore = false;
        } else {
            Loading.showForExit(this, true);
        }
        URL = NetWorkConstant.PORT_URL + NetWorkMethod.importCustList;
        Map<String, String> map = new HashMap<String, String>();
        if (null != phone) {
            map.put(NetWorkMethod.importPhone, phone);
        }
        map.put(NetWorkMethod.page, pageNo + "");
        map.put(NetWorkMethod.pageSize, MyConstant.pageSize + "");
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                stopRefreshOrLoadMore();
            }

            @Override
            public void onResponse(String response) {
                stopRefreshOrLoadMore();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, ImportCustomer.class);
                if (jsReturn.isSuccess()) {
                    if (jsReturn.getListDatas() != null && jsReturn.getListDatas().size() < MyConstant.pageSize) {
                        mListView.setPullLoadEnable(false);
                    } else {
                        mListView.setPullLoadEnable(true);
                    }
                    if (isRefresh) {
                        addTitle(jsReturn.getListDatas());
                        mListAdapter.setListData(jsReturn.getListDatas());
                    } else {
                        page++;
                        addTitle(jsReturn.getListDatas());
                        mListAdapter.addListData(jsReturn.getListDatas());
                    }
                } else {
                    MyToast.showToast(jsReturn.getMsg());
                }
            }
        });
    }

    public void addTitle(List list){
        if(titleList==null){
            titleList=new ArrayList<String>();
        }
        ImportCustomer importCustomer;
        for (int i = 0; i < list.size(); i++) {
            importCustomer= (ImportCustomer) list.get(i);
            int type=MyUtils.compareNowDate(new Date(importCustomer.getImportTime()));
            if(type==0){//今天
                if(!titleList.contains("0")){
                    titleList.add("0");
                    importCustomer.setTitle(MyConstant.today);
                }
                importCustomer.setFormatDate(MyUtils.getFormatDate(importCustomer.getImportTime(), "HH:mm"));
            }else if(type==1){//昨天
                if(!titleList.contains("1")){
                    titleList.add("1");
                    importCustomer.setTitle(MyConstant.yesterday);
                }
                importCustomer.setFormatDate(MyUtils.getFormatDate(importCustomer.getImportTime(), "HH:mm"));
            }else if(type==2){//前天
                if(!titleList.contains("2")){
                    titleList.add("2");
                    importCustomer.setTitle(MyConstant.before_yesterday);
                }
                importCustomer.setFormatDate(MyUtils.getFormatDate(importCustomer.getImportTime(), "HH:mm"));
            }else{//更早
                if(!titleList.contains("3")){
                    titleList.add("3");
                    importCustomer.setTitle(MyConstant.before_more_yesterday);
                }
                importCustomer.setFormatDate(MyUtils.getFormatDate(importCustomer.getImportTime(), "MM-dd HH:mm"));
            }
        }
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_add_customer:        //标题右侧Toast 添加客户
                startActivityForResult(new Intent(mContext, AddCustomerActivity.class), 101);
                mMenuDialog.dismiss();
                break;
            case R.id.img_left_mhead1:
                finish();
                break;
            case R.id.img_right_mhead1:
                showSearchDialog();
                break;
            case R.id.bt_import_clear:
                mEtSearch.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        isReFreshOrLoadMore = true;
        page = 2;
        titleList.clear();
        getCustomerData();
    }

    @Override
    public void onLoadMore() {
        isReFreshOrLoadMore = true;
        getCustomerData(page, false);
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {
    }

    private void setListHeight(SearchAdapter mSearch, ListView mListView) {
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

    private static EditText mEtSearch;
    private TextView tv_import_search;
    private Button bt_import_clear;
    private void showSearchDialog() {
        mSearchDialog = new Dialog(mContext, R.style.Theme_dialog);
        mSearchDialog.setContentView(R.layout.import_people_dialog_search);
        Window win = mSearchDialog.getWindow();
        win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.TOP);
        mSearchDialog.setCanceledOnTouchOutside(true);

        mEtSearch = (EditText) mSearchDialog
                .findViewById(R.id.et_search_dialogSearchHouseManage);
        bt_import_clear = (Button) mSearchDialog
                .findViewById(R.id.bt_import_clear);
        bt_import_clear.setOnClickListener(this);
        tv_import_search = (TextView) mSearchDialog
                .findViewById(R.id.tv_import_search);
        tv_import_search.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                searchKeYuan(mEtSearch.getText().toString().trim());
            }
        });
        mSearchDialog.show();
    }

    private void searchKeYuan(String editString) {
            if (MethodsExtra.isNumeric(editString.toString().trim())) {
                if (!MethodsExtra.isMobileNO(editString.toString().trim())) {
                    MethodsExtra.toast(mContext, "请输入正确的手机号码");
                    return;
                }
            }
        page=2;
        titleList.clear();
        getCustomerData(editString.toString().trim(), 1, true);
        mSearchDialog.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case MyConstant.REFRESH:
                page = 2;
                titleList.clear();
                getCustomerData();
                break;
        }
    }
    @Override
    public void importCustAccept(final int position,String id,final SwipeLayout swipeLayout) {
        intent.setClass(this,AddDemandActivity.class);
        intent.putExtra(MyConstant.isImportCust,true);
        intent.putExtra(MyConstant.pkid,id);
        startActivityForResult(intent, MyConstant.START_REQUEST);
    }
    public void importCustInvalid(final int position,String id,final SwipeLayout swipeLayout) {
        intent.setClass(this, InvalidReasonActivity.class);
        intent.putExtra(MyConstant.pkid,id);
        startActivityForResult(intent, MyConstant.START_REQUEST);
    }
}
