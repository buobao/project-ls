package com.vocinno.centanet.user;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.AppInit;
import com.vocinno.centanet.apputils.SharedPreferencesUtils;
import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.home.HomeActivity;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.centanet.tools.DownloadApp;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.SharedPre;
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
public class UserLoginActivity extends SuperActivity implements HttpInterface {
	private Button mBtnLogin;
	private EditText mEtUserpassword, mEtUserAccount;
	private boolean mIsLoginedJustNow = false,loginError = false;
	private String mUserId = null;
	private MethodsJni methodsJni=new MethodsJni();
	public static UserLoginActivity ula;
	private ImageView iv_splash;
	private boolean isExit;
	private ImageView mDeleteUser,mDeletePwd;

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
						MethodsExtra.startActivity(mContext, HomeActivity.class);
					}

					finish();
					break;
				case R.id.doFail:
					if (!loginError) {
						loginError=true;
						MethodsExtra.toast(mContext, (String) msg.obj);
					}
					break;
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
		ula=this;
		iv_splash = (ImageView) findViewById(R.id.iv_splash);
		iv_splash.setOnClickListener(this);
		mEtUserAccount = (EditText) findViewById(R.id.et_userAccount_UserLoginActivity);
		mEtUserpassword = (EditText) findViewById(R.id.et_userPassword_UserLoginActivity);
		mBtnLogin = (Button) findViewById(R.id.btn_userLogin_UserLoginActivity);
		mDeleteUser = (ImageView)findViewById(R.id.iv_delete_userAccount);  //清空用户名和密码
		mDeletePwd = (ImageView)findViewById(R.id.iv_delete_pwd);	//只清除密码


		mEtUserAccount.setText(SharedPreferencesUtils.getUsername(this));
		mEtUserAccount.setSelection(mEtUserAccount.getText().length());
		mEtUserpassword.setText(SharedPreferencesUtils.getUserpassword(this));
		mEtUserpassword.setSelection(mEtUserpassword.getText().length());
		clearUserInfo();
		clearPassWordInfo();

	}
	/**************************用户名不为空时,点击按钮清除用户信息****************************/
	private void clearUserInfo() {
		if(!TextUtils.isEmpty(mEtUserAccount.getText())){   //不为空就点击删除
			mDeleteUser.setVisibility(View.VISIBLE);
			mDeleteUser.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mEtUserpassword.setText("");
					mEtUserAccount.setText("");
					mEtUserAccount.requestFocus();//用户名获得焦点
				}
			});
		}else{
			mDeleteUser.setVisibility(View.INVISIBLE); 			//为空就隐藏删除按钮
		}
	}

	/**************************密码不为空时,点击按钮只清除密码信息****************************/
	private void clearPassWordInfo() {
		if(!TextUtils.isEmpty(mEtUserpassword.getText())){   //不为空就点击删除
			mDeletePwd.setVisibility(View.VISIBLE);
			mDeletePwd.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mEtUserpassword.setText("");
					mEtUserpassword.requestFocus();//密码框获得焦点
				}
			});
		}else{
			mDeletePwd.setVisibility(View.INVISIBLE); 		//为空就隐藏删除按钮
		}
	}

	/***
	 * 设置监听
	 */
	@Override
	public void setListener() {
		mEtUserAccount.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				clearUserInfo();
			}
		});

		mEtUserpassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				clearPassWordInfo();
			}
		});



		mBtnLogin.setOnClickListener(new NoDoubleClickListener() {
			@Override
			public void onNoDoubleClick(View v) {
				loginError=false;
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
				/*URL = NetWorkConstant.PORT_URL + NetWorkMethod.login;
				Map<String, String> urlMap = new HashMap<String, String>();
				urlMap.put(NetWorkConstant.source, NetWorkConstant.agencyApp);
				urlMap.put(NetWorkConstant.username, userAccount);
				urlMap.put(NetWorkConstant.password, userPassword);
				urlMap.put(NetWorkConstant.version, NetWorkConstant.version_code);
				OKHCM.postAsyn(URL, new OkHttpClientManager.ResultCallback<JSReturn>() {
					@Override
					public void onError(Request request, Exception e) {
						Log.i(",", "");
					}

					@Override
					public void onResponse(JSReturn response) {
//							JSReturn jsReturn = new Gson().fromJson(response.toString(), JSReturn.class);
						if (response.isSuccess()) {
							myApp.setEmpId(response.getEmpId());
							myApp.setToken(response.getToken());
							MyToast.showToast(response.getMsg());
							startActivity(new Intent(UserLoginActivity.this, HomeActivity.class));
						}

					}
				}, urlMap);*/
				MethodsJni.callProxyFun((HttpInterface)mContext,CST_JS.JS_ProxyName_Login,CST_JS.JS_Function_Login_login,CST_JS.getJsonStringForLogin(userAccount, userPassword));
			}
		});
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		methodsJni.setMethodsJni((HttpInterface) this);
		AppInit.init(getApplicationContext());
		MethodsNetwork.refreshAPNTypeInMainThread(this);
		MethodsJni.addNotificationObserver(CST_JS.NOTIFY_NATIVE_LOGIN_RESULT,
				TAG);
		isExit=getIntent().getBooleanExtra(MyConstant.isExit,false);
		if(isExit){
			iv_splash.setVisibility(View.GONE);
		}else{
			String userAccount = mEtUserAccount.getText().toString().trim();
			String userPassword = mEtUserpassword.getText().toString().trim();
			if(MethodsData.isEmptyString(userAccount)){
				iv_splash.setVisibility(View.GONE);
			}else{
				MethodsJni.callProxyFun((HttpInterface)mContext,CST_JS.JS_ProxyName_Login,
						CST_JS.JS_Function_Login_login,
						CST_JS.getJsonStringForLogin(userAccount, userPassword));
			}
		}

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
	private long exitTime = 0;
	@Override
	public void onBack() {
		if(iv_splash.getVisibility()==View.GONE){
//			MethodsExtra.backdailog(mContext);
			if((System.currentTimeMillis()-exitTime) > 2000){
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				MyUtils.removeActivityFromList();
				MyUtils.removeActivityFromAllList();
			}
		}else{
			finish();
			System.exit(0);
		}
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {


		/*methodsJni.setMethodsJni(null);
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
		mHander.sendMessage(msg);*/
	}

	private void downloadApp(String msg) {
		MyDialog.Builder myDialog=new MyDialog.Builder(this);
		myDialog.setTitle("提示");
		myDialog.setMessage(msg);
		myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownloadApp ad=new DownloadApp(UserLoginActivity.this);
				ad.setPath(Environment.getExternalStorageDirectory().getPath() + "/vocinno/appdownload");
				ad.showDownloadDialog();
				/*UpdateManager um=new UpdateManager(UserLoginActivity.this);
				um.showDownloadDialog();*/
				/*OkHttpClientManager.downloadApp("http://a.sh.centanet.com/app/centanet_Release.apk", Environment.getExternalStorageDirectory().getPath() + "/vocinno", new Callback() {
					@Override
					public void onFailure(Request request, IOException e) {
						Log.i("onFailure=====","onFailure");
					}

					@Override
					public void onResponse(Response response) throws IOException {
						Headers responseHeaders = response.headers();
						for (int i = 0; i < responseHeaders.size(); i++) {
							Log.i("responseHeaders=====", responseHeaders.name(i) + ": " + responseHeaders.value(i));
						}
					}
				});*/
				/*UpdateManager um=new UpdateManager(mContext);
				um.showDownloadDialog();*/
				dialog.dismiss();
				/*final Uri uri = Uri.parse(getString(R.string.download_url));
				final Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);*/
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

	@Override
	public void netWorkResult(String name, String className, Object data) {
		JSReturn jReturn = MethodsJson.jsonToJsReturn((String) data,
				HouseList.class);
		Message msg = new Message();
		if (jReturn.isSuccess()) {
			myApp.setToken(jReturn.getToken());
			mUserId = jReturn.getEmpId();
			msg.what = R.id.doSuccess;
			msg.obj = jReturn.getMsg();
		} else {
			iv_splash.setVisibility(View.GONE);
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
	private void saveXml(final String empId,final String token){
		new Thread() {
			@Override
			public void run() {
				addToken(empId, token);
			}
		}.start();
	}
	private void addToken(String empId,String token){
		SharedPreferences spf = getSharedPreferences(SharedPre.xml_token_name, this.MODE_PRIVATE);
		SharedPreferences.Editor editor = spf.edit();
		editor.putString(SharedPre.empId, empId);
		editor.putString(SharedPre.token, token);
		editor.commit();
	}
	private String getToken(){
		SharedPreferences spf = getSharedPreferences(SharedPre.xml_token_name, this.MODE_PRIVATE);
	    return spf.getString(SharedPre.token,null);
	}
	private String getEmpId(){
		SharedPreferences spf = getSharedPreferences(SharedPre.xml_token_name, this.MODE_PRIVATE);
		return spf.getString(SharedPre.empId, null);
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
