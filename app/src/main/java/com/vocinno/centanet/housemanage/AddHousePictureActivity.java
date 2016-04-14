package com.vocinno.centanet.housemanage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.util.mylibrary.photos.PhotoReadyHandler;
import com.util.mylibrary.photos.SelectPhotoManager;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.cst.ImageForJsParams;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.housemanage.adapter.HousePicGridViewAdapter;
import com.vocinno.centanet.housemanage.adapter.MyInterface;
import com.vocinno.centanet.model.BorrowKey;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.UploadImageResult;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.imageutils.selector.SelectorImageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 增加实勘
 * 
 * @author Administrator
 * 
 */
public class AddHousePictureActivity extends SuperSlideMenuActivity implements MyInterface {
	private String takePhotoType,editType,selectType;//判断拍照相册和修改
	//房型，室，厅，厨房，卫生间，其他
	private HousePicGridViewAdapter houseTypeAdapter,roomAdapter,officeAdapter, kitchenAdapter, toiletAdapter,otherAdapter;
	private GridView mGridViewHouseTypePic; // 房型图
	private GridView mGridViewRoomPic; // 室
	private GridView mGridViewOfficePic; // 厅
	private GridView mGridViewKitchenPic; // 厨房//Kitchen
	private GridView mGridViewToiletPic; // 卫生间
	private GridView mGridViewOtherPic; // 其他
	private RelativeLayout mRyltShowGridviewHouseType, mRyltHouseType; // 房型图
	private RelativeLayout mRyltShowGridviewRoom, mRyltRoom;
	private RelativeLayout mRyltShowGridviewOffice, mRyltOffice;
	private RelativeLayout mRyltShowGridviewToilet, mRyltToilet;
	private RelativeLayout mRyltShowGridviewBalcony, mRyltBalcony;
	private RelativeLayout mRyltShowGridviewOther, mRyltOther;

	private RelativeLayout mRyltSubmit;
	private ImageView mImgHouseTypeRightPic;
	private ImageView mImgRoomRightPic;
	private ImageView mImgOfficeRightPic;
	private ImageView mImgToiletRightPic;
	private ImageView mImgBalconyRightPic;
	private ImageView mImgOtherRightPic;
	private TextView mTvHouseTypePicNumber;
	private TextView mTvRoomPicNumber;
	private TextView mTvOfficePicNumber;
	private TextView mTvKitchenPicNumber;
	private TextView mTvToiletPicNumber;
	private TextView mTvOtherPicNumber;
	private View mBack, mTitle, mSubmit;

	private List<String> mHouseTypeImgsList = new ArrayList<String>();
	private List<String> mRoomTypeImgsList = new ArrayList<String>();
	private List<String> mOfficeTypeImgsList = new ArrayList<String>();
	private List<String> mKitchenTypeImgsList = new ArrayList<String>();
	private List<String> mToiletTypeImgsList = new ArrayList<String>();
	private List<String> mOtherTypeImgsList = new ArrayList<String>();

	private List<String> mHouseTypeImgsDescripList = new ArrayList<String>();
	private List<String> mRoomTypeImgsDescripList = new ArrayList<String>();
	private List<String> mOfficeTypeImgsDescripList = new ArrayList<String>();
	private List<String> mKitchenTypeImgsDescripList = new ArrayList<String>();
	private List<String> mToiletTypeImgsDescripList = new ArrayList<String>();
	private List<String> mOtherTypeImgsDescripList = new ArrayList<String>();

	private ArrayList<ImageForJsParams> mUploadImages = new ArrayList<ImageForJsParams>();
	private int mUploadCount = 0;
	private int mUploadTotalImage = 0;

