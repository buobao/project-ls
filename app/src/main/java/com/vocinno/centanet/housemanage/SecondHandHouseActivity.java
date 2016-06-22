package com.vocinno.centanet.housemanage;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.AddAccompanyActivity;
import com.vocinno.centanet.customermanage.ChoosePeopleActivity;
import com.vocinno.centanet.model.ChoosePeople;
import com.vocinno.centanet.model.KeyHouseItem;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.utils.MethodsExtra;

import butterknife.Bind;

/**
 * Created by hewei26 on 2016/6/17.
 */
public class SecondHandHouseActivity extends OtherBaseActivity {

    @Bind(R.id.tv_house_delCode)            //房源编号
            TextView mTvHouseDelCode;
    @Bind(R.id.tv_house_addr)               //房源地址
            TextView mTvHouseAddr;
    @Bind(R.id.btn_choose_people)          //选择陪看人
            Button mBtnChoosePeople;
    @Bind(R.id.tv_accompany_people)       //显示陪看人
            TextView mTvAccompanyPeople;
    @Bind(R.id.cb_accompany_promise)        //经理陪看
            CheckBox mCbAccompanyPromise;

    private ImageView mBack;
    private TextView mSubmit;
    private String mDelCode;    //房源编号
    private String mAddr;       //房屋地址
    private String mRealName;   //姓名
    private ChoosePeople mPeopleItem;
    private Long mHouseId;

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_secondhand_house;
    }

    @Override
    public void initView() {
        //禁用侧滑
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //初始化标题栏
        mBack = (ImageView) MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mSubmit = (TextView) MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.add_second, null);
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        mBtnChoosePeople.setOnClickListener(this);
        mCbAccompanyPromise.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //从"房源列表"返回的数据
        Intent intent = getIntent();
        KeyHouseItem item = (KeyHouseItem) intent.getSerializableExtra(MyConstant.addSecondHouse);
        mDelCode = item.getDelCode();   //房源编号
        mAddr = item.getAddr();         //房屋地址
        mHouseId = item.getHouseId();   //房屋id
        mTvHouseDelCode.setText(mDelCode);
        mTvHouseAddr.setText(mAddr);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_left_mhead1:          //Back
                finish();
                break;
            case R.id.tv_right_mhead1:          //保存 --> 回传到添加带看
                Intent data = new Intent(this, AddAccompanyActivity.class);
                data.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                data.putExtra("delCode",mDelCode);//房源编号
                data.putExtra("addr",mAddr); //房源地址
                data.putExtra("houseId",mHouseId);//房源id
                data.putExtra(MyConstant.peiKan,mPeopleItem);   //陪看人
                data.putExtra("isManager",mCbAccompanyPromise.isChecked()?"0":"1"); //是否 经理陪看
                startActivity(data);
                finish();
                break;
            case R.id.btn_choose_people:        //选择陪看人
                Intent intent = new Intent(this, ChoosePeopleActivity.class);
                startActivityForResult(intent,MyConstant.REQUEST_CHOOSE_PEOPLE);
                break;
            default:
                break;
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
    
    
    /*************************从选择陪看人 页面 返回的数据**************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MyConstant.REQUEST_CHOOSE_PEOPLE && resultCode == MyConstant.CHOOSE_PEOPLE){
            mPeopleItem = (ChoosePeople) data.getSerializableExtra(MyConstant.choose_people);
            if(mPeopleItem != null){
                //显示经理陪看 : 经理及以上 默认打钩
                String jobCode = mPeopleItem.getJobCode();
                if(!jobCode.equals("JWYGW")){
                    mCbAccompanyPromise.setChecked(true);
                }else{
                    mCbAccompanyPromise.setChecked(false);
                }

                //显示陪看人姓名
                mRealName = mPeopleItem.getRealName();
                mTvAccompanyPeople.setText(mRealName);
            }
        }
    }
}
