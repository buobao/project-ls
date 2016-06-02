package com.vocinno.centanet.housemanage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.centanet.apputils.selfdefineview.MyTextView;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;

public class AddHousePictureDescriptionActivity extends SuperActivity {
    private View mViewBack;
    private int mAddImageTotalNumber = 5;
    private int mImageCount = 1;
    private int mType = -1;
    private TextView  tv_time;
    private ImageView  mImgHouseImage;
    private Bitmap mNewBitmap;
    private MyTextView tv_delete_img,tv_save_img;
    private String path,newPath;
    private ImageView mSubmit;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setImageNumber() {
    }

    @Override
    public Handler setHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_add_house_image_detail;
    }
    public int calculateInSampleSize(BitmapFactory.Options options) {
//        int reqWidth=mImgHouseImage.getWidth();
//        int reqHeight=mImgHouseImage.getHeight();

        Display display = getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
        Point size = new Point();
        display.getSize(size);
        int reqWidth = size.x;
        int reqHeight = size.y;

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    public void setImg(String path,ImageView imageView) {
        newPath=MethodsFile.getSmallBitmap(path);
        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//	此时返回bm为空
        options.inJustDecodeBounds = false;         //计算缩放比
        int be = (int) (options.outHeight / (float)600);
        if (be <= 0) be = 1;
        options.inSampleSize = calculateInSampleSize(options);        //
//	重新读入图片，注意这次要把options.inJustDecodeBounds 设为false
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + "   " + h);
        ImageView iv = new ImageView(this);
        imageView.setImageBitmap(bitmap);*/
        Glide.with(this).load(path).centerCrop().crossFade().into(imageView);
    }

    @Override
    public void initView() {
        intent = getIntent();
        path = intent.getStringExtra("path");
//		Bitmap bitmap = ImageUtil.File2Bitmap(path);
//        Bitmap bitmap2 = BitmapFactory.decodeFile(path, getBitmapOption(2));
        MethodsExtra.findHeadTitle1(mContext, mRootView,
                R.string.house_image_detail, null);
        mViewBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
        tv_delete_img = (MyTextView) findViewById(R.id.tv_delete_img);
        tv_delete_img.setOnClickListener(this);
        tv_save_img = (MyTextView) findViewById(R.id.tv_save_img);
        tv_save_img.setOnClickListener(this);
        mImgHouseImage = (ImageView) findViewById(R.id.img_houseDetailPic_AddHouseImageDetailActivity);
//        mImgHouseImage.setImageBitmap(bitmap2);
        setImg(path,mImgHouseImage);

        mSubmit = (ImageView) MethodsExtra.findHeadRightView1(mContext, mRootView, 0, R.drawable.universal_button_done);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void setListener() {
        mViewBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete_img:
                //MethodsDeliverData.mListImagePath.add(path);
                onBack();
            case R.id.tv_save_img:
                showDialog();
                intent.putExtra("path", newPath);
                this.setResult(102, intent);
                this.finish();
                dismissDialog();
                break;
            case R.id.img_right_mhead1:
            /*MethodsDeliverData.mListImagePath.add(path);
			MethodsDeliverData.mListImages.add(path);*/
                showDialog();
                Intent intent = new Intent();
                intent.putExtra("path", newPath);
                this.setResult(102, intent);
                this.finish();
                dismissDialog();
                break;
            case R.id.img_left_mhead1:
                onBack();
                break;
            default:
                break;
        }
    }


    @Override
    public void initData() {
    }

    @Override
    protected void onDestroy() {
        if (mNewBitmap != null) {
            mImgHouseImage.setImageBitmap(null);
            mNewBitmap.recycle();
        }
        super.onDestroy();
    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public void notifCallBack(String name, String className, Object data) {

    }

}
