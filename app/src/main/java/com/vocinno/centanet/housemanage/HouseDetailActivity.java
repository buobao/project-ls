package com.vocinno.centanet.housemanage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.AppInstance;
import com.vocinno.centanet.apputils.SharedPreferencesUtils;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.adapter.CustormerPhoneAdapter;
import com.vocinno.centanet.housemanage.adapter.HousePagerAdapter;
import com.vocinno.centanet.model.BorrowKey;
import com.vocinno.centanet.model.ContactDetail;
import com.vocinno.centanet.model.ContactItem;
import com.vocinno.centanet.model.HouseDetail;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.centanet.model.Image;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.Track;
import com.vocinno.centanet.myinterface.AgainLoading;
import com.vocinno.centanet.myinterface.NoDoubleClickListener;
import com.vocinno.centanet.tools.DivideUtils;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.centanet.tools.customview.CustomListView;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房源详情
 *
 * @author Administrator
 *
 */
public class HouseDetailActivity extends OtherBaseActivity implements AgainLoading{
	private HouseDetail houseDetail = null;
	private String shareImgUrl=null;
	private int shareTag;
	private View mBackView, mMoreView, mTitleView;
	private Dialog mMenuDialog;
	private Dialog mCallCustormerDialog;
	private List<Image> imageUrl;
	private String houseCode;
	private TextView tv_house_detail_name,tv_house_detail_code,tv_house_detail_total_price,tv_house_detail_param,tv_house_detail_area,
			tv_house_detail_toward,tv_house_detail_unit_price,tv_house_detail_number,tv_house_detail_louceng,tv_house_detail_niandai,
			tv_house_detail_date,tv_house_detail_pianqu,tv_house_detail_lookshihao, tv_house_detail_shihao;
	private ViewPager vp_house_detail_img;
	private List<ImageView> ivList;
	private HousePagerAdapter hpAdapter;
	private HouseDetailGenJinAdapter houseDetailGenJinAdapter;
	private CustomListView lv_house_detail_list;
	private ScrollView sv_housedetail;
	private LinearLayout ll_house_detail_addgenjin,ll_house_detail_qiang;
	private boolean isGongFang;

	private String houseDetailPrice;	//房源详情 价格

