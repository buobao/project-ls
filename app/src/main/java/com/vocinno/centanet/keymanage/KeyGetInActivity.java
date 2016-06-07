package com.vocinno.centanet.keymanage;

import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.KeyList;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.input.keyboard.KeyboardUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeyGetInActivity extends OtherBaseActivity {
	private View mBack;
	private EditText mEtPwdOne, mEtPwdTwo, mEtPwdThree, mEtPwdFour, mEtPwdText;
	private KeyboardUtil mKbUtil;
	private boolean isFinishStartKeyListActivity = false;
	private MyDialog.Builder myDialog;
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.img_left_mhead1:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public Handler setHandler() {
		return null;
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activitiy_key_get_in;
	}

	@Override
	public void initView() {
		drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.accept_key,
				null);
		mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		mEtPwdOne = (EditText) findViewById(R.id.et_pwdOne_keyGetInActivity);
		mEtPwdTwo = (EditText) findViewById(R.id.et_pwdTwo_keyGetInActivity);
		mEtPwdThree = (EditText) findViewById(R.id.et_pwdThree_keyGetInActivity);
		mEtPwdFour = (EditText) findViewById(R.id.et_pwdFour_keyGetInActivity);
		mEtPwdText = (EditText) findViewById(R.id.et_pwdText_keyGetInActivity);

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
					receivePin(mEtPwdText.getText().toString());
					/*MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
							CST_JS.JS_Function_KeyProxy_confirmPincode, CST_JS
									.getJsonStringForConfirmPincode(mEtPwdText.getText().toString()));*/
				}
			}
		});
	}

	private void receivePin(String pin) {
		Loading.show(this);
		URL= NetWorkConstant.PORT_URL+ NetWorkMethod.receivePin;
		Map<String, String> map=new HashMap<String,String>();
		map.put(NetWorkMethod.pinCode, pin);
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Loading.dismissLoading();
			}

			@Override
			public void onResponse(String response) {
				Loading.dismissLoading();
				JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, KeyList.class);
				if (jsReturn.isSuccess()) {
					MyToast.showToast(jsReturn.getMsg());
					intent.setClass(KeyGetInActivity.this,KeyManageActivity.class);
					startActivity(intent);
					finish();
				} else {
					MyToast.showToast(jsReturn.getMsg());
				}
			}
		});
	}

	@Override
	public void initData() {
		myDialog=new MyDialog.Builder(this);
		mKbUtil = new KeyboardUtil(mContext, baseView);
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
	public void netWorkResult(String name, String className, Object data) {

	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}
}
