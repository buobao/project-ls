package com.vocinno.centanet.keymanage;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.KeyItem;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeyPinDetailActivity extends OtherBaseActivity implements View.OnClickListener{
	private View mBack;
	private ImageView iv_key_touxiang;
	private TextView tv_key_name,tv_key_pin,tv_pin_sure;
	private ProgressBar pb_key_time;
	private ScheduledExecutorService ses;
	private int progress=0;
	private int fixedProgress,numberProgress;
	private KeyItem keyItem;
	@Override
	public int setContentLayoutId() {
		return R.layout.activity_key_pin_detail;
	}

	@Override
	public void initView() {
		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.key_pin,
				null);
		mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		mBack.setOnClickListener(this);

		iv_key_touxiang= (ImageView) findViewById(R.id.iv_key_touxiang);
		tv_key_name= (TextView) findViewById(R.id.tv_key_name);
		tv_key_pin= (TextView) findViewById(R.id.tv_key_pin);

		tv_pin_sure= (TextView) findViewById(R.id.tv_pin_sure);
		tv_pin_sure.setOnClickListener(this);

		pb_key_time= (ProgressBar) findViewById(R.id.pb_key_time);

	}

	@Override
	public void initData() {
		keyItem= (KeyItem) getIntent().getSerializableExtra(MyConstant.keyObj);
		getPin();
		countdown(10);
	}

	private void getPin() {
		double random = Math.random()*10000000+1000;
		String pinCode = (random+"".toString()).substring(0, 4);
		URL= NetWorkConstant.PORT_URL+ NetWorkMethod.getPin;
		Map<String,String>map=new HashMap<>();
		map.put(NetWorkMethod.keyNum,keyItem.getKeyNum());
		map.put(NetWorkMethod.pinCode,pinCode);
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {

			}
			@Override
			public void onResponse(String response) {
				JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, null);
				if (jsReturn.isSuccess()) {
					String pinCode=jsReturn.getParams().getData();
					tv_key_pin.setText(pinCode);
				}else{
					MyToast.showToast(jsReturn.getMsg());
				}
			}
		});
	}

	public void countdown(int time){
		numberProgress=time*1000/100;
		fixedProgress=1000/numberProgress;
		ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				pb_key_time.setProgress(progress);
				if(progress==1000){
					progress=0;
					pb_key_time.setProgress(progress);
					ses.shutdownNow();
				}
				progress+=fixedProgress;
			}
		},0,100, TimeUnit.MILLISECONDS);
	}
	@Override
	public Handler setHandler() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.img_left_mhead1:
				finish();
			break;
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
