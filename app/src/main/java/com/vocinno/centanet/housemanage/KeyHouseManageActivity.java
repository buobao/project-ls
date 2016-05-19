package com.vocinno.centanet.housemanage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.AppInstance;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.cst.CST_Wheel_Data;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.apputils.selfdefineview.scrolltagviewradio.ScrollTagView;
import com.vocinno.centanet.apputils.selfdefineview.scrolltagviewradio.ScrollTagViewAdapter;
import com.vocinno.centanet.apputils.selfdefineview.scrolltagviewradio.onScrollTagViewChangeListener;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.housemanage.adapter.CustomGridView;
import com.vocinno.centanet.housemanage.adapter.MyFragmentAdapter;
import com.vocinno.centanet.housemanage.adapter.SearchAdapter;
import com.vocinno.centanet.model.EstateSearchItem;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.GetDataInterface;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.centanet.myinterface.TagSlidingInterface;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.user.UserLoginActivity;
import com.vocinno.utils.CustomUtils;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import java.util.ArrayList;
import java.util.List;

/**
 * 房源管理
 *
 * @author Administrator
 */
public class KeyHouseManageActivity extends OtherBaseActivity implements HttpInterface, GetDataInterface, TagSlidingInterface {
    private static final String Weixin_APP_ID = "wx52560d39a9b47eae";
    private int[] mIntScreenWidthHeight = {0, 0};
    private final int NEAR_CHU_ZU = 1;
    private final int MY_CHU_ZU = 4;
    	private int viewPageIndex=0;
    private FrameLayout fl_key_houselist;
    private MyFragmentAdapter pagerAdapter, pagerGongFangAdapter;
    private List<Fragment> fragmentList, gongFangList;
    private ScrollTagView mScrollTagView;
    private LinearLayout ll_tag_contect;
    private ScrollTagViewAdapter mScrollTagViewAdapter;
    private Dialog mMenuDialog, mSearchDialog, mTagSortDialog;
    private TextView mTvAreaSort, mTvPriceSort;
    private PaiXuType mPaiXuType = PaiXuType.None;
    private DrawerLayout drawer_layout;
    private PopupWindow popu;
    private String dongHao;
    private String shiHao;
    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    private enum PaiXuType {
        None, mTvAreaSortUp, mTvAreaSortDown, mTvPriceSortUp, mTvPriceSortDown
    }

