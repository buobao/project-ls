package com.vocinno.centanet.housemanage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.model.HouseDetail;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.SeeFollowIn;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.CustomUtils;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeeFollowInDetailActivity extends OtherBaseActivity {
	private View mViewBack, mSubmitView;
	private TextView mHouseCode,mCustCode;
	private EditText mRemark, mLookCode;
	private HouseDetail mHouseDetail = null;
	private SeeFollowIn mSeeFollowIn;
	private TextView tv_startTime, tv_endTime;
	private MyDialog dialog;
	private View dialogView;
	private WheelView wv_year,wv_month,wv_day,wv_hour,wv_min;
	private String dayText;
	private ImageView iv_start_time_clear,iv_end_time_clear;
	private boolean isStartTime=false;
	private Long startTime,endTime;
	private CheckBox cb_huixie;
	private String mDelCode,houseCode;
	private View rootView;
	private String delegationType;
	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_see_follow_in_house;
	}

	@Override
	public void initView() {
		drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		dialogView = getLayoutInflater().inflate(R.layout.time_dialog, null);
		wv_year= (WheelView) dialogView.findViewById(R.id.wv_year);
		wv_month= (WheelView) dialogView.findViewById(R.id.wv_month);
		wv_day= (WheelView) dialogView.findViewById(R.id.wv_day);
		wv_hour= (WheelView) dialogView.findViewById(R.id.wv_hour);
		wv_min= (WheelView) dialogView.findViewById(R.id.wv_min);
		MethodsExtra.findHeadTitle1(mContext, baseView,
				R.string.followin_look, null);
		mViewBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		rootView=baseView;
//		mSubmitView = MethodsExtra.findHeadRightView1(mContext, baseView, 0,R.drawable.universal_button_undone);
		mSubmitView = MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
		mHouseCode = (TextView) findViewById(R.id.tv_housecode_SeeFollowInDetailActivity);
		mCustCode = (TextView) findViewById(R.id.tv_custcode_SeeFollowInDetailActivity);
		mCustCode.setOnClickListener(this);
		mLookCode = (EditText) findViewById(R.id.tv_lookcode_SeeFollowInDetailActivity);
		mRemark = (EditText) findViewById(R.id.tv_remark_SeeFollowInDetailActivity);
		mRemark.setGravity(Gravity.LEFT);
		cb_huixie = (CheckBox) findViewById(R.id.cb_huixie);

		iv_start_time_clear = (ImageView) findViewById(R.id.iv_start_time_clear);
		iv_start_time_clear.setOnClickListener(this);
		iv_end_time_clear = (ImageView) findViewById(R.id.iv_end_time_clear);
		iv_end_time_clear.setOnClickListener(this);

		tv_startTime = (TextView) findViewById(R.id.tv_startTime);
		tv_startTime.setOnClickListener(this);

		tv_endTime = (TextView) findViewById(R.id.tv_endTime);
		tv_endTime.setOnClickListener(this);

		TextView  tv_write_time = (TextView)findViewById(R.id.tv_write_time);
		tv_write_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		setListener();
	}

	public void setListener() {
		mViewBack.setOnClickListener(this);
		mSubmitView.setOnClickListener(this);

		mSubmitView.setClickable(false);
		mSubmitView.setEnabled(false);

		mLookCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				checkIsFinish();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mRemark.addTextChangedListener(new TextWatcher() {
			String strBeforeText = null;
			int lastEndIndex = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				/*Log.d("wan", "onTextChanged start:before:count " + start + ":"
						+ before + ":" + count);
				int selEndIndex = Selection.getSelectionEnd(mRemark.getText());
				String string = mRemark.getText().toString();
				if (string == null || string.trim().length() == 0) {

				} else {
					if (string.trim().length() > 500) {
						if (strBeforeText != null) {
							mRemark.setText(strBeforeText);
							Selection.setSelection(mRemark.getText(),
									lastEndIndex);
						} else {
							mRemark.setText(string.substring(0, 500));
							if (selEndIndex > 500) {
								selEndIndex = 500;
							}
							Selection.setSelection(mRemark.getText(),
									selEndIndex);
						}
						MethodsExtra.toast(mContext, "描述不能超过500字");
					}
				}*/
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				/*String string = mRemark.getText().toString();
				Log.d("wan",
						"wanggsx beforeTextChanged len = " + string.length());
				if (string.length() == 500) {
					strBeforeText = string;
					lastEndIndex = Selection.getSelectionEnd(mRemark.getText());
				} else {
					strBeforeText = null;
				}*/
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				checkIsFinish();
			}
		});
	}
	public ArrayList<String> getYear(){
		Calendar c = Calendar.getInstance();//首先要获取日历对象
		int mYear = c.get(Calendar.YEAR); // 获取当前年份
		ArrayList<String> list=new ArrayList<String>();
		list.add(mYear+1+"");
		list.add(mYear+"");
		list.add(mYear-1+"");
		list.add(mYear - 2 + "");
		list.add(mYear - 3 + "");
		int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
		int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
		Log.i("=====", "mDay" + mDay);
		int mHour = c.get(Calendar.HOUR_OF_DAY);//时
		int mMinute = c.get(Calendar.MINUTE);//分
		return list;
	}
	public ArrayList<String> getMonth(){
		ArrayList<String>list=new ArrayList<String>();
		for (int i=1;i<=12;i++){
			list.add(i+"");
		}
		return list;
	}
	public ArrayList<String> getDay(String year,String month){
		ArrayList<String>list=new ArrayList<String>();
		int y=Integer.parseInt(year);
		int m=Integer.parseInt(month);
		for(int i=1;i<=28;i++){
			list.add(i + "");
		}
		if(m==2){
			if((y % 4 == 0 && y % 100!=0)||y%400==0){
				list.add("29");
			}
		}else if(m==4||m==6||m==9||m==11){
			list.add("29");
			list.add("30");
		}else{
			list.add("29");
			list.add("30");
			list.add("31");
		}
		return list;
	}
	public ArrayList<String> getHour(){
		ArrayList<String>list=new ArrayList<String>();
		for (int i=1;i<=24;i++){
			list.add(i+"");
		}
		return list;
	}
	public ArrayList<String> getMin(){
		ArrayList<String>list=new ArrayList<String>();
		for (int i=0;i<=59;i++){
			if(i<=9){
				list.add("0"+i);
			}else{
				list.add(i+"");
			}
		}
		return list;
	}
	private void wheelViewSetData() {
		int wvWidth= (CustomUtils.getWindowWidth(this) - 120) / 5;
		final int wvWidth2= (CustomUtils.getWindowWidth(this) - 250) / 5;
		final Calendar c = Calendar.getInstance();

		wv_year.setWvWidth(wvWidth);
		wv_year.setData(getYear(), (CustomUtils.getWindowWidth(this) - 150) / 5);
		wv_year.setSelectItem(1);

		wv_month.setWvWidth(wvWidth2);
		wv_month.setData(getMonth(), (CustomUtils.getWindowWidth(this) - 150) / 5);
		wv_month.setSelectText((c.get(Calendar.MONTH) + 1) + "", 0);

		wv_day.setWvWidth(wvWidth2);
		wv_day.setData(getDay(wv_year.getSelectedText(), wv_month.getSelectedText()), wvWidth2);
		wv_day.setSelectText(c.get(Calendar.DAY_OF_MONTH) + "", 0);
		dayText=c.get(Calendar.DAY_OF_MONTH) + "";

		wv_hour.setWvWidth(wvWidth2);
		wv_hour.setData(getHour(), wvWidth2);
		wv_hour.setSelectText(c.get(Calendar.HOUR_OF_DAY) + "", 0);

		wv_min.setWvWidth(wvWidth2);
		wv_min.setData(getMin(), wvWidth2);
		if(c.get(Calendar.MINUTE)<=9){
			wv_min.setSelectText("0"+c.get(Calendar.MINUTE), 0);
		}else{
			wv_min.setSelectText(c.get(Calendar.MINUTE)+ "", 0);
		}
		wv_year.setOnSelectListener(new WheelView.OnSelectListener() {
			@Override
			public void endSelect(int id, String text) {
				if(wv_month.getSelectedText().equals("2")){
					wv_day.setData(getDay(text, wv_month.getSelectedText()), wvWidth2);
					int y=Integer.parseInt(text);
					if(Integer.parseInt(dayText)<=28){
						wv_day.setSelectText(dayText, 0);

					}else{
						if((y % 4 == 0 && y % 100!=0)||y%400==0){
							wv_day.setSelectText(dayText, 0);
						}else{
							wv_day.setSelectText("28", 0);
							dayText="28";
						}
					}
				}
			}
			@Override
			public void selecting(int id, String text) {

			}
		});
		wv_month.setOnSelectListener(new WheelView.OnSelectListener() {
			@Override
			public void endSelect(int id, String text) {
				wv_day.setData(getDay(wv_year.getSelectedText(), text), wvWidth2);
				int m=Integer.parseInt(text);
				int y=Integer.parseInt(wv_year.getSelectedText());
				if(Integer.parseInt(dayText)<=28){
					wv_day.setSelectText(dayText, 0);
				}else{
					if(m==2){
						if((y % 4 == 0 && y % 100!=0)||y%400==0){
							if(Integer.parseInt(dayText)>29){
								wv_day.setSelectText("29", 0);
								dayText="29";
							}else{
								wv_day.setSelectText(dayText, 0);
							}
						}else{
							wv_day.setSelectText("28", 0);
							dayText="28";
						}
					}else if(m==4||m==6||m==9||m==11){
						if(Integer.parseInt(dayText)>30){
							wv_day.setSelectText("30", 0);
							dayText="30";
						}else{
							wv_day.setSelectText(dayText, 0);
						}
					}else{
						wv_day.setSelectText(dayText, 0);
					}
				}
			}
			@Override
			public void selecting(int id, String text) {

			}
		});
		wv_day.setOnSelectListener(new WheelView.OnSelectListener() {
			@Override
			public void endSelect(int id, String text) {
				dayText=text;
			}
			@Override
			public void selecting(int id, String text) {
			}
		});


	}
	@Override
	public void initData() {
		methodsJni=new MethodsJni();
		methodsJni.setMethodsJni((HttpInterface)this);
		delegationType=getIntent().getStringExtra("delegationType");
		houseCode=getIntent().getStringExtra(MyConstant.houseCode);
		mHouseCode.setText(houseCode);
		mDelCode=MethodsDeliverData.mDelCode.substring(4,5);
		// 注册通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_ADD_HOU_CUST_TRACK_RESULT, TAG);
	}

	private void checkIsFinish() {
		boolean isFinish = true;
		if (mCustCode.getText() == null
				|| mCustCode.getText().toString().length() == 0) {
			isFinish = false;
		}
		if (mLookCode.getText() == null
				|| mLookCode.getText().toString().length() == 0) {
			isFinish = false;
		}
		if (mRemark.getText() == null) {
			isFinish = false;
		} else if (mRemark.getText().toString().length() < 10) {
			isFinish = false;
		} /*else {
			String string = mRemark.getText().toString();
			boolean isHasChinese = false;
			for (int i = 0; i < string.length(); i++) {
				isHasChinese = MethodsData.isChinese(string.charAt(i));
				if (isHasChinese) {
					break;
				}
			}
			if (!isHasChinese && string.length() < 20) {
				isFinish = false;
			}
		}*/
		if (isFinish) {
			Log.i("isFinish======","isFinish=="+isFinish);
//			mSubmitView = MethodsExtra.findHeadRightView1(mContext, rootView, 0, R.drawable.universal_button_done);
			mSubmitView.setEnabled(true);
			mSubmitView.setClickable(true);
		} else {
//			mSubmitView = MethodsExtra.findHeadRightView1(mContext, rootView,0, R.drawable.universal_button_undone);
			mSubmitView.setEnabled(false);
			mSubmitView.setClickable(false);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100&&resultCode==101){
			if(data!=null){
				String custCode = data.getStringExtra("custCode");
				mCustCode.setText(custCode);
				checkIsFinish();
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_custcode_SeeFollowInDetailActivity:
			Intent intent=new Intent(this, CustomerManageActivity.class);
			intent.putExtra("delegationType",delegationType);
			startActivityForResult(intent,100);
			break;
		case R.id.iv_start_time_clear:
			tv_startTime.setText(null);
			startTime=null;
			iv_start_time_clear.setVisibility(View.INVISIBLE);
			break;
		case R.id.iv_end_time_clear:
			tv_endTime.setText(null);
			endTime=null;
			iv_end_time_clear.setVisibility(View.INVISIBLE);
			break;
		case R.id.bt_submit:
			setDate();
			break;
		case R.id.bt_cancel:
			dialog.dismiss();
			break;
		case R.id.tv_startTime:
			wheelViewSetData();
			if(dialog==null){
				dialog=new MyDialog(this);
			}
			isStartTime=true;
			dialogView.findViewById(R.id.bt_cancel).setOnClickListener(this);
			dialogView.findViewById(R.id.bt_submit).setOnClickListener(this);
			dialog.setContentView(dialogView);
			dialog.setCanceledOnTouchOutside(false);
			setDialogFullWidth();
			dialog.show();

			break;
		case R.id.tv_endTime:
			wheelViewSetData();
			if(dialog==null){
				dialog=new MyDialog(this);
			}
			isStartTime=false;
			dialogView.findViewById(R.id.bt_cancel).setOnClickListener(this);
			dialogView.findViewById(R.id.bt_submit).setOnClickListener(this);
			dialog.setContentView(dialogView);
			dialog.setCanceledOnTouchOutside(false);
			setDialogFullWidth();
			dialog.show();
			break;
		case R.id.img_left_mhead1:
			finish();
			break;
		case R.id.tv_right_mhead1:
			if(isContinuousNumer(mRemark.getText().toString().trim())){
				MethodsExtra.toast(mContext,"不能连续输入7个以上数字");
				return;
			}
			String isBack="0";
			if(cb_huixie.isChecked()){
				isBack="1";
			}
			String string = CST_JS.getJsonStringForAddHouCustomerTrack(
					MethodsDeliverData.mDelCode,
					mCustCode.getText().toString(), mLookCode.getText()
							.toString(), mRemark.getText().toString(),startTime,endTime,isBack);
			/*MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_addHouCustomerTrack,
					string);*/
			saveLook(mCustCode.getText().toString(),mLookCode.getText()
					.toString(), mRemark.getText().toString(),startTime,endTime,isBack);
			Log.d(TAG, "AddHouCustomerTrack:" + string);
			break;

		default:
			break;
		}
//		checkIsFinish();
	}

	private void saveLook(String cCode,String mLookCode, String mRemark, Long startTime, Long endTime, String isBack) {
		Loading.show(this);
		URL= NetWorkConstant.PORT_URL+ NetWorkMethod.addLook;
		Map<String,String> map=new HashMap<String,String>();
		map.put(NetWorkMethod.custCode, cCode);
		map.put(NetWorkMethod.delCode,houseCode);
		map.put(NetWorkMethod.lookCode, mLookCode);
		map.put(NetWorkMethod.remark,mRemark);
		map.put(NetWorkMethod.startTime,startTime+"");
		map.put(NetWorkMethod.endTime,endTime+"");
		map.put(NetWorkMethod.isBackWrite,isBack);
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Loading.dismissLoading();
			}
			@Override
			public void onResponse(String response) {
				Loading.dismissLoading();
				JSReturn jsReturn = MethodsJson.jsonToJsReturn(response,SeeFollowIn.class);
				if (jsReturn.isSuccess()) {
					MyToast.showToast(jsReturn.getMsg());
					finish();
				} else {
					MyToast.showToast(jsReturn.getMsg());
				}
			}
		});
	}

	private boolean isNumeric(String str) {
		String reg = "^[0-9]$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	private boolean isContinuousNumer(String str){
		boolean flag=false;
		int j=0;
		for(int i=0;i<str.length();i++){
			if(isNumeric(str.substring(i,i+1))){
				j++;
				if(j>=7){
					flag=true;
					break;
				}
			}else{
				j=0;
			}
		}
		return flag;
	}
	private boolean CompareTimeSize(long time) {
		if(isStartTime){
			if(endTime!=null){//选择开始时间并且之前已经选了结束时间
				if(endTime-time<=0){
					MethodsExtra.toast(this,"开始时间应小于结束时间");
					return false;
				}
			}
		}else{
			if(startTime!=null){//选择结束时间并且之前已经选了开始时间
				if(time-startTime<=0){
					MethodsExtra.toast(this,"结束时间应大于开始时间");
					return false;
				}
			}
		}
		return  true;
	}

	private void setDialogFullWidth() {
		Window win = dialog.getWindow();
		win.setGravity(Gravity.BOTTOM);
		win.getDecorView().setPadding(0, 0, 0, 0);
		win.setBackgroundDrawableResource(android.R.color.transparent);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		win.setAttributes(lp);
	}

	private void showMenuDialog() {

	}

	public void notifCallBack(String name, String className, Object data) {

	}
	public Date setDate(){
		String year=wv_year.getSelectedText();
		String month=wv_month.getSelectedText();
		String day=wv_day.getSelectedText();
		String hour=wv_hour.getSelectedText();
		String min=wv_min.getSelectedText();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date parseDate = sdf.parse(year + "-" + month + "-" + day + " " + hour + ":" + min);
			String dateFormat = sdf.format(parseDate);
			if(isStartTime){

				if(CompareTimeSize(parseDate.getTime())){
					tv_startTime.setText(dateFormat);
					startTime=parseDate.getTime();
					Log.i("startTime=========","startTime"+startTime);
					iv_start_time_clear.setVisibility(View.VISIBLE);
					dialog.dismiss();
				}
			}else{
				if(CompareTimeSize(parseDate.getTime())){
					tv_endTime.setText(dateFormat);
					endTime=parseDate.getTime();
					iv_end_time_clear.setVisibility(View.VISIBLE);
					Log.i("endTime=========", "endTime" + endTime);
					dialog.dismiss();
				}
			}
			return parseDate;
		} catch (ParseException e) {
			e.printStackTrace();
			dialog.dismiss();
			return null;
		}
	}

	@Override
	public void netWorkResult(String name, String className, Object data) {
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}
}
