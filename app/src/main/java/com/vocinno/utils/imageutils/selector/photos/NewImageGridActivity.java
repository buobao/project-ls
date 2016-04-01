package com.vocinno.utils.imageutils.selector.photos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.housemanage.AddHousePictureDescriptionActivity;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.imageutils.selector.photos.NewImageGridAdapter.TextCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public ModelDialog modelDialog;
//	private ProgressDialog mDialog;
	private ImageView launch_photostore_content_break;
	private TextView launch_photostore_content_name;

	List<NewImageItem> dataList;
	GridView gridView;
	NewImageGridAdapter adapter;
	NewAlbumHelper helper;
	TextView bt;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(NewImageGridActivity.this, "最多选择9张图片", 400)
						.show();
				break;
			case 1:
				Toast.makeText(NewImageGridActivity.this, "最多选择1张图片", 400)
						.show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.image_selector_activity_image_grid);
		MethodsDeliverData.mListImagePath = null;
		launch_photostore_content_name = (TextView) findViewById(R.id.launch_photostore_content_name);
		launch_photostore_content_break = (ImageView) findViewById(R.id.launch_photostore_content_break);
		launch_photostore_content_break
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						NewImageGridActivity.this.finish();
					}
				});

//		mDialog = new ProgressDialog(NewImageGridActivity.this);
//		mDialog.setMessage("选择图片中，请稍后。。");
		helper = NewAlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<NewImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);
		// mPhotName = getIntent().getShortExtra("mPhotName", "相册");
		launch_photostore_content_name.setText(getIntent().getStringExtra(
				"mPhotName"));
		initView();
		bt = (TextView) findViewById(R.id.bt_nextImage_NewImageGridActivity);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				mDialog.show();
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}
				Intent intent=new Intent();
				intent.putStringArrayListExtra("pathList", list);
				setResult(RESULT_OK,intent);
				finish();
				/*if (NewBimp.act_bool) {
					// Intent intent = new Intent(NewImageGridActivity.this,
					// NewPublishedActivity.class);
					// startActivity(intent);
					MethodsDeliverData.mListImagePath = list;
					NewBimp.drr.clear();
					adapter.map.values().clear();
					MethodsExtra.startActivity(NewImageGridActivity.this,
							AddHousePictureDescriptionActivity.class, 0);
					setResult(RESULT_OK);
					finish();
				}*/
			}

		});
	}

	/**
	 * 
	 */
	private void initView() {
		showDialog();
		gridView = (GridView) findViewById(R.id.gridview_content);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		Log.v("==dataList.size()=", dataList.size() + "");
		adapter = new NewImageGridAdapter(NewImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			@Override
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				adapter.notifyDataSetChanged();
			}

		});
		dismissDialog();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		mDialog.dismiss();
	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// NewBimp.drr.clear();
			NewImageGridActivity.this.finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}*/
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
