package com.vocinno.utils.imageutils.selector;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.utils.imageutils.selector.photos.NewAlbumHelper;
import com.vocinno.utils.imageutils.selector.photos.NewBimp;
import com.vocinno.utils.imageutils.selector.photos.NewImageBucket;
import com.vocinno.utils.imageutils.selector.photos.NewImageBucketAdapter;
import com.vocinno.utils.imageutils.selector.photos.NewImageGridActivity;
import com.vocinno.utils.imageutils.selector.tools.FileComparator;
import com.vocinno.utils.imageutils.selector.tools.SortList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class SelectorImageActivity extends Activity {
	List<NewImageBucket> dataList;//用来装载数据源的列表
	GridView gridView;
	NewImageBucketAdapter adapter;// 自定义的适配器
	NewAlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	public ModelDialog modelDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_selector_activity_image_bucket);
		helper = NewAlbumHelper.getHelper();
		helper.init(this);
		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(false);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.new_icon_addpic_unfocused);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		showDialog();
		gridView = (GridView) findViewById(R.id.gridview);
		// 对详细相册进行排序
		for (int i = 0; i < dataList.size(); i++) {
			Collections.sort(dataList.get(i).imageList, new FileComparator());
		}
		for (int i = 0; i < dataList.size(); i++) {
			dataList.get(i).setmName(dataList.get(i).bucketName.toString());
		}
		SortList<NewImageBucket> sortList = new SortList<NewImageBucket>();
		sortList.Sort(dataList, "getmName", null);
		adapter = new NewImageBucketAdapter(SelectorImageActivity.this,
				dataList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (isFastDoubleClick()) {
					Intent intent = new Intent(SelectorImageActivity.this,
							NewImageGridActivity.class);
					intent.putExtra(SelectorImageActivity.EXTRA_IMAGE_LIST,
							(Serializable) dataList.get(position).imageList);
					intent.putExtra("mPhotName",
							dataList.get(position).bucketName.toString());
					startActivityForResult(intent, 0);

				}
			}
		});
		dismissDialog();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		setResult(RESULT_OK, data);
		finish();
	};

	private long lastClickTime;
	public boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 2500) {
			return false;
		}
		lastClickTime = time;
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			NewBimp.drr.clear();
			SelectorImageActivity.this.finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void showDialog(){
		if(this.modelDialog==null){
			this.modelDialog= ModelDialog.getModelDialog(this);
		}
		this.modelDialog.show();
	}
	public void dismissDialog(){
		if(this.modelDialog!=null&&this.modelDialog.isShowing()){
			this.modelDialog.dismiss();
		}
	}
}
