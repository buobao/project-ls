package com.vocinno.centanet.apputils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
	private static SharedPreferencesUtils mInstance = null;
	private static Context mContext;
	private static SharedPreferences mSp;
	private static String mStrSPname = "SP_CENTANET";
	private static String mUserName = "USERNAME";
	private static String mUserId = "USERID";
	private static String mUserPwd = "USERPASSWORD";
	private static String mIsLogined = "ISLOGINED";
	// --------------定义的字段(Key_数据类型_名称)--------------
	// 是否是第一次启动完毕
	private static String Key_Bool_IsFirstStartFinish = "IS_FIRST_START_FINISH";

	// --------------定义的字段---------------

	/**
	 * 初始化:每个get和set方法必须调用，外部不可见
	 * 
	 * @param context
	 */
	private static void init(Context context) {
		if (mContext == null || mSp == null) {
			mContext = context;
			mSp = context
					.getSharedPreferences(mStrSPname, Context.MODE_PRIVATE);
		}
	}

	/**
	 * 设置是否完成了第一次启动(展示欢迎页面)
	 * 
	 * @param context
	 * @param tf
	 */
	public static void setIsFirstStartFinish(Context context, boolean tf) {
		init(context);
		mSp.edit().putBoolean(Key_Bool_IsFirstStartFinish, tf).commit();
	}

	public static boolean getIsFirstStartFinish(Context context) {
		init(context);
		return mSp.getBoolean(Key_Bool_IsFirstStartFinish, false);
	}

	// 清理所有缓存字段
	public static void ClearAll(Context context) {
		init(context);
		mSp.edit().clear().commit();
	}

	public static void setLoginIn(Context context, String username, String pwd,
			String userId) {
		init(context);
		mSp.edit().putString(mUserName, username).commit();
		mSp.edit().putString(mUserPwd, pwd).commit();
		mSp.edit().putString(mUserId, userId).commit();
		mSp.edit().putBoolean(mIsLogined, true).commit();
	}

	public static boolean getIsLogined(Context context) {
		init(context);
		return mSp.getBoolean(mIsLogined, false);
	}

	public static String getUsername(Context context) {
		init(context);
		return mSp.getString(mUserName, "");
	}

	public static String getUserId(Context context) {
		init(context);
		return mSp.getString(mUserId, "");
	}

	public static String getUserpassword(Context context) {
		init(context);
		return mSp.getString(mUserPwd, "");
	}

	public static void setLoginOut(Context context) {
		init(context);
		mSp.edit().putBoolean(mIsLogined, false).commit();
		mSp.edit().putString(mUserName, "").commit();
		mSp.edit().putString(mUserPwd, "").commit();
	}
}
