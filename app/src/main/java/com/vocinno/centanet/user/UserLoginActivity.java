package com.vocinno.centanet.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.AppInit;
import com.vocinno.centanet.apputils.SharedPreferencesUtils;
import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.centanet.apputils.UpdateManager;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.home.HomeActivity;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.MethodsNetwork;

import java.util.Calendar;

/**
 * 用户登录
 * 
 * @author Administrator
 * 
 */
public class UserLoginActivity extends SuperActivity {
	private Button mBtnLogin;
	private EditText mEtUserpassword, mEtUserAccount;
	private boolean mIsLoginedJustNow = false;
	private String mUserId = null;

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				dismissDialog();
				switch (msg.what) {
				case R.id.doSuccess:
					if (!mIsLoginedJustNow) {
						mIsLoginedJustNow = true;
						SharedPreferencesUtils.setLoginIn(mContext,
								mEtUserAccount.getText().toString(),
								mEtUserpassword.getText().toString(), mUserId);
						MethodsExtra.toast(mContext, (String) msg.obj);
						MethodsExtra
								.startActivity(mContext, HomeActivity.class);
					}

					finish();
				case R.id.doFail:
					if (!mIsLoginedJustNow) {
						MethodsExtra.toast(mContext, (String) msg.obj);
					}
				default:
					break;
				}
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_user_login;
	}

	/**
	 * 初始化控件
	 */
	@Override
	public void initView() {
		mEtUserAccount = (EditText) findViewById(R.id.et_userAccount_UserLoginActivity);
		mEtUserpassword = (EditText) findViewById(R.id.et_userPassword_UserLoginActivity);
		mBtnLogin = (Button) findViewById(R.id.btn_userLogin_UserLoginActivity);

		mEtUserAccount.setText(SharedPreferencesUtils.getUsername(this));
		mEtUserpassword.setText(SharedPreferencesUtils.getUserpassword(this));
	}

	/***
	 * 设置监听
	 */
	@Override
	public void setListener() {
		mBtnLogin.setOnClickListener(new NoDoubleClickListener() {
			@Override
			public void onNoDoubleClick(View v) {
				String userAccount = mEtUserAccount.getText().toString().trim();
				String userPassword = mEtUserpassword.getText().toString().trim();
				if (MethodsData.isEmptyString(userAccount)) {
					MethodsExtra.toast(mContext, "请输入用户名");
					return;
				} else if (MethodsData.isEmptyString(userPassword)) {
					MethodsExtra.toast(mContext, "请输入密码");
					return;
				}
				showDialog();
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_Login,
						CST_JS.JS_Function_Login_login,
						CST_JS.getJsonStringForLogin(userAccount, userPassword));
			}
		});
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		AppInit.init(getApplicationContext());
		MethodsNetwork.refreshAPNTypeInMainThread(this);
		MethodsJni.addNotificationObserver(CST_JS.NOTIFY_NATIVE_LOGIN_RESULT,
				TAG);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_userLogin_UserLoginActivity:
			break;
		default:
			break;
		}
	}

	/**
	 * 按返回键的时候的监听
	 */
	@Override
	public void onBack() {
		MethodsExtra.backdailog(mContext);
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		JSReturn jReturn = MethodsJson.jsonToJsReturn((String) data,
				HouseList.class);
		Message msg = new Message();
		if (jReturn.isSuccess()) {
			mUserId = jReturn.getEmpId();
			msg.what = R.id.doSuccess;
			msg.obj = jReturn.getMsg();
		} else {
			if("0".equals(jReturn.getCode())){
				//MethodsExtra.toast(this,jReturn.getMsg());
				downloadApp(jReturn.getMsg());
			}else{
				msg.what = R.id.doFail;
				msg.obj = jReturn.getMsg();
			}
		}
		mHander.sendMessage(msg);
	}

	private void downloadApp(String msg) {
		MyDialog.Builder myDialog=new MyDialog.Builder(this);
		myDialog.setTitle("提示");
		myDialog.setMessage(msg);
		myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/*UpdateManager um=new UpdateManager(mContext);
				um.showDownloadDialog();*/
				dialog.dismiss();
				final Uri uri = Uri.parse(getString(R.string.download_url));
				final Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
			}
		});
		myDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		myDialog.create().show();
	}
	public abstract class NoDoubleClickListener implements View.OnClickListener {

		public static final int MIN_CLICK_DELAY_TIME = 1000;
		private long lastClickTime = 0;

		@Override
		public void onClick(View v) {
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
				lastClickTime = currentTime;
				onNoDoubleClick(v);
			}
		}

		protected abstract void onNoDoubleClick(View v);
	}
	/*public void onNoDoubleClick(View v){
		String userAccount = mEtUserAccount.getText().toString().trim();
		String userPassword = mEtUserpassword.getText().toString().trim();
		if (MethodsData.isEmptyString(userAccount)) {
			MethodsExtra.toast(mContext, "请输入用户名");
			return;
		} else if (MethodsData.isEmptyString(userPassword)) {
			MethodsExtra.toast(mContext, "请输入密码");
			return;
		}
		showDialog();
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_Login,
				CST_JS.JS_Function_Login_login,
				CST_JS.getJsonStringForLogin(userAccount, userPassword));
	}*/
}
