package com.vocinno.centanet.tools.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.housemanage.AddHousePictureActivity;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.photo.adapter.PhotoWallAdapter;
import com.vocinno.centanet.apputils.utils.MethodsExtra;
import com.vocinno.centanet.apputils.utils.MethodsFile;

import java.io.File;
import java.util.ArrayList;


/**
 * 选择照片页面
 * Created by hanj on 14-10-15.
 */
public class PhotoWallActivity extends Activity implements View.OnClickListener{
    private ArrayList<String> list;
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;
    private View mBackView, mMoreView, mTitleView;
    public static PhotoWallActivity pwa;
    /**
     * 当前文件夹路径
     */
    private String currentFolder = null;
    /**
     * 当前展示的是否为最近照片
     */
    private boolean isLatest = true;
    private TextView tv_title_mhead1;
    private View baseView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pwa=this;
        baseView= LayoutInflater.from(this).inflate(R.layout.photo_wall,null);
        setContentView(baseView);

        mBackView = MethodsExtra.findHeadLeftView1(this, baseView, 0, 0);
        mBackView.setOnClickListener(this);

        mMoreView = MethodsExtra.findHeadRightView1(this, baseView, R.string.over, 0);
        mMoreView.setOnClickListener(this);

        tv_title_mhead1 = (TextView) findViewById(R.id.tv_title_mhead1);
        tv_title_mhead1.setText(getText(R.string.latest_image));

        mPhotoWall = (GridView) findViewById(R.id.photo_wall_grid);
        list = getLatestImagePaths(100);
        adapter = new PhotoWallAdapter(this, list);
        mPhotoWall.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_left_mhead1:
                backAction();
                break;
            case R.id.tv_right_mhead1:
                //选择图片完成,回到起始页面
                ArrayList<String> paths = getSelectImagePaths();
                ArrayList<String> newPath = new ArrayList<String>();
                if(paths!=null&&paths.size()>0){
                    Loading.show(this);
                    for (int i = 0; i < paths.size(); i++) {
                        newPath.add(MethodsFile.getSmallBitmap(paths.get(i)));
                    }

                    if(PhotoAlbumActivity.paa!=null){
                        PhotoAlbumActivity.paa.finish();
                    }
                    Intent intent = new Intent(PhotoWallActivity.this, AddHousePictureActivity.class);
                    intent.putExtra("code", paths != null ? 100 : 101);
                    intent.putStringArrayListExtra(MyConstant.pathList, newPath);
//                    intent.putStringArrayListExtra(MyConstant.pathList,paths);
                    setResult(RESULT_OK,intent);
                    Loading.dismissLoading();
                    finish();
                }else{
                    if(PhotoAlbumActivity.paa!=null){
                        PhotoAlbumActivity.paa.finish();
                    }
                    finish();
                }
            break;
        }
    }
    /**
     * 第一次跳转至相册页面时，传递最新照片信息
     */
    private boolean firstIn = true;

    /**
     * 点击返回时，跳转至相册页面
     */
    private void backAction() {
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //传递“最近照片”分类信息
        if (firstIn) {
            if (list != null && list.size() > 0) {
                intent.putExtra("latest_count", list.size());
                intent.putExtra("latest_first_img", list.get(0));
            }
            firstIn = false;
        }
        startActivity(intent);
    }

    //重写返回键
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backAction();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 根据图片所属文件夹路径，刷新页面
     */
    private void updateView(int code, String folderPath) {
        list.clear();
        adapter.clearSelectionMap();
        adapter.notifyDataSetChanged();

        if (code == 100) {   //某个相册
            int lastSeparator = folderPath.lastIndexOf(File.separator);
            String folderName = folderPath.substring(lastSeparator + 1);
            tv_title_mhead1.setText(getText(R.string.select_image));
            list.addAll(getAllImagePathsByFolder(folderPath));
        } else if (code == 200) {  //最近照片
            tv_title_mhead1.setText("最近照片");
            list.addAll(getLatestImagePaths(100));
        }

        adapter.notifyDataSetChanged();
        if (list.size() > 0) {
            //滚动至顶部
            mPhotoWall.smoothScrollToPosition(0);
        }
    }


    /**
     * 获取指定路径下的所有图片文件。
     */
    private ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }

        ArrayList<String> imageFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (Utility.isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }

        return imageFilePaths;
    }

    /**
     * 使用ContentProvider读取SD卡最近图片。
     */
    private ArrayList<String> getLatestImagePaths(int maxCount) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<String> latestImagePaths = null;
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {
                latestImagePaths = new ArrayList<String>();

                while (true) {
                    // 获取图片的路径
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);

                    if (latestImagePaths.size() >= maxCount || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }

    //获取已选择的图片路径
    private ArrayList<String> getSelectImagePaths() {
        SparseBooleanArray map = adapter.getSelectionMap();
        if (map.size() == 0) {
            return null;
        }

        ArrayList<String> selectedImageList = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            if (map.get(i)) {
                selectedImageList.add(list.get(i));
            }
        }

        return selectedImageList;
    }

    //从相册页面跳转至此页
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int code = intent.getIntExtra("code", -1);
        boolean first = intent.getBooleanExtra("first", false);
        if(first){
            adapter.clearList();
        }
        if (code == 100) {
            //某个相册
            String folderPath = intent.getStringExtra("folderPath");
            if (isLatest || (folderPath != null && !folderPath.equals(currentFolder))) {
                currentFolder = folderPath;
                updateView(100, currentFolder);
                isLatest = false;
            }
        } else if (code == 200) {
            //“最近照片”
            if (!isLatest) {
                updateView(200, null);
                isLatest = true;
            }
        }
    }

}