	private final int UPLOAD_COMPLETED = 10002;
	private final int UPLOAD_PIC_FAIL = 10001;
	private boolean mHasUploadSuccess = false;
	private String delCode;
	private EditText et_miaoshu;
	private CheckBox cb_ishd;
	@Override
	public Handler setHandler() {

		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				AddHousePictureActivity.this.closeMenu(msg);
				switch (msg.what) {
					case UPLOAD_COMPLETED:
					showDialog();
					mHasUploadSuccess = true;
//					MethodsExtra.toast(mContext, "图片上传成功");
					String miaoShu=et_miaoshu.getText().toString();//描述
					int isHD=0;
					if(cb_ishd.isChecked()){
						isHD=1;
					}
					String teString = CST_JS.getJsonStringForUploadImages(delCode, mUploadImages,miaoShu,isHD);
					Log.d("wan", "wanggsx uploadsuccess string:" + teString);
					MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
							CST_JS.JS_Function_HouseResource_uploadImages,
							teString);
				case UPLOAD_PIC_FAIL:
					dismissDialog();
					if (!mHasUploadSuccess) {
						MethodsExtra.toast(mContext, "文件上传失败");
					}
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_add_house_detail;
	}

	@Override
	public void initView() {
		myDialog=new MyDialog.Builder(this);
		delCode=getIntent().getStringExtra("delCode");
		mBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mSubmit = MethodsExtra.findHeadRightView1(mContext, mRootView, 0,
				R.drawable.universal_button_done);
		mTitle = MethodsExtra.findHeadTitle1(mContext, mRootView,
				R.string.add_camara_pic, null);
		// 房型图 获取到数据之后填充入的tv
		mTvHouseTypePicNumber = (TextView) findViewById(R.id.tv_fangXingTu_AddHousePicDetailActivity);
		// 房型图 点击需要被更换图片的控件
		mImgHouseTypeRightPic = (ImageView) findViewById(R.id.imgView_addHousePic_AddHousePicDetailActivity);
		// 房型图 根据点击判断是否显示的rylt
		mRyltShowGridviewHouseType = (RelativeLayout) findViewById(R.id.rylt_showGridviewHouse_AddHousePicDetailActivity);
		// 房型图 点击这个控件改变图片 并且显示上一个rylt
		mRyltHouseType = (RelativeLayout) findViewById(R.id.rylt_houseType_AddHousePicDetailActivity);
		// 房型图 填充图片的gridview
		mGridViewHouseTypePic = (GridView) findViewById(R.id.gv_showGridviewHouse_AddHousePicDetailActivity);

		// 室 获取到数据之后填充入的tv
		mTvRoomPicNumber = (TextView) findViewById(R.id.tv_shi_AddHousePicDetailActivity);
		// 室 点击需要被更换的图片空间
		mImgRoomRightPic = (ImageView) findViewById(R.id.imgView_addRoomPic_AddHousePicDetailActivity);
		// 室 根据点击判断是否显示的rylt
		mRyltShowGridviewRoom = (RelativeLayout) findViewById(R.id.rlyt_showGridviewRoom_AddHousePicDetailActivity);
		// 室 点击这个控件改变图片 并且显示上一个rylt
		mRyltRoom = (RelativeLayout) findViewById(R.id.rlyt_addRoom_AddHousePicDetailActivity);
		// 室 填充图片的gridview
		mGridViewRoomPic = (GridView) findViewById(R.id.gv_showGridviewRoom_AddHousePicDetailActivity);

		// 厅 获取到数据之后填充入的tv
		mTvOfficePicNumber = (TextView) findViewById(R.id.tv_ting_AddHousePicDetailActivity);
		mImgOfficeRightPic = (ImageView) findViewById(R.id.imgView_tingPic_AddHousePicDetailActivity);
		mRyltShowGridviewOffice = (RelativeLayout) findViewById(R.id.rlyt_showGridviewOffice_AddHousePicDetailActivity);
		mRyltOffice = (RelativeLayout) findViewById(R.id.rlyt_addOffice_AddHousePicDetailActivity);
		mGridViewOfficePic = (GridView) findViewById(R.id.gv_showGridviewOffice_addhousepicdetailactivity);

		// 厨房
		mTvKitchenPicNumber = (TextView) findViewById(R.id.tv_toiletNumber_AddHousePicDetailActivity);
		mImgToiletRightPic = (ImageView) findViewById(R.id.imgView_chooseToilet_AddHousePicDetailActivity);
		mRyltShowGridviewToilet = (RelativeLayout) findViewById(R.id.rlyt_showGridviewToilet_AddHousePicDetailActivity);
		mRyltToilet = (RelativeLayout) findViewById(R.id.rlyt_toilet_AddHousePicDetailActivity);
		mGridViewKitchenPic = (GridView) findViewById(R.id.gv_showGridviewToilet_AddHousePicDetailActivity);

		// 卫生间
		mTvToiletPicNumber = (TextView) findViewById(R.id.tv_balconyNumber_AddHousePicDetailActivity);
		mImgBalconyRightPic = (ImageView) findViewById(R.id.imgView_chooseBalcony_AddHousePicDetailActivity);
		mRyltShowGridviewBalcony = (RelativeLayout) findViewById(R.id.rlyt_showGridviewBalcony_AddHousePicDetailActivity);
		mRyltBalcony = (RelativeLayout) findViewById(R.id.rlyt_balcony_AddHousePicDetailActivity);
		mGridViewToiletPic = (GridView) findViewById(R.id.gv_showGridviewBalcony_AddHousePicDetailActivity);

		// 其他
		mTvOtherPicNumber = (TextView) findViewById(R.id.tv_otheNumber_AddHousePicDetailActivity);
		mImgOtherRightPic = (ImageView) findViewById(R.id.imgView_chooseOther_AddHousePicDetailActivity);
		mRyltShowGridviewOther = (RelativeLayout) findViewById(R.id.rlyt_showGridviewOther_AddHousePicDetailActivity);
		mRyltOther = (RelativeLayout) findViewById(R.id.rlyt_other_AddHousePicDetailActivity);
		mGridViewOtherPic = (GridView) findViewById(R.id.gv_showGridviewOther_AddHousePicDetailActivity);

		mRyltSubmit = (RelativeLayout) findViewById(R.id.rlyt_changeSure_AddHousePicDetailActivity);
		et_miaoshu = (EditText) findViewById(R.id.et_miaoshu);
		cb_ishd = (CheckBox) findViewById(R.id.cb_ishd);
	}

