package com.vocinno.centanet.wxapi;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.vocinno.centanet.apputils.AppInstance;
import com.vocinno.centanet.apputils.utils.MethodsExtra;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 注册微信sdk
		AppInstance.mWXAPI.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	// 第三方发送到微信的请求处理后的响应结果，会回调该函数
	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// 分享成功
			MethodsExtra.toast(getApplicationContext(), "分享成功");
			finish();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// 分享取消
			MethodsExtra.toast(getApplicationContext(), "分享取消");
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// 分享拒绝
			MethodsExtra.toast(getApplicationContext(), "分享拒绝");
			finish();
			break;
		}

	}

}
