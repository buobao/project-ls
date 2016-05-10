package com.vocinno.centanet.apputils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.tools.MyLoadDialog;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.utils.MethodsJni;

import cn.jpush.android.api.JPushInterface;

public abstract class SuperActivity extends Activity implements OnClickListener {
	public static String TAG = null;
	public Activity mContext = null;
	public Handler mHander = null;
	public static View mRootView = null;
	public ModelDialog modelDialog;
	public OkHttpClientManager OKHCM;
	public String URL;
	public AppApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp=(AppApplication)getApplication();
		MyConstant.setMyApp(myApp);
		MyToast.getInstance(this);
		MyLoadDialog.getInstance(this);
		AppInstance.mListActivitys.add(this);
		mContext = this;
		TAG = this.getClass().getName();
		mRootView = LayoutInflater.from(this).inflate(setContentLayoutId(),
				null);
		setContentView(mRootView);
		mHander = setHandler();
		initView();
		setListener();
		initData();

	}

	public abstract Handler setHandler();

	public abstract int setContentLayoutId();

	public abstract void initView();

	public abstract void setListener();

	public abstract void initData();

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		Log.d(TAG, "输出Context 类：" + mContext.getClass().getName());
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onDestroy() {
		Activity activity = AppInstance.mListActivitys
				.get(AppInstance.mListActivitys.size() - 1);
		if (activity.getClass() == this.getClass()) {
			AppInstance.mListActivitys
					.remove(AppInstance.mListActivitys.size() - 1);
		}
		MethodsJni.removeAllNotifications(TAG);
		System.gc();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		onBack();
	}

	public abstract void onBack();

	// 数据回调函数(来自通知)
	public abstract void notifCallBack(final String name,
			final String className, final Object data);

	public String setTAG() {
		// TODO Auto-generated method stub
		return null;
	}
	public void showDialog(){
		if(this.modelDialog==null){
			this.modelDialog=ModelDialog.getModelDialog(this);
		}
		this.modelDialog.show();
	}
	public void dismissDialog(){
		if(this.modelDialog!=null&&this.modelDialog.isShowing()){
			this.modelDialog.dismiss();
		}
	}
}