	@Override
	@SuppressLint("HandlerLeak")
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case R.id.doGetImg:
						Bitmap bitmap=(Bitmap)msg.obj;
						wechatShare(zoomImage(bitmap, 100,100));
						break;
					case R.id.doGetImgError:
						loadImageSimpleTargetApplicationContext();
						break;
					default:
						break;
				}
			}
		};
	}
	private SimpleTarget target = new SimpleTarget<Bitmap>( 96, 96 ) {
		@Override
		public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//			imageView2.setImageBitmap( bitmap );
//			Bitmap map=BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
			if(bitmap!=null){
				wechatShare(bitmap);
			}else{
				Bitmap map=BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
				wechatShare(map);
			}

		}
	};

	private void loadImageSimpleTargetApplicationContext() {
		Glide.with(this.getApplicationContext()) // safer!
				.load(imageUrl.get(0).getUrl())
				.asBitmap()
				.into( target );
	}
	@Override
	public int setContentLayoutId() {
		return R.layout.activity_house_detail;
	}

	@Override
	public void initView() {
		drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		setView();
		houseCode = getIntent().getStringExtra(MyConstant.houseCode);
		isGongFang = getIntent().getBooleanExtra(MyConstant.isGongFang,false);
		if(isGongFang){
			ll_house_detail_qiang.setVisibility(View.VISIBLE);
			ll_house_detail_borrow.setVisibility(View.GONE);
			ll_house_detail_contact.setVisibility(View.GONE);
		}else{
			ll_house_detail_qiang.setVisibility(View.GONE);
			ll_house_detail_borrow.setVisibility(View.GONE);
			ll_house_detail_contact.setVisibility(View.VISIBLE);
		}
		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.housedecribe,
				null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		mMoreView = MethodsExtra.findHeadRightView1(mContext, baseView, 0, 0);
		mBackView.setOnClickListener(this);
		mMoreView.setOnClickListener(new NoDoubleClickListener() {
			@Override
			protected void onNoDoubleClick(View v) {
				if(houseDetail!=null&&houseDetail.getDelCode()!=null){
					showMenuDialog();
				}
			}
		});
		mTitleView = MethodsExtra
				.findHeadTitle1(mContext, baseView, 0, "房源详情");
	}

	private void setView() {
		tv_house_detail_name= (TextView) findViewById(R.id.tv_house_detail_name);
		tv_house_detail_code= (TextView) findViewById(R.id.tv_house_detail_code);
		tv_house_detail_total_price= (TextView) findViewById(R.id.tv_house_detail_total_price);
		tv_house_detail_param= (TextView) findViewById(R.id.tv_house_detail_param);
		tv_house_detail_area= (TextView) findViewById(R.id.tv_house_detail_area);
		tv_house_detail_toward= (TextView) findViewById(R.id.tv_house_detail_toward);
		tv_house_detail_unit_price= (TextView) findViewById(R.id.tv_house_detail_unit_price);
		tv_house_detail_number= (TextView) findViewById(R.id.tv_house_detail_number);
		tv_house_detail_louceng= (TextView) findViewById(R.id.tv_house_detail_louceng);
		tv_house_detail_niandai= (TextView) findViewById(R.id.tv_house_detail_niandai);
		tv_house_detail_date= (TextView) findViewById(R.id.tv_house_detail_date);
		tv_house_detail_pianqu= (TextView) findViewById(R.id.tv_house_detail_pianqu);
		tv_house_detail_lookshihao= (TextView) findViewById(R.id.tv_house_detail_lookshihao);
		tv_house_detail_lookshihao.setOnClickListener(this);

		ll_house_detail_qiang = (LinearLayout)findViewById(R.id.ll_house_detail_qiang);
		ll_house_detail_qiang.setOnClickListener(this);

		ll_house_detail_borrow = (LinearLayout)findViewById(R.id.ll_house_detail_borrow);
		ll_house_detail_borrow.setOnClickListener(this);

		ll_house_detail_contact = (LinearLayout)findViewById(R.id.ll_house_detail_contact);
		ll_house_detail_contact.setOnClickListener(this);

		tv_house_detail_shihao = (TextView) findViewById(R.id.tv_house_detail_shihao);

		ll_house_detail_addgenjin= (LinearLayout) findViewById(R.id.ll_house_detail_addgenjin);
		ll_house_detail_addgenjin.setOnClickListener(this);

		sv_housedetail= (ScrollView) findViewById(R.id.sv_housedetail);
		lv_house_detail_list= (CustomListView) findViewById(R.id.lv_house_detail_list);

		vp_house_detail_img= (ViewPager) findViewById(R.id.vp_house_detail_img);
		vp_house_detail_img.setOffscreenPageLimit(1);
		vp_house_detail_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				tv_house_detail_number.setText(position + 1 + "/" + imageUrl.size());
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		pl_progress.setAgainLoading((AgainLoading)this);
	}
	private void setData() {
//		showDialog();
		pl_progress.showProgress();
		URL= NetWorkConstant.PORT_URL+ NetWorkMethod.houInfo;
		Map<String,String>map=new HashMap<String,String>();
		map.put(NetWorkMethod.delCode, houseCode);
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				dismissDialog();
				pl_progress.showErrorText();
			}
			@Override
			public void onResponse(String response) {
				dismissDialog();
				JSReturn jReturn = MethodsJson.jsonToJsReturn(response,HouseDetail.class);
				if (!jReturn.isSuccess()) {
//					MethodsExtra.toast(mContext, jReturn.getMsg());
					myDialog = new MyDialog.Builder(HouseDetailActivity.this);
					myDialog.setMessage(jReturn.getMsg());
					myDialog.setTitle("提示");
					myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					myDialog.setDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							finish();
						}
					});
					myDialog.create().show();
				} else {
					setHouseInfo(jReturn);
				}
				pl_progress.showContent();
			}
		});
	}

	private void setHouseInfo(JSReturn jReturn) {
		houseDetail = (HouseDetail) jReturn.getObject();
		imageUrl = houseDetail.getImg();
		hpAdapter=new HousePagerAdapter(this);
		ivList=new ArrayList<ImageView>();
		for(int i=0;i< imageUrl.size();i++){
			ImageView iv=new ImageView(this);
			iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iv.setAdjustViewBounds(true);
			iv.setMaxHeight(MyUtils.dip2px(this, 250));
			ivList.add(iv);
		}
		tv_house_detail_number.setText((imageUrl.size() == 0 ? 0 : 1) + "/" + imageUrl.size());
		hpAdapter.setUrlList(imageUrl);
		hpAdapter.setList(ivList);
		vp_house_detail_img.setAdapter(hpAdapter);

		tv_house_detail_name.setText(houseDetail.getAddr());
		tv_house_detail_code.setText(houseDetail.getDelCode());
		tv_house_detail_param.setText(houseDetail.getFrame());
		tv_house_detail_area.setText( houseDetail.getSquare()+"㎡");
		tv_house_detail_toward.setText( houseDetail.getOrient());
		tv_house_detail_louceng.setText(houseDetail.getFloor());
		tv_house_detail_niandai.setText(houseDetail.getYear());
		tv_house_detail_date.setText(houseDetail.getDelDate());
		tv_house_detail_pianqu.setText(houseDetail.getArea());
		if(!houseDetail.isShowroomBtn()){
			tv_house_detail_shihao.setText(houseDetail.getRoomNo());
			tv_house_detail_shihao.setVisibility(View.VISIBLE);
			tv_house_detail_louceng.setText(houseDetail.getFloor());
			tv_house_detail_lookshihao.setVisibility(View.GONE);
			tv_house_detail_name.setText(houseDetail.getAddr() + " " + houseDetail.getBuildingname());

		}
		BigDecimal bPrice;
		if (HouseItem.ZU.equals(houseDetail.getDelegationType())) {
			/**************************租房****************************/
			try {
				bPrice = new BigDecimal(houseDetail.getPrice());
			} catch (Exception e) {
				bPrice = new BigDecimal("0.00");
			}
			tv_house_detail_total_price.setText(bPrice.setScale(0,BigDecimal.ROUND_HALF_UP) +"元");
			tv_house_detail_unit_price.setText("");
		} else {
			/**************************售房****************************/
			try {
				bPrice = new BigDecimal(Double.parseDouble(houseDetail.getPrice()));
				bPrice = DivideUtils.divide(bPrice,2);
			} catch (Exception e) {
				bPrice = new BigDecimal("0.00");
			}

			if(DivideUtils.isWan){
				houseDetailPrice = bPrice +"万";
			}else{
				houseDetailPrice = bPrice +"亿";
			}

			double square=Double.parseDouble(houseDetail.getSquare());
			double price=Double.parseDouble(houseDetail.getPrice()) / 10000;
			double unitPrice=MyUtils.division(price,square,2);
			tv_house_detail_total_price.setText(houseDetailPrice);
			tv_house_detail_unit_price.setText(unitPrice+ "万/㎡ ");
		}
		List<Track> trackList = houseDetail.getTrack();
		houseDetailGenJinAdapter=new HouseDetailGenJinAdapter(this);
		houseDetailGenJinAdapter.setList(trackList);
		lv_house_detail_list.setAdapter(houseDetailGenJinAdapter);
		sv_housedetail.postDelayed(new Runnable() {
			@Override
			public void run() {
//				sv_housedetail.fullScroll(ScrollView.FOCUS_UP);
//				sv_housedetail.scrollTo(0,0);
			}
		}, 100);
	}

	@Override
	public void initData() {
		setData();
	}

	/**************************增加实勘页面 完成后回调****************************/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
			case 101:
				String  roomNo=data.getStringExtra("roomNo");
				String  buiding=data.getStringExtra("buiding");
				String  floor=data.getStringExtra(MyConstant.floor);
				tv_house_detail_shihao.setText(roomNo);
				tv_house_detail_shihao.setVisibility(View.VISIBLE);
				tv_house_detail_louceng.setText(floor);
				tv_house_detail_lookshihao.setVisibility(View.GONE);
				tv_house_detail_name.setText(houseDetail.getAddr() + " " + buiding);
				break;
			case MyConstant.REFRESH:
				setData();
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_house_detail_qiang:
				qiangGangFang();
				break;
			case R.id.ll_house_detail_borrow:
				borrowKey();
				break;
			case R.id.ll_house_detail_contact:
				showCallCosturmerDialog();
				break;
			case R.id.tv_house_detail_lookshihao:
				if(!houseDetail.isRequireReason()){//false不需要原因，true需要
					lookShiHao();
				}else{
					String delCode=houseDetail.getDelCode();
					String houseId=houseDetail.getHouseId();
					Intent intent = new Intent(mContext,
							HouseReasonActivity.class);
					intent.putExtra("delCode",delCode);
					intent.putExtra("houseId",houseId);
					MethodsExtra.startActivityForResult(mContext, 100, intent);
				}
				break;
			case R.id.ll_house_detail_addgenjin:
				if (houseDetail != null && houseDetail.getDelCode() != null) {
					MethodsDeliverData.mDelCode = houseDetail.getDelCode();
				} else {
					MethodsExtra.toast(mContext, "房源编号不能为空");
				}
				intent.setClass(mContext, AddFollowInHouseActivity.class);
				intent.putExtra(MyConstant.houseCode,houseDetail.getDelCode());
				startActivityForResult(intent, 10);
				break;
			case R.id.img_left_mhead1:
				finish();
				break;
			case R.id.ll_house_detail_borrow_key:
				mMenuDialog.dismiss();
				MethodsDeliverData.mKeyType = 1;
				showBorrowKey();
				break;
			case R.id.ll_house_detail_contact_yezhu:
				mMenuDialog.dismiss();
				showBorrowKey(false);
				showCallCosturmerDialog();
				break;
			case R.id.ll_house_detail_share_friend:
				// 这里是友盟分享的dialog
				mMenuDialog.dismiss();
				shareTag=0;