    private LinearLayout ll_house_list, ll_dialog_wheelview_two0, ll_dialog_wheelview_two1, ll_dialog_wheelview_two2, ll_dialog_wheelview_two3, ll_dialog_wheelview_two4;
    private List<String> mHistorySearch;
    private ListView lv_house_list;
    private View mViewBack, mViewMore;
    private Drawable drawable;
    private int layoutIndex = -1;//用于记录打开条件视图的下标
    private List<LinearLayout> layoutList;
    private GridViewAdapter mHouseTagAdapter;
    private String[] mPrice = {null};// 价格
    private String[] mSquare = {null};// 面积
    private String[] mFrame = {null};// 户型
    private String[] mTags = {null};// 标签
    private String[] mUserType = {null};// 类型
    public String searchId[] = {""};
    public String searchType[] = {""};
    private int tagSelectIndex;
    private KeyHouseFragment keyHouseFragment;
    private List<EstateSearchItem> mSearchListData;
    private EditText et_house_dong, et_house_shi;
    private ImageButton ib_tag_jiantou;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        TAG = this.getClass().getName();
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_key_house_manage;
    }

    @Override
    public Handler setHandler() {
        return null;
    }

    private void showSortDialog() {
        mSearchDialog = new Dialog(mContext, R.style.Theme_dialog);
        mSearchDialog.setContentView(R.layout.dialog_sort_house_manage);
        Window win = mSearchDialog.getWindow();
        win.setGravity(Gravity.TOP);
        mSearchDialog.setCanceledOnTouchOutside(true);
        win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        mTvAreaSort = (TextView) mSearchDialog
                .findViewById(R.id.tv_sortArea_HouseManageActivity);
        mTvPriceSort = (TextView) mSearchDialog
                .findViewById(R.id.tv_priceArea_HouseManageActivity);

        mTvAreaSort.setOnClickListener(this);
        mTvPriceSort.setOnClickListener(this);
        // 五个筛选条件的箭头指向
        if (mPaiXuType == PaiXuType.mTvAreaSortUp) {
            drawable = getResources().getDrawable(
                    R.drawable.h_manage_order_icon_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mTvAreaSort.setCompoundDrawables(null, null, drawable, null);
            mTvAreaSort.setCompoundDrawablePadding(10);

        } else if (mPaiXuType == PaiXuType.mTvAreaSortDown) {
            drawable = getResources().getDrawable(
                    R.drawable.h_manage_order_icon_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mTvAreaSort.setCompoundDrawables(null, null, drawable, null);
            mTvAreaSort.setCompoundDrawablePadding(10);

        } else if (mPaiXuType == PaiXuType.mTvPriceSortUp) {
            drawable = getResources().getDrawable(
                    R.drawable.h_manage_order_icon_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mTvPriceSort.setCompoundDrawables(null, null, drawable, null);
            mTvPriceSort.setCompoundDrawablePadding(10);

        } else if (mPaiXuType == PaiXuType.mTvPriceSortDown) {
            drawable = getResources().getDrawable(
                    R.drawable.h_manage_order_icon_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mTvPriceSort.setCompoundDrawables(null, null, drawable, null);
            mTvPriceSort.setCompoundDrawablePadding(10);
        } else {
            drawable = getResources().getDrawable(
                    R.drawable.h_manage_more_icon_order);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mTvAreaSort.setCompoundDrawables(null, null, drawable, null);
            mTvAreaSort.setCompoundDrawablePadding(10);
            mTvPriceSort.setCompoundDrawables(null, null, drawable, null);
            mTvPriceSort.setCompoundDrawablePadding(10);
        }
        mSearchDialog.show();
    }

    @Override
    public void initView() {
        MethodsExtra.findHeadTitle1(mContext, baseView,
                R.string.keyhouselist, null);
        fl_key_houselist = (FrameLayout) findViewById(R.id.fl_key_houselist);
        ll_tag_contect = (LinearLayout) findViewById(R.id.ll_tag_contect);
        ib_tag_jiantou = (ImageButton) findViewById(R.id.ib_tag_jiantou);
        ib_tag_jiantou.setOnClickListener(this);
        drawer_layout = (DrawerLayout) baseView.findViewById(R.id.drawer_layout);
        setViewPager();
        addLinearLayout();
        mViewBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mViewMore = MethodsExtra.findHeadRightView1(mContext, baseView, 0, 0);
        mViewBack.setOnClickListener(this);
        mViewMore.setOnClickListener(this);
        mScrollTagView = (ScrollTagView) baseView
                .findViewById(R.id.scrolltag_houseManage_houseManageActivity);
        mScrollTagView.setInterface((TagSlidingInterface) this);
        mScrollTagViewAdapter = new ScrollTagViewAdapter(mContext);
        // 添加标签
        mScrollTagViewAdapter.add(" 价格 ");
        mScrollTagViewAdapter.add(" 面积 ");
        mScrollTagViewAdapter.add(" 户型 ");
        mScrollTagViewAdapter.add(" 标签 ");
        mScrollTagViewAdapter.add(" 类型 ");
        mScrollTagView.setAdapter(mScrollTagViewAdapter,
                new onScrollTagViewChangeListener() {
                    @Override
                    public void onChanged(int index) {
                        if (index < 5 && index >= 0) {
                            selectTag(index);
                            tagSelectIndex = index;
                        }
                    }
                });

    }

    private void setViewPager() {

    }


    @Override
    public void initData() {
        keyHouseFragment = new KeyHouseFragment((GetDataInterface)this,0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_key_houselist, keyHouseFragment).commit();
        mIntScreenWidthHeight = MethodsData.getScreenWidthHeight(mContext);
        registerWeiXin();
    }

    @Override
    public void getListData(String type, String price, String square, String frame, String tag, String usageType, int page, int pageSize, String sidx, String sord, String searchId, String searchType) {
        if ((HouseType.YUE_KAN + "").equals(type)) {
            methodsJni.callProxyFun(hif, CST_JS.JS_ProxyName_HouseResource,
                    CST_JS.JS_Function_HouseResource_houLookPlanListMobile, CST_JS
                            .getJsonStringForHouseListGetList(type, price, square, frame, tag, usageType, page, pageSize, sidx, sord, searchId, searchType));
        } else {
            methodsJni.callProxyFun(hif, CST_JS.JS_ProxyName_HouseResource,CST_JS.JS_Function_HouseResource_getList, CST_JS.getJsonStringForHouseListGetList(type, price, square, frame, tag, usageType, page, pageSize, sidx, sord, searchId, searchType, dongHao == null ? "" : dongHao, shiHao == null ? "" : shiHao));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case MyConstant.REFRESH:
//                getDataFromNetwork(mType, mPageIndexs[mCurrentPageIndex]);
                break;
            case RESULT_OK:
                if (requestCode == 10) {

                }
                break;
        }
    }

    // tag点击后调用
    public void selectTag(int currentIndex) {
        showTagSortDialog(currentIndex);
    }

    @Override
    public void sliding(boolean flag) {
        if (flag) {
            ib_tag_jiantou.setImageResource(R.drawable.jiantou_right);
            slidingFlag = true;
        } else {
            ib_tag_jiantou.setImageResource(R.drawable.jiantou_left);
            slidingFlag = false;
        }
    }

    private boolean slidingFlag;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ib_tag_jiantou:
                slidingFlag = !slidingFlag;
                if (slidingFlag) {
                    ib_tag_jiantou.setImageResource(R.drawable.jiantou_right);
                } else {
                    ib_tag_jiantou.setImageResource(R.drawable.jiantou_left);
                }
                mScrollTagView.setSliding(slidingFlag);
                break;
            case R.id.tv_searchinmap_HouseManageActivity:
                mMenuDialog.dismiss();
                MethodsExtra.startActivity(mContext, MapActivity.class);
                break;
            case R.id.tv_searchbyekey_HouseManageActivity:
                mMenuDialog.dismiss();
                showSearchDialog();
                break;
            case R.id.tv_sort_HouseManageActivity:
                mMenuDialog.dismiss();
                showSortDialog();
                break;
            case R.id.tv_priceArea_HouseManageActivity:
                // 按照价格排序
                // 点击一下 +1 根据变量的奇数偶数来判断传递什么参数
                // 下一个case也是一样的
                if (mPaiXuType == PaiXuType.None) {
                    mPaiXuType = PaiXuType.mTvPriceSortUp;
                    searchByOrder("price", "asc");
                } else if (mPaiXuType == PaiXuType.mTvPriceSortDown) {
                    mPaiXuType = PaiXuType.mTvPriceSortUp;
                    searchByOrder("price", "asc");

                } else if (mPaiXuType == PaiXuType.mTvPriceSortUp) {
                    mPaiXuType = PaiXuType.mTvPriceSortDown;
                    searchByOrder("price", "desc");

                } else {
                    mPaiXuType = PaiXuType.mTvPriceSortUp;
                    searchByOrder("price", "asc");
                }
                mSearchDialog.dismiss();
                break;
            case R.id.tv_sortArea_HouseManageActivity:
                // 按照价面积排序
                if (mPaiXuType == PaiXuType.None) {
                    mPaiXuType = PaiXuType.mTvAreaSortUp;
                    searchByOrder("acre", "asc");
                } else if (mPaiXuType == PaiXuType.mTvAreaSortUp) {
                    mPaiXuType = PaiXuType.mTvAreaSortDown;
                    searchByOrder("acre", "desc");
                } else if (mPaiXuType == PaiXuType.mTvAreaSortDown) {
                    mPaiXuType = PaiXuType.mTvAreaSortUp;
                    searchByOrder("acre", "asc");
                } else {
                    mPaiXuType = PaiXuType.mTvAreaSortUp;
                    searchByOrder("acre", "asc");
                }
                mSearchDialog.dismiss();
//                showDialog();
                break;
            case R.id.btn_submit_modelOneWheelView://类型--确定
                // 类型筛选（没有接口）
                WheelView mWheelViewOne = (WheelView) findViewById(R.id.wheelview_modelOneWheelView);
                mUserType[viewPageIndex] = mWheelViewOne.getSelectedText()
                        .replace("全部类型", "");
                searchByTag(tagSelectIndex, mUserType[viewPageIndex]);
                ll_dialog_wheelview_two4.setVisibility(View.GONE);
                layoutIndex = -1;
                break;
            case R.id.btn_submit_modelPriceWheelView:
                WheelView wheelStart0 = (WheelView) findViewById(R.id.wheelview_start_modelPriceWheelView);
                WheelView wheelEnd0 = (WheelView) findViewById(R.id.wheelview_end_modelPriceWheelView);
                String startString = wheelStart0.getSelectedText().split("万")[0];
                String endString = wheelEnd0.getSelectedText().trim()
                        .equals("不限") ? wheelEnd0.getSelectedText().trim()
                        : wheelEnd0.getSelectedText().split("万")[0];
//				if (viewPageIndex!=1&&viewPageIndex != 4) {
                startString += "0000";
                if (!"不限".equals(endString)) {
                    endString += "0000";
                }
//				}
                mPrice[viewPageIndex] = startString.trim()
                        .replaceAll("万", "").replaceAll("元", "")
                        + "-"
                        + endString.trim().replaceAll("万", "")
                        .replaceAll("元", "");
                if (endString.trim().equals("不限")
                        || Integer.parseInt(startString.trim()
                        .replaceAll("万", "").replaceAll("元", "")) < Integer
                        .parseInt(endString.trim().replaceAll("万", "")
                                .replaceAll("元", ""))) {
                    searchByTag(tagSelectIndex, mPrice[viewPageIndex]);
                    ll_dialog_wheelview_two0.setVisibility(View.GONE);
                    layoutIndex = -1;
                } else {
                    MethodsExtra.toast(mContext, "最高价格应大于最低价格");
                }
                break;
            case R.id.btn_submit_modelTwoWheelView:
                WheelView wheelStart = (WheelView) findViewById(R.id.wheelview_start_modelTwoWheelView);
                WheelView wheelEnd = (WheelView) findViewById(R.id.wheelview_end_modelTwoWheelView);

                String startString1 = wheelStart.getSelectedText().split("平米")[0]
                        + "";
                String endString1 = wheelEnd.getSelectedText().split("平米")[0]
                        + "";
                mSquare[viewPageIndex] = startString1 + "-" + endString1;
                if (endString1.equals("不限")
                        || Integer.parseInt(startString1.trim()) < Integer
                        .parseInt(endString1.trim())) {

                    searchByTag(tagSelectIndex, mSquare[viewPageIndex]);

                    ll_dialog_wheelview_two1.setVisibility(View.GONE);
                    layoutIndex = -1;
                } else {
                    MethodsExtra.toast(mContext, "最大面积应大于最小面积");
                    return;
                }

                break;
            case R.id.btn_submit_modelFourWheelView://户型--确定
                WheelView mWheelView1 = (WheelView) findViewById(R.id.wheelview_first_modelFourWheelView);
                WheelView mWheelView2 = (WheelView) findViewById(R.id.wheelview_second_modelFourWheelView);
                WheelView mWheelView3 = (WheelView) findViewById(R.id.wheelview_third_modelFourWheelView);
                WheelView mWheelView4 = (WheelView) findViewById(R.id.wheelview_forth_modelFourWheelView);
                mFrame[viewPageIndex] = mWheelView1.getSelectedText() + "-"
                        + mWheelView2.getSelectedText() + "-"
                        + mWheelView3.getSelectedText() + "-"
                        + mWheelView4.getSelectedText();

                searchByTag(tagSelectIndex, mFrame[viewPageIndex]);

                ll_dialog_wheelview_two2.setVisibility(View.GONE);
                layoutIndex = -1;
                break;
            case R.id.btn_submit_dialogTagSelector://标签--确定
                // 标签
                mTags[viewPageIndex] = mHouseTagAdapter.getSelectedTags();

                searchByTag(tagSelectIndex, mTags[viewPageIndex]);

                ll_dialog_wheelview_two3.setVisibility(View.GONE);
                layoutIndex = -1;
                break;
            case R.id.backView_dialogOneWheelview://类型--取消
                ll_dialog_wheelview_two4.setVisibility(View.GONE);
                layoutIndex = -1;
                break;
            case R.id.backView_dialogTwoWheelview:
                View btnBack1 = findViewById(R.id.backView_dialogTwoWheelview);
                LinearLayout ll1 = (LinearLayout) btnBack1.getParent().getParent();
                if (ll1.getVisibility() == View.VISIBLE) {
                    ll1.setVisibility(View.GONE);
                }
                Log.i("LinearLayout=1=id=", ll1.getId() + "===");
                break;
            case R.id.backView_dialogPriceWheelview:
                View btnBack0 = findViewById(R.id.backView_dialogPriceWheelview);
                LinearLayout ll0 = (LinearLayout) btnBack0.getParent().getParent();
                if (ll0.getVisibility() == View.VISIBLE) {
                    ll0.setVisibility(View.GONE);
                }
                Log.i("LinearLayout=1=id=", ll0.getId() + "===");
                break;
            case R.id.backView_dialogFourWheelView://户型--取消
                View btnBack2 = findViewById(R.id.backView_dialogFourWheelView);
                LinearLayout ll2 = (LinearLayout) btnBack2.getParent().getParent();
                if (ll2.getVisibility() == View.VISIBLE) {
                    ll2.setVisibility(View.GONE);
                }
                Log.i("LinearLayout=2=id=", ll2.getId() + "===");
                break;
            case R.id.backView_dialogTagSelector://标签--取消
                ll_dialog_wheelview_two3.setVisibility(View.GONE);
                layoutIndex = -1;
                break;
            case R.id.img_left_mhead1:
                onBack();
                break;
            case R.id.img_right_mhead1:
                closeOtherWheelView(layoutIndex);
                showMenuDialog();
                break;
            case R.id.tv_house_search:
                dongHao = et_house_dong.getText().toString();
                shiHao = et_house_shi.getText().toString();
                searchByKeyWord(searchId[viewPageIndex], searchType[viewPageIndex]);
                mSearchDialog.dismiss();
                break;
            case R.id.ry_exit:
                intent.setClass(this, UserLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MyConstant.isExit, true);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void searchByOrder(String param, String order) {
        switch (0) {
            case 0:
                keyHouseFragment.searchByOrderForList(param, order);
                keyHouseFragment.getData(1, false);
                break;
        }
    }

    private void searchByKeyWord(String searchId, String searchType) {
        switch (0) {
            case 0:
                keyHouseFragment.searchByKeyWord(searchId, searchType);
                keyHouseFragment.getData(1, false);
                break;
        }

    }

    private void searchByTag(int tagIndex, String param) {
        switch (0) {
            case 0:
                keyHouseFragment.searchForList(tagIndex, param);
                break;
        }
    }


    @Override
    public void netWorkResult(String name, String className, Object data) {
        dismissDialog();
        if (name.equals(CST_JS.NOTIFY_NATIVE_HOU_LIST_RESULT)|| name.equals(CST_JS.NOTIFY_NATIVE_HOU_LIST_SEARCH_RESULT) || name.equals(CST_JS.NOTIFY_NATIVE_CUSTOMER_YUE_RESULT)) {
            JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data, HouseList.class);
            int type = Integer.parseInt(jsReturn.getParams().getType());
            if (jsReturn.isSuccess()) {
                int dataType = jsReturn.getParams().getIsAppend() ? 1 : 0;
                switch (type) {
                    case HouseType.YAO_SHI:
                        keyHouseFragment.setListData(dataType, jsReturn.getListDatas());
                        break;
                }
            } else {
                MethodsExtra.toast(mContext, jsReturn.getMsg());
            }
        } else if (name.equals(CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_RESULT)) {
            JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
                    EstateSearchItem.class);
            if (jsReturn.isSuccess()) {
                if (jsReturn.getListDatas() != null) {
                    if (jsReturn.getListDatas().size() > 0) {
                        mSearchListData = jsReturn.getListDatas();
                        SearchAdapter mSearch = new SearchAdapter(mContext, mSearchListData);
                        lv_house_list.setAdapter(mSearch);
                        if (jsReturn.getListDatas().size() == 1 && isHiddenList) {
                            ll_house_list.setVisibility(View.GONE);
                            mEtSearch.setBackgroundResource(R.drawable.dialog_search_edit_bg_house_manage);
                            isHiddenList = false;
                        } else {
                            ll_house_list.setVisibility(View.VISIBLE);
                            mEtSearch.setBackgroundResource(R.drawable.dialog_search_edit_show);
                        }
                    } else {
//					MethodsExtra.toast(mContext,"抱歉没有搜索到房源");
                        //抱歉没有搜索到该房源
                    }
                }
            } else {
                MethodsExtra.toast(mContext, jsReturn.getMsg());
            }

        }
    }

    MethodsJni methodsJni;

    private void searchHouse(String editString) {
        lv_house_list.setVisibility(View.INVISIBLE);
        if (editString == null || editString.length() <= 0) {
//					MethodsExtra.toast(mContext,"抱歉没有搜索到房源");
//            MethodsExtra.toast(mContext, "请输入搜索条件");
        } else {
            if (methodsJni == null) {
                methodsJni = new MethodsJni();
            }
            methodsJni.setMethodsJni((HttpInterface) this);
            // 在打字期间添加搜索栏数据
            MethodsJni.callProxyFun(
                    CST_JS.JS_ProxyName_HouseResource,
                    CST_JS.JS_Function_HouseResource_searchEstateName,
                    CST_JS.getJsonStringForHouseListSearchEstateName(
                            editString, 1, 20));
            lv_house_list.setVisibility(View.VISIBLE);
        }
    }

    private void showMenuDialog() {
        mMenuDialog = new Dialog(mContext, R.style.Theme_dialog);
        mMenuDialog.setContentView(R.layout.dialog_menu_house_manage);
        Window win = mMenuDialog.getWindow();
        win.setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.RIGHT | Gravity.TOP);
        mMenuDialog.setCanceledOnTouchOutside(true);
        mMenuDialog.show();
        TextView mTvSearchInMap = (TextView) mMenuDialog
                .findViewById(R.id.tv_searchinmap_HouseManageActivity);
        TextView mTvSearchByKey = (TextView) mMenuDialog
                .findViewById(R.id.tv_searchbyekey_HouseManageActivity);
        TextView mTvSort = (TextView) mMenuDialog
                .findViewById(R.id.tv_sort_HouseManageActivity);
        mTvSearchInMap.setOnClickListener(this);
        mTvSearchByKey.setOnClickListener(this);
        mTvSort.setOnClickListener(this);

        mTvSearchInMap.setVisibility(View.GONE);
    }

    public static EditText mEtSearch;
    private TextView tv_house_search;
    private boolean isHiddenList;

    private void showSearchDialog() {
        mSearchDialog = new Dialog(mContext, R.style.Theme_dialog);
        mSearchDialog.setContentView(R.layout.dialog_search_house_manage2);
        Window win = mSearchDialog.getWindow();
        win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.TOP);
        mSearchDialog.setCanceledOnTouchOutside(true);
        ll_house_list = (LinearLayout) mSearchDialog.findViewById(R.id.ll_house_list);
        lv_house_list = (ListView) mSearchDialog.findViewById(R.id.lv_house_list);
        final SearchAdapter mSearch = new SearchAdapter(mContext,
                new ArrayList<EstateSearchItem>());
        lv_house_list.setAdapter(mSearch);
        et_house_dong = (EditText) mSearchDialog.findViewById(R.id.et_house_dong);
        et_house_dong.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_house_dong.setHint("");
                } else {
                    et_house_dong.setHint("栋");
                }
            }
        });
        et_house_shi = (EditText) mSearchDialog.findViewById(R.id.et_house_shi);
        et_house_shi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_house_shi.setHint("");
                } else {
                    et_house_shi.setHint("室");
                }
            }
        });
        mEtSearch = (EditText) mSearchDialog
                .findViewById(R.id.et_search_dialogSearchHouseManage);
        tv_house_search = (TextView) mSearchDialog
                .findViewById(R.id.tv_house_search);
        tv_house_search.setOnClickListener(this);
        lv_house_list = (ListView) mSearchDialog.findViewById(R.id.lv_house_list);
        Button mBtnClean = (Button) mSearchDialog
                .findViewById(R.id.btn_close_dialogSearchHouseManage);
        mBtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ll_house_list.setVisibility(View.GONE);
                mEtSearch.setBackgroundResource(R.drawable.dialog_search_edit_bg_house_manage);
                mEtSearch.setText("");
                mSearch.setList(null);
                mSearch.notifyDataSetChanged();
            }
        });
        // 根据mEtSearch得到的字符串去请求

        mEtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0 == null || arg0.toString().trim().length() <= 0) {
                    ll_house_list.setVisibility(View.GONE);
                    mEtSearch.setBackgroundResource(R.drawable.dialog_search_edit_bg_house_manage);
                } else {
                    searchHouse(arg0.toString().trim());
                }
            }
        });

        lv_house_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                searchId[viewPageIndex] = mSearchListData.get(arg2).getSearchId() + "";
                searchType[viewPageIndex] = mSearchListData.get(arg2).getSearchType();
                mEtSearch.setText(mSearchListData.get(arg2).getSearchName());
                ll_house_list.setVisibility(View.GONE);
                mEtSearch.setBackgroundResource(R.drawable.dialog_search_edit_bg_house_manage);
                isHiddenList = true;
