package com.vocinno.centanet.keymanage.adapter;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vocinno.centanet.apputils.selfdefineview.TouchableImageView;

public class ViewHolderGiveKey {
	public enum KeyItemState {
		NORMAL, RETURN, GIVE_ONE_REQUEST_PIN, GIVE_TWO_REQUEST_HEADER, GIVE_THREE_REQUEST_DRAG, GIVE_FOUR_FINISH;
	}

	public KeyItemState keyItemState = KeyItemState.NORMAL;
	RelativeLayout mRlytContent;
	// 用于拖动的球形图片
	public TouchableImageView mTouchableImgViewGiveKey0;
	// 借钥匙需要输入的pin码
	public TextView mTvGiveKeyPassWord;
	// 借钥匙需要放入借用者头像的控件
	public ImageView mImageViewGiveKey1;
	public ImageView iv_house_img;
	public ImageView mImgViewGiveKey0Bg;
	public ImageView mImgViewRtnKey;
	TextView mBorrowState01, mBorrowState02, mHouseAddress, mKeyNumb;
	public String mStrKeyNum = null;
	public TextView mTvTime;
	public TextView mTvTishi;
	public TextView tv_fenhang;
	public TextView tv_time;

	/**
	 * 输入借用钥匙的pin密码
	 * 
	 * @param strPin
	 */
	public void setPinCode(String strPin) {
		mTvGiveKeyPassWord.setText(strPin);
		keyItemState = KeyItemState.GIVE_TWO_REQUEST_HEADER;
	}
}
