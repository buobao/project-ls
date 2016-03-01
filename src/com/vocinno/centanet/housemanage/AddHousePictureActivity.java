package com.vocinno.centanet.housemanage;

import java.util.ArrayList;
import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.cst.ImageForJsParams;
import com.vocinno.centanet.housemanage.adapter.HousePicGridViewAdapter;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.UploadImageResult;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 增加实勘
 * 
 * @author Administrator
 * 
 */
public class AddHousePictureActivity extends SuperSlideMenuActivity {

	private GridView mGridViewHouseTypePic; // 房型图
	private GridView mGridViewRoomPic; // 室
	private GridView mGridViewOfficePic; // 厅
	private GridView mGridViewToiletPic; // 卫生间
	private GridView mGridViewBalconyPic; // 阳台
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
	private TextView mTvToiletPicNumber;
	private TextView mTvBalconyPicNumber;
	private TextView mTvOtherPicNumber;
	private View mBack, mTitle, mSubmit;

	private List<String> mHouseTypeImgsList = new ArrayList<String>();
	private List<String> mRoomTypeImgsList = new ArrayList<String>();
	private List<String> mOfficeTypeImgsList = new ArrayList<String>();
	private List<String> mToiletTypeImgsList = new ArrayList<String>();
	private List<String> mBalconyTypeImgsList = new ArrayList<String>();
	private List<String> mOtherTypeImgsList = new ArrayList<String>();

	private List<String> mHouseTypeImgsDescripList = new ArrayList<String>();
	private List<String> mRoomTypeImgsDescripList = new ArrayList<String>();
	private List<String> mOfficeTypeImgsDescripList = new ArrayList<String>();
	private List<String> mToiletTypeImgsDescripList = new ArrayList<String>();
	private List<String> mBalconyTypeImgsDescripList = new ArrayList<String>();
	private List<String> mOtherTypeImgsDescripList = new ArrayList<String>();

	private ArrayList<ImageForJsParams> mUploadImages = new ArrayList<ImageForJsParams>();
	private int mUploadCount = 0;
	private int mUploadTotalImage = 0;

	private final int UPLOAD_COMPLETED = 10002;
	private final int UPLOAD_PIC_FAIL = 10001;
	private View mPbUploading;
	private boolean mHasUploadSuccess = false;

