package com.vocinno.centanet.apputils.utils.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vocinno.centanet.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FileExplore extends ListActivity {
	private List<Map<String, Object>> mData;
	private String mDir = "/sdcard";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		Bundle bl = intent.getExtras();
		String title = bl.getString("explorer_title");
		Uri uri = intent.getData();
		mDir = uri.getPath();

		setTitle(title);
		mData = getData();
		MyAdapter adapter = new MyAdapter(this);
		setListAdapter(adapter);

		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay();
		LayoutParams p = getWindow().getAttributes();
		p.height = (d.getHeight());
		p.width = (d.getWidth());
		getWindow().setAttributes(p);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		File f = new File(mDir);
		File[] files = f.listFiles();

		if (!mDir.equals("/sdcard")) {
			map = new HashMap<String, Object>();
			map.put("title", "../");
			map.put("info", f.getParent());
			map.put("img", R.drawable.ex_folder);
			list.add(map);
		}
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				map = new HashMap<String, Object>();
				map.put("title", files[i].getName());
				map.put("info", files[i].getPath());
				if (files[i].isDirectory())
					map.put("img", R.drawable.ex_folder);
				else if (files[i].getName().toLowerCase().endsWith("avi"))
					map.put("img", R.drawable.ex_avi);
				else if (files[i].getName().toLowerCase().endsWith("bmp"))
					map.put("img", R.drawable.ex_bmp);
				else if (files[i].getName().toLowerCase().endsWith("chm"))
					map.put("img", R.drawable.ex_chm);
				else if (files[i].getName().toLowerCase().endsWith("doc")
						|| files[i].getName().toLowerCase().endsWith("docx"))
					map.put("img", R.drawable.ex_doc);
				else if (files[i].getName().toLowerCase().endsWith("ico"))
					map.put("img", R.drawable.ex_ico);
				else if (files[i].getName().toLowerCase().endsWith("jpg"))
					map.put("img", R.drawable.ex_jpg);
				else if (files[i].getName().toLowerCase().endsWith("png"))
					map.put("img", R.drawable.ex_png);
				else if (files[i].getName().toLowerCase().endsWith("mov"))
					map.put("img", R.drawable.ex_mov);
				else if (files[i].getName().toLowerCase().endsWith("mp3"))
					map.put("img", R.drawable.ex_mp3);
				else if (files[i].getName().toLowerCase().endsWith("mp4"))
					map.put("img", R.drawable.ex_mp4);
				else if (files[i].getName().toLowerCase().endsWith("mpeg"))
					map.put("img", R.drawable.ex_mpeg);
				else if (files[i].getName().toLowerCase().endsWith("pdf"))
					map.put("img", R.drawable.ex_pdf);
				else if (files[i].getName().toLowerCase().endsWith("ppt")
						|| files[i].getName().toLowerCase().endsWith("pptx"))
					map.put("img", R.drawable.ex_ppt);
				else if (files[i].getName().toLowerCase().endsWith("rar"))
					map.put("img", R.drawable.ex_rar);
				else if (files[i].getName().toLowerCase().endsWith("txt"))
					map.put("img", R.drawable.ex_txt);
				else if (files[i].getName().toLowerCase().endsWith("wav"))
					map.put("img", R.drawable.ex_wav);
				else if (files[i].getName().toLowerCase().endsWith("wma"))
					map.put("img", R.drawable.ex_wma);
				else if (files[i].getName().toLowerCase().endsWith("wmv"))
					map.put("img", R.drawable.ex_wmv);
				else if (files[i].getName().toLowerCase().endsWith("xls")
						|| files[i].getName().toLowerCase().endsWith("xlsx"))
					map.put("img", R.drawable.ex_xls);
				else if (files[i].getName().toLowerCase().endsWith("xml"))
					map.put("img", R.drawable.ex_xml);
				else if (files[i].getName().toLowerCase().endsWith("zip"))
					map.put("img", R.drawable.ex_zip);
				else
					map.put("img", R.drawable.ex_file);
				list.add(map);
			}
		}
		return list;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("MyListView4-click", (String) mData.get(position).get("info"));
		if ((Integer) mData.get(position).get("img") == R.drawable.ex_folder) {
			mDir = (String) mData.get(position).get("info");
			mData = getData();
			MyAdapter adapter = new MyAdapter(this);
			setListAdapter(adapter);
		} else {
			finishWithResult((String) mData.get(position).get("info"));
		}
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView info;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.listview_file_explore,
						null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setBackgroundResource((Integer) mData.get(position).get(
					"img"));
			holder.title.setText((String) mData.get(position).get("title"));
			holder.info.setText((String) mData.get(position).get("info"));
			holder.info.setVisibility(View.GONE);
			return convertView;
		}
	}

	private void finishWithResult(String path) {
		Bundle conData = new Bundle();
		conData.putString("results", "Thanks Thanks");
		Intent intent = new Intent();
		intent.putExtras(conData);
		Uri startDir = Uri.fromFile(new File(path));
		intent.setDataAndType(startDir,
				"vnd.android.cursor.dir/lysesoft.andexplorer.file");
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		if (mDir.equals("/sdcard")
				&& mData.get(0).get("info")
						.equals("/sdcard/System Volume Information")) {
			// 退出
			super.onBackPressed();
		} else {
			// 返回上一级目录
			mDir = (String) mData.get(0).get("info");
			mData = getData();
			MyAdapter adapter = new MyAdapter(this);
			setListAdapter(adapter);
		}
	}

};