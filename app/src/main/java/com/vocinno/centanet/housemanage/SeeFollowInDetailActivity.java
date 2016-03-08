package com.vocinno.centanet.housemanage;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.model.HouseDetail;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.SeeFollowIn;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

public class SeeFollowInDetailActivity extends SuperSlideMenuActivity {
	private View mViewBack, mSubmitView;
	private TextView mHouseCode;
	private EditText mRemark, mCustCode, mLookCode;
	private HouseDetail mHouseDetail = null;
	private SeeFollowIn mSeeFollowIn;

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				SeeFollowInDetailActivity.this.closeMenu(msg);
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_see_follow_in_house;
	}

	@Override
	public void initView() {
		MethodsExtra.findHeadTitle1(mContext, mRootView,
				R.string.followin_look, null);
		mViewBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mSubmitView = MethodsExtra.findHeadRightView1(mContext, mRootView, 0,
				R.drawable.universal_button_undone);
		mHouseCode = (TextView) findViewById(R.id.tv_housecode_SeeFollowInDetailActivity);
		mCustCode = (EditText) findViewById(R.id.tv_custcode_SeeFollowInDetailActivity);
		mLookCode = (EditText) findViewById(R.id.tv_lookcode_SeeFollowInDetailActivity);
		mRemark = (EditText) findViewById(R.id.tv_remark_SeeFollowInDetailActivity);
		mRemark.setGravity(Gravity.LEFT);
	}

	@Override
	public void setListener() {
		mViewBack.setOnClickListener(this);
		mSubmitView.setOnClickListener(this);

		mSubmitView.setClickable(false);

		mCustCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				checkIsFinish();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mLookCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				checkIsFinish();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mRemark.addTextChangedListener(new TextWatcher() {
			String strBeforeText = null;
			int lastEndIndex = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.d("wan", "onTextChanged start:before:count " + start + ":"
						+ before + ":" + count);
				int selEndIndex = Selection.getSelectionEnd(mRemark.getText());
				String string = mRemark.getText().toString();
				if (string == null || string.trim().length() == 0) {

				} else {
					if (string.trim().length() > 500) {
						if (strBeforeText != null) {
							mRemark.setText(strBeforeText);
							Selection.setSelection(mRemark.getText(),
									lastEndIndex);
						} else {
							mRemark.setText(string.substring(0, 500));
							if (selEndIndex > 500) {
								selEndIndex = 500;
							}
							Selection.setSelection(mRemark.getText(),
									selEndIndex);
						}
						MethodsExtra.toast(mContext, "描述不能超过500字");
					}
				}
				checkIsFinish();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				String string = mRemark.getText().toString();
				Log.d("wan",
						"wanggsx beforeTextChanged len = " + string.length());
				if (string.length() == 500) {
					strBeforeText = string;
					lastEndIndex = Selection.getSelectionEnd(mRemark.getText());
				} else {
					strBeforeText = null;
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	@Override
	public void initData() {
		mHouseCode.setText(MethodsDeliverData.mDelCode);
		// 注册通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_ADD_HOU_CUST_TRACK_RESULT, TAG);
	}

	private void checkIsFinish() {
		Boolean isFinish = true;
		if (mCustCode.getText() == null
				|| mCustCode.getText().toString().length() == 0) {
			isFinish = false;
		}
		if (mLookCode.getText() == null
				|| mLookCode.getText().toString().length() == 0) {
			isFinish = false;
		}
		if (mRemark.getText() == null) {
			isFinish = false;
		} else if (mRemark.getText().toString().length() < 10) {
			isFinish = false;
		} else {
			String string = mRemark.getText().toString();
			boolean isHasChinese = false;
			for (int i = 0; i < string.length(); i++) {
				isHasChinese = MethodsData.isChinese(string.charAt(i));
				if (isHasChinese) {
					break;
				}
			}
			if (!isHasChinese && string.length() < 20) {
				isFinish = false;
			}
		}
		if (isFinish) {
			mSubmitView = MethodsExtra.findHeadRightView1(mContext, mRootView,
					0, R.drawable.universal_button_done);
			mSubmitView.setClickable(true);
		} else {
			mSubmitView = MethodsExtra.findHeadRightView1(mContext, mRootView,
					0, R.drawable.universal_button_undone);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			onBack();
			break;
		case R.id.img_right_mhead1:
			String string = CST_JS.getJsonStringForAddHouCustomerTrack(
					MethodsDeliverData.mDelCode,
					mCustCode.getText().toString(), mLookCode.getText()
							.toString(), mRemark.getText().toString());
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_addHouCustomerTrack,
					string);
			Log.d(TAG, "AddHouCustomerTrack:" + string);
			break;

		default:
			break;
		}
		checkIsFinish();
	}

	private void showMenuDialog() {

	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		String strJson = (String) data;
		JSReturn jsReturn = MethodsJson.jsonToJsReturn(strJson,
				SeeFollowIn.class);
		if (jsReturn.isSuccess()) {
			MethodsExtra.toast(mContext, "保存成功");
			finish();
		} else {
			MethodsExtra.toast(mContext, jsReturn.getMsg());
		}
	}

}
