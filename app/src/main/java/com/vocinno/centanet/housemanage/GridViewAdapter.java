package com.vocinno.centanet.housemanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vocinno.centanet.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {

	private Activity mContext;

	private List<String> mSelectedTags = new ArrayList<String>();
	List<Map<String, ?>> listDatas = new ArrayList<Map<String, ?>>();

	public GridViewAdapter(Activity context, List<String> tags,
			List<String> listSelectedTags) {
		this.mContext = context;
		setData(tags);
		mSelectedTags = listSelectedTags;
	}

	public void setData(List<String> tags) {
		for (int i = 0; i < tags.size(); i++) {
			Map map = new HashMap<String, String>();
			map.put("title", tags.get(i));
			map.put("id", i);
			listDatas.add(map);
		}
	}

	@Override
	public int getCount() {
		return listDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return listDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public String getSelectedTags() {
		String tags = "";
		for (int i = 0; i < mSelectedTags.size(); i++) {
			if (tags.equals("")) {
				tags = mSelectedTags.get(i).trim();
			} else {
				tags = tags + "-" + mSelectedTags.get(i).trim();
			}
		}
		return tags;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TagItemViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.model_tag_item_selector, null);
			viewHolder = new TagItemViewHolder();
			viewHolder.btnButton = (Button) convertView
					.findViewById(R.id.btn_modelTagItemSelector);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.imgView_modelTagItemSelector);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (TagItemViewHolder) convertView.getTag();
		}
		String text = (String) listDatas.get(position).get("title");
		viewHolder.btnButton.setTag(1000 + position);
		viewHolder.btnButton.setSelected(false);
		viewHolder.btnButton.setText(text);
		if (mSelectedTags != null && mSelectedTags.size() >= 1) {
			for (int i = 0; i < mSelectedTags.size(); i++) {
				if (mSelectedTags.get(i).equals(text)) {
					switchState(position, viewHolder.btnButton, false);
				}
			}
		}
		final int index = position;
		viewHolder.btnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchState(index, v, true);
			}
		});
		return convertView;
	}

	// 选中或取消选中状态变更后需要更新背景
	void switchState(final int postion, final View v, boolean canEdit) {
		if (listDatas == null || listDatas.size() == 0) {
			return;
		}
		View viewP = (View) v.getParent();
		ImageView imgView = (ImageView) viewP
				.findViewById(R.id.imgView_modelTagItemSelector);
		if (v.isSelected()) {
			v.setSelected(false);
			imgView.setSelected(false);
			((Button) v).setTextColor(mContext.getResources().getColor(
					R.color.black));
			if (canEdit) {
				String strText = (String) listDatas.get(postion).get("title");
				for (int i = 0; i < mSelectedTags.size(); i++) {
					if (mSelectedTags.get(i).equals(strText)) {
						mSelectedTags.remove(i);
						i--;
					}
				}
			}
		} else {
			v.setSelected(true);
			imgView.setSelected(true);
			((Button) v).setTextColor(mContext.getResources().getColor(
					R.color.white));
			if (canEdit) {
				String strText = (String) listDatas.get(postion).get("title");
				for (int i = 0; i < mSelectedTags.size(); i++) {
					if (mSelectedTags.get(i).equals(strText)) {
						mSelectedTags.remove(i);
						i--;
					}
				}
				mSelectedTags.add(strText);
			}
		}
	}

	class TagItemViewHolder {
		Button btnButton;
		ImageView imageView;
	}

}
