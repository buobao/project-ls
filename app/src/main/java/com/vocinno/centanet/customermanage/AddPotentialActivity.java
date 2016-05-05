package com.vocinno.centanet.customermanage;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 添加客户
 *
 * @author Administrator
 *
 */
@SuppressLint("CutPasteId")
public class AddPotentialActivity extends OtherBaseActivity {
	private Map<String, String> mapPianQu = new HashMap<String, String>();
	private static enum ConnectionType {
		none, connTel, connQQ, connWeixin
	};
	private View mBackView;
	private ImageView mSubmitView;
	private EditText et_name_addqianke,tv_tel_addqianke;
	private EditText /*mEtConnectionNumber, */mEtCustormerName, mEtOtherInfo;

	private ConnectionType mCurrConnType = ConnectionType.none;

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_add_potential;
	}

	@Override
	public void initView() {
		mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		mSubmitView = (ImageView)MethodsExtra.findHeadRightView1(mContext, baseView, 0,
				R.drawable.universal_button_undone);

		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.add_potential_customer,
				null);

		et_name_addqianke = (EditText) findViewById(R.id.et_name_addqianke);
		mEtOtherInfo = (EditText) findViewById(R.id.et_otherInfo_addqianke);
		tv_tel_addqianke = (EditText) findViewById(R.id.tv_tel_addqianke);
		setListener();
	}

	public void setListener() {
		mBackView.setOnClickListener(this);
		mSubmitView.setOnClickListener(this);
		mSubmitView.setClickable(false);
		mSubmitView.setEnabled(false);
		et_name_addqianke.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String tel = tv_tel_addqianke.getText().toString().trim();
				if(tel.length()<=0||s.toString().trim().length()<=0){
					mSubmitView.setImageResource(R.drawable.universal_button_undone);
					mSubmitView.setClickable(false);
					mSubmitView.setEnabled(false);
				}else{
					if(tel.toString().trim().length()==11){
						mSubmitView.setImageResource(R.drawable.universal_button_done);
						mSubmitView.setClickable(true);
						mSubmitView.setEnabled(true);
					}else{
						mSubmitView.setImageResource(R.drawable.universal_button_undone);
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

		});
		tv_tel_addqianke.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String name = et_name_addqianke.getText().toString().trim();
				if(name.length()<=0||s.toString().trim().length()<=0){
					mSubmitView.setImageResource(R.drawable.universal_button_undone);
					mSubmitView.setClickable(false);
					mSubmitView.setEnabled(false);
				}else{
					if(s.toString().trim().length()==11){
						mSubmitView.setImageResource(R.drawable.universal_button_done);
						mSubmitView.setClickable(true);
						mSubmitView.setEnabled(true);
					}else{
						mSubmitView.setImageResource(R.drawable.universal_button_undone);
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

		});

	}
	@Override
	public void initData() {
		// 添加通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_ADD_CUSTOMER_RESULT, TAG);
	}
	@Override
	public Handler setHandler() {
		return null;
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.img_left_mhead1:
				finish();
				break;
			case R.id.img_right_mhead1:
				if(!isMobileNO(tv_tel_addqianke.getText().toString().trim())){
					MethodsExtra.toast(mContext,"手机号码格式不正确");
					return;
				}
				showDialog();
				// 上传数据
				String strJson = CST_JS.getJsonStringForAddCustomer(et_name_addqianke.getText().toString().toString(),
						tv_tel_addqianke.getText().toString(),mEtOtherInfo.getText().toString());
				MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_CustomerList,
						CST_JS.JS_Function_CustomerList_addCustomer, strJson);
				break;
			default:
				break;
		}
	}
	@Override
	public void netWorkResult(String name, String className, Object data) {
		JSReturn jsReturn;
		if(name.equals(CST_JS.NOTIFY_NATIVE_CHECK_PNONENO)){
			jsReturn = MethodsJson.jsonToJsReturn((String) data, JSONObject.class);
			if(jsReturn.isSuccess()){
			}else{
				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}
		} else {
			modelDialog.dismiss();
			jsReturn = MethodsJson.jsonToJsReturn((String) data, Object.class);
			if (jsReturn.isSuccess()) {
				MethodsExtra.toast(mContext, jsReturn.getMsg());
				finish();
			} else {
				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}
		}
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}
	private void checkIsFinish() {
		boolean isFinish = true;
		if (tv_tel_addqianke.getText() == null || tv_tel_addqianke.getText().toString().length() == 0) {
			isFinish = false;
		}
		if(!(isMobileNO("") || TextUtils.isEmpty(""))) {
			isFinish = false;
		}
		if (isFinish) {
			mSubmitView = (ImageView)MethodsExtra.findHeadRightView1(mContext, baseView,
					0, R.drawable.universal_button_done);
			mSubmitView.setClickable(true);
			mSubmitView.setEnabled(true);
		} else {
			mSubmitView = (ImageView)MethodsExtra.findHeadRightView1(mContext, baseView,
					0, R.drawable.universal_button_undone);
			mSubmitView.setClickable(false);
			mSubmitView.setEnabled(false);
		}
	}

	public static boolean isMobileNO(String mobiles) {
		if(mobiles!=null){
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0,7]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			return m.matches();
		}
		return false;
	}

	public void notifCallBack(String name, String className, Object data) {

	}
	public void setLoseFocus(){
		if(isMobileNO("") && !TextUtils.isEmpty("")){
			if(tv_tel_addqianke.isFocusable()){
				tv_tel_addqianke.setFocusable(false);
			}
		}else{
			MethodsExtra.toast(mContext, "手机号码有误，请重新输入！");
			tv_tel_addqianke.setFocusable(true);
			tv_tel_addqianke.setFocusableInTouchMode(true);
		}
	}
}
