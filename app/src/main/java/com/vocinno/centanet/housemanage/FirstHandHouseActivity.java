package com.vocinno.centanet.housemanage;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.util.mylibrary.photos.PhotoReadyHandler;
import com.util.mylibrary.photos.SelectPhotoManager;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.housemanage.adapter.CustomGridView;
import com.vocinno.centanet.housemanage.adapter.FirstHandPicAdapter;
import com.vocinno.centanet.housemanage.adapter.MyInterface;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.photo.PhotoWallActivity;
import com.vocinno.utils.MethodsExtra;

/**
 * Created by hewei26 on 2016/6/17.
 */
public class FirstHandHouseActivity extends OtherBaseActivity implements MyInterface {

    private ImageView mBack;
    private TextView mSubmit;
    private FirstHandPicAdapter picAdapter;
    private CustomGridView gv_first_house;
    @Override
    public int setContentLayoutId() {
        return R.layout.activity_firsthand_house;
    }

    @Override
    public void initView() {
        //禁用侧滑
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mBack = (ImageView) MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mSubmit = (TextView) MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.add_first, null);
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        gv_first_house= (CustomGridView) findViewById(R.id.gv_first_house);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_left_mhead1:
                finish();
            break;
            case R.id.tv_right_mhead1:
                MyToast.showToast("保存");
            break;
        }
    }

    @Override
    public void initData() {
        picAdapter=new FirstHandPicAdapter(this,(MyInterface)this);
        gv_first_house.setAdapter(picAdapter);
    }

    @Override
    public void takePhoto(String type) {
        SelectPhotoManager.getInstance().setPhotoReadyHandler(new PhotoReadyHandler() {
            @Override
            public void onPhotoReady(int from, String imgPath) {
                intent.setClass(FirstHandHouseActivity.this, AddHousePictureDescriptionActivity.class);
                intent.putExtra(MyConstant.path, imgPath);
                intent.putExtra(MyConstant.title,"图片预览");
                startActivityForResult(intent, 101);
            }
        });
        SelectPhotoManager.setImgSavePath(Environment.getExternalStorageDirectory().getPath() + "/vocinno");
        SelectPhotoManager.getInstance().start(this, 0);
    }

    @Override
    public void selectPhoto(String type) {
        intent.setClass(this, PhotoWallActivity.class);
        startActivityForResult(intent, 201);//选择图片
    }

    @Override
    public void editPhoto(String type, String imgPath, String describe) {
        intent.setClass(this, EditPicDetailActivity.class);
        intent.putExtra(MyConstant.path, imgPath);
        startActivityForResult(intent, 301);//编辑
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            switch (requestCode){
                case 101:
                    String path = data.getStringExtra(MyConstant.path);
                    break;
                case 201:
                    break;
                case 301:
                    break;
            }
        }
        if(requestCode==1&&resultCode==-1){
            SelectPhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {

    }
    @Override
    public void onRefresh() {

    }
    @Override
    public void onLoadMore() {

    }
}
