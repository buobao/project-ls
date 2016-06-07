package com.vocinno.centanet.housemanage;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.HouseInMap;
import com.vocinno.centanet.model.HouseMapList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 地图详情
 * 
 * @author Administrator
 * 
 */
public class MapActivity extends OtherBaseActivity {
	public static String mDelType = "";//在房源列表更改参数

	private LatLng mLatLngMax;
	private LatLng mLatLngMin;

	// 百度地图
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner mMyListener = new MyLocationListenner();
	boolean mIsFirstLoc = true;// 是否首次定位

	private View mBackView;
	private LinearLayout mLlytBaseView;

	private boolean isGetPosition = false;

	private ArrayList<HouseInMap> mListPoiInfos;

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case R.id.doUpdate:
					refreshOverlays(msg.obj != null ? (ArrayList<HouseInMap>) msg.obj
							: null);
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_map;
	}

	@Override
	public void initView() {
		drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		mMapView = (MapView) findViewById(R.id.mapView_MapActivity);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(msu);
		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.house_near,
				null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		// 定位初始化
		mLocClient = new LocationClient(this);

		setListener();
	}

	public void setListener() {
		mBackView.setOnClickListener(this);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, null));
		mLocClient.registerLocationListener(mMyListener);
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				refreshData();
			}
		});
	}

	@Override
	public void initData() {

		isGetPosition = false;
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 开启定位功能
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void netWorkResult(String name, String className, Object data) {
		if (name.equals(CST_JS.NOTIFY_NATIVE_HOU_LIST_INMAP_RESULT)) {
			// 更新
			JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
					HouseMapList.class);
			if (jsReturn.getObject() != null) {
				Message msg = mHander.obtainMessage(R.id.doUpdate,
						((HouseMapList) jsReturn.getObject()).getMapPoint());
				mHander.sendMessage(msg);
			}
		}
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (mIsFirstLoc) {
				mIsFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	void refreshData() {
		LatLng maxPoint = mBaiduMap.getProjection().fromScreenLocation(
				new Point(0, 0));
		LatLng minPoint = mBaiduMap.getProjection().fromScreenLocation(
				new Point(getWindowManager().getDefaultDisplay().getWidth(),
						getWindowManager().getDefaultDisplay().getHeight()));
		Log.d("wan", "wanggsx getscreenloc:(" + minPoint.latitude + ","
				+ minPoint.longitude + ")" + "(" + maxPoint.latitude + ","
				+ maxPoint.longitude + ")");
		if (mLatLngMin != null
				&& mLatLngMax != null
				&& (mLatLngMin.latitude == minPoint.latitude && mLatLngMin.longitude == minPoint.longitude)) {
			// 没有移动则不刷新
			return;
		}
		mLatLngMax = maxPoint;
		mLatLngMin = minPoint;
		
		getHouseData(mDelType,
				mLatLngMin.latitude, mLatLngMax.latitude,
				mLatLngMin.longitude, mLatLngMax.longitude);
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
				CST_JS.JS_Function_HouseResource_getHouseInMap, CST_JS
						.getJsonStringForHouseListGetHouseInMap(mDelType,
								mLatLngMin.latitude, mLatLngMax.latitude,
								mLatLngMin.longitude, mLatLngMax.longitude));
		isGetPosition = true;
	}

	private void getHouseData(String mDelType, double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
		URL= NetWorkConstant.PORT_URL+ NetWorkMethod.houInMap;
		Map<String, String> map=new HashMap<String,String>();
		map.put(NetWorkMethod.delType, mDelType);
		map.put(NetWorkMethod.latMin, minLatitude+"");
		map.put(NetWorkMethod.latMax, maxLatitude+"");
		map.put(NetWorkMethod.attMin, minLongitude+"");
		map.put(NetWorkMethod.attMax, maxLongitude+"");
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
			}
			@Override
			public void onResponse(String response) {
				JSReturn jsReturn = MethodsJson.jsonToJsReturn(response,HouseMapList.class);
				if(jsReturn.isSuccess()){
					if (jsReturn.getObject() != null) {
						ArrayList<HouseInMap> mapPoint = ((HouseMapList) jsReturn.getObject()).getMapPoint();
						refreshOverlays(mapPoint != null ? mapPoint: null);
					}
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}


	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	void refreshOverlays(ArrayList<HouseInMap> mPoiInfoList) {
		// // 创造测试数据
		// mPoiInfoList = new ArrayList<HouseInMap>();
		// HouseInMap map = new HouseInMap();
		// map.setName("延吉大楼");
		// map.setLat("" + (31.182 + 0.003 * 0));
		// map.setAtt("" + (121.48461 - 0.0003 * 0));
		// map.setEntityId("37");
		// map.setCount("14");
		// mPoiInfoList.add(map);
		//
		// map = new HouseInMap();
		// map.setName("桥升公寓");
		// map.setLat("" + (31.182 + 0.003 * 1));
		// map.setAtt("" + (121.48461 - 0.0003 * 1));
		// map.setEntityId("39");
		// map.setCount("3");
		// mPoiInfoList.add(map);
		//
		// map = new HouseInMap();
		// map.setName("明光公寓");
		// map.setLat("" + (31.182 + 0.003 * 2));
		// map.setAtt("" + (121.48461 - 0.0003 * 2));
		// map.setEntityId("43");
		// map.setCount("7");
		// mPoiInfoList.add(map);
		//
		// map = new HouseInMap();
		// map.setName("广灵二村");
		// map.setLat("" + (31.182 + 0.003 * 3));
		// map.setAtt("" + (121.48461 - 0.0003 * 3));
		// map.setEntityId("44");
		// map.setCount("11");
		// mPoiInfoList.add(map);
		//
		// map = new HouseInMap();
		// map.setName("汶水东路77弄");
		// map.setLat("" + (31.182 + 0.003 * 4));
		// map.setAtt("" + (121.48461 - 0.0003 * 4));
		// map.setEntityId("49");
		// map.setCount("8");
		// mPoiInfoList.add(map);
		//
		// map = new HouseInMap();
		// map.setName("平和大楼");
		// map.setLat("" + (31.182 + 0.003 * 5));
		// map.setAtt("" + (121.48461 - 0.0003 * 5));
		// map.setEntityId("51");
		// map.setCount("1");
		// mPoiInfoList.add(map);

		if (mListPoiInfos == null || mListPoiInfos.size() == 0) {
			mListPoiInfos = mPoiInfoList;
			mBaiduMap.clear();
			addOverLayMarkers(mPoiInfoList);
			mListPoiInfos = mPoiInfoList;
		} else {
			mBaiduMap.clear();
			addOverLayMarkers(mPoiInfoList);
			mListPoiInfos = mPoiInfoList;
			// ArrayList<HouseInMap> listAdd = new ArrayList<HouseInMap>();
			// ArrayList<HouseInMap> listRemove = new ArrayList<HouseInMap>();
			// // 计算哪些标记需要添加，哪些需要移除
			//
			// addOverLayMarkers(listAdd);
			// removeOverLayMarker(listRemove);
		}
	}

	void addOverLayMarkers(ArrayList<HouseInMap> mPoiInfoList) {
		for (int i = 0; i < mPoiInfoList.size(); i++) {
			View view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.widget_bdmap_maker, null);
			TextView houseName = (TextView) view
					.findViewById(R.id.tv_hotel_price);
			houseName.setText(mPoiInfoList.get(i).getName());
			BitmapDescriptor markerIcon = BitmapDescriptorFactory
					.fromBitmap(getViewBitmap(view));
			Bundle bundle = new Bundle();
			bundle.putInt("marker_id",
					Integer.parseInt(mPoiInfoList.get(i).getEntityId()));
			OverlayOptions houseMarker = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(mPoiInfoList.get(i)
									.getLat()), Double.parseDouble(mPoiInfoList
									.get(i).getAtt()))).icon(markerIcon)
					.zIndex(9).draggable(false).extraInfo(bundle);
			mBaiduMap.addOverlay(houseMarker);
		}
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				Bundle bundle = marker.getExtraInfo();
				int id = bundle.getInt("marker_id");
				Toast.makeText(MapActivity.this, "entityId:" + id,
						Toast.LENGTH_SHORT).show();
				MethodsDeliverData.bundle = new Bundle();
				MethodsDeliverData.bundle.putInt("house_id", id);
				MethodsDeliverData.bundle.putString("type", "from_map");
				finish();
				return true;
			}
		});
	}

	void removeOverLayMarker(ArrayList<HouseInMap> mPoiInfoList) {

	}

	// 截取图片作为Maker
	private Bitmap getViewBitmap(View addViewContent) {
		addViewContent.setDrawingCacheEnabled(true);
		addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(),
				addViewContent.getMeasuredHeight());
		addViewContent.buildDrawingCache();
		Bitmap cacheBitmap = addViewContent.getDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		cacheBitmap.recycle();
		return bitmap;
	}

	public void notifCallBack(String name, String className, Object data) {

	}
}