//			wechatShare(0);
				Loading.show(this);
//			returnBitmap(imageUrl.get(0).getUrl());
//			String imgUrl=imageUrl.get(0).getUrl();
				if(imageUrl!=null&&imageUrl.size()>0){
					loadImageSimpleTargetApplicationContext();
				}else{
					Bitmap map=BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
					wechatShare(map);
				}

				break;
			case R.id.ll_house_detail_share_friend_circle:
				// 这里是友盟分享的dialog
				mMenuDialog.dismiss();
				shareTag=1;
				Loading.show(this);
//			returnBitmap(imageUrl.get(0).getUrl());
//			String imgUrl2=imageUrl.get(0).getUrl();
				if(imageUrl!=null&&imageUrl.size()>0){
					loadImageSimpleTargetApplicationContext();
				}else{
					Bitmap map=BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
					wechatShare(map);
				}
				break;
			case R.id.ll_house_detail_addimg:
				mMenuDialog.dismiss();
				if (houseDetail != null && houseDetail.getDelCode() != null) {
					MethodsDeliverData.mDelCode = houseDetail.getDelCode();
				} else {
					MethodsExtra.toast(mContext, "houseDetail不能为空");
				}
				if(houseDetail !=null&& houseDetail.getDelCode()!=null){
					Intent addHousePictureIntent=new Intent(this,AddHousePictureActivity.class);
					addHousePictureIntent.putExtra("delCode", houseDetail.getDelCode());
					addHousePictureIntent.putExtra("explmsg", houseDetail.getExplmsg());
					this.startActivityForResult(addHousePictureIntent, MyConstant.addPic);
				}else{
					MethodsExtra.toast(this,"房源编号为空无法增加实勘");
				}
				break;
			case R.id.ll_house_detail_followsee:
				mMenuDialog.dismiss();
				if (houseDetail != null && houseDetail.getDelCode() != null) {
					MethodsDeliverData.mDelCode = houseDetail.getDelCode();
					Intent it=new Intent(mContext,SeeFollowInDetailActivity.class);
					it.putExtra("delegationType", houseDetail.getDelegationType());
					it.putExtra(MyConstant.houseCode, houseDetail.getDelCode());
					startActivity(it);
				} else {
					MethodsExtra.toast(mContext, "houseDetail不能为空");
					Intent it=new Intent(mContext,SeeFollowInDetailActivity.class);
					it.putExtra("delegationType","");
					startActivityForResult(it,100);
				}
