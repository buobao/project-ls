package com.vocinno.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class StartActivityUtil {

	/**
	 * @param mContext
	 * @param targetActivity
	 *            例：AppStartUtil.startActivity(MainActivity.this,
	 *            LoginActivity.class.getName());
	 */
	public static void startActivity(Context mContext, Class targetActivity) {
		Intent intent = new Intent(mContext, targetActivity);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		mContext.startActivity(intent);
	}

	public static void startActivity(Context mContext, Class targetActivity,
			int flag) {
		Intent intent = new Intent(mContext, targetActivity);
		intent.setFlags(flag);
		mContext.startActivity(intent);
	}

	public static void startActivityForResult(Activity activity,
			int requestCode, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void startActivityForResult(Activity activity,
			Class targetActivity, int requestCode) {
		Intent intent = new Intent(activity, targetActivity);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void startActivity(Context mContext, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		mContext.startActivity(intent);
	}
}
