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
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.add_first, null);
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        mBtnChoosePeople.setOnClickListener(this);
        mCbAccompanyPromise.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //从"我的出售"返回携带的数据
        Intent intent = getIntent();
        KeyHouseItem item = (KeyHouseItem) intent.getSerializableExtra(MyConstant.addSecondHouse);
        mTvHouseDelCode.setText(item.getDelCode());
        mTvHouseAddr.setText(item.getAddr());


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_left_mhead1:          //Back
                finish();
                break;
            case R.id.tv_right_mhead1:          //保存

                break;
            case R.id.cb_accompany_promise:     //经理陪看
                mCbAccompanyPromise.setChecked(!mCbAccompanyPromise.isChecked());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MyConstant.REQUEST_CHOOSE_PEOPLE && resultCode == MyConstant.CHOOSE_PEOPLE){
            ChoosePeople people = (ChoosePeople) data.getSerializableExtra(MyConstant.choose_people);


        }
    }
}
