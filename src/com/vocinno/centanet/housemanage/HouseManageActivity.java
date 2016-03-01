package com.vocinno.centanet.housemanage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.AppInstance;
import com.vocinno.centanet.apputils.SuperSlideMenuFragmentActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.cst.CST_Wheel_Data;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.apputils.selfdefineview.scrolltagviewradio.ScrollTagView;
import com.vocinno.centanet.apputils.selfdefineview.scrolltagviewradio.ScrollTagViewAdapter;
import com.vocinno.centanet.apputils.selfdefineview.scrolltagviewradio.onScrollTagViewChangeListener;
import com.vocinno.centanet.model.EstateSearchItem;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

/**
 * 房源管理
 * 
 * @author Administrator
 * 
 */
public class HouseManageActivity extends SuperSlideMenuFragmentActivity {
	private boolean isInitView = false;

	private enum PaiXuType {
		None, mTvAreaSortUp, mTvAreaSortDown, mTvPriceSortUp, mTvPriceSortDown
	}

	// 公房列表容器
	private LinearLayout mLlytPublicContainer;
	// ViewPager 分页参数
	public ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private int mType = HouseType.WO_DE; // 默认是我的房源(包括出租、出售、约看、我的)，否则认为是公房列表或钥匙房源列表
	private int mCurrentPageIndex = getPageIndexFromType(mType);
	private int[] mFragmentTagIndexs = { -1, -1, -1, -1, -1, -1 };

	// 每一页，一共四页
	private FourKindsHouseFragment[] mArrayFragments = { null, null, null,
			null, null, null };
	public List[] mArrayHouseItemList = { null, null, null, null, null, null };
	public int[] mPageIndexs = { 1, 1, 1, 1, 1, 1 };

	// 标题栏按钮
	private View mViewBack, mViewMore;
	private ScrollTagView mScrollTagView;
	private ScrollTagViewAdapter mScrollTagViewAdapter;
	//
	private Dialog mMenuDialog, mSearchDialog, mTagSortDialog;
	private List<String> mHistorySearch;
	private ListView mListView;
	private int[] mIntScreenWidthHeight = { 0, 0 };

	private GridViewAdapter mHouseTagAdapter;
	// 用于排序以及筛选的条件参数
	private PaiXuType mPaiXuType = PaiXuType.None;
	private String[] mPrice = { null, null, null, null, null, null };// 价格
	private String[] mSquare = { null, null, null, null, null, null };// 面积
	private String[] mFrame = { null, null, null, null, null, null };// 户型
	private String[] mTags = { null, null, null, null, null, null };// 标签
	private String[] mUserType = { null, null, null, null, null, null };// 类型
	private List<EstateSearchItem> mSearchListData;
	private TextView mTvAreaSort, mTvPriceSort;
	private Drawable drawable;

	private static final String Weixin_APP_ID = "wx52560d39a9b47eae";

