package com.vocinno.centanet;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SharedPreferencesUtils;
import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.home.HomeActivity;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.user.UserLoginActivity;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.MethodsNetwork;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.content.Intent;

public class StartActivity extends SuperActivity {
	private boolean mFirstFinish; // 判断是否第一次打开软件
	private Animation mAnimation;
	private boolean mIsLoginedJustNow = false;

	@Override
	protected void onStart() {
		super.onStart();
		into();
	}

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.doSuccess:
					if (!mIsLoginedJustNow) {
						mIsLoginedJustNow = true;
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
		return R.layout.activity_start;
	}

	@Override
	public void initView() {
	}

	@Override
	public void setListener() {
	}

	@Override
	public void initData() {
		MethodsNetwork.refreshAPNTypeInMainThread(this);
	}

	// 进入主程序的方法
	private void into() {
		if (MethodsNetwork.netWorkType >= 1) {
			// 如果网络可用则判断是否第一次进入，如果是第一次则进入欢迎界面
			mFirstFinish = SharedPreferencesUtils.getIsFirstStartFinish(this);
			// 设置动画效果是alpha，在anim目录下的alpha.xml文件中定义动画效果
			mAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);
			// 给view设置动画效果
			mRootView.startAnimation(mAnimation);
			mAnimation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
				}

				// 这里监听动画结束的动作，在动画结束的时候开启一个线程，这个线程中绑定一个Handler,并
				// 在这个Handler中调用goHome方法，而通过postDelayed方法使这个方法延迟500毫秒执行，达到
				// 达到持续显示第一屏500毫秒的效果
				@Override
				public void onAnimationEnd(Animation arg0) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Intent intent;
							// 如果第一次，则进入引导页WelcomeActivity
							if (SharedPreferencesUtils.getIsLogined(mContext)) {
								Log.d("wanggsx", "wanggsxtagstartactivity 注册通知");
								MethodsJni.addNotificationObserver(
										CST_JS.NOTIFY_NATIVE_LOGIN_RESULT, TAG);
								MethodsJni
										.callProxyFun(
												CST_JS.JS_ProxyName_Login,
												CST_JS.JS_Function_Login_login,
												CST_JS.getJsonStringForLogin(
														SharedPreferencesUtils
																.getUsername(mContext),
														SharedPreferencesUtils
																.getUserpassword(mContext)));
							} else if (mFirstFinish) {
								intent = new Intent(StartActivity.this,
										UserLoginActivity.class);
								startActivity(intent);
								// 设置Activity的切换效果
								overridePendingTransition(R.anim.in_from_right,
										R.anim.out_to_left);
								StartActivity.this.finish();
							} else {
								intent = new Intent(StartActivity.this,
										WelcomeActivity.class);
								startActivity(intent);
								// 设置Activity的切换效果
								overridePendingTransition(R.anim.in_from_right,
										R.anim.out_to_left);
								StartActivity.this.finish();
							}
						}
					}, 0);
				}
			});
		} else {
			// 没有网络
			MethodsExtra.toast(this, null);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBack() {
		MethodsExtra.backdailog(mContext);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		Log.d("wanggsx", "wanggsxtagstartactivity name:" + name + " className:"
				+ className);
		JSReturn jReturn = MethodsJson.jsonToJsReturn((String) data,
				HouseList.class);
		Message msg = new Message();
		if (jReturn.isSuccess()) {
			msg.what = R.id.doSuccess;
			msg.obj = jReturn.getMsg();
		} else {
			msg.what = R.id.doFail;
			msg.obj = jReturn.getMsg();
		}
		mHander.sendMessage(msg);
	}
}
