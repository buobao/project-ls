package com.vocinno.utils.imageutils.selector.photos;

import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.utils.imageutils.selector.tools.ConvertToBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NewMainAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mList;

	public NewMainAdapter(Context context, List<String> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;

		holder = new Holder();
		convertView = View.inflate(mContext,
				R.layout.image_selector_item_image_grid, null);
		holder.iv = (ImageView) convertView.findViewById(R.id.image_launch);
		holder.iv.setVisibility(View.VISIBLE);
		Bitmap bm = ConvertToBitmap.GetToBitmap(mList.get(position), 100, 100);
		holder.iv.setImageBitmap(bm);
		return convertView;
	}

	class Holder {
		private ImageView iv;
	}

}
