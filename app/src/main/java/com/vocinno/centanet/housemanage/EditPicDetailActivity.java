package com.vocinno.centanet.housemanage;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 点击单张图片进行编辑 包括修改文字描述 删除图片等功能
 * 
 * @author Administrator
 * 
 */
public class EditPicDetailActivity extends SuperSlideMenuActivity {

	private RelativeLayout mGoBackBtn;
	private RelativeLayout mFinishBtn;
	private View mBack, mTitle, mSubmit;
	private EditText mEtImageDescription;
	private ImageView mImgHouseDetail;
	private ImageView mImgAllowEditBtn;

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				EditPicDetailActivity.this.closeMenu(msg);
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_edit_pic_detail;
	}

	@Override
	public void initView() {
		mBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mTitle = MethodsExtra.findHeadTitle1(mContext, mRootView, 0, "修改实堪描述");
		mSubmit = MethodsExtra.findHeadRightView1(mContext, mRootView, 0,
				R.drawable.universal_button_done);

		// 提取图片以及描述的Id然后进行处理
		mEtImageDescription = (EditText) findViewById(R.id.et_changeImage_EditPicDetailActivity);
		mImgHouseDetail = (ImageView) findViewById(R.id.img_houseDetail_EditPicDetailActivity);
		mImgAllowEditBtn = (ImageView) findViewById(R.id.img_allow_EditPicDetailActivity);

		mImgHouseDetail.setImageBitmap(BitmapFactory
				.decodeFile(MethodsDeliverData.mEditorImage));
		mEtImageDescription
				.setText(MethodsDeliverData.mEditorImageDescriptionString);
		// mEtImageDescription.setFocusable(false);
		// mEtImageDescription.setFocusableInTouchMode(false);
		mEtImageDescription.setEnabled(false);
	}

	@Override
	public void setListener() {
		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mImgAllowEditBtn.setOnClickListener(this);
	}

	@Override
	public void initData() {
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			finish();
			break;
		case R.id.img_right_mhead1:
			MethodsDeliverData.mChangedImageDescriptionString = mEtImageDescription
					.getText().toString();
			finish();
			break;
		case R.id.img_allow_EditPicDetailActivity:
			// mEtImageDescription.setFocusableInTouchMode(true);
			// mEtImageDescription.setFocusable(true);
			mEtImageDescription.setEnabled(true);
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
