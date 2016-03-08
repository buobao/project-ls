package com.vocinno.utils;

import java.util.ArrayList;
import java.util.List;

import com.vocinno.centanet.housemanage.HouseType;

import android.R.integer;
import android.graphics.Bitmap;
import android.os.Bundle;

public class MethodsDeliverData {
	// flag
	public static int flag = -1;
	public static int flag1 = -1;
	// 字符串
	public static String string = null;
	// 普通传递(一般情况下，特殊情况下均可以使用)
	public static Bundle bundle = null;
	// 用于向WebViewActivity传递参数(网址)
	public static String currentWebViewURL = null;
	// 用于向VideoPlayerActivity传递播放参数(url)
	public static String currentVideoURL = null;
	// 用于拍照完毕后向预览界面传递图片
	public static Bitmap mBitmap = null;
	// 图片选择完毕后返回的图片路径
	public static List<String> mListImagePath = new ArrayList<String>();
	// 钥匙管理进入房源详情的几种途径
	public static int mKeyType = -1;
	// 侧边栏进入的几种途径
		public static int mSidebarType = -1;
	// 进入实堪的列表标记
	public static String mHouseType = "";
	//
	public static List<String> mListCitImagePathList = null;
	// 裁剪后的图片
	public static List<String> mListImages = new ArrayList<String>();
	// 是否是我的客源
	public static boolean isMyCustomer = true;
	// 是否是我的房源
	public static int mIntHouseType = HouseType.NONE;
	// 实堪中的照片描述
	public static List<String> mListImageDescription = new ArrayList<String>();
	//本次已经添加了多少张图片
	public static int hasImageNum = 0;

	// 修改或实堪描述页面传递数据
	public static String mEditorImage = null;
	public static String mEditorImageDescriptionString = null;
	public static String mChangedImageDescriptionString = null;
	//房源列表到房源详情间传递的数据
	public static String mDelCode=null;
}
