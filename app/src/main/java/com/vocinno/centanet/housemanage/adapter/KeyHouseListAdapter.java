package com.vocinno.centanet.housemanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vocinno.centanet.R;
import com.vocinno.centanet.housemanage.HouseDetailActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity2;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.model.KeyHouseItem;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.utils.MethodsDeliverData;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyHouseListAdapter extends BaseAdapter {

	private Context mContext;
	private List<KeyHouseItem> mListHouses;
	private int mType = HouseType.NONE;
	public static Map<Integer,String[]> tagMap;
	public KeyHouseListAdapter(Context mContext, int type) {
		this.mContext = mContext;
		this.mType = type;
		tagMap=new HashMap<>();
	}

	public void setDataList(List<KeyHouseItem> listHouses) {
		this.mListHouses=null;
		this.mListHouses = listHouses;
//		notifyDataSetChanged();
	}
	public void addDataList(List<KeyHouseItem> listHouses) {
		if(this.mListHouses==null||this.mListHouses.size()<=0){
			this.mListHouses = listHouses;
		}else{
			this.mListHouses.addAll(listHouses);
		}
//		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if (mListHouses == null) {
			return 0;
		}
		return mListHouses.size();
	}

	@Override
	public Object getItem(int position) {

		return this.mListHouses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_house_listview, null);
			holder.ll_tag_view = (LinearLayout) convertView.findViewById(R.id.ll_tag_view);
			holder.mImgViewImage = (ImageView) convertView
					.findViewById(R.id.img_image_itemHouseListView);
			holder.mImgViewKeyIcon = (ImageView) convertView
					.findViewById(R.id.img_keyIcon_itemHouseListView);
			holder.mImgisHD = (ImageView) convertView
					.findViewById(R.id.img_ishd);
			holder.mTvAddr = (TextView) convertView
					.findViewById(R.id.tv_addr_itemHouseListView);
			holder.mTvDetail = (TextView) convertView
					.findViewById(R.id.tv_detail_itemHouseListView);
			holder.mTvUnitprice = (TextView) convertView
					.findViewById(R.id.tv_unitprice_itemHouseListView);
			/*holder.mTvTag1 = (TextView) convertView
					.findViewById(R.id.tv_tag1_itemHouseListView);
			holder.mTvTag2 = (TextView) convertView
					.findViewById(R.id.tv_tag2_itemHouseListView);
			holder.mTvTag3 = (TextView) convertView
					.findViewById(R.id.tv_tag3_itemHouseListView);*/
			holder.mTvPrice = (TextView) convertView
					.findViewById(R.id.tv_price_itemHouseListView);
			holder.mTvDateTime = (TextView) convertView
					.findViewById(R.id.tv_time_itemHouseListView);
			holder.mTvKeyState = (TextView) convertView
					.findViewById(R.id.tv_keyState_itemHouseListView);
			holder.mTvUnit = (TextView) convertView
					.findViewById(R.id.tv_danwei_itemHouseListView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final KeyHouseItem item = mListHouses.get(position);
		if(item.ishidden()){
			holder.mTvAddr.setText(item.getAddr());
			holder.mTvKeyState.setText(item.getFloor());
			TextPaint tp = holder.mTvKeyState.getPaint();
			holder.mTvKeyState.setTextSize(11);
			tp.setFakeBoldText(false);
			holder.mTvKeyState.setTextColor(mContext.getResources().getColor(R.color.red));
			if(item.getFloor()==null||item.getFloor().trim().length()<=0){
				holder.mTvKeyState.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
			}else{
				holder.mTvKeyState.setBackgroundResource(R.drawable.bg_house_list_red_house_manage);
			}
		}else{
			holder.mTvKeyState.setText("");
			holder.mTvAddr.setText(item.getAddr()+"  "+item.getBuilding_name());
			TextPaint tp = holder.mTvKeyState.getPaint();
			tp.setFakeBoldText(true);
			holder.mTvKeyState.setTextSize(15);
			holder.mTvKeyState.setTextColor(mContext.getResources().getColor(R.color.house_list_text_color));
			holder.mTvKeyState.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
		}
//		String delCode = item.getDelCode().substring(4, 5);
		String delegationType= item.getDelegationType();
		if(!KeyHouseItem.SHOU.equalsIgnoreCase(delegationType)){
			holder.mTvUnitprice.setVisibility(View.INVISIBLE);
		}

		if(item.getIsHD()==1){
			holder.mImgisHD.setVisibility(View.VISIBLE);
		}else{
			holder.mImgisHD.setVisibility(View.GONE);
		}
		if (item.getKeyStatus().equals("在店")
				&& Integer.parseInt(item.getKeyCount()) > 0) {
			holder.mImgViewKeyIcon.setVisibility(View.VISIBLE);
		} else {
			holder.mImgViewKeyIcon.setVisibility(View.INVISIBLE);
		}
		holder.mTvDetail.setText(item.getFrame() + " " + item.getSquare() + "平"
				+ " " + item.getOrient());
		BigDecimal bUnitPrice, bPrice;
		if (item.getUnitprice().equals("NaN")) {
			bUnitPrice = new BigDecimal("0.00");
		} else {
			try {
				bUnitPrice = new BigDecimal(item.getUnitprice());// 保留两位小数
			} catch (Exception e) {
				bUnitPrice = new BigDecimal("0.00");// 保留两位小数
			}
		}

		if (KeyHouseItem.ZU.equalsIgnoreCase(delegationType)) {
			holder.mTvUnitprice.setText(bUnitPrice.setScale(2,
					BigDecimal.ROUND_HALF_UP) + "万/㎡");
			try {
				bPrice = new BigDecimal(item.getPrice());
			} catch (Exception e) {
				bPrice = new BigDecimal("0.00");
			}
			holder.mTvPrice.setText(bPrice
					.setScale(0, BigDecimal.ROUND_HALF_UP) + "");
			holder.mTvUnit.setText("元");
		} else {
			holder.mTvUnitprice.setText(bUnitPrice.setScale(2,
					BigDecimal.ROUND_HALF_UP) + "万/㎡");// 单价
			try {
				bPrice = new BigDecimal(
						Double.parseDouble(item.getPrice()) / 10000);
			} catch (Exception e) {
				bPrice = new BigDecimal("0.00");
			}
			holder.mTvPrice.setText(bPrice
					.setScale(0, BigDecimal.ROUND_HALF_UP) + "");
			holder.mTvUnit.setText("万");
		}
		holder.mTvDateTime.setText(item.getActiveTime());
//		holder.mTvTag1.setText(item.getTag());

		if(tagMap==null){
			tagMap=new HashMap<>();
		}
		if(!(item.getTag()==null||item.getTag().trim().length()<=0)){
			tagMap.put(position,getTag(item.getTag()));
//			String[] itemTag = getTag(item.getTag());
			int dip = MyUtils.px2dip(mContext, (float)7);
			LinearLayout.LayoutParams LayoutParams=new LinearLayout.LayoutParams(-2,-2);
			LayoutParams.setMargins(MyUtils.px2dip(mContext, (float)12),0,0,0);
			holder.ll_tag_view.removeAllViews();
			for (int i = 0; i <tagMap.get(position).length ; i++) {
				TextView tv=new TextView(mContext);
				tv.setText(tagMap.get(position)[i]);
				tv.setTextSize(11);
				tv.setPadding(dip, 0, dip, 0);
				tv.setGravity(Gravity.CENTER);
				tv.setLayoutParams(LayoutParams);
				tv.setTextColor(mContext.getResources().getColor(R.color.house_tag_color));
				tv.setBackgroundResource(R.drawable.bg_house_tag_blue_house_manage);
				holder.ll_tag_view.addView(tv);
			}
		}
		/*if (!item.getTag().equals("")) {
			holder.mTvTag1.setVisibility(View.VISIBLE);
		} else {
			holder.mTvTag1.setVisibility(View.INVISIBLE);
		}*/
		if (item.getImg()!=null&&item.getImg().length()>0) {
			/*MethodsFile.downloadAsynicImageByUrl((Activity) mContext, item
					.getImg().get(0).getUrl(), holder.mImgViewImage);*/
			Glide.with(mContext).load(item.getImg()).centerCrop()
					.crossFade()
					.into(holder.mImgViewImage);
		}
		if (position % 2 == 1) {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.white));
		} else {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.lightgray));
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MethodsDeliverData.mDelCode = item.getDelCode();
				if (mType == HouseType.YAO_SHI) {
					MethodsDeliverData.flag = -1;
					MethodsDeliverData.mKeyType = 1;
				} else if (mType == HouseType.GONG_FANG) {
					MethodsDeliverData.flag = 1;
					MethodsDeliverData.mKeyType = -1;
				} else {
					MethodsDeliverData.flag = -1;
					MethodsDeliverData.mKeyType = -1;
				}
//				MethodsExtra.startActivity(mContext, HouseDetailActivity.class);
				Intent intent=new Intent(mContext, HouseDetailActivity.class);
				intent.putExtra(MyUtils.INTO_FROM_LIST,true);
				((HouseManageActivity2) mContext).startActivityForResult(intent, 10);
			}
		});
		return convertView;
	}
	public String[] getTag(String string){
		String[]stringTag=string.split(",");
		return stringTag;
	}
	public class ViewHolder {
		LinearLayout ll_tag_view;
		ImageView mImgViewImage;
		ImageView mImgViewKeyIcon;
		ImageView mImgisHD;
		TextView mTvAddr;
		TextView mTvDetail;
		TextView mTvUnitprice;
//		TextView mTvTag1, mTvTag2, mTvTag3;
		TextView mTvPrice;
		TextView mTvDateTime;
		TextView mTvKeyState;
		TextView mTvUnit;
	}
}
