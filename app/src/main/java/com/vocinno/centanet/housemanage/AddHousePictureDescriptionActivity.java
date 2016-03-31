package com.vocinno.centanet.housemanage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.ImageUtil;
import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddHousePictureDescriptionActivity extends SuperActivity {
    private View mViewBack;
    private int mAddImageTotalNumber = 5;
    private int mImageCount = 1;
    private int mType = -1;
    private TextView mTvImageCount, tv_time;
    private TextView mTvNextStep;
    private ImageView mImgNextIcon, mImgHouseImage;
    private EditText mEtImgDescription;
    private RelativeLayout mReltNextBtn;
    private RelativeLayout mReltLastBtn;
    private Bitmap mNewBitmap;
    private LinearLayout ll_delete_img;
    private String path;
    private String describe;
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

    public void setImg(String path,ImageView imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//	此时返回bm为空
        options.inJustDecodeBounds = false;         //计算缩放比
        int be = (int) (options.outHeight / (float)600);
        if (be <= 0) be = 1;
        options.inSampleSize = be;        //
//	重新读入图片，注意这次要把options.inJustDecodeBounds 设为false
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + "   " + h);
        ImageView iv = new ImageView(this);
        imageView.setImageBitmap(bitmap);
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
        ll_delete_img = (LinearLayout) findViewById(R.id.ll_delete_img);
        ll_delete_img.setOnClickListener(this);
        mTvImageCount = (TextView) findViewById(R.id.tv_countImageNumber_AddHouseImageDetailActivity);
        mTvNextStep = (TextView) findViewById(R.id.tv_rightButton_AddHouseImageDetailActivity);
        mImgNextIcon = (ImageView) findViewById(R.id.img_rightButton_AddHouseImageDetailActivity);
        mReltNextBtn = (RelativeLayout) findViewById(R.id.rlyt_rightButton_AddHouseImageDetailActivity);
        mReltLastBtn = (RelativeLayout) findViewById(R.id.rlyt_leftButton_AddHouseImageDetailActivity);
        mImgHouseImage = (ImageView) findViewById(R.id.img_houseDetailPic_AddHouseImageDetailActivity);
        mEtImgDescription = (EditText) findViewById(R.id.et_houseImageDetail_AddHouseImageDetailActivity);
        tv_time = (TextView) findViewById(R.id.tv_timeImageNumber_AddHouseImageDetailActivity);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = new Date();
        tv_time.setText("拍摄于:" + sdf.format(date));
//        mImgHouseImage.setImageBitmap(bitmap2);
        setImg(path,mImgHouseImage);

        mSubmit = (ImageView) MethodsExtra.findHeadRightView1(mContext, mRootView, 0, R.drawable.universal_button_done);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void setListener() {
        mViewBack.setOnClickListener(this);
        mReltNextBtn.setOnClickListener(this);
        mReltLastBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_delete_img:
                //MethodsDeliverData.mListImagePath.add(path);
                onBack();
                break;
            case R.id.img_right_mhead1:
            /*MethodsDeliverData.mListImagePath.add(path);
			MethodsDeliverData.mListImages.add(path);*/
                showDialog();
                String describe = mEtImgDescription.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("path", path);
                intent.putExtra("describe", describe);
                this.setResult(102, intent);
                this.finish();
                dismissDialog();
                break;
            case R.id.img_left_mhead1:
                onBack();
                break;
            case R.id.rlyt_rightButton_AddHouseImageDetailActivity:
                nextStep();
                break;
            case R.id.rlyt_leftButton_AddHouseImageDetailActivity:
                lastStep();
                break;
            default:
                break;
        }
    }

    private void refreshImageView() {
        if (MethodsDeliverData.mListImagePath == null
                || MethodsDeliverData.mListImagePath.size() < mImageCount) {
            return;
        }
        Bitmap bitmapTest = MethodsFile.decodeFile(
                MethodsDeliverData.mListImagePath.get(mImageCount - 1), false,
                false);
        if (bitmapTest == null) {
            return;
        }
        if (mNewBitmap != null) {
            mImgHouseImage.setImageBitmap(null);
            mNewBitmap.recycle();
        }
        double height = bitmapTest.getHeight();
        double width = bitmapTest.getWidth();
        float standard = (float) (562.0 / 750.0);
        float real = (float) (height / width);

        if (real < standard) {
            mNewBitmap = MethodsFile.cropImage(bitmapTest,
                    (int) ((width - height * (750.0 / 562.0)) / 2.0), 0,
                    (int) (height * (750.0 / 562.0)), (int) height);
        } else if (real == standard) {
            mNewBitmap = bitmapTest;
        } else if (real > standard) {
            mNewBitmap = MethodsFile.cropImage(bitmapTest, 0,
                    (int) ((height - width * (562.0 / 750.0)) / 2.0),
                    (int) width, (int) (width * (562.0 / 750.0)));
        }
        // String strPath = MethodsFile.saveBitmapToFile(bitmapCit);
        if (MethodsDeliverData.mListImages.size() < mImageCount) {
            MethodsDeliverData.mListImages
                    .add(MethodsDeliverData.mListImagePath.get(mImageCount - 1));
        }
        mImgHouseImage.setImageBitmap(mNewBitmap);
        bitmapTest.recycle();
    }

    private void nextStep() {
        mImageCount = mImageCount + 1;
        // 将对图片的描述添加进列表
        if (MethodsDeliverData.mListImageDescription.size() < mImageCount) {
            MethodsDeliverData.mListImageDescription.add(mEtImgDescription
                    .getText().toString());
            mEtImgDescription.setText("");
        } else {
            MethodsDeliverData.mListImageDescription.set(mImageCount - 2,
                    mEtImgDescription.getText().toString());
            mEtImgDescription.setText(MethodsDeliverData.mListImageDescription
                    .get(mImageCount - 1));
        }
        if (mImageCount < mAddImageTotalNumber) {
            refreshImageView();
            mTvImageCount.setText(mImageCount + "/" + mAddImageTotalNumber);
            mTvNextStep.setText("下一步");
            mImgNextIcon.setBackgroundResource(R.drawable.work_icon_next);

            // 将对图片的描述添加进列表
            // if (MethodsDeliverData.mListImageDescription.size() <=
            // mImageCount){
            // MethodsDeliverData.mListImageDescription.add(mEtImgDescription.getText().toString());
            // }
            // else{
            // mEtImgDescription.setText(MethodsDeliverData.mListImageDescription.get(mImageCount));
            // MethodsDeliverData.mListImageDescription.set(mImageCount - 1,
            // mEtImgDescription.getText().toString());
            // }
        } else if (mImageCount == mAddImageTotalNumber) {
            refreshImageView();
            mTvImageCount.setText(mImageCount + "/" + mAddImageTotalNumber);
            mTvNextStep.setText(R.string.over);
            mImgNextIcon.setBackgroundResource(R.drawable.work_icon_done);
            // 将对图片的描述添加进列表
            // if (MethodsDeliverData.mListImageDescription.size() <=
            // mImageCount){
            // MethodsDeliverData.mListImageDescription.add(mEtImgDescription.getText().toString());
            // }
            // else{
            // mEtImgDescription.setText(MethodsDeliverData.mListImageDescription.get(mImageCount));
            // MethodsDeliverData.mListImageDescription.set(mImageCount - 1,
            // mEtImgDescription.getText().toString());
            // }

            mEtImgDescription.setText("");
        } else if (mImageCount > mAddImageTotalNumber) {
            MethodsDeliverData.mListImagePath = new ArrayList<String>();
            finish();
        }
    }

    private void lastStep() {
        mImageCount = mImageCount - 1;

        if (mImageCount > 0) {
            refreshImageView();
            mTvImageCount.setText(mImageCount + "/" + mAddImageTotalNumber);
            mTvNextStep.setText(R.string.next);
            mImgNextIcon.setBackgroundResource(R.drawable.work_icon_next);
            // 将对图片的描述添加进列表
            if (MethodsDeliverData.mListImageDescription.size() <= mImageCount) {
                //
                MethodsDeliverData.mListImageDescription.add(mEtImgDescription
                        .getText().toString());
            } else {
                MethodsDeliverData.mListImageDescription.set(mImageCount,
                        mEtImgDescription.getText().toString());
            }
            mEtImgDescription.setText(MethodsDeliverData.mListImageDescription
                    .get(mImageCount - 1));
        } else {
            mImageCount = 1 + mImageCount;
        }
    }

    @Override
    public void initData() {
		/*mImageCount = 1;
		mAddImageTotalNumber = MethodsDeliverData.mListImagePath.size();
		// 初始化页面
		mTvImageCount.setText(mImageCount + "/" + mAddImageTotalNumber);
		refreshImageView();*/
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