	@Override
	public void setListener() {
		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mRyltSubmit.setOnClickListener(this);
		mRyltHouseType.setOnClickListener(this);
		mRyltRoom.setOnClickListener(this);
		mRyltOffice.setOnClickListener(this);
		mRyltToilet.setOnClickListener(this);
		mRyltBalcony.setOnClickListener(this);
		mRyltOther.setOnClickListener(this);
	}

	private void changeImageDescription(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(
					MethodsDeliverData.mEditorImageDescriptionString)) {
				list.set(i, MethodsDeliverData.mChangedImageDescriptionString);
			}
		}
		MethodsDeliverData.mChangedImageDescriptionString = null;
	}

	@Override
	public void initData() {
		MethodsDeliverData.mListImageDescription = new ArrayList<String>();
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_UPLOAD_IMGS_RESULT, TAG);
		  houseTypeAdapter = new HousePicGridViewAdapter(
				mContext, "houseType");
		  roomAdapter = new HousePicGridViewAdapter(
				mContext, "room");
		  officeAdapter = new HousePicGridViewAdapter(
				mContext, "office");
		  kitchenAdapter = new HousePicGridViewAdapter(
				mContext, "kitchen");
		  toiletAdapter = new HousePicGridViewAdapter(
				mContext, "toilet");
		otherAdapter = new HousePicGridViewAdapter(
				mContext, "other");
		// 根据数据设置可以添加的图片数量
		mTvHouseTypePicNumber.setText("房型图(" + "0/9" + ")");// R.string.fangxingtu
		mTvRoomPicNumber.setText("室(" + "0/9" + ")");// R.string.shi+
		mTvOfficePicNumber.setText("厅(" + "0/9" + ")");// R.string.ting+
		mTvKitchenPicNumber.setText("厨(" + "0/9" + ")");// R.string.wei+
		mTvToiletPicNumber.setText("卫(" + "0/9" + ")");// R.string.yang+
		mTvOtherPicNumber.setText("其他(" + "0/9" + ")");// R.string.other+

		houseTypeAdapter.setCount(1);
		roomAdapter.setCount(1);
		officeAdapter.setCount(1);
		toiletAdapter.setCount(1);
		kitchenAdapter.setCount(1);
		otherAdapter.setCount(1);
		mGridViewHouseTypePic.setAdapter(houseTypeAdapter);// new
															// HousePicGridViewAdapter(mContext));
		mGridViewRoomPic.setAdapter(roomAdapter);// new
													// HousePicGridViewAdapter(mContext));
		mGridViewOfficePic.setAdapter(officeAdapter);
		mGridViewKitchenPic.setAdapter(kitchenAdapter);
		mGridViewToiletPic.setAdapter(toiletAdapter);
		mGridViewOtherPic.setAdapter(otherAdapter);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			onBack();
			break;
		case R.id.rylt_houseType_AddHousePicDetailActivity:
			if (mRyltShowGridviewHouseType.getVisibility() == View.GONE) {
				mImgHouseTypeRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_up);
				mRyltShowGridviewHouseType.setVisibility(View.VISIBLE);
			} else {
				mImgHouseTypeRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_down);
				mRyltShowGridviewHouseType.setVisibility(View.GONE);
			}
			break;
		case R.id.rlyt_addRoom_AddHousePicDetailActivity:
			if (mRyltShowGridviewRoom.getVisibility() == View.GONE) {
				mImgRoomRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_up);
				mRyltShowGridviewRoom.setVisibility(View.VISIBLE);
			} else {
				mImgRoomRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_down);
				mRyltShowGridviewRoom.setVisibility(View.GONE);
			}
			break;
		case R.id.rlyt_addOffice_AddHousePicDetailActivity:
			if (mRyltShowGridviewOffice.getVisibility() == View.GONE) {
				mImgOfficeRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_up);
				mRyltShowGridviewOffice.setVisibility(View.VISIBLE);
			} else {
				mImgOfficeRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_down);
				mRyltShowGridviewOffice.setVisibility(View.GONE);
			}
			break;
		case R.id.rlyt_other_AddHousePicDetailActivity:
			if (mRyltShowGridviewOther.getVisibility() == View.GONE) {
				mImgOtherRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_up);
				mRyltShowGridviewOther.setVisibility(View.VISIBLE);
			} else {
				mImgOtherRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_down);
				mRyltShowGridviewOther.setVisibility(View.GONE);
			}
			break;
		case R.id.rlyt_toilet_AddHousePicDetailActivity:
			if (mRyltShowGridviewToilet.getVisibility() == View.GONE) {
				mImgToiletRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_up);
				mRyltShowGridviewToilet.setVisibility(View.VISIBLE);
			} else {
				mImgToiletRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_down);
				mRyltShowGridviewToilet.setVisibility(View.GONE);
			}
			break;
		case R.id.rlyt_balcony_AddHousePicDetailActivity:
			if (mRyltShowGridviewBalcony.getVisibility() == View.GONE) {
				mImgBalconyRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_up);
				mRyltShowGridviewBalcony.setVisibility(View.VISIBLE);
			} else {
				mImgBalconyRightPic
						.setBackgroundResource(R.drawable.h_manage_icon_down);
				mRyltShowGridviewBalcony.setVisibility(View.GONE);
			}
			break;
		// 提交照片
		case R.id.img_right_mhead1:
			uploadImg();
			break;
		// 提交照片
		case R.id.rlyt_changeSure_AddHousePicDetailActivity:
