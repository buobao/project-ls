package com.vocinno.centanet.housemanage;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.util.mylibrary.photos.PhotoReadyHandler;
import com.util.mylibrary.photos.SelectPhotoManager;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.ChoosePeopleActivity;
import com.vocinno.centanet.entity.TCmLookHouse;
import com.vocinno.centanet.housemanage.adapter.CustomGridView;
import com.vocinno.centanet.housemanage.adapter.FirstHandPicAdapter;
import com.vocinno.centanet.housemanage.adapter.MyInterface;
import com.vocinno.centanet.model.ChoosePeople;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.UploadImageResult;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.photo.PhotoWallActivity;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hewei26 on 2016/6/17.
 */
public class FirstHandHouseActivity extends OtherBaseActivity implements MyInterface {

    private ImageView mBack;
    private TextView mSubmit;
    private FirstHandPicAdapter picAdapter;
    private CustomGridView gv_first_house;
    private StringBuffer imgId;
    private CheckBox cb_first_peikan;
    private TextView tv_first_choosepeople;
    private EditText et_first_address;
    private Button bt_first_choose;
    private FinalHttp fh;
    private TCmLookHouse lookHouse;

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

        bt_first_choose= (Button) findViewById(R.id.bt_first_choose);
        bt_first_choose.setOnClickListener(this);

        et_first_address= (EditText) findViewById(R.id.et_first_address);
        tv_first_choosepeople= (TextView) findViewById(R.id.tv_first_choosepeople);
        cb_first_peikan= (CheckBox) findViewById(R.id.cb_first_peikan);
        cb_first_peikan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_first_choose:
                intent.setClass(this, ChoosePeopleActivity.class);
                startActivityForResult(intent,MyConstant.REQUEST_CHOOSE_PEOPLE);
            break;
            case R.id.img_left_mhead1:
                finish();
            break;
            case R.id.tv_right_mhead1:
                validationData();
                imgId=new StringBuffer();
                uploadImg();
            break;
        }
    }

    private void uploadImg() {
        Loading.show(this);
        List<String>list=new ArrayList<String>();
        list=picAdapter.getList();
        if(null!=list&&list.size()>0){
            for (int i = 0; i <list.size() ; i++) {
                upload(list.get(i));
            }
        }
    }
    private void upload(String path){
        AjaxParams params = new AjaxParams();
        fh=new FinalHttp();
        try {
            params.put("file1", new File(path));// 上传文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Loading.dismissLoading();
            MyToast.showToast("图片没找到,请确认之后再试");
            return;
        }
        fh.post(getText(R.string.serverurl).toString(), params, new AjaxCallBack<String>() {
            @Override
            public void onLoading(long count, long current) {
            }
            @Override
            public void onSuccess(String result) {
                try {
                    JSReturn jsReturn = MethodsJson.jsonToJsReturn(result, UploadImageResult.class);
                    List<UploadImageResult> imageResult = jsReturn.getListDatas();
                    String id=imageResult.get(0).getFileId();
                    imgId.append(id + ",");
                    if(imgId.toString().split(",").length==picAdapter.getList().size()){
                        imgId=imgId.deleteCharAt(imgId.length()-1);
                        lookHouse.setImgList(picAdapter.getList());
                        lookHouse.setFilesId(imgId.toString());
                        lookHouse.setAccompanyName(tv_first_choosepeople.getText().toString().trim());
                        lookHouse.setHouAddr(et_first_address.getText().toString().trim());
                        if(cb_first_peikan.isChecked()){
                            lookHouse.setAccompanyPromise("1");
                        }else{
                            lookHouse.setAccompanyPromise("0");
                        }
                        intent.putExtra(MyConstant.addFirstHouse,lookHouse);
//                        Log.i("lookHouse", lookHouse.getImgList().size() + "==" + lookHouse.getFilesId() + "=="
//                                + lookHouse.getAccompanyName() + "==" + lookHouse.getAccompanyPromise() + "==" + lookHouse.getHouAddr());
                        setResult(MyConstant.RESULT_ADDFIRST, intent);
                        finish();
                    }
                    fh=null;
                }catch (Exception e){
                    fh=null;
                    MyToast.showToast("上传失败,请重新再试");
                    Loading.dismissLoading();
                    return;
                }
            }
        });
    }
    private void validationData() {
        if(et_first_address.getText()==null||et_first_address.getText().toString().trim().length()==0){
            MyToast.showToast("房屋地址不能为空");
            return;
        }else if(tv_first_choosepeople.getText()==null||tv_first_choosepeople.getText().toString().trim().length()==0){
            MyToast.showToast("陪看人不能为空");
            return;
        }else if(null==picAdapter.getList()||picAdapter.getList().size()==0){
            MyToast.showToast("附件不能为空");
            return;
        }
    }

    @Override
    public void initData() {
        picAdapter=new FirstHandPicAdapter(this,(MyInterface)this);
        gv_first_house.setAdapter(picAdapter);
        lookHouse = new TCmLookHouse();
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
                    picAdapter.addPath(path);
                    break;
                case 201://选择图片
                    List<String> list=data.getStringArrayListExtra(MyConstant.pathList);
                    picAdapter.addData(list);
                    break;
                case 301://编辑
                    String deletepath = data.getStringExtra(MyConstant.path);
                    picAdapter.removePath(deletepath);
                    break;
            }
        }
        if(requestCode==1&&resultCode==-1){
            SelectPhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
        }
        if(resultCode==MyConstant.CHOOSE_PEOPLE){
            if(data!=null){
                ChoosePeople people =(ChoosePeople)data.getSerializableExtra(MyConstant.choose_people);
                tv_first_choosepeople.setText(people.getRealName());
                if(people != null){
                    //显示经理陪看 : 经理及以上 默认打钩
                    String jobCode = people.getJobCode();
                    if(!jobCode.equals("JWYGW")){
                        cb_first_peikan.setChecked(true);
                    }
                    //显示陪看人姓名
                    tv_first_choosepeople.setText(people.getRealName());
                }


            }
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
