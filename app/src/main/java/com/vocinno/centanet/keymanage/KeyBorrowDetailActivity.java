package com.vocinno.centanet.keymanage;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.utils.MethodsExtra;

import android.os.Handler;
import android.view.View;

public class KeyBorrowDetailActivity extends SuperSlideMenuActivity {
	private View mBack, mTitle, mAdd;

	@Override
	public Handler setHandler() {
		return null;
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_key_borrow_detail;
	}

	@Override
	public void initView() {
		mBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mTitle = MethodsExtra.findHeadTitle1(mContext, mRootView, R.string.key,null);
		mAdd = MethodsExtra.findHeadRightView1(mContext, mRootView, 0,
				R.drawable.universal_button_add);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			onBack();
			break;
		case R.id.img_right_mhead1:
			break;
		default:
			break;
		}
	}

	@Override
	public void setListener() {
		mBack.setOnClickListener(this);
		mAdd.setOnClickListener(this);
	}

	@Override
	public void initData() {
	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {

	}
}
