package com.vocinno.centanet;

import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.home.HomeActivity;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.apputils.utils.MethodsExtra;
import com.vocinno.centanet.apputils.utils.MethodsFile;
import com.vocinno.centanet.apputils.utils.MethodsJni;
import com.vocinno.centanet.apputils.utils.MethodsJson;
import com.vocinno.centanet.apputils.utils.MethodsNetwork;
import com.vocinno.centanet.apputils.utils.MethodsThirdParty;
import com.zbar.lib.CaptureActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends SuperActivity implements OnClickListener {
	private EditText mEtText, mEtUrl;
	private Button mBtnSaveToFile, mBtnOpenWeb, mBtnLoginByQQ, mBtnScan;
//
	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.doSuccess:
					MethodsExtra.toast(mContext, "登录成功");
					MethodsExtra.startActivity(mContext, HomeActivity.class);
					finish();
				case R.id.doFail:
				default:
					break;
				}
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	public void initView() {
		mContext = MainActivity.this;
		mEtText = (EditText) findViewById(R.id.et_Text_mainActivity);
		mBtnSaveToFile = (Button) findViewById(R.id.btn_SaveFile_mainActivity);
		mEtUrl = (EditText) findViewById(R.id.et_WebViewUrl_mainActivity);
		mBtnOpenWeb = (Button) findViewById(R.id.btn_OpenWeb_mainActivity);
		mBtnLoginByQQ = (Button) findViewById(R.id.btn_LoginByQQ_mainActivity);
		mBtnScan = (Button) findViewById(R.id.btn_Scan_mainActivity);
	}

	@Override
	public void setListener() {
		mBtnSaveToFile.setOnClickListener(this);
		mBtnOpenWeb.setOnClickListener(this);
		mBtnLoginByQQ.setOnClickListener(this);
		mBtnScan.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// 初始化app
		mEtUrl.setText("http://blog.csdn.net/wanggsx918");
		MethodsJni.addNotificationObserver(CST_JS.NOTIFY_NATIVE_LOGIN_RESULT,
				TAG);
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_Login,
				CST_JS.JS_Function_Login_login,
				CST_JS.getJsonStringForLogin("tanmy1", "1"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_SaveFile_mainActivity:
			// 保存输入框内文字到文件
			MethodsFile.writeFile(MethodsFile.getAutoFileDirectory()
					+ "wanggsx.txt", mEtText.getText().toString());
			break;
		case R.id.btn_OpenWeb_mainActivity:
			MethodsNetwork.openWebView(this, mEtUrl.getText().toString());
			break;
		case R.id.btn_LoginByQQ_mainActivity:
			MethodsThirdParty.loginInFromQQ(this);
			break;
		case R.id.btn_Scan_mainActivity:
			startActivity(new Intent(MainActivity.this, CaptureActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onBack() {
		MethodsExtra.backdailog(this);
	}

	private boolean a = false;

	@Override
	public void notifCallBack(String name, String className, Object data) {
		Log.d("MainActivity", "main notifCallBack " + data);
		JSReturn jReturn = MethodsJson.jsonToJsReturn((String) data,
				HouseList.class);
		if (jReturn.isSuccess()) {
			mHander.sendEmptyMessage(R.id.doSuccess);
		} else {
			mHander.sendEmptyMessage(R.id.doFail);
		}
	}
}
