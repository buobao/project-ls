package com.vocinno.centanet.customermanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.cst.CST_Wheel_Data;
import com.vocinno.centanet.apputils.cst.CST_Wheel_Data.WheelType;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.PianQu;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 添加客户
 *
 * @author Administrator
 *
 */
@SuppressLint("CutPasteId")
public class AddCustomerActivity extends SuperSlideMenuActivity {

	private Map<String, String> mapPianQu = new HashMap<String, String>();

	private static enum ConnectionType {
		none, connTel, connQQ, connWeixin
	};

	private View mBackView, mSubmitView;
	private RelativeLayout mRyltConnectionBanner, mRyltTypeBanner,
			mRyltPlaceBanner, mRyltAreaBanner, mRyltPriceBanner,
			mRyltPianquBanner, mRyltPhone, mRyltQQ, mRyltQiuzu, mRyltQiumai,
			mRyltChoosePlaceContainer, mRyltChoosePriceContaner,
			mRyltChoosePianquContainer, mRyltChooseAreaContainer, mRyltWX;

	private ImageView mImgConnectionRight, mImgTypeRight, mImgPlaceRight,
			mImgPianquRight, mImgAreaRight, mImgPriceRight, mImgPhoneImage,
			mImgPhoneIcon, mImgQQImage, mImgQQIcon, mImgWXImage, mImgWXIcon;
	private LinearLayout mLlytConntectTypeContainer, mLlytTypeContainer;
	private ImageView mImgQiumai, mImgQiuzu;
	private TextView mTvCustormerArea, mTvCustormerPrice, mTvCustormerPlace,

	mTvCustormerNumber, mTvCustormerType, mTvCustormerPianqu;
	private Button mBtnSubmitChoosePlace, mBtnSubmitChoosePianqu,
			mBtnSubmitChooseArea, mBtnSubmitChoosePrice;
	private WheelView mWheelViewChoosePlace, mWheelViewChoosePianqu,
			mWheelViewChooseAreaLast, mWheelViewChooseAreaTop,
			mWheelViewChoosePriceTop, mWheelViewChoosePriceLast;
	private EditText mEtConnectionNumber, mEtCustormerName, mEtOtherInfo;

