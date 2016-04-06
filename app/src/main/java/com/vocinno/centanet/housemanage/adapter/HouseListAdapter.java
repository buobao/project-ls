package com.vocinno.centanet.housemanage.adapter;

import java.math.BigDecimal;
import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.housemanage.HouseDetailActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HouseListAdapter extends BaseAdapter {

	private Context mContext;
	private List<HouseItem> mListHouses;
	private int mType = HouseType.NONE;

	public HouseListAdapter(Context mContext, int type) {
		this.mContext = mContext;
		this.mType = type;
	}

	public void setDataList(List<HouseItem> listHouses) {
		this.mListHouses = listHouses;
		notifyDataSetChanged();
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

		return null;
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_house_listview, null);
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
			holder.mTvTag1 = (TextView) convertView
					.findViewById(R.id.tv_tag1_itemHouseListView);
			holder.mTvTag2 = (TextView) convertView
					.findViewById(R.id.tv_tag2_itemHouseListView);
			holder.mTvTag3 = (TextView) convertView
					.findViewById(R.id.tv_tag3_itemHouseListView);
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
		final HouseItem item = mListHouses.get(position);
		String delCode = item.getDelCode().substring(4, 5);
		if(!"S".equalsIgnoreCase(delCode)){
			holder.mTvUnitprice.setVisibility(View.INVISIBLE);
		}
		holder.mTvAddr.setText(item.getAddr());

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
		holder.mTvDetail.setText(item.getFrame() + " " + item.getSquare() + "㎡"
				+ " " + item.getFloor() + " " + item.getOrient());
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

		if (item.getDelCode().charAt(4) == 'Z') {
			holder.mTvUnitprice.setText(bUnitPrice.setScale(2,
					BigDecimal.ROUND_HALF_UP) + "万/㎡");
			try {
				bPrice = new BigDecimal(item.getPrice());
			} catch (Exception e) {
				bPrice = new BigDecimal("0.00");
			}
			holder.mTvPrice.setText(bPrice
					.setScale(2, BigDecimal.ROUND_HALF_UP) + "");
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
					.setScale(2, BigDecimal.ROUND_HALF_UP) + "");
			holder.mTvUnit.setText("万");
		}
		holder.mTvDateTime.setText(item.getActiveTime());
		holder.mTvTag1.setText(item.getTag());
		if (!item.getTag().equals("")) {
			holder.mTvTag1.setVisibility(View.VISIBLE);
		} else {
			holder.mTvTag1.setVisibility(View.INVISIBLE);
		}
		if (item.getImg() != null || item.getImg().size() != 0) {
			MethodsFile.downloadAsynicImageByUrl((Activity) mContext, item
					.getImg().get(0).getUrl(), holder.mImgViewImage);
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
				((HouseManageActivity) mContext).startActivityForResult(intent, 10);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		ImageView mImgViewImage;
		ImageView mImgViewKeyIcon;
		ImageView mImgisHD;
		TextView mTvAddr;
		TextView mTvDetail;
		TextView mTvUnitprice;
		TextView mTvTag1, mTvTag2, mTvTag3;
		TextView mTvPrice;
		TextView mTvDateTime;
		TextView mTvKeyState;
		TextView mTvUnit;
	}
}