	@SuppressLint("HandlerLeak")
	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				HouseManageActivity.this.closeMenu(msg);
				switch (msg.what) {
				case R.id.FINISH_LOAD_ALL_DATA:
					int intType = (int) msg.obj;
					if (mCurrentPageIndex == getPageIndexFromType(intType)) {
						mArrayFragments[mCurrentPageIndex]
								.notifyDatasetChanged();
					}
					break;
				// case R.id.FINISH_LOAD_MORE:
				// .setPullLoadEnable(false);
				default:
					break;
				}
			}
		};
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
	public int setContentLayoutId() {
		return R.layout.activity_house_manage;
	}

	public void switchCurrentPageIndex(int houseType) {
		mCurrentPageIndex = getPageIndexFromType(houseType);
	}

	@Override
	public void initView() {
		AppInstance.mHouseManageActivity = this;
		if (MethodsDeliverData.mIntHouseType != HouseType.NONE) {
			mType = MethodsDeliverData.mIntHouseType;
		}
		mCurrentPageIndex = getPageIndexFromType(mType);
		mViewBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mViewMore = MethodsExtra.findHeadRightView1(mContext, mRootView, 0, 0);
		mScrollTagView = (ScrollTagView) mRootView
				.findViewById(R.id.scrolltag_houseManage_houseManageActivity);
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
						}
					}
				});
		mViewPager = (ViewPager) mRootView.findViewById(R.id.viewpager);
		if (mType == HouseType.GONG_FANG) {
			// 公房
			MethodsDeliverData.mIntHouseType = HouseType.NONE;
			MethodsExtra.findHeadTitle1(mContext, mRootView,
					R.string.house_public, null);
			mViewPager.setVisibility(View.GONE);
			mLlytPublicContainer = (LinearLayout) mRootView
					.findViewById(R.id.llyt_containerForFragment_houseManageActivity);
			mLlytPublicContainer.setVisibility(View.VISIBLE);

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			FourKindsHouseFragment fragment = new FourKindsHouseFragment(
					mCurrentPageIndex);
			fragmentTransaction.add(
					R.id.llyt_containerForFragment_houseManageActivity,
					fragment);
			fragmentTransaction.commit();
			mArrayFragments[mCurrentPageIndex] = fragment;
			resetSearchCondation(mCurrentPageIndex);
		} else if (mType == HouseType.YAO_SHI) {
			// 钥匙列表
			MethodsDeliverData.mIntHouseType = HouseType.NONE;
			MethodsExtra.findHeadTitle1(mContext, mRootView, 0, "钥匙房源管理");
			mViewPager.setVisibility(View.GONE);
			mLlytPublicContainer = (LinearLayout) mRootView
					.findViewById(R.id.llyt_containerForFragment_houseManageActivity);
			mLlytPublicContainer.setVisibility(View.VISIBLE);
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			FourKindsHouseFragment fragment = new FourKindsHouseFragment(
					mCurrentPageIndex);
			fragmentTransaction.add(
					R.id.llyt_containerForFragment_houseManageActivity,
					fragment);
			fragmentTransaction.commit();
			mArrayFragments[mCurrentPageIndex] = fragment;
			resetSearchCondation(mCurrentPageIndex);
		} else if (mType == HouseType.CHU_SHOU || mType == HouseType.CHU_ZU
				|| mType == HouseType.YUE_KAN || mType == HouseType.WO_DE) {
			if (isInitView == false) {
				initViewPager();
				isInitView = true;
			}
			switch (mType) {
			case HouseType.CHU_ZU:
				// 出租
				mType = HouseType.CHU_ZU;
				MethodsExtra.findHeadTitle1(mContext, mRootView,
						R.string.house_chuzu, null);
				break;
			case HouseType.CHU_SHOU:
				// 出售
				mType = HouseType.CHU_SHOU;
				MethodsExtra.findHeadTitle1(mContext, mRootView,
						R.string.house_chushou, null);
				break;
			case HouseType.YUE_KAN:
				// 预约
				mType = HouseType.YUE_KAN;
				MethodsExtra.findHeadTitle1(mContext, mRootView,
						R.string.house_yuekan, null);
				break;
			case HouseType.WO_DE:
				// 我的
				mType = HouseType.WO_DE;
				MethodsExtra.findHeadTitle1(mContext, mRootView,
						R.string.house_my, null);
				break;
			default:
				break;
			}
			mViewPager.setVerticalScrollbarPosition(mCurrentPageIndex);
		}
	}

	public void switchHouseType(int houseType) {
		switch (houseType) {
		case HouseType.CHU_ZU:
			// 出租
			mType = HouseType.CHU_ZU;
			MethodsExtra.findHeadTitle1(mContext, mRootView,
					R.string.house_chuzu, null);
			break;
		case HouseType.CHU_SHOU:
			// 出售
			mType = HouseType.CHU_SHOU;
			MethodsExtra.findHeadTitle1(mContext, mRootView,
					R.string.house_chushou, null);
			break;
		case HouseType.YUE_KAN:
			// 预约
			mType = HouseType.YUE_KAN;
			MethodsExtra.findHeadTitle1(mContext, mRootView,
					R.string.house_yuekan, null);
			break;
		case HouseType.WO_DE:
			// 我的
			mType = HouseType.WO_DE;
			MethodsExtra.findHeadTitle1(mContext, mRootView, R.string.house_my,
					null);
			break;
		default:
			break;
		}
		mCurrentPageIndex = getPageIndexFromType(mType);
		mViewPager.setCurrentItem(mCurrentPageIndex);
		// 调用数据
		if (mArrayHouseItemList[mCurrentPageIndex] == null) {
			// 获取数据
			getDataFromNetwork(mType, 1);
		} else {
			callData();
			mArrayFragments[mCurrentPageIndex].notifyDatasetChanged();
		}
		mScrollTagView.selectedTab1(mFragmentTagIndexs[mCurrentPageIndex],
				false, false);
	}

	@Override
	public void setListener() {
		mViewBack.setOnClickListener(this);
		mViewMore.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switchHouseType(getTypeFromPageIndex(arg0));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	// pageIndex 转 type
	public static int getTypeFromPageIndex(int pageIndex) {
		int type = -1;
		switch (pageIndex) {
		case 0:
			type = HouseType.CHU_SHOU;
			break;
		case 1:
			type = HouseType.CHU_ZU;
			break;
		case 2:
			type = HouseType.YUE_KAN;
			break;
		case 3:
			type = HouseType.WO_DE;
			break;
		case 4:
			type = HouseType.GONG_FANG;
			break;
		case 5:
			type = HouseType.YAO_SHI;
			break;
		default:
			break;
		}
		return type;
	}

	// type 转 pageIndex
	public static int getPageIndexFromType(int type) {
		int pageIndex = -1;
		switch (type) {
		case HouseType.CHU_ZU:
			pageIndex = 1;
			break;
		case HouseType.CHU_SHOU:
			pageIndex = 0;
			break;
		case HouseType.YUE_KAN:
			pageIndex = 2;
			break;
		case HouseType.WO_DE:
			pageIndex = 3;
			break;
		case HouseType.GONG_FANG:
			pageIndex = 4;
			break;
		case HouseType.YAO_SHI:
			pageIndex = 5;
			break;
		default:
			break;
		}
		return pageIndex;
	}

	void resetSearchCondation(int index) {
		mPrice[index] = "0-不限";
		mSquare[index] = "0-不限";
		mFrame[index] = "不限-不限-不限-不限";
		mTags[index] = "";
		mUserType[index] = ""; // 所有类型
	}

	private void callData() {
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
				CST_JS.JS_Function_HouseResource_getList, CST_JS
						.getJsonStringForHouseListGetList("" + mType,
								mPrice[mCurrentPageIndex],
								mSquare[mCurrentPageIndex],
								mFrame[mCurrentPageIndex],
								mTags[mCurrentPageIndex],
								mUserType[mCurrentPageIndex], 1, 20, "", "",
								"", ""));
	}

	@Override
	public void initData() {
		mIntScreenWidthHeight = MethodsData.getScreenWidthHeight(mContext);
		// 注册通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_HOU_LIST_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_HOU_LIST_SEARCH_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_HOU_LIST_INMAP_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_HOU_LIST_CLICK_MAP_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_RESULT, TAG);
		getDataFromNetwork(mType, mPageIndexs[mCurrentPageIndex]);
		registerWeiXin();
	}

	void initViewPager() {
		mPagerAdapter = new PagerAdapter(mContext.getSupportFragmentManager());
		// 将数据进行分类并分别传入每一个FourKindsHouseFragment中进行使用
		for (int i = 0; i <= 3; i++) {
			FourKindsHouseFragment fragment = new FourKindsHouseFragment(i);
			mArrayFragments[i] = fragment;
			mPagerAdapter.addFragment(fragment);
			resetSearchCondation(i);
		}
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(mCurrentPageIndex);
	}

	// tag点击后调用
	public void selectTag(int currentIndex) {
		showTagSortDialog(currentIndex);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_searchinmap_HouseManageActivity:
			mMenuDialog.dismiss();
			if (mType == HouseType.CHU_SHOU) {
				// 出售
				MapActivity.mDelType = "s";
			} else if (mType == HouseType.CHU_SHOU) {
				// 出租
				MapActivity.mDelType = "r";
			}
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
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"price", "asc", "", ""));
			} else if (mPaiXuType == PaiXuType.mTvPriceSortDown) {
				mPaiXuType = PaiXuType.mTvPriceSortUp;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"price", "asc", "", ""));

			} else if (mPaiXuType == PaiXuType.mTvPriceSortUp) {
				mPaiXuType = PaiXuType.mTvPriceSortDown;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"price", "desc", "", ""));

			} else {
				mPaiXuType = PaiXuType.mTvPriceSortUp;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"price", "asc", "", ""));
			}
			mSearchDialog.dismiss();
			break;
		case R.id.tv_sortArea_HouseManageActivity:
			// 按照价面积排序
			if (mPaiXuType == PaiXuType.None) {
				mPaiXuType = PaiXuType.mTvAreaSortUp;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"acre", "asc", "", ""));
			} else if (mPaiXuType == PaiXuType.mTvAreaSortUp) {
				mPaiXuType = PaiXuType.mTvAreaSortDown;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"acre", "desc", "", ""));
			} else if (mPaiXuType == PaiXuType.mTvAreaSortDown) {
				mPaiXuType = PaiXuType.mTvAreaSortUp;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"acre", "asc", "", ""));
			} else {
				mPaiXuType = PaiXuType.mTvAreaSortUp;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"acre", "asc", "", ""));
			}
			mSearchDialog.dismiss();
			break;
		case R.id.btn_submit_modelOneWheelView:
			// 类型筛选（没有接口）
			WheelView mWheelViewOne = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_modelOneWheelView);
			// mUserType = CST_Wheel_Data.getCodeForLouXing(mWheelViewOne
			// .getSelectedText());//此处改为直接传递选中的参数(汉字)
			mUserType[mCurrentPageIndex] = mWheelViewOne.getSelectedText()
					.replace("全部类型", "");
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_getList, CST_JS
							.getJsonStringForHouseListGetList(mType + "",
									mPrice[mCurrentPageIndex],
									mSquare[mCurrentPageIndex],
									mFrame[mCurrentPageIndex],
									mTags[mCurrentPageIndex],
									mUserType[mCurrentPageIndex], 1, 20, "",
									"", "", ""));
			mTagSortDialog.dismiss();
			break;
		case R.id.btn_submit_modelTwoWheelView:
			WheelView wheelStart = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_start_modelTwoWheelView);
			WheelView wheelEnd = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_end_modelTwoWheelView);
			if (mFragmentTagIndexs[mCurrentPageIndex] == 0) {
				String startString = wheelStart.getSelectedText().split("万")[0];
				String endString = wheelEnd.getSelectedText().trim()
						.equals("不限") ? wheelEnd.getSelectedText().trim()
						: wheelEnd.getSelectedText().split("万")[0];
				if (mType != HouseType.CHU_ZU) {
					startString += "0000";
					if (!"不限".equals(endString)) {
						endString += "0000";
					}
				}
				mPrice[mCurrentPageIndex] = startString.trim()
						.replaceAll("万", "").replaceAll("元", "")
						+ "-"
						+ endString.trim().replaceAll("万", "")
								.replaceAll("元", "");
				if (endString.trim().equals("不限")
						|| Integer.parseInt(startString.trim()
								.replaceAll("万", "").replaceAll("元", "")) <= Integer
								.parseInt(endString.trim().replaceAll("万", "")
										.replaceAll("元", ""))) {
					MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
							CST_JS.JS_Function_HouseResource_getList, CST_JS
									.getJsonStringForHouseListGetList(mType
											+ "", mPrice[mCurrentPageIndex],
											mSquare[mCurrentPageIndex],
											mFrame[mCurrentPageIndex],
											mTags[mCurrentPageIndex],
											mUserType[mCurrentPageIndex], 1,
											20, "", "", "", ""));
					mTagSortDialog.dismiss();
				} else {
					MethodsExtra.toast(mContext, "最高价格不能小于最低价格");
				}
			} else if (mFragmentTagIndexs[mCurrentPageIndex] == 1) {
				String startString = wheelStart.getSelectedText().split("平米")[0]
						+ "";
				String endString = wheelEnd.getSelectedText().split("平米")[0]
						+ "";
				mSquare[mCurrentPageIndex] = startString + "-" + endString;
				if (endString.equals("不限")
						|| Integer.parseInt(startString.trim()) <= Integer
								.parseInt(endString.trim())) {
					MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
							CST_JS.JS_Function_HouseResource_getList, CST_JS
									.getJsonStringForHouseListGetList(mType
											+ "", mPrice[mCurrentPageIndex],
											mSquare[mCurrentPageIndex],
											mFrame[mCurrentPageIndex],
											mTags[mCurrentPageIndex],
											mUserType[mCurrentPageIndex], 1,
											20, "", "", "", ""));
					mTagSortDialog.dismiss();
				} else {
					MethodsExtra.toast(mContext, "最大面积不能小于最小面积");
				}

			} else if (mFragmentTagIndexs[mCurrentPageIndex] == 2) {
				String startString = wheelStart.getSelectedText().toString();
				String endString = wheelEnd.getSelectedText().toString();
				mFrame[mCurrentPageIndex] = startString + "-" + endString;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"", "", "", ""));
				mTagSortDialog.dismiss();
			} else if (mFragmentTagIndexs[mCurrentPageIndex] == 3) {
				String startString = wheelStart.getSelectedText().toString();
				String endString = wheelEnd.getSelectedText().toString();
				mTags[mCurrentPageIndex] = startString + "," + endString;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, CST_JS
								.getJsonStringForHouseListGetList(mType + "",
										mPrice[mCurrentPageIndex],
										mSquare[mCurrentPageIndex],
										mFrame[mCurrentPageIndex],
										mTags[mCurrentPageIndex],
										mUserType[mCurrentPageIndex], 1, 20,
										"", "", "", ""));
				mTagSortDialog.dismiss();
			}
			break;
		case R.id.btn_submit_modelFourWheelView:
			WheelView mWheelView1 = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_first_modelFourWheelView);
			WheelView mWheelView2 = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_second_modelFourWheelView);
			WheelView mWheelView3 = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_third_modelFourWheelView);
			WheelView mWheelView4 = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_forth_modelFourWheelView);

			mFrame[mCurrentPageIndex] = mWheelView1.getSelectedText() + "-"
					+ mWheelView2.getSelectedText() + "-"
					+ mWheelView3.getSelectedText() + "-"
					+ mWheelView4.getSelectedText();
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_getList, CST_JS
							.getJsonStringForHouseListGetList(mType + "",
									mPrice[mCurrentPageIndex],
									mSquare[mCurrentPageIndex],
									mFrame[mCurrentPageIndex],
									mTags[mCurrentPageIndex],
									mUserType[mCurrentPageIndex], 1, 20, "",
									"", "", ""));
			mTagSortDialog.dismiss();
			break;
		case R.id.btn_submit_dialogTagSelector:
			// 标签
			mTags[mCurrentPageIndex] = mHouseTagAdapter.getSelectedTags();
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_getList, CST_JS
							.getJsonStringForHouseListGetList(mType + "",
									mPrice[mCurrentPageIndex],
									mSquare[mCurrentPageIndex],
									mFrame[mCurrentPageIndex],
									mTags[mCurrentPageIndex],
									mUserType[mCurrentPageIndex], 1, 20, "",
									"", "", ""));
			mTagSortDialog.dismiss();
			break;
		case R.id.backView_dialogOneWheelview:
			mTagSortDialog.dismiss();
			break;
		case R.id.backView_dialogTwoWheelview:
			mTagSortDialog.dismiss();
			break;
		case R.id.backView_dialogFourWheelView:
			mTagSortDialog.dismiss();
			break;
		case R.id.backView_dialogTagSelector:
			mTagSortDialog.dismiss();
			break;
		case R.id.img_left_mhead1:
			onBack();
			break;
		case R.id.img_right_mhead1:
			showMenuDialog();
			break;
		default:
			break;
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

		if (mType != HouseType.CHU_SHOU && mType != HouseType.CHU_ZU) {
			mTvSearchInMap.setVisibility(View.GONE);
		} else {
			mTvSearchInMap.setVisibility(View.VISIBLE);
		}
	}

	private void showSearchDialog() {
		mSearchDialog = new Dialog(mContext, R.style.Theme_dialog);
		mSearchDialog.setContentView(R.layout.dialog_search_house_manage);
		Window win = mSearchDialog.getWindow();
		win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		win.setGravity(Gravity.TOP);
		mSearchDialog.setCanceledOnTouchOutside(true);
		mListView = (ListView) mSearchDialog
				.findViewById(R.id.lv_historySearch_dialogSearchHouseManage);
		SearchAdapter mSearch = new SearchAdapter(mContext,
				new ArrayList<EstateSearchItem>());
		mListView.setAdapter(mSearch);

		final EditText mEtSearch = (EditText) mSearchDialog
				.findViewById(R.id.et_search_dialogSearchHouseManage);
		Button mBtnSearch = (Button) mSearchDialog
				.findViewById(R.id.btn_search_dialogSearchHouseManage);
		TextView mTvAround = (TextView) mSearchDialog
				.findViewById(R.id.tv_around_dialogSearchHouseManage);
		final ListView mLvHostory = (ListView) mSearchDialog
				.findViewById(R.id.lv_historySearch_dialogSearchHouseManage);
		Button mBtnClean = (Button) mSearchDialog
				.findViewById(R.id.btn_close_dialogSearchHouseManage);
		mBtnSearch.setOnClickListener(this);
		mTvAround.setOnClickListener(this);
		mBtnClean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mEtSearch.setText("");
				mLvHostory.setVisibility(View.GONE);
			}
		});
		// 根据mEtSearch得到的字符串去请求

		mEtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				Log.d("on text changed", "true");
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				Log.d("before text changed", "true");
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// 在打字期间添加搜索栏数据
				MethodsJni.callProxyFun(
						CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_searchEstateName,
						CST_JS.getJsonStringForHouseListSearchEstateName(
								arg0.toString(), 1, 20));
				mLvHostory.setVisibility(View.VISIBLE);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int type = mType;
				if (mType == 1) {
					type = 2;
				} else if (mType == 2) {
					type = 1;
				}
				String reqparm = CST_JS
						.getJsonStringForHouseListGetList(type + "",
								mPrice[mCurrentPageIndex],
								mSquare[mCurrentPageIndex],
								mFrame[mCurrentPageIndex],
								mTags[mCurrentPageIndex],
								mUserType[mCurrentPageIndex], 1, 20, "", "",
								mSearchListData.get(arg2).getSearchId() + "",
								mSearchListData.get(arg2).getSearchType());
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, reqparm);
				mSearchDialog.dismiss();
			}

		});
		// 然后填充入listView
		if (mHistorySearch != null) {
			mLvHostory.setVisibility(View.VISIBLE);
		}
		mSearchDialog.show();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "wanggsx houseManage onDestroy");
		super.onDestroy();
	}

	private void showTagSortDialog(int tagIndex) {
		mTagSortDialog = new Dialog(mContext, R.style.Theme_dialog_transparent);
		mTagSortDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				mTagSortDialog = null;
			}
		});
		Window win = mTagSortDialog.getWindow();
		ArrayList<String> list = new ArrayList<String>();
		boolean isNeedRecoverFromLast = false;
		if (mFragmentTagIndexs[mCurrentPageIndex] == tagIndex) {
			isNeedRecoverFromLast = true;
		} else {
			mFragmentTagIndexs[mCurrentPageIndex] = tagIndex;
			resetSearchCondation(mCurrentPageIndex);
		}
		switch (tagIndex) {
		case 0:
			// 价格
			list.clear();
			for (int i = 0; i < 6; i++) {
				list.add(i * 100 + "");
			}
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_wheelview_two, null);
			mTagSortDialog.setContentView(view);
			win.setGravity(Gravity.TOP);
			mTagSortDialog.setCanceledOnTouchOutside(false);
			WheelView mWheelViewL = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_start_modelTwoWheelView);
			if (mType == HouseType.CHU_ZU) {
				mWheelViewL
						.setData(CST_Wheel_Data
								.getListDatas(CST_Wheel_Data.WheelType.priceChuzuStart));
				// 初始化位置
				if (isNeedRecoverFromLast) {
					String str = mPrice[mCurrentPageIndex].substring(0,
							mPrice[mCurrentPageIndex].indexOf("-"));
					mWheelViewL.setSelectText(str + "元", 0);
				} else {
					mWheelViewL.setSelectItem(0);
				}
			} else {
				mWheelViewL
						.setData(CST_Wheel_Data
								.getListDatas(CST_Wheel_Data.WheelType.priceChushouStart));
				// 初始化位置
				if (isNeedRecoverFromLast) {
					String str = mPrice[mCurrentPageIndex].substring(0,
							mPrice[mCurrentPageIndex].indexOf("-"));
					mWheelViewL.setSelectText(Integer.parseInt(str) / 10000
							+ "万", 0);
				} else {
					mWheelViewL.setSelectItem(0);
				}
			}
			mWheelViewL.setEnable(true);
			WheelView mWheelViewT = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_end_modelTwoWheelView);
			if (mType == HouseType.CHU_ZU) {
				mWheelViewT.setData(CST_Wheel_Data
						.getListDatas(CST_Wheel_Data.WheelType.priceChuzuEnd));
				// 初始化位置
				if (isNeedRecoverFromLast) {
					String str = mPrice[mCurrentPageIndex]
							.substring(mPrice[mCurrentPageIndex].indexOf("-") + 1);
					mWheelViewT.setSelectText(str + "元",
							mWheelViewT.getListSize() - 1);
				} else {
					// mWheelViewT.setSelectItem(mWheelViewT.getListSize() - 1);
					mWheelViewT.setSelectItem(0);
				}
			} else {
				mWheelViewT
						.setData(CST_Wheel_Data
								.getListDatas(CST_Wheel_Data.WheelType.priceChushouEnd));
				// 初始化位置
				if (isNeedRecoverFromLast) {
					String str = mPrice[mCurrentPageIndex]
							.substring(mPrice[mCurrentPageIndex].indexOf("-") + 1);
					mWheelViewT.setSelectText(Integer.parseInt(str) / 10000
							+ "万", mWheelViewT.getListSize() - 1);
				} else {
					// mWheelViewT.setSelectItem(mWheelViewT.getListSize() - 1);
					mWheelViewT.setSelectItem(0);
				}
			}
			mWheelViewT.setEnable(true);
			win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			Button btnOk = (Button) mTagSortDialog
					.findViewById(R.id.btn_submit_modelTwoWheelView);
			btnOk.setOnClickListener(this);

			View btnBack = mTagSortDialog
					.findViewById(R.id.backView_dialogTwoWheelview);
			btnBack.setOnClickListener(this);
			break;
		case 1:
			// 面积
			list.clear();
			for (int i = 0; i < 6; i++) {
				list.add(i * 100 + "");
			}
			View view1 = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_wheelview_two, null);
			mTagSortDialog.setContentView(view1);
			win.setGravity(Gravity.TOP);
			mTagSortDialog.setCanceledOnTouchOutside(false);
			WheelView mWheelViewL1 = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_start_modelTwoWheelView);
			mWheelViewL1.setData(CST_Wheel_Data
					.getListDatas(CST_Wheel_Data.WheelType.squareStart));
			mWheelViewL1.setEnable(true);
			WheelView mWheelViewT1 = (WheelView) mTagSortDialog
					.findViewById(R.id.wheelview_end_modelTwoWheelView);
			mWheelViewT1.setData(CST_Wheel_Data
					.getListDatas(CST_Wheel_Data.WheelType.squareEnd));
			mWheelViewT1.setEnable(true);
			// 初始化位置
			if (isNeedRecoverFromLast) {
				String str = mSquare[mCurrentPageIndex].substring(0,
						mSquare[mCurrentPageIndex].indexOf("-"));
				mWheelViewL1.setSelectText(str + "平米", 0);
			} else {
				mWheelViewL1.setSelectItem(0);
			}
			if (isNeedRecoverFromLast) {
				String str = mSquare[mCurrentPageIndex]
						.substring(mSquare[mCurrentPageIndex].indexOf("-") + 1);
				mWheelViewT1.setSelectText(str + "平米",
						mWheelViewT1.getListSize() - 1);
			} else {
				// mWheelViewT1.setSelectItem(mWheelViewT1.getListSize() - 1);
				mWheelViewT1.setSelectItem(0);
			}
			win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			Button btnOk1 = (Button) mTagSortDialog
					.findViewById(R.id.btn_submit_modelTwoWheelView);
			btnOk1.setOnClickListener(this);

			View btnBack1 = mTagSortDialog
					.findViewById(R.id.backView_dialogTwoWheelview);
			btnBack1.setOnClickListener(this);
			((TextView) view1
					.findViewById(R.id.tv_startTitle_modelTwoWheelView))
					.setText(R.string.minarea);
			((TextView) view1.findViewById(R.id.tv_endTitle_modelTwoWheelView))
					.setText(R.string.maxarea);
			break;
		case 2:
			// 户型
			list.clear();
			for (int i = 0; i < 6; i++) {
				list.add(i + "");
			}
			View view2 = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_wheelview_four, null);
			mTagSortDialog.setContentView(view2);
			win.setGravity(Gravity.TOP);
			mTagSortDialog.setCanceledOnTouchOutside(false);
			String[] strs = mFrame[mCurrentPageIndex].split("-");
			WheelView mWheelView1 = (WheelView) view2
					.findViewById(R.id.wheelview_first_modelFourWheelView);
			mWheelView1.setData(CST_Wheel_Data
					.getListDatas(CST_Wheel_Data.WheelType.huXing));
			mWheelView1.setEnable(true);
			WheelView mWheelView2 = (WheelView) view2
					.findViewById(R.id.wheelview_second_modelFourWheelView);
			mWheelView2.setData(CST_Wheel_Data
					.getListDatas(CST_Wheel_Data.WheelType.huXing));
			mWheelView2.setEnable(true);
			WheelView mWheelView3 = (WheelView) view2
					.findViewById(R.id.wheelview_third_modelFourWheelView);
			mWheelView3.setData(CST_Wheel_Data
					.getListDatas(CST_Wheel_Data.WheelType.huXing));
			mWheelView3.setEnable(true);
			WheelView mWheelView4 = (WheelView) view2
					.findViewById(R.id.wheelview_forth_modelFourWheelView);
			mWheelView4.setData(CST_Wheel_Data
					.getListDatas(CST_Wheel_Data.WheelType.huXing));
			mWheelView4.setEnable(true);
			if (isNeedRecoverFromLast) {
				mWheelView1.setSelectText(strs[0], 0);
				mWheelView2.setSelectText(strs[1], 0);
				mWheelView3.setSelectText(strs[2], 0);
				mWheelView4.setSelectText(strs[3], 0);
			} else {
				mWheelView1.setSelectItem(0);
				mWheelView2.setSelectItem(0);
				mWheelView3.setSelectItem(0);
				mWheelView4.setSelectItem(0);
			}
			win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			Button btnOk2 = (Button) view2
					.findViewById(R.id.btn_submit_modelFourWheelView);
			btnOk2.setOnClickListener(this);

			View btnBack2 = view2
					.findViewById(R.id.backView_dialogFourWheelView);
			btnBack2.setOnClickListener(this);
			break;
		case 3:
			// 标签
			View view3 = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_tag_selector, null);
			mTagSortDialog.setContentView(view3);
			win.setGravity(Gravity.TOP);
			mTagSortDialog.setCanceledOnTouchOutside(false);
			GridView gridView = (GridView) view3
					.findViewById(R.id.gridView_dialogTagSelector);
			gridView.setColumnWidth((mIntScreenWidthHeight[0] - 30) / 4);
			Button btnSubmit = (Button) view3
					.findViewById(R.id.btn_submit_dialogTagSelector);
			View backView = view3.findViewById(R.id.backView_dialogTagSelector);
			List<String> listTags = new ArrayList<String>();
			if (isNeedRecoverFromLast) {
				String[] strTags = mTags[mCurrentPageIndex].split("-");
				for (int i = 0; i < strTags.length; i++) {
					listTags.add(strTags[i]);
				}
			}
			mHouseTagAdapter = new GridViewAdapter(mContext,
					CST_Wheel_Data
							.getListDatas(CST_Wheel_Data.WheelType.biaoQian),
					listTags);
			gridView.setAdapter(mHouseTagAdapter);
			btnSubmit.setOnClickListener(this);
			backView.setOnClickListener(this);
			win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			gridView.setItemChecked(0, true);
			break;
		case 4:
			// 类型
			View view4 = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_wheelview_one, null);
			mTagSortDialog.setContentView(view4);
			win.setGravity(Gravity.TOP);
			mTagSortDialog.setCanceledOnTouchOutside(false);
			WheelView mWheelViewOne = (WheelView) view4
					.findViewById(R.id.wheelview_modelOneWheelView);
			mWheelViewOne.setData(CST_Wheel_Data
					.getListDatas(CST_Wheel_Data.WheelType.louXing));
			mWheelViewOne.setEnable(true);
			if (isNeedRecoverFromLast) {
				mWheelViewOne.setSelectText(mUserType[mCurrentPageIndex], 0);
			} else {
				mWheelViewOne.setSelectItem(0);
			}
			win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			Button btnOkOne = (Button) view4
					.findViewById(R.id.btn_submit_modelOneWheelView);
			btnOkOne.setOnClickListener(this);

			View backOne = view4.findViewById(R.id.backView_dialogOneWheelview);
			backOne.setOnClickListener(this);
			break;
		default:
			break;
		}
		mTagSortDialog.show();

	}

	public class SearchAdapter extends BaseAdapter {

		private Activity mContext;
		private List<EstateSearchItem> mListTexts;

		public SearchAdapter(Activity context, List<EstateSearchItem> listTexts) {
			mContext = context;
			mListTexts = listTexts;
		}

		@Override
		public int getCount() {
			return mListTexts.size();
		}

		@Override
		public Object getItem(int position) {
			return mListTexts.get(position);
		}

		public List<EstateSearchItem> getListData() {
			return mListTexts;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SearchHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.listitem_search_house_manage_dialog, null);
				holder = new SearchHolder();
				holder.mTvSearchText = (TextView) convertView
						.findViewById(R.id.tv_text_listitemSearchHouseManageDialog);
				convertView.setTag(holder);
			} else {
				holder = (SearchHolder) convertView.getTag();
			}
			holder.mTvSearchText.setText(mListTexts.get(position)
					.getSearchName());
			return convertView;
		}

		class SearchHolder {
			TextView mTvSearchText;
		}
	}

	@Override
	public void onBack() {
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

		MethodsJni.removeAllNotifications(TAG);
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

	@Override
	protected void onResume() {
		super.onResume();
		if (MethodsDeliverData.bundle != null) {
			if (MethodsDeliverData.bundle.get("type") == "from_map") {
				String reqparm = CST_JS.getJsonStringForHouseListGetList(mType
						+ "", mPrice[mCurrentPageIndex],
						mSquare[mCurrentPageIndex], mFrame[mCurrentPageIndex],
						mTags[mCurrentPageIndex], mUserType[mCurrentPageIndex],
						1, 20, "", "",
						MethodsDeliverData.bundle.getInt("house_id") + "",
						"estate");
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getList, reqparm);
			}
		} else {
			// callData();
		}
	}

	// 调用数据
	void getDataFromNetwork(int type, int page) {
		if (mPaiXuType == PaiXuType.None) {
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_getList, CST_JS
							.getJsonStringForHouseListGetList("" + type,
									mPrice[mCurrentPageIndex],
									mSquare[mCurrentPageIndex],
									mFrame[mCurrentPageIndex],
									mTags[mCurrentPageIndex],
									mTags[mCurrentPageIndex], page, 20, "", "",
									"", ""));
		} else if (mPaiXuType == PaiXuType.mTvPriceSortDown) {
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_getList, CST_JS
							.getJsonStringForHouseListGetList(type + "",
									mPrice[mCurrentPageIndex],
									mSquare[mCurrentPageIndex],
									mFrame[mCurrentPageIndex],
									mTags[mCurrentPageIndex],
									mUserType[mCurrentPageIndex], page, 20,
									"price", "desc", "", ""));
		} else if (mPaiXuType == PaiXuType.mTvPriceSortUp) {
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_getList, CST_JS
							.getJsonStringForHouseListGetList(type + "",
									mPrice[mCurrentPageIndex],
									mSquare[mCurrentPageIndex],
									mFrame[mCurrentPageIndex],
									mTags[mCurrentPageIndex],
									mUserType[mCurrentPageIndex], page, 20,
									"price", "asc", "", ""));

		} else if (mPaiXuType == PaiXuType.mTvAreaSortUp) {
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_getList, CST_JS
							.getJsonStringForHouseListGetList(type + "",
									mPrice[mCurrentPageIndex],
									mSquare[mCurrentPageIndex],
									mFrame[mCurrentPageIndex],
									mTags[mCurrentPageIndex],
									mUserType[mCurrentPageIndex], page, 20,
									"acre", "asc", "", ""));
		} else if (mPaiXuType == PaiXuType.mTvAreaSortDown) {
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_getList, CST_JS
							.getJsonStringForHouseListGetList(type + "",
									mPrice[mCurrentPageIndex],
									mSquare[mCurrentPageIndex],
									mFrame[mCurrentPageIndex],
									mTags[mCurrentPageIndex],
									mUserType[mCurrentPageIndex], page, 20,
									"acre", "desc", "", ""));
		} else {
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_getList, CST_JS
							.getJsonStringForHouseListGetList("" + type,
									mPrice[mCurrentPageIndex],
									mSquare[mCurrentPageIndex],
									mFrame[mCurrentPageIndex],
									mTags[mCurrentPageIndex],
									mTags[mCurrentPageIndex], page, 20, "", "",
									"", ""));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void notifCallBack(String name, String className, Object data) {
		if (name.equals(CST_JS.NOTIFY_NATIVE_HOU_LIST_RESULT)
				|| name.equals(CST_JS.NOTIFY_NATIVE_HOU_LIST_SEARCH_RESULT)) {
			// 更新
			JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
					HouseList.class);
			int type = Integer.parseInt(jsReturn.getParams().getType());
			int pageIndex = getPageIndexFromType(type);
			Message msg = new Message();
			if (jsReturn.isSuccess()) {
				if (jsReturn.getParams().getIsAppend()) {
					((HouseManageActivity) mContext).mArrayHouseItemList[pageIndex]
							.addAll(jsReturn.getListDatas());

					if (jsReturn.getListDatas() == null
							|| jsReturn.getListDatas().size() == 0) {
						// 没有更多数据：
						mArrayFragments[pageIndex].mLvHouseList
								.setPullLoadEnable(false);
					}
					mPageIndexs[mType - 1] = mPageIndexs[mType - 1] + 1;
					Log.d(TAG, "wanggsx 追加");
				} else {
					((HouseManageActivity) mContext).mArrayHouseItemList[pageIndex] = jsReturn
							.getListDatas();
					Log.d(TAG, "wanggsx 赋值");
				}

				msg.what = R.id.FINISH_LOAD_ALL_DATA;
				msg.obj = type;
				mHander.sendMessage(msg);
				if (jsReturn.getListDatas() == null) {
					if (mArrayFragments[pageIndex] != null) {
						mArrayFragments[pageIndex]
								.sendmsgFinishRefreshOrLoadMore();

					}
				} else {
					if (mPageIndexs[pageIndex] >= 2) {
						mPageIndexs[pageIndex]--;
					}
					if (mArrayFragments[pageIndex] != null) {
						mArrayFragments[pageIndex]
								.sendmsgFinishRefreshOrLoadMore();
					}
				}
			}
		} else if (name.equals(CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_RESULT)) {
			JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
					EstateSearchItem.class);
			mSearchListData = jsReturn.getListDatas();
			SearchAdapter mSearch = new SearchAdapter(mContext, mSearchListData);
			mListView.setAdapter(mSearch);
		}
	}

	private void registerWeiXin() {
		// 通过WXAPIFactory工厂获取实例
		AppInstance.mWXAPI = WXAPIFactory.createWXAPI(mContext, Weixin_APP_ID,
				true);
		// 将应用的appid注册到微信
		AppInstance.mWXAPI.registerApp(Weixin_APP_ID);
	}

}
