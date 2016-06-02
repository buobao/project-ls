package com.vocinno.centanet.keymanage;

import java.util.ArrayList;

import android.os.Handler;
import android.view.View;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.KeyList;
import com.vocinno.centanet.apputils.utils.MethodsExtra;
import com.vocinno.centanet.apputils.utils.MethodsJni;
import com.vocinno.centanet.apputils.utils.MethodsJson;
import com.vocinno.centanet.apputils.utils.input.keyboard.KeyboardUtil;

import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class KeyGetInActivity extends SuperActivity {
	private View mBack;
	private EditText mEtPwdOne, mEtPwdTwo, mEtPwdThree, mEtPwdFour, mEtPwdText;
	private KeyboardUtil mKbUtil;
	private boolean isFinishStartKeyListActivity = false;
	private MyDialog.Builder myDialog;
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.img_left_mhead1:
			onBack();
			break;
		default:
			break;
		}
	}

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.doSuccess:
					MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
							CST_JS.JS_Function_KeyProxy_confirmPincode, CST_JS
									.getJsonStringForConfirmPincode(mEtPwdText
											.getText().toString()));
					MethodsExtra.startActivity(KeyGetInActivity.this,
							KeyManageActivity.class);
					finish();
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activitiy_key_get_in;
	}

	@Override
	public void initView() {
		MethodsExtra.findHeadTitle1(mContext, mRootView, R.string.accept_key,
				null);
		mBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mEtPwdOne = (EditText) findViewById(R.id.et_pwdOne_keyGetInActivity);
		mEtPwdTwo = (EditText) findViewById(R.id.et_pwdTwo_keyGetInActivity);
		mEtPwdThree = (EditText) findViewById(R.id.et_pwdThree_keyGetInActivity);
		mEtPwdFour = (EditText) findViewById(R.id.et_pwdFour_keyGetInActivity);
		mEtPwdText = (EditText) findViewById(R.id.et_pwdText_keyGetInActivity);
	}

	@Override
	public void setListener() {
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CONFIRM_PINCODE, TAG);
		mBack.setOnClickListener(this);
		mEtPwdText.addTextChangedListener(new TextWatcher() {
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
				if (mEtPwdText.getText() != null
						&& mEtPwdText.getText().toString().length() == 4) {
					showDialog();
					MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
							CST_JS.JS_Function_KeyProxy_confirmPincode, CST_JS
									.getJsonStringForConfirmPincode(mEtPwdText
											.getText().toString()));
				}
			}
		});

	}

	@Override
	public void initData() {
		myDialog=new MyDialog.Builder(this);
		mKbUtil = new KeyboardUtil(mContext, mRootView);
		ArrayList<EditText> list = new ArrayList<EditText>();
		list.add(mEtPwdOne);
		list.add(mEtPwdTwo);
		list.add(mEtPwdThree);
		list.add(mEtPwdFour);
		list.add(mEtPwdText);
		mKbUtil.setListEditText(list);
		/*mEtPwdOne.setInputType(InputType.TYPE_NULL);
		mEtPwdTwo.setInputType(InputType.TYPE_NULL);
		mEtPwdThree.setInputType(InputType.TYPE_NULL);
		mEtPwdFour.setInputType(InputType.TYPE_NULL);*/

	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		dismissDialog();
		if (name.equals(CST_JS.NOTIFY_NATIVE_CONFIRM_PINCODE)) {
			JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
					KeyList.class);
			if (jsReturn.isSuccess()) {
				if (!isFinishStartKeyListActivity) {
					MethodsExtra.toast(mContext, jsReturn.getMsg());
					mHander.sendEmptyMessageDelayed(R.id.doSuccess, 50);
					isFinishStartKeyListActivity = true;
					/*myDialog.setMessage(jsReturn.getMsg());
					myDialog.setTitle("提示");
					myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							mHander.sendEmptyMessageDelayed(R.id.doSuccess, 50);
							isFinishStartKeyListActivity = true;
						}
					});
					myDialog.create().show();*/
				}
			} else {
				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}
		}
	}

}
