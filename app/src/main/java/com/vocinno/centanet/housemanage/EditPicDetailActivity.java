package com.vocinno.centanet.housemanage;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.ImageUtil;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.apputils.selfdefineview.MyTextView;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	private MyTextView tv_delete_img,tv_save_img;
	private Intent intent;
	private String imgPath,imgDescribe;
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
		intent=getIntent();
		imgPath=intent.getStringExtra("path");
		imgDescribe=intent.getStringExtra("describe");
		mBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mTitle = MethodsExtra.findHeadTitle1(mContext, mRootView, 0, "修改实堪描述");
		mSubmit = MethodsExtra.findHeadRightView1(mContext, mRootView, 0,
				R.drawable.universal_button_done);

		// 提取图片以及描述的Id然后进行处理
		tv_delete_img = (MyTextView) findViewById(R.id.tv_delete_img);
		tv_delete_img.setOnClickListener(this);
		tv_save_img = (MyTextView) findViewById(R.id.tv_save_img);
		tv_save_img.setOnClickListener(this);

		mEtImageDescription = (EditText) findViewById(R.id.et_changeImage_EditPicDetailActivity);
		mImgHouseDetail = (ImageView) findViewById(R.id.img_houseDetail_EditPicDetailActivity);
		mImgAllowEditBtn = (ImageView) findViewById(R.id.img_allow_EditPicDetailActivity);
		mImgHouseDetail.setImageBitmap(ImageUtil.File2Bitmap(imgPath));
		/*mImgHouseDetail.setImageBitmap(BitmapFactory
				.decodeFile(MethodsDeliverData.mEditorImage));*/
		mEtImageDescription
				.setText(intent.getStringExtra("describe")==null?"":intent.getStringExtra("describe"));
		// mEtImageDescription.setFocusable(false);
		// mEtImageDescription.setFocusableInTouchMode(false);
//		mEtImageDescription.setEnabled(false);
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
		case R.id.tv_delete_img:
			myDialog=new MyDialog.Builder(this);
			myDialog.setTitle("提示");
			myDialog.setMessage("是否删除图片?");
			myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					intent.putExtra("path", imgPath);
					setResult(203, intent);
					EditPicDetailActivity.this.finish();
				}
			});
			myDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			myDialog.create().show();
			break;
			case R.id.tv_save_img:
			saveImg();
			break;

		case R.id.img_left_mhead1:
			finish();
			break;
		case R.id.img_right_mhead1:
			saveImg();
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

	private void saveImg() {
		intent.putExtra("path",imgPath);
		if(imgDescribe.equals(mEtImageDescription.getText().toString())){
			intent.putExtra("different",false);
		}else{
			intent.putExtra("different",true);
			intent.putExtra("describe",mEtImageDescription.getText().toString());
		}
		setResult(202,intent);
		this.finish();
	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {

	}
}