//                searchByKeyWord(searchId[viewPageIndex],searchType[viewPageIndex]);
//                mSearchDialog.dismiss();
            }

        });
        // 然后填充入listView
        if (mHistorySearch != null) {
            lv_house_list.setVisibility(View.VISIBLE);
        }
        mSearchDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_HOU_LIST_RESULT, TAG);
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_HOU_LIST_SEARCH_RESULT, TAG);
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_HOU_LIST_INMAP_RESULT, TAG);
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_HOU_LIST_CLICK_MAP_RESULT, TAG);
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_RESULT, TAG);
        MethodsJni.removeNotificationObserver(
                CST_JS.NOTIFY_NATIVE_CUSTOMER_YUE_RESULT, TAG);

        MethodsJni.removeAllNotifications(TAG);
    }

    private void showTagSortDialog(int tagIndex) {
        mTagSortDialog = new Dialog(mContext, R.style.Theme_dialog_transparent);
        mTagSortDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mTagSortDialog = null;
            }
        });
        Window win = mTagSortDialog.getWindow();
        ArrayList<String> list = new ArrayList<String>();
        boolean isNeedRecoverFromLast = false;

        switch (tagIndex) {
            case 0:
                if (ll_dialog_wheelview_two0.getVisibility() == View.VISIBLE) {
                    ll_dialog_wheelview_two0.setVisibility(View.GONE);
                    layoutIndex = -1;
                } else {
                    closeOtherWheelView(layoutIndex);//关闭其他已经打开的选择框
                    // 价格
                    list.clear();
                    for (int i = 0; i < 6; i++) {
                        list.add(i * 100 + "");
                    }
                    WheelView mWheelViewL = (WheelView) findViewById(R.id.wheelview_start_modelPriceWheelView);
                    mWheelViewL
                            .setData(CST_Wheel_Data
                                    .getListDatas(CST_Wheel_Data.WheelType.priceChushouStart), CustomUtils.getWindowWidth(this));
                    mWheelViewL.setSelectItem(0);
                    mWheelViewL.setEnable(true);
                    WheelView mWheelViewT = (WheelView) findViewById(R.id.wheelview_end_modelPriceWheelView);
                    mWheelViewT
                            .setData(CST_Wheel_Data
                                    .getListDatas(CST_Wheel_Data.WheelType.priceChushouEnd), CustomUtils.getWindowWidth(this));
                    mWheelViewT.setSelectItem(0);
                    mWheelViewT.setEnable(true);

                    Button btnOk = (Button) findViewById(R.id.btn_submit_modelPriceWheelView);
                    btnOk.setOnClickListener(this);

                    View btnBack = findViewById(R.id.backView_dialogPriceWheelview);
                    btnBack.setOnClickListener(this);

                    ll_dialog_wheelview_two0.setVisibility(View.VISIBLE);
                    layoutIndex = 0;
                }

                break;
            case 1:
                if (ll_dialog_wheelview_two1.getVisibility() == View.VISIBLE) {
                    ll_dialog_wheelview_two1.setVisibility(View.GONE);
                    layoutIndex = -1;
                } else {
                    closeOtherWheelView(layoutIndex);//关闭其他已经打开的选择框
                    // 面积
                    list.clear();
                    for (int i = 0; i < 6; i++) {
                        list.add(i * 100 + "");
                    }
                    WheelView mWheelViewL11 = (WheelView) findViewById(R.id.wheelview_start_modelTwoWheelView);
                    mWheelViewL11.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.squareStart), CustomUtils.getWindowWidth(this) / 2 - 5);
                    mWheelViewL11.setEnable(true);

                    WheelView mWheelViewT1 = (WheelView) findViewById(R.id.wheelview_end_modelTwoWheelView);
                    mWheelViewT1.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.squareEnd), CustomUtils.getWindowWidth(this) / 2 - 5);
                    mWheelViewT1.setEnable(true);
                    // 初始化位置
                    mWheelViewL11.setSelectItem(0);
                    mWheelViewT1.setSelectItem(0);

                    Button btnOk1 = (Button) findViewById(R.id.btn_submit_modelTwoWheelView);
                    btnOk1.setOnClickListener(this);
                    View btnBack1 = findViewById(R.id.backView_dialogTwoWheelview);
                    btnBack1.setOnClickListener(this);
                    ((TextView) findViewById(R.id.tv_startTitle_modelTwoWheelView))
                            .setText(R.string.minarea);
                    ((TextView) findViewById(R.id.tv_endTitle_modelTwoWheelView))
                            .setText(R.string.maxarea);
                    ll_dialog_wheelview_two1.setVisibility(View.VISIBLE);
                    layoutIndex = 1;
                }

                break;
            case 2:
                if (ll_dialog_wheelview_two2.getVisibility() == View.VISIBLE) {
                    ll_dialog_wheelview_two2.setVisibility(View.GONE);
                    layoutIndex = -1;
                } else {
                    closeOtherWheelView(layoutIndex);
                    // 户型
                    list.clear();
                    for (int i = 0; i < 6; i++) {
                        list.add(i + "");
                    }
//                    String[] strs = mFrame[mCurrentPageIndex].split("-");
                    WheelView mWheelView1 = (WheelView) findViewById(R.id.wheelview_first_modelFourWheelView);
                    mWheelView1.setData(CST_Wheel_Data
                            .getListDatas(CST_Wheel_Data.WheelType.huXing), CustomUtils.getWindowWidth(this) / 9);
                    mWheelView1.setEnable(true);
                    WheelView mWheelView2 = (WheelView) findViewById(R.id.wheelview_second_modelFourWheelView);
                    mWheelView2.setData(CST_Wheel_Data
                            .getListDatas(CST_Wheel_Data.WheelType.huXing), CustomUtils.getWindowWidth(this) / 9);
                    mWheelView2.setEnable(true);
                    WheelView mWheelView3 = (WheelView) findViewById(R.id.wheelview_third_modelFourWheelView);
                    mWheelView3.setData(CST_Wheel_Data
                            .getListDatas(CST_Wheel_Data.WheelType.huXing), CustomUtils.getWindowWidth(this) / 9);
                    mWheelView3.setEnable(true);
                    WheelView mWheelView4 = (WheelView) findViewById(R.id.wheelview_forth_modelFourWheelView);
                    mWheelView4.setData(CST_Wheel_Data
                            .getListDatas(CST_Wheel_Data.WheelType.huXing), CustomUtils.getWindowWidth(this) / 9);
                    mWheelView4.setEnable(true);
                    /*if (isNeedRecoverFromLast) {
                        mWheelView1.setSelectText(strs[0], 0);
                        mWheelView2.setSelectText(strs[1], 0);
                        mWheelView3.setSelectText(strs[2], 0);
                        mWheelView4.setSelectText(strs[3], 0);
                    } else {*/
                    mWheelView1.setSelectItem(0);
                    mWheelView2.setSelectItem(0);
                    mWheelView3.setSelectItem(0);
                    mWheelView4.setSelectItem(0);
//                    }
                    win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

                    Button btnOk2 = (Button) findViewById(R.id.btn_submit_modelFourWheelView);
                    btnOk2.setOnClickListener(this);

                    View btnBack2 = findViewById(R.id.backView_dialogFourWheelView);
                    btnBack2.setOnClickListener(this);
                    ll_dialog_wheelview_two2.setVisibility(View.VISIBLE);
                    layoutIndex = 2;
                }

                break;
            case 3:
                if (ll_dialog_wheelview_two3.getVisibility() == View.VISIBLE) {
                    ll_dialog_wheelview_two3.setVisibility(View.GONE);
                    layoutIndex = -1;
                } else {
                    closeOtherWheelView(layoutIndex);
                    // 标签
                    CustomGridView gridView = (CustomGridView) findViewById(R.id.gridView_dialogTagSelector);
                    gridView.setColumnWidth((mIntScreenWidthHeight[0] - 30) / 4);
                    Button btnSubmit = (Button) findViewById(R.id.btn_submit_dialogTagSelector);
                    View backView = findViewById(R.id.backView_dialogTagSelector);
                    backView.setOnClickListener(this);
                    List<String> listTags = new ArrayList<String>();
                    /*if (isNeedRecoverFromLast) {
//                        String[] strTags = mTags[mCurrentPageIndex].split("-");
                        for (int i = 0; i < strTags.length; i++) {
                            listTags.add(strTags[i]);
                        }
                    }*/
                    mHouseTagAdapter = new GridViewAdapter(mContext,
                            CST_Wheel_Data
                                    .getListDatas(CST_Wheel_Data.WheelType.biaoQian),
                            listTags);
                    gridView.setAdapter(mHouseTagAdapter);
                    btnSubmit.setOnClickListener(this);
                    win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                    gridView.setItemChecked(0, true);

                    ll_dialog_wheelview_two3.setVisibility(View.VISIBLE);
                    layoutIndex = 3;
                }

                break;
            case 4:
                if (ll_dialog_wheelview_two4.getVisibility() == View.VISIBLE) {
                    ll_dialog_wheelview_two4.setVisibility(View.GONE);
                    layoutIndex = -1;
                } else {
                    closeOtherWheelView(layoutIndex);
                    // 类型
                    WheelView mWheelViewOne = (WheelView) findViewById(R.id.wheelview_modelOneWheelView);
                    mWheelViewOne.setData(CST_Wheel_Data
                            .getListDatas(CST_Wheel_Data.WheelType.louXing), CustomUtils.getWindowWidth(this));
                    mWheelViewOne.setEnable(true);
                   /* if (isNeedRecoverFromLast) {
                        mWheelViewOne.setSelectText(mUserType[mCurrentPageIndex], 0);
                    } else {*/
                    mWheelViewOne.setSelectItem(0);
//                    }
                    Button btnOkOne = (Button) findViewById(R.id.btn_submit_modelOneWheelView);
                    btnOkOne.setOnClickListener(this);

                    View backOne = findViewById(R.id.backView_dialogOneWheelview);
                    backOne.setOnClickListener(this);
                    ll_dialog_wheelview_two4.setVisibility(View.VISIBLE);
                    layoutIndex = 4;
                }
                break;

            default:
                break;
        }
