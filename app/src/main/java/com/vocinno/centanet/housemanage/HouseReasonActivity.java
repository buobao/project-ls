package com.vocinno.centanet.housemanage;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.utils.MethodsExtra;

public class HouseReasonActivity extends SuperSlideMenuActivity {
    private View mBackView, mSubmit, mTitleView;

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_house_reason;
    }

    @Override
    public void initView() {
        mBackView = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
        mSubmit = MethodsExtra.findHeadRightView1(mContext, mRootView, 0, R.drawable.universal_button_undone);
        mTitleView = MethodsExtra
                .findHeadTitle1(mContext, mRootView, 0, getResources().getString(R.string.lookhousereason));
    }
    @Override
    public void setListener() {
        mBackView.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_left_mhead1:
                onBack();
                break;
            default:
                break;
        }
    }

    @Override
    public Handler setHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                HouseReasonActivity.this.closeMenu(msg);
            }
        };
    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public void notifCallBack(String name, String className, Object data) {

    }

}
