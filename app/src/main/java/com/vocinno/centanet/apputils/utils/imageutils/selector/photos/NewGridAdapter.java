package com.vocinno.centanet.apputils.utils.imageutils.selector.photos;

import java.io.IOException;

import com.vocinno.centanet.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

@SuppressLint("HandlerLeak")
public class NewGridAdapter extends BaseAdapter {
	private LayoutInflater inflater; // 瑙嗗浘瀹瑰櫒
	private int selectedPosition = -1;// 閫変腑鐨勪綅缃�
	private boolean shape;
	private Context context;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public NewGridAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void update() {
		loading();
	}

	@Override
	public int getCount() {
		return (NewBimp.bmp.size() + 1);
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * ListView Item璁剧疆
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int coord = position;
		ViewHolder holder = null;
		if (convertView == null) {

			convertView = inflater
					.inflate(R.layout.image_selector_item_published_grida,
							parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == NewBimp.bmp.size()) {
			holder.image.setImageDrawable(context.getResources().getDrawable(
					R.drawable.new_icon_addpic_unfocused));
			if (position == 9) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setImageBitmap(NewBimp.bmp.get(position));
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				NewGridAdapter.this.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (NewBimp.max == NewBimp.drr.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						try {
							String path = NewBimp.drr.get(NewBimp.max);
							System.out.println(path);

							Bitmap bm = NewBimp.revitionImageSize(path);

							NewBimp.bmp.add(bm);
							String newStr = path.substring(
									path.lastIndexOf("/") + 1,
									path.lastIndexOf("."));

							NewFileUtils.saveBitmap(bm, "" + newStr);
							NewBimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
}
