package com.vocinno.centanet.apputils;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.vocinno.centanet.apputils.utils.LocationUtil;
import com.vocinno.centanet.apputils.utils.MethodsJni;

import android.content.Context;

public final class AppInit {

	public static void init(final Context context) {
		// 系统崩溃日志输出
		AppCrashHandler.getInstance().init(context);

		// 百度地图初始化 : 首先用来获取经纬度
		SDKInitializer.initialize(context);

		// 极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(context);

		// 开启网络经纬度标记
		LocationUtil.startGetLocation(context);

		// 初始化jni,用于初始化js引擎
		MethodsJni.initJniLibrary(context);

	}
}
