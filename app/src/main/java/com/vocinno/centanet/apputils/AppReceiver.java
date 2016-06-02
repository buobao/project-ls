package com.vocinno.centanet.apputils;

import cn.jpush.android.api.JPushInterface;

import com.vocinno.centanet.apputils.utils.MethodsNetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 此广播采用静态注册方式进行注册
 * 
 * @author wanggsx
 * 
 */
public class AppReceiver extends BroadcastReceiver {
	final String SYSTEM_DIALOG_REASON_KEY = "reason";
	final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();

		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {

			String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

			if (reason != null && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
				// 自己随意控制程序，关闭...
				Toast.makeText(context, "MyAppReceiver:come in",
						Toast.LENGTH_SHORT).show();
			}
		} else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			MethodsNetwork.refreshAPNType(context);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Toast.makeText(
					context,
					"收到了自定义消息。消息内容是："
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE),
					Toast.LENGTH_SHORT).show();
			// 自定义消息不会展示在通知栏，完全要开发者写代码去处理
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Toast.makeText(context, "收到了通知", Toast.LENGTH_SHORT).show();
			// 在这里可以做些统计，或者做些其他工作
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Toast.makeText(context, "用户点击打开了通知", Toast.LENGTH_SHORT).show();
		}
	}
}
