package com.vocinno.utils.imageutils.selector.photos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.vocinno.centanet.R;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsFile;

public class NewImageGridAdapter extends BaseAdapter {

	private TextCallback textcallback = null;
	final String TAG = getClass().getSimpleName();
	Activity act;
	List<NewImageItem> dataList;
	Map<String, String> map = new HashMap<String, String>();
	private Handler mHandler;
	private int selectTotal = 0;

	public static interface TextCallback {
		public void onListen(int count);
	}

	public void setTextCallback(TextCallback listener) {
		textcallback = listener;
	}

	public NewImageGridAdapter(Activity act, List<NewImageItem> list,
			Handler mHandler) {
		this.act = act;
		dataList = list;
		this.mHandler = mHandler;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(act,
					R.layout.image_selector_item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image_launch);
			holder.selected = (ImageView) convertView
					.findViewById(R.id.isselected);
			holder.text = (TextView) convertView
					.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final NewImageItem item = dataList.get(position);
		holder.iv.setVisibility(View.VISIBLE);
		holder.selected.setVisibility(View.VISIBLE);
		holder.text.setVisibility(View.VISIBLE);
		if (item.thumbnailPath != null) {
			holder.iv.setTag(item.thumbnailPath);
			String imagePath = item.thumbnailPath;
			String imageUrl = Scheme.FILE.wrap(imagePath);
			MethodsFile.downloadImgByUrl(imageUrl, holder.iv);

		} else {
			holder.iv.setTag(item.imagePath);
			String imagePath = item.imagePath;
			String imageUrl = Scheme.FILE.wrap(imagePath);
			MethodsFile.downloadImgByUrl(imageUrl, holder.iv);
		}

		if (item.isSelected) {
			holder.selected.setImageResource(R.drawable.new_icon_data_select);
			holder.text.setBackgroundResource(R.drawable.new_bgd_relatly_line);
		} else {
			holder.selected.setImageResource(-1);
			holder.text.setBackgroundColor(0x00000000);
		}
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String path;
				if (item.thumbnailPath != null) {
					path = item.thumbnailPath;
				} else {
					path = item.imagePath;
				}

				if ((MethodsDeliverData.hasImageNum + selectTotal) < 9) {
					item.isSelected = !item.isSelected;
					if (item.isSelected) {
						holder.selected
								.setImageResource(R.drawable.new_icon_data_select);
						holder.text
								.setBackgroundResource(R.drawable.new_bgd_relatly_line);
						selectTotal++;
						if (textcallback != null)
							textcallback.onListen(selectTotal);
						map.put(path, path);

					} else if (!item.isSelected) {
						holder.selected.setImageResource(-1);
						holder.text.setBackgroundColor(0x00000000);
						selectTotal--;
						if (textcallback != null)
							textcallback.onListen(selectTotal);
						map.remove(path);
					}
				} else if ((MethodsDeliverData.hasImageNum + selectTotal) >= 9) {
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holder.selected.setImageResource(-1);
						selectTotal--;
						map.remove(path);

					} else {
						Message message = Message.obtain(mHandler, 0);
						message.sendToTarget();
					}
				}

			}

		});
		return convertView;
	}
}
