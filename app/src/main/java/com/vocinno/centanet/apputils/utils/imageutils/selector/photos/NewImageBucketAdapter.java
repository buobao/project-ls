package com.vocinno.centanet.apputils.utils.imageutils.selector.photos;

import java.util.List;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.utils.MethodsFile;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewImageBucketAdapter extends BaseAdapter {

	final String TAG = getClass().getSimpleName();

	Activity act;
	/**
	 * 图片集列表
	 */
	List<NewImageBucket> dataList;

	public NewImageBucketAdapter(Activity act, List<NewImageBucket> list) {
		this.act = act;
		dataList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView name;
		private TextView count;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder;
		if (arg1 == null) {
			holder = new Holder();
			arg1 = View.inflate(act, R.layout.image_selector_item_image_bucket,
					null);
			holder.iv = (ImageView) arg1.findViewById(R.id.image);
			holder.selected = (ImageView) arg1.findViewById(R.id.isselected);
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.count = (TextView) arg1.findViewById(R.id.count);
			arg1.setTag(holder);
		} else {
			holder = (Holder) arg1.getTag();
		}
		NewImageBucket item = dataList.get(arg0);
		holder.count.setText("" + item.count);
		holder.name.setText(item.bucketName);
		Log.v("==tem.bucketName==", item.bucketName + "");
		Log.v("==item.count==", item.count + "");
		holder.selected.setVisibility(View.GONE);
		if (item.imageList != null && item.imageList.size() > 0) {
			String thumbPath = item.imageList.get(0).thumbnailPath;
			String sourcePath = item.imageList.get(0).imagePath;

			if (thumbPath != null) {
				holder.iv.setTag(thumbPath);
				// String imagePath = thumbPath;
				String imagePath = thumbPath;
				String imageUrl = Scheme.FILE.wrap(imagePath);
				MethodsFile.downloadImgByUrl(imageUrl, holder.iv);
			} else {
				holder.iv.setTag(sourcePath);
				String imagePath = sourcePath;
				String imageUrl = Scheme.FILE.wrap(imagePath);
				MethodsFile.downloadImgByUrl(imageUrl, holder.iv);
			}

		} else {
			holder.iv.setImageBitmap(null);
			Log.e(TAG, "no images in bucket " + item.bucketName);
		}
		return arg1;
	}
}