//		mTagSortDialog.show();

    }


    public void onBack() {

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerWeiXin() {
        // 通过WXAPIFactory工厂获取实例
        AppInstance.mWXAPI = WXAPIFactory.createWXAPI(mContext, Weixin_APP_ID,
                true);
        // 将应用的appid注册到微信
        AppInstance.mWXAPI.registerApp(Weixin_APP_ID);
    }

    private void addLinearLayout() {

        ll_dialog_wheelview_two0 = (LinearLayout) baseView.findViewById(R.id.ll_dialog_wheelview_two0);
        ll_dialog_wheelview_two1 = (LinearLayout) findViewById(R.id.ll_dialog_wheelview_two1);
        ll_dialog_wheelview_two2 = (LinearLayout) findViewById(R.id.ll_dialog_wheelview_two2);
        ll_dialog_wheelview_two3 = (LinearLayout) findViewById(R.id.ll_dialog_wheelview_two3);
        ll_dialog_wheelview_two4 = (LinearLayout) findViewById(R.id.ll_dialog_wheelview_two4);
        layoutList = new ArrayList<LinearLayout>();
        layoutList.add(ll_dialog_wheelview_two0);
        layoutList.add(ll_dialog_wheelview_two1);
        layoutList.add(ll_dialog_wheelview_two2);
        layoutList.add(ll_dialog_wheelview_two3);
        layoutList.add(ll_dialog_wheelview_two4);
    }

    private void closeOtherWheelView(int i) {
        if (i >= 0) {
            layoutList.get(i).setVisibility(View.GONE);
            this.layoutIndex = -1;
        }
    }
}
