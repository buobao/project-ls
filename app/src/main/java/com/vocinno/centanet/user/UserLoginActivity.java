package com.vocinno.centanet.user;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.SharedPre;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.MethodsNetwork;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录
 *
 * @author Administrator
 */
public class UserLoginActivity extends SuperActivity implements HttpInterface,View.OnFocusChangeListener {
	private Button mBtnLogin;
	private EditText mEtUserpassword, mEtUserAccount;
	private boolean mIsLoginedJustNow = false, loginError = false;
	private String mUserId = null;
	private MethodsJni methodsJni = new MethodsJni();
	public static UserLoginActivity ula;
	private ImageView iv_splash;
	private boolean isExit;
	private ImageView mDeleteUser, mDeletePwd;
	//
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */

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
							loginError = true;
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
		mbr=new MyBroadcastReceiver();
		IntentFilter iFilter=new IntentFilter();
		iFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(mbr, iFilter);
		return R.layout.activity_user_login;
	}

	/**
	 * 初始化控件
	 */
	@Override
	public void initView() {
		ula = this;
		iv_splash = (ImageView) findViewById(R.id.iv_splash);
		iv_splash.setOnClickListener(this);
		mEtUserAccount = (EditText) findViewById(R.id.et_userAccount_UserLoginActivity);
		mEtUserpassword = (EditText) findViewById(R.id.et_userPassword_UserLoginActivity);
		mBtnLogin = (Button) findViewById(R.id.btn_userLogin_UserLoginActivity);
		mDeleteUser = (ImageView) findViewById(R.id.iv_delete_userAccount);  //清空用户名和密码
		mDeletePwd = (ImageView) findViewById(R.id.iv_delete_pwd);    //只清除密码

		mEtUserAccount.setText(SharedPreferencesUtils.getUsername(this));
		mEtUserAccount.setSelection(mEtUserAccount.getText().length());
		mEtUserAccount.setOnFocusChangeListener(this);	//EidtText焦点监听

		mEtUserpassword.setText(SharedPreferencesUtils.getUserpassword(this));
		mEtUserpassword.setSelection(mEtUserpassword.getText().length());
		mEtUserpassword.setOnFocusChangeListener(this);
	}


	/**
	 * 输入框焦点改变监听
     */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch(v.getId()) {
			case R.id.et_userAccount_UserLoginActivity:
				if(hasFocus && !TextUtils.isEmpty(mEtUserAccount.getText())){
					mDeleteUser.setVisibility(View.VISIBLE);
				}else{
					mDeleteUser.setVisibility(View.INVISIBLE);
				}
				break;
			case R.id.et_userPassword_UserLoginActivity:
				if(hasFocus && !TextUtils.isEmpty(mEtUserpassword.getText())){
					mDeletePwd.setVisibility(View.VISIBLE);
				}else{
					mDeletePwd.setVisibility(View.INVISIBLE);
				}
				break;
			default:
				break;
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
				if(mEtUserAccount.isFocused() && !TextUtils.isEmpty(mEtUserAccount.getText())){
					mDeleteUser.setVisibility(View.VISIBLE);
				}else{
					mDeleteUser.setVisibility(View.INVISIBLE);
				}
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
				if(mEtUserpassword.isFocused() && !TextUtils.isEmpty(mEtUserpassword.getText())){
					mDeletePwd.setVisibility(View.VISIBLE);
				}else{
					mDeletePwd.setVisibility(View.INVISIBLE);
				}
			}
		});



		mDeletePwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEtUserpassword.setText("");
				mDeletePwd.setVisibility(View.INVISIBLE);
			}
		});

		mDeleteUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEtUserpassword.setText("");
				mEtUserAccount.setText("");
				mDeleteUser.setVisibility(View.INVISIBLE);
			}
		});


		mBtnLogin.setOnClickListener(new NoDoubleClickListener() {
			@Override
			public void onNoDoubleClick(View v) {
				loginError = false;
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
				MethodsJni.callProxyFun((HttpInterface) mContext, CST_JS.JS_ProxyName_Login, CST_JS.JS_Function_Login_login, CST_JS.getJsonStringForLogin(userAccount, userPassword));
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
		isExit = getIntent().getBooleanExtra(MyConstant.isExit, false);
		if (isExit) {
			iv_splash.setVisibility(View.GONE);
		} else {
			String userAccount = mEtUserAccount.getText().toString().trim();
			String userPassword = mEtUserpassword.getText().toString().trim();
			if (MethodsData.isEmptyString(userAccount)) {
				iv_splash.setVisibility(View.GONE);
			} else {
				MethodsJni.callProxyFun((HttpInterface) mContext, CST_JS.JS_ProxyName_Login,
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
		if (iv_splash.getVisibility() == View.GONE) {
//			MethodsExtra.backdailog(mContext);
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				MyUtils.removeActivityFromList();
				MyUtils.removeActivityFromAllList();
			}
		} else {
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
		MyDialog.Builder myDialog = new MyDialog.Builder(this);
		myDialog.setTitle("提示");
		myDialog.setMessage(msg);
		myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownloadApp ad = new DownloadApp(UserLoginActivity.this);
				String filePath=Environment.getExternalStorageDirectory().getPath() + "/vocinno/appdownload";
				File file = new File(filePath);
				if(!file.exists()) {
					file.mkdirs();
				}
				/*ad.setPath(filePath);
				ad.showDownloadDialog();*/
				showDownloadDialog();
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
			if ("0".equals(jReturn.getCode())) {
				//MethodsExtra.toast(this,jReturn.getMsg());
				downloadApp(jReturn.getMsg());
			} else {
				msg.what = R.id.doFail;
				msg.obj = jReturn.getMsg();
			}
		}
		mHander.sendMessage(msg);
	}

	private void saveXml(final String empId, final String token) {
		new Thread() {
			@Override
			public void run() {
				addToken(empId, token);
			}
		}.start();
	}

	private void addToken(String empId, String token) {
		SharedPreferences spf = getSharedPreferences(SharedPre.xml_token_name, this.MODE_PRIVATE);
		SharedPreferences.Editor editor = spf.edit();
		editor.putString(SharedPre.empId, empId);
		editor.putString(SharedPre.token, token);
		editor.commit();
	}

	private String getToken() {
		SharedPreferences spf = getSharedPreferences(SharedPre.xml_token_name, this.MODE_PRIVATE);
		return spf.getString(SharedPre.token, null);
	}

	private String getEmpId() {
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
	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
				long dId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				if(!isRemove&&downId==dId&&downId>0){
					ses.shutdown();
					File apkfile =getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS+"/"+getText(R.string.apk_name).toString());
					if (!apkfile.exists()) {
						return;
					}else{
						mProgress.setProgress(100);
						tv_progress.setText("100");
					}
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
							"application/vnd.android.package-archive");
					context.startActivity(i);
//				Toast.makeText(UserLoginActivity.this, "下载完成！", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mbr!=null){
			unregisterReceiver(mbr);
		}
	}

	private ProgressBar mProgress;
	private TextView tv_progress;
	private MyBroadcastReceiver  mbr;
	private long downId;
	private boolean isRemove;
	private DownloadManager dm;
	private DownloadManager.Request request;
	private ScheduledExecutorService ses;
	private MyDialog alterDialog;
	private int  downLoadFileSize=0;
	private int  sizeTotal,progress;
	public void showDownloadDialog() {
		if(isNetworkConnected(this)){
			File file =getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS+"/"+getText(R.string.apk_name).toString());
			if (file.exists()) {
				file.delete();
			}
			dm= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
			request = new DownloadManager.Request(Uri.parse(getText(R.string.download_url).toString()));
			request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,getText(R.string.apk_name).toString());
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
			final LayoutInflater inflater = LayoutInflater.from(this);
			View v = inflater.inflate(R.layout.progress_update_client, null);
			mProgress = (ProgressBar) v.findViewById(R.id.progress);
			tv_progress = (TextView) v.findViewById(R.id.tv_progress);
			tv_progress.setText("0");
			/*****************************************/
			MyDialog.Builder myDialog=new MyDialog.Builder(this);
			myDialog.setTitle("软件版本更新");
			myDialog.setMessage(null);
			myDialog.setContentView(v);
			myDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			myDialog.setDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					ses.shutdown();
					isRemove=true;
					dm.remove(downId);
				}
			});
			alterDialog = myDialog.create();
			alterDialog.show();
			downloadApk();
		}else{
			MyToast.showToast(this, "当前无网络连接,请稍后再试");
			return;
		}

	}

	private void downloadApk() {
		try {
			isRemove=false;
			downId=dm.enqueue(request);
		}catch (Exception e){
			MyToast.showToast(this,"下载管理器被停用,请开启之后再试");
			return;
		}
		 ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				DownloadManager.Query query=new DownloadManager.Query();
				query.setFilterById(downId);
				Cursor cursor=dm.query(query);
				if(cursor.moveToNext()){
					downLoadFileSize=cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
					sizeTotal=cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
				}
				cursor.close();
				if(sizeTotal>0&&downLoadFileSize>0){
					progress=(int) (((float)downLoadFileSize / sizeTotal) * 100);
					mProgress.setProgress(progress);
					tv_progress.postDelayed(new Runnable() {
						@Override
						public void run() {
							tv_progress.setText(progress + "");
						}
					},100);
				}
			}
		},0,500, TimeUnit.MILLISECONDS);
	}

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected())
			{
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return info.isAvailable();
				}
			}
		}
		return false;
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