//			uploadAndConnectJs();
			uploadImg();
			break;
		default:
			break;
		}

	}
	public void  uploadImg(){
		// 计算总共需要上传的图片数量
		mUploadTotalImage = mHouseTypeImgsList.size()
				+ mRoomTypeImgsList.size() + mOfficeTypeImgsList.size()
				+ mOtherTypeImgsList.size()
				+ mToiletTypeImgsList.size()
				+ mKitchenTypeImgsList.size();
		if(mUploadTotalImage<=0){
			MethodsExtra.toast(mContext, "当前没有可上传的图片");
			return;
		}

		myDialog.setTitle("提示");
		myDialog.setMessage("是否上传图片?");
		myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				uploadAndConnectJs();
				/*mUploadTotalImage=1;
				new Thread(new Runnable() {
					@Override
					public void run() {
						uploadImage(ImageForJsParams.PIC_TYPE_HOUSE,
								mHouseTypeImgsList, mHouseTypeImgsDescripList);
					}
				}).start();*/
			}
		});
		myDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		myDialog.create().show();
	}
	private void uploadAndConnectJs() {
		showDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				uploadImage(ImageForJsParams.PIC_TYPE_HOUSE,
						mHouseTypeImgsList, mHouseTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_LIVINGROOMT,
						mRoomTypeImgsList, mRoomTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_ROOM,
						mOfficeTypeImgsList, mOfficeTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_OTHER,
						mOtherTypeImgsList, mOtherTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_TOILET,
						mToiletTypeImgsList, mToiletTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_KITCHEN,
						mKitchenTypeImgsList, mKitchenTypeImgsDescripList);
			}
		}).start();
	}

	private void uploadImage(final String type, final List<String> list,
			final List<String> descriptionList) {
		if (list.size() == 0) {
			return;
		}
		try {
			final List<String> descriptionList2 = descriptionList;
			for (int i = 0; i < list.size(); i++) {
				final int index = i;
				String result = MethodsFile.uploadFile(getString(R.string.serverurl), list.get(i));
//				String result = MethodsFile.uploadFileT(new File(list.get(i)),getString(R.string.serverurl));
				Log.d("wan", "wanggsx uploadFile " + i + " resut=" + result);
				JSReturn jsReturn = null;
				jsReturn = MethodsJson.jsonToJsReturn(result,
						UploadImageResult.class);
				List<UploadImageResult> imageResult = jsReturn.getListDatas();
				ImageForJsParams tempData = new ImageForJsParams();
				tempData.setType(type);
//				tempData.setDesc(descriptionList2.get(index));
				tempData.setPic(imageResult.get(0).getFileId());
				mUploadImages.add(tempData);
				mUploadCount = mUploadCount + 1;
				if (mUploadCount == mUploadTotalImage) {
					mHander.sendEmptyMessage(UPLOAD_COMPLETED);
					return;
				}
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			mHander.sendEmptyMessage(UPLOAD_PIC_FAIL);
		}
	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		dismissDialog();
		if (name.equals(CST_JS.NOTIFY_NATIVE_UPLOAD_IMGS_RESULT)) {
			String strJson = (String) data;
			JSReturn jReturn = MethodsJson.jsonToJsReturn(strJson,
					BorrowKey.class);
			MyDialog.Builder myDialog=new MyDialog.Builder(this);
			myDialog.setTitle("提示");
			if(jReturn.isSuccess()){
				myDialog.setMessage(jReturn.getMsg());
				myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
			}else{
				myDialog.setMessage("图片上传失败 是否继续上传?");
//				myDialog.setMessage(jReturn.getMsg());
				myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showDialog();
						mHander.sendEmptyMessage(UPLOAD_COMPLETED);
					}
				});
				myDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			}
			myDialog.create().show();
		}
	}
	@Override
	public void editPhoto(String type,String imgPath, String describe) {
		Intent intent=new Intent(this, EditPicDetailActivity.class);
		intent.putExtra("path",imgPath);
		intent.putExtra("describe", "");
		editType=type;
//		MethodsExtra.startActivity(this, EditPicDetailActivity.class);
		startActivityForResult(intent, 201);//编辑图片
	}
	@Override
	public void selectPhoto(String type) {
		selectType=type;
		Intent intent=new Intent(this, SelectorImageActivity.class);
		startActivityForResult(intent, 301);//选择图片
	}
	@Override
	public void takePhoto(final String type) {
		SelectPhotoManager.getInstance().setPhotoReadyHandler(new PhotoReadyHandler() {
			@Override
			public void onPhotoReady(int from, String imgPath) {
				Log.i("=imgPath=", "==" + imgPath);
//				mHouseTypeImgsList.add(imgPath);
//				MethodsDeliverData.mListImagePath.add(imgPath);
				Intent intent = new Intent(AddHousePictureActivity.this,
						AddHousePictureDescriptionActivity.class);
				intent.putExtra("path", imgPath);
				startActivityForResult(intent, 101);
				takePhotoType = type;
//				houseTypeAdapter.notifyDataSetChanged();
			}
		});
		SelectPhotoManager.setImgSavePath(Environment.getExternalStorageDirectory().getPath() + "/vocinno");
		SelectPhotoManager.getInstance().start(this, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==101&&resultCode==102){//拍照返回
			String path = data.getStringExtra("path");
			String describe = data.getStringExtra("describe");
			switch (takePhotoType){
				case "houseType":
					mHouseTypeImgsList.add(path);
					mHouseTypeImgsDescripList.add(describe);
//					mTvHouseTypePicNumber.setText("房型图" + "(" + mHouseTypeImgsList.size() + "/9" + ")");
					mTvHouseTypePicNumber.setText(getFormatString("houseType", mHouseTypeImgsList));
					houseTypeAdapter.setData(mHouseTypeImgsList, mHouseTypeImgsDescripList);
					mGridViewHouseTypePic.setAdapter(houseTypeAdapter);
					break;
				case "room":
					mRoomTypeImgsList.add(path);
					mRoomTypeImgsDescripList.add(describe);
					mTvRoomPicNumber.setText(getFormatString("room", mRoomTypeImgsList));// R.string.shi+
					roomAdapter.setData(mRoomTypeImgsList, mRoomTypeImgsDescripList);
					mGridViewRoomPic.setAdapter(roomAdapter);
					break;
				case "office":
//					mTvOfficePicNumber.setText("厅(" + "0/9" + ")");// R.string.ting+
					mOfficeTypeImgsList.add(path);
					mOfficeTypeImgsDescripList.add(describe);
					mTvOfficePicNumber.setText(getFormatString("office", mOfficeTypeImgsList));// R.string.shi+
					officeAdapter.setData(mOfficeTypeImgsList, mOfficeTypeImgsDescripList);
					mGridViewOfficePic.setAdapter(officeAdapter);
					break;
				case "kitchen":
//					mTvKitchenPicNumber.setText("厨(" + "0/9" + ")");// R.string.wei+
					mKitchenTypeImgsList.add(path);
					mKitchenTypeImgsDescripList.add(describe);
					mTvKitchenPicNumber.setText(getFormatString("kitchen", mKitchenTypeImgsList));// R.string.shi+
					kitchenAdapter.setData(mKitchenTypeImgsList, mKitchenTypeImgsDescripList);
					mGridViewKitchenPic.setAdapter(kitchenAdapter);

					break;
				case "toilet":
//					mTvToiletPicNumber.setText("卫(" + "0/9" + ")");// R.string.yang+
					mToiletTypeImgsList.add(path);
					mToiletTypeImgsDescripList.add(describe);
					mTvToiletPicNumber.setText(getFormatString("toilet", mToiletTypeImgsList));// R.string.shi+
					toiletAdapter.setData(mToiletTypeImgsList, mToiletTypeImgsDescripList);
					mGridViewToiletPic.setAdapter(toiletAdapter);
					break;
				case "other":
//					mTvOtherPicNumber.setText("其他(" + "0/9" + ")");// R.string.other+
					mOtherTypeImgsList.add(path);
					mOtherTypeImgsDescripList.add(describe);
					mTvOtherPicNumber.setText(getFormatString("toilet", mOtherTypeImgsList));// R.string.shi+
					otherAdapter.setData(mOtherTypeImgsList, mOtherTypeImgsDescripList);
					mGridViewOtherPic.setAdapter(otherAdapter);
					break;
			}

		}else if(requestCode==201){//编辑图片
			if(data!=null){
				String path=data.getStringExtra("path");
				boolean different=data.getBooleanExtra("different",false);
				switch (resultCode){
					case 202://完成修改
						if(different){
							String describe=data.getStringExtra("describe");
							switch (editType){//houseType room office kitchen toilet  other
								case "houseType":
									if(mHouseTypeImgsList.contains(path)){
										int index=mHouseTypeImgsList.indexOf(path);
										mHouseTypeImgsDescripList.set(index, describe);
										//houseTypeAdapter.setData(mHouseTypeImgsList, mHouseTypeImgsDescripList);
										//mGridViewHouseTypePic.setAdapter(houseTypeAdapter);
									}
									break;
								case "room":
									if(mRoomTypeImgsList.contains(path)){
										int index=mRoomTypeImgsList.indexOf(path);
										mRoomTypeImgsDescripList.set(index, describe);
										//roomAdapter.setData(mRoomTypeImgsList, mRoomTypeImgsDescripList);
										//mGridViewRoomPic.setAdapter(roomAdapter);
									}
									break;
								case "office":
									if(mOfficeTypeImgsList.contains(path)){
										int index=mOfficeTypeImgsList.indexOf(path);
										mOfficeTypeImgsDescripList.set(index, describe);
										//officeAdapter.setData(mOfficeTypeImgsList, mOfficeTypeImgsDescripList);
										//mGridViewOfficePic.setAdapter(officeAdapter);
									}
									break;
								case "kitchen":
									if(mKitchenTypeImgsList.contains(path)){
										int index=mKitchenTypeImgsList.indexOf(path);
										mKitchenTypeImgsDescripList.set(index, describe);
										//kitchenAdapter.setData(mKitchenTypeImgsList, mKitchenTypeImgsDescripList);
										//mGridViewKitchenPic.setAdapter(kitchenAdapter);
									}
									break;
								case "toilet":
									if(mToiletTypeImgsList.contains(path)){
										int index=mToiletTypeImgsList.indexOf(path);
										mToiletTypeImgsDescripList.set(index, describe);
										//toiletAdapter.setData(mToiletTypeImgsList, mToiletTypeImgsDescripList);
										//mGridViewToiletPic.setAdapter(toiletAdapter);
									}
									break;
								case "other":
									if(mOtherTypeImgsList.contains(path)){
										int index=mOtherTypeImgsList.indexOf(path);
										mOtherTypeImgsDescripList.set(index, describe);
										//otherAdapter.setData(mOtherTypeImgsList, mOtherTypeImgsDescripList);
										//mGridViewOtherPic.setAdapter(otherAdapter);
									}
									break;
							}
						}
						break;
					case 203://删除
						switch (editType){//houseType room office  toilet balcony  other
							case "houseType":
								if(mHouseTypeImgsList.contains(path)){
									int index=mHouseTypeImgsList.indexOf(path);
									mHouseTypeImgsList.remove(index);
//								mHouseTypeImgsDescripList.remove(index);
									mTvHouseTypePicNumber.setText(getFormatString("houseType", mHouseTypeImgsList));
									houseTypeAdapter.setData(mHouseTypeImgsList, mHouseTypeImgsDescripList);
									mGridViewHouseTypePic.setAdapter(houseTypeAdapter);
								}
								break;
							case "room":
								if(mRoomTypeImgsList.contains(path)){
									int index=mRoomTypeImgsList.indexOf(path);
									mRoomTypeImgsList.remove(index);
//								mRoomTypeImgsDescripList.remove(index);
									mTvRoomPicNumber.setText(getFormatString("room", mRoomTypeImgsList));
									roomAdapter.setData(mRoomTypeImgsList, mRoomTypeImgsDescripList);
									mGridViewRoomPic.setAdapter(roomAdapter);
								}
								break;
							case "office":
								if(mOfficeTypeImgsList.contains(path)){
									int index=mOfficeTypeImgsList.indexOf(path);
									mOfficeTypeImgsList.remove(index);
//								mOfficeTypeImgsDescripList.remove(index);
									mTvOfficePicNumber.setText(getFormatString("office", mOfficeTypeImgsList));
									officeAdapter.setData(mOfficeTypeImgsList, mOfficeTypeImgsDescripList);
									mGridViewOfficePic.setAdapter(officeAdapter);
								}
								break;
							case "kitchen":
								if(mKitchenTypeImgsList.contains(path)){
									int index=mKitchenTypeImgsList.indexOf(path);
									mKitchenTypeImgsList.remove(index);
//								mKitchenTypeImgsDescripList.remove(index);
									mTvKitchenPicNumber.setText(getFormatString("kitchen", mKitchenTypeImgsList));
									kitchenAdapter.setData(mKitchenTypeImgsList, mKitchenTypeImgsDescripList);
									mGridViewKitchenPic.setAdapter(kitchenAdapter);
								}
								break;
							case "toilet":
								if(mToiletTypeImgsList.contains(path)){
									int index=mToiletTypeImgsList.indexOf(path);
									mToiletTypeImgsList.remove(index);
//								mToiletTypeImgsDescripList.remove(index);
									mTvToiletPicNumber.setText(getFormatString("toilet", mToiletTypeImgsList));
									toiletAdapter.setData(mToiletTypeImgsList, mToiletTypeImgsDescripList);
									mGridViewToiletPic.setAdapter(toiletAdapter);
								}
								break;
							case "other":
								if(mOtherTypeImgsList.contains(path)){
									int index=mOtherTypeImgsList.indexOf(path);
									mOtherTypeImgsList.remove(index);
//								mOtherTypeImgsDescripList.remove(index);
									mTvOtherPicNumber.setText(getFormatString("other", mOtherTypeImgsList));
									toiletAdapter.setData(mOtherTypeImgsList, mOtherTypeImgsDescripList);
									mGridViewOtherPic.setAdapter(toiletAdapter);
								}
								break;
						}
						break;
			}

			}
		}else if(requestCode==301&&resultCode==RESULT_OK){
			if(data!=null){
				List<String> list=data.getStringArrayListExtra("pathList");
				switch (selectType){
					case "houseType":
						addImgPath(mHouseTypeImgsList,list,"houseType",mTvHouseTypePicNumber,mGridViewHouseTypePic,houseTypeAdapter);
						break;
					case "room":
						addImgPath(mRoomTypeImgsList,list,"room",mTvRoomPicNumber,mGridViewRoomPic,roomAdapter);
						break;
					case "office":
						addImgPath(mOfficeTypeImgsList,list,"office",mTvOfficePicNumber,mGridViewOfficePic,officeAdapter);
						break;
					case "kitchen":
						addImgPath(mKitchenTypeImgsList,list,"kitchen",mTvKitchenPicNumber,mGridViewKitchenPic,kitchenAdapter);
						break;
					case "toilet":
						addImgPath(mToiletTypeImgsList,list,"toilet",mTvToiletPicNumber,mGridViewToiletPic,toiletAdapter);
						break;
					case "other":
						addImgPath(mOtherTypeImgsList,list,"other",mTvOtherPicNumber,mGridViewOtherPic,otherAdapter);
						break;
				}
			}
		}else if(requestCode==1&&resultCode==-1){
			SelectPhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
		}
	}
	public void addImgPath(List list,List addList,String type,TextView textView,GridView view,HousePicGridViewAdapter adapter){
		list.addAll(addList);
		textView.setText(getFormatString(type, list));
		adapter.setData(list, mHouseTypeImgsDescripList);
		view.setAdapter(adapter);
	}
	public String getFormatString(String type, List list){
		String format="";
		switch (type){
			case "houseType":
				format=String.format(getString(R.string.fangxingtu),list.size());
				break;
			case "room":
				format=String.format(getString(R.string.shi),list.size());
				break;
			case "office":
				format=String.format(getString(R.string.ting),list.size());
				break;
			case "kitchen":
				format=String.format(getString(R.string.chufang),list.size());
				break;
			case "toilet":
				format=String.format(getString(R.string.wei),list.size());
				break;
			case "other":
				format=String.format(getString(R.string.other),list.size());
				break;
		}
		return format;
	}
}