	@Override
	public Handler setHandler() {

		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				AddHousePictureActivity.this.closeMenu(msg);
				switch (msg.what) {
				case UPLOAD_COMPLETED:
					mHasUploadSuccess = true;
					MethodsExtra.toast(mContext, "图片上传成功");
					mPbUploading.setVisibility(View.GONE);
					String teString = CST_JS.getJsonStringForUploadImages(
							MethodsDeliverData.mDelCode, mUploadImages);
					Log.d("wan", "wanggsx uploadsuccess string:" + teString);
					MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
							CST_JS.JS_Function_HouseResource_uploadImages,
							teString);
					finish();
				case UPLOAD_PIC_FAIL:
					if (!mHasUploadSuccess) {
						MethodsExtra.toast(mContext, "文件上传失败");
						mPbUploading.setVisibility(View.GONE);
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
		mTvToiletPicNumber = (TextView) findViewById(R.id.tv_toiletNumber_AddHousePicDetailActivity);
		mImgToiletRightPic = (ImageView) findViewById(R.id.imgView_chooseToilet_AddHousePicDetailActivity);
		mRyltShowGridviewToilet = (RelativeLayout) findViewById(R.id.rlyt_showGridviewToilet_AddHousePicDetailActivity);
		mRyltToilet = (RelativeLayout) findViewById(R.id.rlyt_toilet_AddHousePicDetailActivity);
		mGridViewToiletPic = (GridView) findViewById(R.id.gv_showGridviewToilet_AddHousePicDetailActivity);

		// 卫生间
		mTvBalconyPicNumber = (TextView) findViewById(R.id.tv_balconyNumber_AddHousePicDetailActivity);
		mImgBalconyRightPic = (ImageView) findViewById(R.id.imgView_chooseBalcony_AddHousePicDetailActivity);
		mRyltShowGridviewBalcony = (RelativeLayout) findViewById(R.id.rlyt_showGridviewBalcony_AddHousePicDetailActivity);
		mRyltBalcony = (RelativeLayout) findViewById(R.id.rlyt_balcony_AddHousePicDetailActivity);
		mGridViewBalconyPic = (GridView) findViewById(R.id.gv_showGridviewBalcony_AddHousePicDetailActivity);

		// 其他
		mTvOtherPicNumber = (TextView) findViewById(R.id.tv_otheNumber_AddHousePicDetailActivity);
		mImgOtherRightPic = (ImageView) findViewById(R.id.imgView_chooseOther_AddHousePicDetailActivity);
		mRyltShowGridviewOther = (RelativeLayout) findViewById(R.id.rlyt_showGridviewOther_AddHousePicDetailActivity);
		mRyltOther = (RelativeLayout) findViewById(R.id.rlyt_other_AddHousePicDetailActivity);
		mGridViewOtherPic = (GridView) findViewById(R.id.gv_showGridviewOther_AddHousePicDetailActivity);

		mRyltSubmit = (RelativeLayout) findViewById(R.id.rlyt_changeSure_AddHousePicDetailActivity);
		mPbUploading = findViewById(R.id.pb_uploading_AddHousePicDetailActivity);
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
	protected void onResume() {
		super.onResume();
		if (MethodsDeliverData.mHouseType != "") {
			switch (MethodsDeliverData.mHouseType) {
			case "houseType":
				if (MethodsDeliverData.mChangedImageDescriptionString == null) {
					mHouseTypeImgsList.addAll(MethodsDeliverData.mListImages);
					mHouseTypeImgsDescripList
							.addAll(MethodsDeliverData.mListImageDescription);
				} else {
					changeImageDescription(mHouseTypeImgsDescripList);
				}

				HousePicGridViewAdapter houseTypeAdapter = new HousePicGridViewAdapter(
						mContext, "houseType");
				if (MethodsDeliverData.mListImages.size() != 0) {
					mTvHouseTypePicNumber.setText("房型图" + "("
							+ mHouseTypeImgsList.size() + "/9" + ")");
					houseTypeAdapter.setData(mHouseTypeImgsList,
							mHouseTypeImgsDescripList);
					mGridViewHouseTypePic.setAdapter(houseTypeAdapter);
					MethodsDeliverData.mListImages = new ArrayList<String>();
					MethodsDeliverData.mHouseType = "";
				}
				break;
			case "room":
				if (MethodsDeliverData.mChangedImageDescriptionString == null) {
					mRoomTypeImgsList.addAll(MethodsDeliverData.mListImages);
					mRoomTypeImgsDescripList
							.addAll(MethodsDeliverData.mListImageDescription);
				} else {
					changeImageDescription(mRoomTypeImgsDescripList);
				}

				HousePicGridViewAdapter roomAdapter = new HousePicGridViewAdapter(
						mContext, "room");
				if (MethodsDeliverData.mListImages.size() != 0) {
					mTvRoomPicNumber.setText("室(" + mRoomTypeImgsList.size()
							+ "/9" + ")");
					roomAdapter.setData(mRoomTypeImgsList,
							mRoomTypeImgsDescripList);
					mGridViewRoomPic.setAdapter(roomAdapter);
					MethodsDeliverData.mHouseType = "";
					MethodsDeliverData.mListImages = new ArrayList<String>();
				}
				break;
			case "office":
				if (MethodsDeliverData.mChangedImageDescriptionString == null) {
					mOfficeTypeImgsList.addAll(MethodsDeliverData.mListImages);
					mOfficeTypeImgsDescripList
							.addAll(MethodsDeliverData.mListImageDescription);
				} else {
					changeImageDescription(mOfficeTypeImgsDescripList);
				}

				HousePicGridViewAdapter officeAdapter = new HousePicGridViewAdapter(
						mContext, "office");
				if (MethodsDeliverData.mListImages.size() != 0) {
					mTvOfficePicNumber.setText("厅("
							+ mOfficeTypeImgsList.size() + "/9" + ")");
					officeAdapter.setData(mOfficeTypeImgsList,
							mOfficeTypeImgsDescripList);
					mGridViewOfficePic.setAdapter(officeAdapter);
					MethodsDeliverData.mHouseType = "";
					MethodsDeliverData.mListImages = new ArrayList<String>();
				}
				break;
			case "toilet":
				if (MethodsDeliverData.mChangedImageDescriptionString == null) {
					mToiletTypeImgsList.addAll(MethodsDeliverData.mListImages);
					mToiletTypeImgsDescripList
							.addAll(MethodsDeliverData.mListImageDescription);
				} else {
					changeImageDescription(mToiletTypeImgsDescripList);
				}
				HousePicGridViewAdapter toiletAdapter = new HousePicGridViewAdapter(
						mContext, "toilet");
				if (MethodsDeliverData.mListImages.size() != 0) {
					mTvToiletPicNumber.setText("厨("
							+ mToiletTypeImgsList.size() + "/9" + ")");
					toiletAdapter.setData(mToiletTypeImgsList,
							mToiletTypeImgsDescripList);
					mGridViewToiletPic.setAdapter(toiletAdapter);
					MethodsDeliverData.mHouseType = "";
					MethodsDeliverData.mListImages = new ArrayList<String>();
				}
				break;
			case "balcony":
				if (MethodsDeliverData.mChangedImageDescriptionString == null) {
					mBalconyTypeImgsList.addAll(MethodsDeliverData.mListImages);
					mBalconyTypeImgsDescripList
							.addAll(MethodsDeliverData.mListImageDescription);
				} else {
					changeImageDescription(mBalconyTypeImgsDescripList);
				}

				HousePicGridViewAdapter balconyAdapter = new HousePicGridViewAdapter(
						mContext, "balcony");
				if (MethodsDeliverData.mListImages.size() != 0) {
					mTvBalconyPicNumber.setText("卫("
							+ mBalconyTypeImgsList.size() + "/9" + ")");
					balconyAdapter.setData(mBalconyTypeImgsList,
							mBalconyTypeImgsDescripList);
					mGridViewBalconyPic.setAdapter(balconyAdapter);
					MethodsDeliverData.mHouseType = "";
					MethodsDeliverData.mListImages = new ArrayList<String>();
				}
				break;
			case "other":
				if (MethodsDeliverData.mChangedImageDescriptionString == null) {
					mOtherTypeImgsList.addAll(MethodsDeliverData.mListImages);
					mOtherTypeImgsDescripList
							.addAll(MethodsDeliverData.mListImageDescription);
				} else {
					changeImageDescription(mOtherTypeImgsDescripList);
				}

				HousePicGridViewAdapter otherAdapter = new HousePicGridViewAdapter(
						mContext, "other");
				if (MethodsDeliverData.mListImages.size() != 0) {
					mTvOtherPicNumber.setText("其他(" + mOtherTypeImgsList.size()
							+ "/9" + ")");
					otherAdapter.setData(mOtherTypeImgsList,
							mOtherTypeImgsDescripList);
					mGridViewOtherPic.setAdapter(otherAdapter);
					MethodsDeliverData.mHouseType = "";
					MethodsDeliverData.mListImages = new ArrayList<String>();
				}
				break;

			default:
				break;
			}
			MethodsDeliverData.mListImageDescription = new ArrayList<String>();
		}
	}

	@Override
	public void initData() {
		HousePicGridViewAdapter houseTypeAdapter = new HousePicGridViewAdapter(
				mContext, "houseType");
		HousePicGridViewAdapter roomAdapter = new HousePicGridViewAdapter(
				mContext, "room");
		HousePicGridViewAdapter officeAdapter = new HousePicGridViewAdapter(
				mContext, "office");
		HousePicGridViewAdapter toiletAdapter = new HousePicGridViewAdapter(
				mContext, "toilet");
		HousePicGridViewAdapter balconyAdapter = new HousePicGridViewAdapter(
				mContext, "balcony");
		HousePicGridViewAdapter otherAdapter = new HousePicGridViewAdapter(
				mContext, "other");
		// 根据数据设置可以添加的图片数量
		mTvHouseTypePicNumber.setText("房型图(" + "0/9" + ")");// R.string.fangxingtu
		mTvRoomPicNumber.setText("室(" + "0/9" + ")");// R.string.shi+
		mTvOfficePicNumber.setText("厅(" + "0/9" + ")");// R.string.ting+
		mTvToiletPicNumber.setText("厨(" + "0/9" + ")");// R.string.wei+
		mTvBalconyPicNumber.setText("卫(" + "0/9" + ")");// R.string.yang+
		mTvOtherPicNumber.setText("其他(" + "0/9" + ")");// R.string.other+

		houseTypeAdapter.setCount(1);
		roomAdapter.setCount(1);
		officeAdapter.setCount(1);
		toiletAdapter.setCount(1);
		balconyAdapter.setCount(1);
		otherAdapter.setCount(1);
		mGridViewHouseTypePic.setAdapter(houseTypeAdapter);// new
															// HousePicGridViewAdapter(mContext));
		mGridViewRoomPic.setAdapter(roomAdapter);// new
													// HousePicGridViewAdapter(mContext));
		mGridViewOfficePic.setAdapter(officeAdapter);
		mGridViewToiletPic.setAdapter(toiletAdapter);
		mGridViewBalconyPic.setAdapter(balconyAdapter);
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
			uploadAndConnectJs();
			break;
		// 提交照片
		case R.id.rlyt_changeSure_AddHousePicDetailActivity:
			uploadAndConnectJs();
			break;
		default:
			break;
		}

	}

	private void uploadAndConnectJs() {
		mPbUploading.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 计算总共需要上传的图片数量
				mUploadTotalImage = mHouseTypeImgsList.size()
						+ mRoomTypeImgsList.size() + mOfficeTypeImgsList.size()
						+ mOtherTypeImgsList.size()
						+ mBalconyTypeImgsList.size()
						+ mToiletTypeImgsList.size();
				uploadImage(ImageForJsParams.PIC_TYPE_HOUSE,
						mHouseTypeImgsList, mHouseTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_LIVINGROOMT,
						mRoomTypeImgsList, mRoomTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_ROOM,
						mOfficeTypeImgsList, mOfficeTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_OTHER,
						mOtherTypeImgsList, mOtherTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_BALCONY,
						mBalconyTypeImgsList, mBalconyTypeImgsDescripList);
				uploadImage(ImageForJsParams.PIC_TYPE_TOILET,
						mToiletTypeImgsList, mToiletTypeImgsDescripList);
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
				String result = MethodsFile
						.uploadFile(
								"http://61.152.255.241:8081/aist-filesvr-web/servlet/fileUpload",
								list.get(i));
				Log.d("wan", "wanggsx uploadFile " + i + " resut=" + result);
				JSReturn jsReturn = null;
				jsReturn = MethodsJson.jsonToJsReturn(result,
						UploadImageResult.class);
				List<UploadImageResult> imageResult = jsReturn.getListDatas();
				ImageForJsParams tempData = new ImageForJsParams();
				tempData.setType(type);
				tempData.setDesc(descriptionList2.get(index));
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

	}

}