	private static final int MESSAGE_CLOSE_CHOOSER = 1001;
	private static final int MESSAGE_REFRESH_WHEELVIEWPIANQU = 1002;
	private String mStrQQ, mStrTel, mStrWeixin;
	private ConnectionType mCurrConnType = ConnectionType.none;

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_add_custormer;
	}

	@Override
	public void initView() {
		// 需要获取的输入控件
		mEtConnectionNumber = (EditText) findViewById(R.id.et_connectionNumber_addCustomerActivity);
		mEtCustormerName = (EditText) findViewById(R.id.et_name_addCustomerActivity);
		// 需要添加点击事件的RelativeLayout
		mBackView = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mSubmitView = MethodsExtra.findHeadRightView1(mContext, mRootView, 0,
				R.drawable.universal_button_undone);
		MethodsExtra.findHeadTitle1(mContext, mRootView, R.string.addcustomer,
				null);
		mEtOtherInfo = (EditText) findViewById(R.id.et_otherInfo_addCustomerActivity);
		mRyltConnectionBanner = (RelativeLayout) findViewById(R.id.rlyt_connectionBanner_addCustomerActivity);
		mRyltTypeBanner = (RelativeLayout) findViewById(R.id.rlyt_typeBanner_addCustomerActivity);
		mRyltPlaceBanner = (RelativeLayout) findViewById(R.id.rlyt_placeBanner_addCustomerActivity);
		mRyltPianquBanner = (RelativeLayout) findViewById(R.id.rlyt_pianquBanner_addCustomerActivity);
		mRyltAreaBanner = (RelativeLayout) findViewById(R.id.rlyt_areaBanner_addCustomerActivity);
		mRyltPriceBanner = (RelativeLayout) findViewById(R.id.rlyt_priceBanner_addCustomerActivity);
		// 点击Relative之后需要改变的右边的img
		mImgConnectionRight = (ImageView) findViewById(R.id.img_connectionRight_addCustomerActivity);
		mImgTypeRight = (ImageView) findViewById(R.id.img_chooseType_addCustomerActivity);
		mImgPlaceRight = (ImageView) findViewById(R.id.img_choosePlace_addCustomerActivity);
		mImgPianquRight = (ImageView) findViewById(R.id.img_choosePianqu_addCustomerActivity);
		mImgAreaRight = (ImageView) findViewById(R.id.img_chooseArea_addCustomerActivity);
		mImgPriceRight = (ImageView) findViewById(R.id.img_choosePrice_addCustomerActivity);
		// 点击Relative之后需要改变的textview
		mTvCustormerNumber = (TextView) findViewById(R.id.tv_connect_addCustomerActivity);
		mTvCustormerType = (TextView) findViewById(R.id.tv_type_addCustomerActivity);
		mTvCustormerPlace = (TextView) findViewById(R.id.tv_changePlace_addCustomerActivity);
		mTvCustormerPianqu = (TextView) findViewById(R.id.tv_changePianqu_addCustomerActivity);
		mTvCustormerArea = (TextView) findViewById(R.id.tv_changeArea_addCustomerActivity);
		mTvCustormerPrice = (TextView) findViewById(R.id.tv_changePrice_addCustomerActivity);
		// 点击Relative之后需要显示的控件
		mLlytConntectTypeContainer = (LinearLayout) findViewById(R.id.llyt_connectionType_addCustomerActivity);
		mLlytTypeContainer = (LinearLayout) findViewById(R.id.llyt_chooseCustormerType_addCustomerActivity);
		mRyltChoosePlaceContainer = (RelativeLayout) findViewById(R.id.rlyt_choosePlace_addCustomerActivity);
		mRyltChoosePianquContainer = (RelativeLayout) findViewById(R.id.rlyt_choosePianqu_addCustomerActivity);
		mRyltChooseAreaContainer = (RelativeLayout) findViewById(R.id.rlyt_chooseArea_addCustomerActivity);
		((TextView) mRyltChooseAreaContainer
				.findViewById(R.id.tv_startTitle_modelTwoWheelView))
				.setText(R.string.minarea);
		((TextView) mRyltChooseAreaContainer
				.findViewById(R.id.tv_endTitle_modelTwoWheelView))
				.setText(R.string.maxarea);
		mRyltChoosePriceContaner = (RelativeLayout) findViewById(R.id.rlyt_choosePrice_addCustomerActivity);
		// 原本隐藏起来的控件里需要加点击监听的
		mRyltPhone = (RelativeLayout) findViewById(R.id.rlyt_phone_addCustomerActivity);
		mImgPhoneImage = (ImageView) findViewById(R.id.img_phone_addCustomerActivity);
		mImgPhoneIcon = (ImageView) findViewById(R.id.img_addPhone_addCustomerActivity);

		mRyltQQ = (RelativeLayout) findViewById(R.id.rlyt_qq_addCustomerActivity);
		mImgQQImage = (ImageView) findViewById(R.id.img_qq_addCustomerActivity);
		mImgQQIcon = (ImageView) findViewById(R.id.img_addqq_addCustomerActivity);

		mRyltWX = (RelativeLayout) findViewById(R.id.rlyt_wx_addCustomerActivity);
		mImgWXImage = (ImageView) findViewById(R.id.img_wx_addCustomerActivity);
		mImgWXIcon = (ImageView) findViewById(R.id.img_addwx_addCustomerActivity);

		mRyltQiuzu = (RelativeLayout) findViewById(R.id.rlyt_isChooseQiuzu_addCustomerActivity);
		mRyltQiumai = (RelativeLayout) findViewById(R.id.rlyt_isChooseQiumai_addCustomerActivity);
		mWheelViewChoosePlace = (WheelView) mRyltChoosePlaceContainer
				.findViewById(R.id.wheelview_modelOneWheelView);
		mWheelViewChoosePianqu = (WheelView) mRyltChoosePianquContainer
				.findViewById(R.id.wheelview_modelOneWheelView);
		mWheelViewChooseAreaLast = (WheelView) mRyltChooseAreaContainer
				.findViewById(R.id.wheelview_end_modelTwoWheelView);
		mWheelViewChooseAreaTop = (WheelView) mRyltChooseAreaContainer
				.findViewById(R.id.wheelview_start_modelTwoWheelView);
		mWheelViewChoosePriceLast = (WheelView) mRyltChoosePriceContaner
				.findViewById(R.id.wheelview_end_modelTwoWheelView);
		mWheelViewChoosePriceTop = (WheelView) mRyltChoosePriceContaner
				.findViewById(R.id.wheelview_start_modelTwoWheelView);
		mBtnSubmitChoosePlace = (Button) mRyltChoosePlaceContainer
				.findViewById(R.id.btn_submit_modelOneWheelView);
		mBtnSubmitChoosePianqu = (Button) mRyltChoosePianquContainer
				.findViewById(R.id.btn_submit_modelOneWheelView);
		mBtnSubmitChooseArea = (Button) mRyltChooseAreaContainer
				.findViewById(R.id.btn_submit_modelTwoWheelView);
		mBtnSubmitChoosePrice = (Button) mRyltChoosePriceContaner
				.findViewById(R.id.btn_submit_modelTwoWheelView);

		// 原本隐藏起来的控件里需要被改变的
		mImgQiuzu = (ImageView) findViewById(R.id.img_isChooseQiuzu_addCustomerActivity);
		mImgQiumai = (ImageView) findViewById(R.id.img_isChooseQiumai_addCustomerActivity);
	}

	@Override
	public void setListener() {

		mRyltPhone.setOnClickListener(this);
		mRyltQQ.setOnClickListener(this);
		mRyltWX.setOnClickListener(this);
		mRyltQiuzu.setOnClickListener(this);
		mRyltQiumai.setOnClickListener(this);
		mBtnSubmitChoosePlace.setOnClickListener(this);
		mBtnSubmitChoosePianqu.setOnClickListener(this);
		mBtnSubmitChooseArea.setOnClickListener(this);
		mBtnSubmitChoosePrice.setOnClickListener(this);

		mBackView.setOnClickListener(this);
		mSubmitView.setOnClickListener(this);
		findViewById(R.id.et_connectionNumberOK_addCustomerActivity)
				.setOnClickListener(this);
		;
		mRyltConnectionBanner.setOnClickListener(this);
		mRyltTypeBanner.setOnClickListener(this);
		mRyltPlaceBanner.setOnClickListener(this);
		mRyltPianquBanner.setOnClickListener(this);
		mRyltAreaBanner.setOnClickListener(this);
		mRyltPriceBanner.setOnClickListener(this);

		mSubmitView.setClickable(false);
		mEtConnectionNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				switch (mCurrConnType) {
					case connQQ:
						mStrQQ = mEtConnectionNumber.getText().toString();
						Log.d(TAG, "afterTextChanged strQQ:" + mStrQQ);
						break;
					case connTel:
						mStrTel = mEtConnectionNumber.getText().toString();
						Log.d(TAG, "afterTextChanged mStrTel:" + mStrTel);
						break;
					case connWeixin:
						mStrWeixin = mEtConnectionNumber.getText().toString();
						Log.d(TAG, "afterTextChanged mStrWeixin:" + mStrWeixin);
						break;
					default:
						break;
				}
				mTvCustormerNumber.setText(checkConnText());
			}
		});

		mEtCustormerName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				checkIsFinish();
			}
		});
	}

	@Override
	public void initData() {
		// 添加通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_ADD_CUSTOMER_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_GET_AREA_RESULT, TAG);
		MethodsJni.addNotificationObserver(CST_JS.NOTIFY_NATIVE_CHECK_PNONENO,TAG);
		mWheelViewChoosePlace.setData(CST_Wheel_Data
				.getListDatas(CST_Wheel_Data.WheelType.area));
		mWheelViewChoosePianqu.setData(new ArrayList<String>());
		mWheelViewChooseAreaLast.setData(CST_Wheel_Data
				.getListDatas(CST_Wheel_Data.WheelType.squareEnd));
		mWheelViewChooseAreaTop.setData(CST_Wheel_Data
				.getListDatas(CST_Wheel_Data.WheelType.squareStart));
		mWheelViewChoosePriceLast.setData(CST_Wheel_Data
				.getListDatas(CST_Wheel_Data.WheelType.priceChushouEnd));
		mWheelViewChoosePriceTop.setData(CST_Wheel_Data
				.getListDatas(CST_Wheel_Data.WheelType.priceChushouStart));
		mWheelViewChoosePlace.setEnable(true);
		mWheelViewChoosePianqu.setEnable(true);
		mWheelViewChooseAreaLast.setEnable(true);
		mWheelViewChooseAreaTop.setEnable(true);
		mWheelViewChoosePriceLast.setEnable(true);
		mWheelViewChoosePriceTop.setEnable(true);
		mWheelViewChooseAreaLast.setSelectItem(0);
		mWheelViewChooseAreaTop.setSelectItem(0);
		mWheelViewChoosePriceLast.setSelectItem(0);
		mWheelViewChoosePriceTop.setSelectItem(0);
	}

	@Override
	public Handler setHandler() {

		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				AddCustomerActivity.this.closeMenu(msg);
				switch (msg.what) {
					case MESSAGE_CLOSE_CHOOSER:
						mRyltChoosePlaceContainer.setVisibility(View.GONE);
						mRyltChoosePianquContainer.setVisibility(View.GONE);
						mRyltChooseAreaContainer.setVisibility(View.GONE);
						mRyltChoosePriceContaner.setVisibility(View.GONE);
						break;
					case MESSAGE_REFRESH_WHEELVIEWPIANQU:
						mWheelViewChoosePianqu.setData((ArrayList<String>) msg.obj);
						break;
					default:
						break;
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.img_left_mhead1:
				onBack();
				break;
			case R.id.img_right_mhead1:
				// 上传数据
				String reqType = null;
				if (mTvCustormerType.getText().toString().equals("求租")) {
					reqType = "rent";
				} else {
					reqType = "buy";
				}
				String price = null;
				String oldP = mTvCustormerPrice.getText().toString()
						.replace("万", "").replace("元", "");
				if ("buy".equals(reqType)) {
					price = oldP.substring(0, oldP.indexOf("-")) + "0000"
							+ oldP.substring(oldP.indexOf("-") - 1, oldP.length());
					if (!price.endsWith("不限")) {
						price += "0000";
					}
				} else {
					price = oldP;
				}
				String strJson = CST_JS.getJsonStringForAddCustomer(
						mEtCustormerName.getText().toString(), mStrTel, mStrQQ,
						mStrWeixin, reqType,
						mapPianQu.get(mTvCustormerPianqu.getText().toString()),
						mTvCustormerArea.getText().toString().replace("平米", ""),
						price, mEtOtherInfo.getText().toString());
				Log.d(TAG, "updateAddUser :" + strJson);
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
						CST_JS.JS_Function_CustomerList_addCustomer, strJson);

				break;
			case R.id.rlyt_isChooseQiuzu_addCustomerActivity:
				// 求租
				mImgQiumai
						.setBackgroundResource(R.drawable.c_manage_button_unselected);
				mImgQiuzu.setBackgroundResource(R.drawable.c_manage_button_choose);
				mTvCustormerType.setText("求租");
				closeTypeContainer();
				if (mTvCustormerPrice.getText() != null
						&& mTvCustormerPrice.getText().toString().contains("万")) {
					mTvCustormerPrice.setText("");
				}
				mWheelViewChoosePriceTop.setData(CST_Wheel_Data
						.getListDatas(WheelType.priceChuzuStart));
				mWheelViewChoosePriceLast.setData(CST_Wheel_Data
						.getListDatas(WheelType.priceChuzuEnd));
				mWheelViewChoosePriceTop.setSelectItem(0);
				mWheelViewChoosePriceLast.setSelectItem(0);
				mWheelViewChoosePriceTop.invalidate();
				mWheelViewChoosePriceLast.invalidate();
				break;
			case R.id.rlyt_isChooseQiumai_addCustomerActivity:
				// 求购
				mImgQiumai.setBackgroundResource(R.drawable.c_manage_button_choose);
				mImgQiuzu
						.setBackgroundResource(R.drawable.c_manage_button_unselected);
				mTvCustormerType.setText("求购");
				closeTypeContainer();
				if (mTvCustormerPrice.getText() != null
						&& mTvCustormerPrice.getText().toString().contains("元")) {
					mTvCustormerPrice.setText("");
				}
				mWheelViewChoosePriceTop.setData(CST_Wheel_Data
						.getListDatas(WheelType.priceChushouStart));
				mWheelViewChoosePriceLast.setData(CST_Wheel_Data
						.getListDatas(WheelType.priceChushouEnd));
				mWheelViewChoosePriceTop.setSelectItem(0);
				mWheelViewChoosePriceLast.setSelectItem(0);
				mWheelViewChoosePriceTop.invalidate();
				mWheelViewChoosePriceLast.invalidate();
				break;
			// 如果六个选择全选了 才给提交
			// 分别是 名字 电话 类型 地点 面积 价格
			case R.id.et_connectionNumberOK_addCustomerActivity:
			case R.id.rlyt_connectionBanner_addCustomerActivity:
				if (mLlytConntectTypeContainer.getVisibility() == View.GONE) {
					mLlytConntectTypeContainer.setVisibility(View.VISIBLE);
					mImgConnectionRight
							.setBackgroundResource(R.drawable.h_manage_icon_up);
					checkOpenOrClose(mLlytConntectTypeContainer.getId());
					if (mCurrConnType == ConnectionType.none) {
						addPhoneNumber();
					}
				} else {
					if (isMobileNO(mStrTel) || TextUtils.isEmpty(mStrTel)) {
						String phoneJson = CST_JS.getJsonStringForCheckPhoneNORepeated(mStrTel);
						MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
								CST_JS.JS_Function_CustomerList_addCustomer_checkPhoneNORepeated, phoneJson);
						//closeConnectionTypeContainer();
					} else {
						MethodsExtra.toast(mContext, "手机号码有误，请重新输入！");
					}
				}
				break;
			case R.id.rlyt_typeBanner_addCustomerActivity:
				if (mLlytTypeContainer.getVisibility() == View.GONE) {
					mLlytTypeContainer.setVisibility(View.VISIBLE);
					mImgTypeRight
							.setBackgroundResource(R.drawable.h_manage_icon_up);
					checkOpenOrClose(mLlytTypeContainer.getId());
				} else {
					closeTypeContainer();
				}
				MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
				break;
			case R.id.rlyt_placeBanner_addCustomerActivity:
				if (mRyltChoosePlaceContainer.getVisibility() == View.GONE) {
					mRyltChoosePlaceContainer.setVisibility(View.VISIBLE);
					mImgPlaceRight
							.setBackgroundResource(R.drawable.h_manage_icon_up);
					checkOpenOrClose(mRyltChoosePlaceContainer.getId());
				} else {
					closePlaceContainer();
				}
				MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
				break;
			case R.id.rlyt_pianquBanner_addCustomerActivity:
				if (mRyltChoosePianquContainer.getVisibility() == View.GONE
						&& mWheelViewChoosePianqu.getListSize() >= 1) {
					mRyltChoosePianquContainer.setVisibility(View.VISIBLE);
					mImgPianquRight
							.setBackgroundResource(R.drawable.h_manage_icon_up);
					checkOpenOrClose(mRyltChoosePianquContainer.getId());
				} else if (mRyltChoosePianquContainer.getVisibility() == View.GONE) {
					MethodsExtra.toast(mContext, "所选区域无对应片区!");
				} else {
					closePianquContainer();
				}
				MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
				break;
			case R.id.rlyt_areaBanner_addCustomerActivity:
				if (mRyltChooseAreaContainer.getVisibility() == View.GONE) {
					mRyltChooseAreaContainer.setVisibility(View.VISIBLE);
					mImgAreaRight
							.setBackgroundResource(R.drawable.h_manage_icon_up);
					checkOpenOrClose(mRyltChooseAreaContainer.getId());
				} else {
					closeAreaContainer();
				}
				MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
				break;
			case R.id.rlyt_priceBanner_addCustomerActivity:
				if (mRyltChoosePriceContaner.getVisibility() == View.GONE) {
					mRyltChoosePriceContaner.setVisibility(View.VISIBLE);
					mImgPriceRight
							.setBackgroundResource(R.drawable.h_manage_icon_up);
					checkOpenOrClose(mRyltChoosePriceContaner.getId());
				} else {
					closePriceContainer();
				}
				MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
				break;
			case R.id.rlyt_phone_addCustomerActivity:
				addPhoneNumber();
				break;
			case R.id.rlyt_qq_addCustomerActivity:
				addQQNumber();
				break;
			case R.id.rlyt_wx_addCustomerActivity:
				addWXNumber();
				break;
			case R.id.btn_submit_modelOneWheelView:
				if (mRyltChoosePlaceContainer.getVisibility() == View.VISIBLE) {
					mWheelViewChoosePianqu.setData(new ArrayList<String>());
					// 区域 mRyltPlaceBanner
					mTvCustormerPlace.setText(mWheelViewChoosePlace
							.getSelectedText());
					String strCode = CST_Wheel_Data
							.getCodeForArea(mWheelViewChoosePlace.getSelectedText());
					MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
							CST_JS.JS_Function_HouseResource_getDistrictArray,
							CST_JS.getJsonStringForGetAreaArray(strCode));
					mTvCustormerPianqu.setText("");
					closePlaceContainer();
				} else if (mRyltChoosePianquContainer.getVisibility() == View.VISIBLE) {
					// 片区
					mTvCustormerPianqu.setText(mWheelViewChoosePianqu
							.getSelectedText());
					closePianquContainer();
				}
				break;
			case R.id.btn_submit_modelTwoWheelView:
				if (mRyltChooseAreaContainer.getVisibility() == View.VISIBLE) {
					// 面积
					if (mWheelViewChooseAreaLast.getSelectedText().trim()
							.equals("不限")
							|| Integer.parseInt(mWheelViewChooseAreaTop
							.getSelectedText().trim().replaceAll("平米", "")) <= Integer
							.parseInt(mWheelViewChooseAreaLast
									.getSelectedText().trim()
									.replaceAll("平米", ""))) {
						mTvCustormerArea.setText(mWheelViewChooseAreaTop
								.getSelectedText()
								+ "-"
								+ mWheelViewChooseAreaLast.getSelectedText());
						closeAreaContainer();
					} else {
						MethodsExtra.toast(mContext, "最大面积不能小于最小面积");
					}
				} else if (mRyltChoosePriceContaner.getVisibility() == View.VISIBLE) {
					// 价格
					if (mWheelViewChoosePriceLast.getSelectedText().trim()
							.equals("不限")
							|| Integer.parseInt(mWheelViewChoosePriceTop
							.getSelectedText().trim().replaceAll("万", "")
							.replaceAll("元", "")) <= Integer
							.parseInt(mWheelViewChoosePriceLast
									.getSelectedText().trim()
									.replaceAll("万", "")
									.replaceAll("元", ""))) {
						mTvCustormerPrice.setText(mWheelViewChoosePriceTop
								.getSelectedText()
								+ "-"
								+ mWheelViewChoosePriceLast.getSelectedText());
						closePriceContainer();
					} else {
						MethodsExtra.toast(mContext, "最高价格不能小于最低价格");
					}
				}
				break;
			default:
				break;
		}
		checkIsFinish();
	}

	private void iconTest() {
		mImgPhoneImage.setImageResource(R.drawable.c_manage_icon_contact01);
		mImgQQImage.setImageResource(R.drawable.c_manage_icon_qq01);
		mImgWXImage.setImageResource(R.drawable.c_manage_icon_wechat01);

		mImgPhoneIcon.setImageResource(R.drawable.add);
		mImgQQIcon.setImageResource(R.drawable.add);
		mImgWXIcon.setImageResource(R.drawable.add);
	}

	private void addPhoneNumber() {
		iconTest();
		mImgPhoneImage.setImageResource(R.drawable.c_manage_icon_contact);
		mImgPhoneIcon.setImageResource(R.drawable.h_manage_icon_choose);
		mEtConnectionNumber.setHint(R.string.addphonenumber);
		mCurrConnType = ConnectionType.connTel;

		mEtConnectionNumber.setText(mStrTel);
		mEtConnectionNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

	}

	private void addQQNumber() {
		iconTest();
		mImgQQImage.setImageResource(R.drawable.c_manage_icon_qq);
		mImgQQIcon.setImageResource(R.drawable.h_manage_icon_choose);
		mEtConnectionNumber.setHint(R.string.addqqnumber);
		mCurrConnType = ConnectionType.connQQ;
		mEtConnectionNumber.setText(mStrQQ);
		mEtConnectionNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	private void addWXNumber() {
		iconTest();
		mImgWXImage.setImageResource(R.drawable.c_manage_icon_wechat);
		mImgWXIcon.setImageResource(R.drawable.h_manage_icon_choose);
		mEtConnectionNumber.setHint(R.string.addwxnumber);
		mCurrConnType = ConnectionType.connWeixin;
		mEtConnectionNumber.setText(mStrWeixin);
		mEtConnectionNumber.setInputType(InputType.TYPE_CLASS_TEXT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHander.sendEmptyMessageDelayed(MESSAGE_CLOSE_CHOOSER, 50);
	}

	@Override
	public void onBack() {
		finish();
	}

	private void checkIsFinish() {
		boolean isFinish = true;
		if (mEtCustormerName.getText() == null
				|| mEtCustormerName.getText().toString().length() == 0) {
			isFinish = false;
		} else if (mStrQQ == null && mStrTel == null && mStrWeixin == null) {
			isFinish = false;
		} else if (mTvCustormerType.getText() == null
				|| mTvCustormerType.getText().toString().length() == 0) {
			isFinish = false;
		} else if (mTvCustormerPlace.getText() == null
				|| mTvCustormerPlace.getText().toString().length() == 0) {
			isFinish = false;
		} else if (mTvCustormerPianqu.getText() == null
				|| mTvCustormerPianqu.getText().toString().length() == 0) {
			isFinish = false;
		} else if (mTvCustormerArea.getText() == null
				|| mTvCustormerArea.getText().toString().length() == 0) {
			isFinish = false;
		} else if (mTvCustormerPrice.getText() == null
				|| mTvCustormerPrice.getText().toString().length() == 0) {
			isFinish = false;
		}

		if (isFinish) {
			mSubmitView = MethodsExtra.findHeadRightView1(mContext, mRootView,
					0, R.drawable.universal_button_done);
			mSubmitView.setClickable(true);
		} else {
			mSubmitView = MethodsExtra.findHeadRightView1(mContext, mRootView,
					0, R.drawable.universal_button_undone);
			mSubmitView.setClickable(false);
		}
	}

	private void checkOpenOrClose(int intId) {
		if (intId != mRyltChoosePlaceContainer.getId()) {
			closePlaceContainer();
		}

		if (intId != mRyltChoosePianquContainer.getId()) {
			closePianquContainer();
		}

		if (intId != mRyltChoosePriceContaner.getId()) {
			closePriceContainer();
		}

		if (intId != mRyltChooseAreaContainer.getId()) {
			closeAreaContainer();
		}

		if (intId != mLlytConntectTypeContainer.getId()) {
			closeConnectionTypeContainer();
		}

		if (intId != mLlytTypeContainer.getId()) {
			closeTypeContainer();
		}
	}

	private void closePlaceContainer() {
		mRyltChoosePlaceContainer.setVisibility(View.GONE);
		mImgPlaceRight.setBackgroundResource(R.drawable.h_manage_icon_down);
	}

	private void closePianquContainer() {
		mRyltChoosePianquContainer.setVisibility(View.GONE);
		mImgPianquRight.setBackgroundResource(R.drawable.h_manage_icon_down);
	}

	private void closePriceContainer() {
		mRyltChoosePriceContaner.setVisibility(View.GONE);
		mImgPriceRight.setBackgroundResource(R.drawable.h_manage_icon_down);
	}

	private void closeAreaContainer() {
		mRyltChooseAreaContainer.setVisibility(View.GONE);
		mImgAreaRight.setBackgroundResource(R.drawable.h_manage_icon_down);
	}

	private void closeConnectionTypeContainer() {
		mLlytConntectTypeContainer.setVisibility(View.GONE);
		mImgConnectionRight
				.setBackgroundResource(R.drawable.h_manage_icon_down);
	}

	private void closeTypeContainer() {
		mLlytTypeContainer.setVisibility(View.GONE);
		mImgTypeRight.setBackgroundResource(R.drawable.h_manage_icon_down);
	}

	private String checkConnText() {
		StringBuffer sb = new StringBuffer("");
		if (mStrTel != null && mStrTel.length() >= 1) {
			sb.append("tel:" + mStrTel);
		}

		if (mStrQQ != null && mStrQQ.length() >= 1) {
			sb.append("qq:" + mStrQQ);
		}

		if (mStrWeixin != null && mStrWeixin.length() >= 1) {
			sb.append("wx:" + mStrWeixin);
		}
		return sb.toString();
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0,7]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		JSReturn jsReturn;
		if (name.equals(CST_JS.NOTIFY_NATIVE_GET_AREA_RESULT)) {
			// 获取片区
			jsReturn = MethodsJson.jsonToJsReturn((String) data, PianQu.class);
			List<PianQu> listPianQu = jsReturn.getListDatas();
			mapPianQu.clear();
			ArrayList<String> listStr = new ArrayList<String>();
			if (jsReturn.isSuccess() && listPianQu != null) {
				for (int i = 0; i < listPianQu.size(); i++) {
					PianQu pq = listPianQu.get(i);
					mapPianQu.put(pq.getAreaName(), "" + pq.getAreaCode());
					listStr.add(pq.getAreaName());
				}
			}
			Message msg = new Message();
			msg.what = MESSAGE_REFRESH_WHEELVIEWPIANQU;
			msg.obj = listStr;
			mHander.sendMessage(msg);
		}else if(name.equals(CST_JS.NOTIFY_NATIVE_CHECK_PNONENO)){
			jsReturn = MethodsJson.jsonToJsReturn((String) data, JSONObject.class);
			if(jsReturn.isSuccess()){
				closeConnectionTypeContainer();
			}else{
				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}
		} else {
			jsReturn = MethodsJson.jsonToJsReturn((String) data, Object.class);
			if (jsReturn.isSuccess()) {
				MethodsExtra.toast(mContext, "新建客源成功");
				finish();
			} else {
				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}
		}
	}

}