//			MethodsExtra.startActivity(mContext,SeeFollowInDetailActivity.class);
				break;
			default:
				break;
		}
	}

	private void qiangGangFang() {
		Loading.show(this);
		URL=NetWorkConstant.PORT_URL+NetWorkMethod.claim;
		Map<String, String> map=new HashMap<String,String>();
		map.put(NetWorkMethod.delCode,houseDetail.getDelCode());
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Loading.dismissLoading();
			}
			@Override
			public void onResponse(String response) {
				Loading.dismissLoading();
				JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) response,Object.class);
				if (jsReturn.isSuccess()) {
					MethodsExtra.toast(mContext, jsReturn.getMsg());
					setResult(MyConstant.REFRESH);
					finish();
				} else {
					MethodsExtra.toast(mContext, jsReturn.getMsg());
				}
			}
		});
	}

	private void borrowKey() {
		showDialog();
		//mobile/hou/key/borrow
		URL=NetWorkConstant.PORT_URL+NetWorkMethod.borrow;
		Map<String, String> map=new HashMap<String,String>();
		map.put(NetWorkMethod.delCode,houseDetail.getDelCode());
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				dismissDialog();
			}
			@Override
			public void onResponse(String response) {
				dismissDialog();
				JSReturn jReturn = MethodsJson.jsonToJsReturn(response,BorrowKey.class);
				BorrowKey borrowKey = (BorrowKey) jReturn.getObject();
				if (borrowKey != null) {
					if (borrowKey.isSuccess() == false) {
						MethodsExtra.toast(mContext,borrowKey.getMsg());
						MethodsDeliverData.mKeyType = 4;
					} else {
						MethodsExtra.toast(mContext, borrowKey.getMsg());
					}
				} else {
					// 钥匙不存在
					MethodsDeliverData.mKeyType = 0;
					MethodsDeliverData.mKeyType = 4;
				}
			}
		});
	}

	private void lookShiHao() {
		showDialog();
		URL=NetWorkConstant.PORT_URL+NetWorkMethod.doRoomview;
		Map<String, String> map=new HashMap<String,String>();
		//"查看室号", houseDetail.getDelCode(), houseDetail.getHouseId(), "10080001"))
		map.put(NetWorkMethod.type,"10080001");
		map.put(NetWorkMethod.reason,"查看室号");
		map.put(NetWorkMethod.delCode,houseDetail.getDelCode());
		map.put(NetWorkMethod.houseId,houseDetail.getHouseId());
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				dismissDialog();
			}
			@Override
			public void onResponse(String response) {
				dismissDialog();
				JSReturn jReturnHouseDetail = MethodsJson.jsonToJsReturn(response,HouseDetail.class);
				HouseDetail hDetail = (HouseDetail) jReturnHouseDetail.getObject();
				if(jReturnHouseDetail.isSuccess()){
					String roomNo= hDetail.getRoomNo();
					tv_house_detail_name.setText(houseDetail.getAddr() + " " + hDetail.getBuiding());
					tv_house_detail_lookshihao.setVisibility(View.GONE);
					tv_house_detail_louceng.setText(hDetail.getFloor());
					tv_house_detail_shihao.setText(roomNo);
					tv_house_detail_shihao.setVisibility(View.VISIBLE);
				}else{
					MethodsExtra.toast(mContext, jReturnHouseDetail.getMsg());
				}
			}
		});
	}
	private LinearLayout ll_house_detail_borrow_key,ll_house_detail_contact_yezhu,ll_house_detail_share_friend,
			ll_house_detail_share_friend_circle,
			ll_house_detail_addimg,ll_house_detail_followsee,ll_house_detail_contact,ll_house_detail_borrow;
	private void showMenuDialog() {
		mMenuDialog = new Dialog(mContext, R.style.Theme_dialog);
		mMenuDialog.setContentView(R.layout.dialog_menu_house_resouse_detail);
		Window win = mMenuDialog.getWindow();
		win.setGravity(Gravity.RIGHT | Gravity.TOP);
		mMenuDialog.setCanceledOnTouchOutside(true);
		mMenuDialog.show();
		win.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		ll_house_detail_borrow_key = (LinearLayout) mMenuDialog.findViewById(R.id.ll_house_detail_borrow_key);
		ll_house_detail_contact_yezhu = (LinearLayout) mMenuDialog.findViewById(R.id.ll_house_detail_contact_yezhu);
		ll_house_detail_share_friend = (LinearLayout) mMenuDialog.findViewById(R.id.ll_house_detail_share_friend);
		ll_house_detail_share_friend_circle = (LinearLayout) mMenuDialog.findViewById(R.id.ll_house_detail_share_friend_circle);
		ll_house_detail_addimg = (LinearLayout) mMenuDialog.findViewById(R.id.ll_house_detail_addimg);
		ll_house_detail_followsee = (LinearLayout) mMenuDialog.findViewById(R.id.ll_house_detail_followsee);

		ll_house_detail_borrow_key.setOnClickListener(this);
		ll_house_detail_contact_yezhu.setOnClickListener(this);
		ll_house_detail_share_friend.setOnClickListener(this);
		ll_house_detail_share_friend_circle.setOnClickListener(this);
		ll_house_detail_addimg.setOnClickListener(this);
		ll_house_detail_followsee.setOnClickListener(this);
	}

	private void showCallCosturmerDialog() {
		mCallCustormerDialog = new Dialog(mContext, R.style.Theme_dialog);
		mCallCustormerDialog
				.setContentView(R.layout.dialog_call_custormer_house_resouse_detial);
		Window win = mCallCustormerDialog.getWindow();
		win.setGravity(Gravity.BOTTOM);
		mCallCustormerDialog.setCanceledOnTouchOutside(true);

		win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		getContactList();
	}

	private void getContactList() {
		Loading.show(this);
		URL=NetWorkConstant.PORT_URL+NetWorkMethod.contactList;
		Map<String, String> map=new HashMap<String,String>();
		map.put(NetWorkMethod.delCode,houseDetail.getDelCode());
		map.put(NetWorkMethod.delegationType,houseDetail.getDelegationType());
		map.put(NetWorkMethod.disturb,houseDetail.getDisturb());
		map.put(NetWorkMethod.isfgint,houseDetail.getIsfgint());
		map.put(NetWorkMethod.isqpct,houseDetail.getIsqpct());
		map.put(NetWorkMethod.houseId,houseDetail.getHouseId());
		/*delegationType ="delegationType ";
    public static final String disturb ="disturb";
    public static final String isfgint ="isfgint";
    public static final String isqpct  ="isqpct";*/
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Loading.dismissLoading();
			}

			@Override
			public void onResponse(String response) {
				Loading.dismissLoading();
				JSReturn jReturn = MethodsJson.jsonToJsReturn(response,ContactDetail.class);
				if(jReturn.isSuccess()){
					ContactDetail cDetail = (ContactDetail)jReturn.getObject();
					if(cDetail.isSuccess()){
						ListView mListViewCustormer = (ListView) mCallCustormerDialog.findViewById(R.id.lv_custormerPhone_HouseDetailActivity);
						ContactDetail contactData = (ContactDetail) jReturn.getObject();
						List<ContactItem> testData = contactData.getContactList(); // new
						if (testData == null || testData.size() == 0) {
							MethodsExtra.toast(mContext, "此房源未录入电话");
							return;
						}
						CustormerPhoneAdapter phoneAdapter = new CustormerPhoneAdapter(
								mContext, testData);
						mListViewCustormer.setAdapter(phoneAdapter);
						mListViewCustormer
								.setOnItemClickListener(new AdapterView.OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> arg0, View arg1,
															int arg2, long arg3) {
										TextView tvTel = (TextView) arg1
												.findViewById(R.id.tv_custNothing_CustormerPhoneAdapter);

										Intent intent = new Intent(Intent.ACTION_CALL, Uri
												.parse("tel:" + tvTel.getText().toString().trim()));
										mContext.startActivity(intent);
										mCallCustormerDialog.dismiss();
									}
								});
						mCallCustormerDialog.show();
					}else{
						MyToast.showToast(cDetail.getMsg());
					}
				}else{
					MyToast.showToast(jReturn.getMsg());
				}

			}
		});
	}

	private void showBorrowKey(boolean flag) {
		ll_house_detail_qiang.setVisibility(View.GONE);
		if(isGongFang){

		}
		if(flag){
			ll_house_detail_borrow.setVisibility(View.VISIBLE);
			ll_house_detail_contact.setVisibility(View.GONE);
		}else{
			ll_house_detail_borrow.setVisibility(View.GONE);
			ll_house_detail_contact.setVisibility(View.VISIBLE);
		}

	}
	// 展示钥匙相关按钮
	private void showBorrowKey() {
		showBorrowKey(true);
	}
	/**
	 * 微信分享 0：分享到微信好友 1：分享到微信朋友圈
	 **/
	private void wechatShare(Bitmap bitmap) {
		Loading.dismissLoading();
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://a.sh.centanet.com/sales-web/mobile/houShare/"
				+ houseDetail.getDelCode() + "/"
				+ SharedPreferencesUtils.getUserId(mContext);
		WXMediaMessage msg = new WXMediaMessage(webpage);
		BigDecimal bPrice = new BigDecimal(houseDetail.getPrice());
		bPrice=bPrice.divide(new BigDecimal(10000), 0, BigDecimal.ROUND_HALF_UP);
		BigDecimal bSquare = new BigDecimal(houseDetail.getSquare());
		bSquare=bSquare.setScale(0, BigDecimal.ROUND_HALF_UP);
		msg.title = houseDetail.getArea()+" "+ houseDetail.getAddr()+" "+ houseDetail.getFrame()+" "+bSquare+"平 "+bPrice+"万";
		msg.description = "中原地产房源分享";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		bitmap.recycle();
		msg.thumbData =  baos.toByteArray();
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "img"+String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = shareTag == 0 ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		AppInstance.mWXAPI.sendReq(req);
	}
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
								   double newHeight) {
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}
	// 在需要分享的地方添加代码:
	// wechatShare(0);//分享到微信好友
	// wechatShare(1);//分享到微信朋友圈
	private void returnBitmap(final String urlpath) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Bitmap btMap =null;
					URL url = new URL(urlpath);
					URLConnection conn = url.openConnection();
					InputStream in;
					conn.connect();
					in = conn.getInputStream();
					btMap = BitmapFactory.decodeStream(in);
					dismissDialog();
					Message msg = Message.obtain();
					msg.what =R.id.doGetImg;
					msg.obj=btMap;
					mHander.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					dismissDialog();
					mHander.sendEmptyMessage(R.id.doGetImgError);
//					MethodsExtra.toast(mContext,"图片获取失败");
				}
			}
		}).start();
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

	@Override
	public void againLoading() {
		setData();
	}
}
