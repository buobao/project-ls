package com.vocinno.centanet.apputils.utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.content.Context;
import android.util.Log;

public class LocationUtil {
	// 纬度
	private static double lat = 0;
	// 经度
	private static double lon = 0;

	static MyLocationListenner myListener = new MyLocationListenner();
	static LocationClient mLocClient;

	/**
	 * 由地理位置返回城市名称
	 * 
	 * @param context
	 * @return
	 */
	public static void startGetLocation(Context context) {
		// 定位初始化
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public static class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.d("wan",
					"wanggsx getLocationFromBaiDu " + location.getLongitude());
			// map view 销毁后不在处理新接收的位置
			if (location != null) {
				lat = location.getLatitude();
				lon = location.getLongitude();
				mLocClient.unRegisterLocationListener(myListener);
				mLocClient.stop();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 获取纬度
	 * 
	 * @return
	 */
	public static double getLatitude() {
		return lat;
	}

	/**
	 * 获取经度
	 * 
	 * @return
	 */
	public static double getLongitude() {
		return lon;
	}

}
