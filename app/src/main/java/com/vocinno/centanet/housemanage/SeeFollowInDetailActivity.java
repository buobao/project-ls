package com.vocinno.centanet.housemanage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.model.HouseDetail;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.SeeFollowIn;
import com.vocinno.utils.CustomUtils;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SeeFollowInDetailActivity extends SuperSlideMenuActivity {
	private View mViewBack, mSubmitView;
	private TextView mHouseCode;
	private EditText mRemark, mCustCode, mLookCode;
	private HouseDetail mHouseDetail = null;
	private SeeFollowIn mSeeFollowIn;
	private TextView tv_startTime, tv_endTime;
	private MyDialog dialog;
	private View dialogView;
	private WheelView wv_year,wv_month,wv_day,wv_hour,wv_min;
	private String dayText;
	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				SeeFollowInDetailActivity.this.closeMenu(msg);
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_see_follow_in_house;
	}

	@Override
	public void initView() {
		dialogView = getLayoutInflater().inflate(R.layout.time_dialog, null);
		wv_year= (WheelView) dialogView.findViewById(R.id.wv_year);
		wv_month= (WheelView) dialogView.findViewById(R.id.wv_month);
		wv_day= (WheelView) dialogView.findViewById(R.id.wv_day);
		wv_hour= (WheelView) dialogView.findViewById(R.id.wv_hour);
		wv_min= (WheelView) dialogView.findViewById(R.id.wv_min);
		MethodsExtra.findHeadTitle1(mContext, mRootView,
				R.string.followin_look, null);
		mViewBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mSubmitView = MethodsExtra.findHeadRightView1(mContext, mRootView, 0,
				R.drawable.universal_button_undone);
		mHouseCode = (TextView) findViewById(R.id.tv_housecode_SeeFollowInDetailActivity);
		mCustCode = (EditText) findViewById(R.id.tv_custcode_SeeFollowInDetailActivity);
		mLookCode = (EditText) findViewById(R.id.tv_lookcode_SeeFollowInDetailActivity);
		mRemark = (EditText) findViewById(R.id.tv_remark_SeeFollowInDetailActivity);
		mRemark.setGravity(Gravity.LEFT);

		tv_startTime = (TextView) findViewById(R.id.tv_startTime);
		tv_startTime.setOnClickListener(this);
		tv_endTime = (TextView) findViewById(R.id.tv_endTime);
		tv_endTime.setOnClickListener(this);
	}

	@Override
	public void setListener() {
		mViewBack.setOnClickListener(this);
		mSubmitView.setOnClickListener(this);

		mSubmitView.setClickable(false);

		mCustCode.addTextChangedListener(new TextWatcher() {

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
				Log.d("wan", "onTextChanged start:before:count " + start + ":"
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
				}
				checkIsFinish();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				String string = mRemark.getText().toString();
				Log.d("wan",
						"wanggsx beforeTextChanged len = " + string.length());
				if (string.length() == 500) {
					strBeforeText = string;
					lastEndIndex = Selection.getSelectionEnd(mRemark.getText());
				} else {
					strBeforeText = null;
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}
	public ArrayList<String> getYear(){
		Calendar c = Calendar.getInstance();//首先要获取日历对象
		int mYear = c.get(Calendar.YEAR); // 获取当前年份
		ArrayList<String> list=new ArrayList<String>();
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
		}else if(m==4||m==6||m==9||m==12){
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
		wv_year.setSelectItem(0);

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
		mHouseCode.setText(MethodsDeliverData.mDelCode);
		// 注册通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_ADD_HOU_CUST_TRACK_RESULT, TAG);
	}

	private void checkIsFinish() {
		Boolean isFinish = true;
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
		} else {
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
		}
		if (isFinish) {
			mSubmitView = MethodsExtra.findHeadRightView1(mContext, mRootView,
					0, R.drawable.universal_button_done);
			mSubmitView.setClickable(true);
		} else {
			mSubmitView = MethodsExtra.findHeadRightView1(mContext, mRootView,
					0, R.drawable.universal_button_undone);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_submit:
			dialog.dismiss();
			break;
		case R.id.bt_cancel:
			dialog.dismiss();
			break;
		case R.id.tv_startTime:
			wheelViewSetData();
			if(dialog==null){
				dialog=new MyDialog(this);
			}
			dialogView.findViewById(R.id.bt_cancel).setOnClickListener(this);
			dialogView.findViewById(R.id.bt_submit).setOnClickListener(this);
			dialog.setContentView(dialogView);
			setDialogFullWidth();
			dialog.show();

			break;
		case R.id.tv_endTime:
			wheelViewSetData();
			if(dialog==null){
				dialog=new MyDialog(this);
			}
			dialogView.findViewById(R.id.bt_cancel).setOnClickListener(this);
			dialogView.findViewById(R.id.bt_submit).setOnClickListener(this);
			dialog.setContentView(dialogView);
			setDialogFullWidth();
			dialog.show();
			break;
		case R.id.img_left_mhead1:
			onBack();
			break;
		case R.id.img_right_mhead1:
			String string = CST_JS.getJsonStringForAddHouCustomerTrack(
					MethodsDeliverData.mDelCode,
					mCustCode.getText().toString(), mLookCode.getText()
							.toString(), mRemark.getText().toString());
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
					CST_JS.JS_Function_HouseResource_addHouCustomerTrack,
					string);
			Log.d(TAG, "AddHouCustomerTrack:" + string);
			break;

		default:
			break;
		}
		checkIsFinish();
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

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		String strJson = (String) data;
		JSReturn jsReturn = MethodsJson.jsonToJsReturn(strJson,
				SeeFollowIn.class);
		if (jsReturn.isSuccess()) {
			MethodsExtra.toast(mContext, "保存成功");
			finish();
		} else {
			MethodsExtra.toast(mContext, jsReturn.getMsg());
		}
	}

}
