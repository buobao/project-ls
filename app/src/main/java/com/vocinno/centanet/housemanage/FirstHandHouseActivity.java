package com.vocinno.centanet.housemanage;

import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.utils.MethodsExtra;

/**
 * Created by hewei26 on 2016/6/17.
 */
public class FirstHandHouseActivity extends OtherBaseActivity {

    private ImageView mBack;
    private TextView mSubmit;

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
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.add_second, null);
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

    }

    @Override
    public void initData() {

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
