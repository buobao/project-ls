package com.vocinno.centanet.keymanage;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.utils.MethodsExtra;

import android.os.Handler;
import android.view.View;

public class KeyManageDetailActivity extends SuperSlideMenuActivity {
	private View mBack, mTitle, mMoreView;

	@Override
	public Handler setHandler() {
		return null;
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_key_manage_detail;
	}

	@Override
	public void initView() {
		mBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		MethodsExtra.findHeadTitle1(mContext, mRootView, R.string.keydetail, null);
	}

	@Override
	public void setListener() {
		mBack.setOnClickListener(this);
	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			onBack();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {

	}

}
