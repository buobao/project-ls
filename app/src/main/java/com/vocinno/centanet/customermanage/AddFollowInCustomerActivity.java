package com.vocinno.centanet.customermanage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

public class AddFollowInCustomerActivity extends SuperSlideMenuActivity {
	private View mBackView;
	private ImageView mSubmitView;
	private String mCustorCode = null;
	private TextView mTvDate;
	private EditText mEtContent;

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_add_follow_in_customer;
	}

	@Override
	public void initView() {
		MethodsExtra.findHeadTitle1(mContext, mRootView,
				R.string.followincustomer, null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mSubmitView = (ImageView) MethodsExtra.findHeadRightView1(mContext,
				mRootView, 0, R.drawable.universal_button_undone);
		mTvDate = (TextView) findViewById(R.id.tv_date_addFollowInCustomerActivity);
		mEtContent = (EditText) findViewById(R.id.et_content_addFollowInCustomerActivity);

		mEtContent.addTextChangedListener(new TextWatcher() {
			String strBeforeText = null;
			int lastEndIndex = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.d("wan", "onTextChanged start:before:count " + start + ":"
						+ before + ":" + count);
				int selEndIndex = Selection.getSelectionEnd(mEtContent
						.getText());
				String string = mEtContent.getText().toString();
				if (string == null || string.trim().length() < 10) {
					mSubmitView.setImageDrawable(getResources().getDrawable(
							R.drawable.universal_button_undone));
					mSubmitView.setClickable(false);
				} else {
					if (string.trim().length() > 500) {
						if (strBeforeText != null) {
							mEtContent.setText(strBeforeText);
							Selection.setSelection(mEtContent.getText(),
									lastEndIndex);
						} else {
							mEtContent.setText(string.substring(0, 500));
							if (selEndIndex > 500) {
								selEndIndex = 500;
							}
							Selection.setSelection(mEtContent.getText(),
									selEndIndex);
						}
						MethodsExtra.toast(mContext, "描述不能超过500字");
					}
					mSubmitView.setImageDrawable(getResources().getDrawable(
							R.drawable.universal_button_done));
					mSubmitView.setClickable(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				String string = mEtContent.getText().toString();
				Log.d("wan",
						"wanggsx beforeTextChanged len = " + string.length());
				if (string.length() == 500) {
					strBeforeText = string;
					lastEndIndex = Selection.getSelectionEnd(mEtContent
							.getText());
				} else {
					strBeforeText = null;
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	public boolean matche(String input, String matches) {
		return Pattern.matches(matches, input);
	}

	@Override
	public void setListener() {
		mBackView.setOnClickListener(this);
		mSubmitView.setOnClickListener(this);
	}

	@Override
	public void initData() {
		mCustorCode = MethodsDeliverData.string;
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CUST_TRACK_RESULT, TAG);
		mTvDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));
	}

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				AddFollowInCustomerActivity.this.closeMenu(msg);
			}
		};
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_right_mhead1:
			String content = mEtContent.getText().toString();
			if (content != null && MethodsData.isHaveEnoughNumber(content, 8)) {
				MethodsExtra.toast(mContext, "不能输入连续的8个数字");
			} else if (content != null && content.trim().length() >= 1) {
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
						CST_JS.JS_Function_CustomerList_addTrackInfo, CST_JS
								.getJsonStringForAddTrackInfo(mCustorCode,
										content));
			}
			mSubmitView.setImageDrawable(getResources().getDrawable(
					R.drawable.universal_button_undone));
			mSubmitView.setClickable(false);
			break;
		case R.id.img_left_mhead1:
			onBack();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
				Object.class);
		if (jsReturn.isSuccess()) {
			MethodsExtra.toast(mContext, "保存成功");
			String content = mEtContent.getText().toString();
			String time = mTvDate.getText().toString();
			Intent intent = new Intent();
			intent.putExtra("content", content);
			intent.putExtra("time", time);
			setResult(10, intent);
			finish();
		}
	}
}
