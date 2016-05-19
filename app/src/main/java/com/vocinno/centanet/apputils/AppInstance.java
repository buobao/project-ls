package com.vocinno.centanet.apputils;

import java.util.ArrayList;
import java.util.List;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.vocinno.centanet.housemanage.KeyHouseManageActivity;

import android.app.Activity;

public final class AppInstance {
	public static List<Activity> mListActivitys = new ArrayList<Activity>();
	
	public static KeyHouseManageActivity mHouseManageActivity = null;
	// 微信实例
	public static IWXAPI mWXAPI;

}
