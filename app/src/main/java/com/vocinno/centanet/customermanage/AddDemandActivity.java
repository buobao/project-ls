package com.vocinno.centanet.customermanage;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_Wheel_Data;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.utils.CustomUtils;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.view.refreshablelistview.XListView;

public class AddDemandActivity extends OtherBaseActivity {

    private XListView mLvCustormers;
    private View mBack;
    private ImageView mSubmitView,iv_zu_demand,iv_gou_demand;
    private Intent intent;

    private RelativeLayout rl_type_demand,rl_fangxing_demand,rl_place_demand,rl_pianqu_demand,rl_area_demand,rl_price_demand;
    private RelativeLayout il_fangxing_demand,il_place_demand,il_pianqu_demand,il_area_demand,il_price_demand;
    private WheelView wv_start_fangxing_demand,wv_end_fangxing_demand,wv_place_demand,wv_pianqu_demand,wv_start_area_demand,wv_end_area_demand,wv_start_price_demand,wv_end_price_demand;
    private LinearLayout ll_type_demand;
    private RelativeLayout rl_zu_demand,rl_gou_demand;
    private CheckBox cb_type_demand,cb_fangxing_demand,cb_place_demand,cb_pianqu_demand,cb_area_demand,cb_price_demand;
    private String custCode;
    private TextView tv_type_demand,tv_fangxing_demand,tv_changePlace_demand,tv_changePianqu_demand,tv_changeArea_demand,tv_changePrice_demand;
    private String reqType;

    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_add_demand;
    }

    @Override
    public void initView() {
        custCode = getIntent().getStringExtra("custCode");
        MethodsExtra.findHeadTitle1(mContext, baseView,
                R.string.my_potential_customer, null);
        mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mBack.setOnClickListener(this);
        mSubmitView = (ImageView)MethodsExtra.findHeadRightView1(mContext, baseView, 0,
                R.drawable.universal_button_undone);
        mSubmitView.setOnClickListener(this);

        iv_zu_demand= (ImageView) findViewById(R.id.iv_zu_demand);
        iv_gou_demand= (ImageView) findViewById(R.id.iv_gou_demand);

        rl_type_demand= (RelativeLayout) findViewById(R.id.rl_type_demand);
        rl_type_demand.setOnClickListener(this);

        rl_fangxing_demand= (RelativeLayout) findViewById(R.id.rl_fangxing_demand);
        rl_fangxing_demand.setOnClickListener(this);

        rl_place_demand= (RelativeLayout) findViewById(R.id.rl_place_demand);
        rl_place_demand.setOnClickListener(this);

        rl_pianqu_demand= (RelativeLayout) findViewById(R.id.rl_pianqu_demand);
        rl_pianqu_demand.setOnClickListener(this);

        rl_area_demand= (RelativeLayout) findViewById(R.id.rl_area_demand);
        rl_area_demand.setOnClickListener(this);

        rl_price_demand= (RelativeLayout) findViewById(R.id.rl_price_demand);
        rl_price_demand.setOnClickListener(this);


        cb_type_demand= (CheckBox) findViewById(R.id.cb_type_demand);
        cb_type_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_fangxing_demand= (CheckBox) findViewById(R.id.cb_fangxing_demand);
        cb_fangxing_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_place_demand= (CheckBox) findViewById(R.id.cb_place_demand);
        cb_place_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_pianqu_demand= (CheckBox) findViewById(R.id.cb_pianqu_demand);
        cb_pianqu_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_area_demand= (CheckBox) findViewById(R.id.cb_area_demand);
        cb_area_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_price_demand= (CheckBox) findViewById(R.id.cb_price_demand);
        cb_price_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());


        rl_zu_demand= (RelativeLayout) findViewById(R.id.rl_zu_demand);
        rl_zu_demand.setOnClickListener(this);
        rl_gou_demand= (RelativeLayout) findViewById(R.id.rl_gou_demand);
        rl_gou_demand.setOnClickListener(this);

        ll_type_demand= (LinearLayout) findViewById(R.id.ll_type_demand);
        il_fangxing_demand= (RelativeLayout) findViewById(R.id.il_fangxing_demand);
        il_fangxing_demand.setVisibility(View.GONE);

        il_place_demand= (RelativeLayout) findViewById(R.id.il_place_demand);
        il_place_demand.setVisibility(View.GONE);

        il_pianqu_demand= (RelativeLayout) findViewById(R.id.il_pianqu_demand);
        il_pianqu_demand.setVisibility(View.GONE);

        il_area_demand= (RelativeLayout) findViewById(R.id.il_area_demand);
        il_area_demand.setVisibility(View.GONE);

        il_price_demand= (RelativeLayout) findViewById(R.id.il_price_demand);
        il_price_demand.setVisibility(View.GONE);


        wv_start_fangxing_demand= (WheelView) il_fangxing_demand.findViewById(R.id.wheelview_start_modelTwoWheelView);
        wv_end_fangxing_demand= (WheelView) il_fangxing_demand.findViewById(R.id.wheelview_end_modelTwoWheelView);
        wv_place_demand= (WheelView) il_place_demand.findViewById(R.id.wheelview_modelOneWheelView);
        wv_pianqu_demand= (WheelView) il_pianqu_demand.findViewById(R.id.wheelview_modelOneWheelView);
        wv_start_area_demand= (WheelView) il_area_demand.findViewById(R.id.wheelview_start_modelTwoWheelView);
        wv_end_area_demand= (WheelView) il_area_demand.findViewById(R.id.wheelview_end_modelTwoWheelView);
        wv_start_price_demand= (WheelView) il_price_demand.findViewById(R.id.wheelview_start_modelTwoWheelView);
        wv_end_price_demand= (WheelView) il_price_demand.findViewById(R.id.wheelview_end_modelTwoWheelView);

        initWheelViewData();
    }

    private void initWheelViewData() {
        wv_start_fangxing_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.fangxing), CustomUtils.getWindowWidth(this)/2);
        wv_start_fangxing_demand.setEnabled(true);
        wv_start_fangxing_demand.setSelectItem(0);

        wv_end_fangxing_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.maxfangxing), CustomUtils.getWindowWidth(this)/2);
        wv_end_fangxing_demand.setEnabled(true);
        wv_end_fangxing_demand.setSelectItem(0);

        wv_place_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.area), CustomUtils.getWindowWidth(this));
        wv_place_demand.setEnabled(true);
        wv_place_demand.setSelectItem(0);

        wv_start_area_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.squareStart), CustomUtils.getWindowWidth(this)/2);
        wv_start_area_demand.setEnabled(true);
        wv_start_area_demand.setSelectItem(0);

        wv_end_area_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.squareEnd), CustomUtils.getWindowWidth(this)/2);
        wv_end_area_demand.setEnabled(true);
        wv_end_area_demand.setSelectItem(0);

        wv_start_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChushouStart), CustomUtils.getWindowWidth(this)/2);
        wv_start_price_demand.setEnabled(true);
        wv_start_price_demand.setSelectItem(0);

        wv_end_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChushouEnd), CustomUtils.getWindowWidth(this)/2);
        wv_end_price_demand.setEnabled(true);
        wv_end_price_demand.setSelectItem(0);

    }

    private CompoundButton.OnCheckedChangeListener getCheckBoxChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        };
    }
    private void checkOpenOrClose(boolean flag,int intId) {
        if(flag){
            if (intId != ll_type_demand.getId()) {
                closeTypeContainer();
            }
            if (intId != il_fangxing_demand.getId()) {
                closeFangxingContainer();
            }
            if (intId != il_place_demand.getId()) {
                closePlaceContainer();
            }
            if (intId != il_pianqu_demand.getId()) {
                closePianquContainer();
            }
            if (intId != il_area_demand.getId()) {
                closeAreaContainer();
            }
            if (intId != il_price_demand.getId()) {
                closePriceContainer();
            }
        }

    }

    private void closeTypeContainer() {
        ll_type_demand.setVisibility(View.GONE);
        cb_type_demand.setChecked(false);
    }
    private void closeFangxingContainer() {
        il_fangxing_demand.setVisibility(View.GONE);
        cb_fangxing_demand.setChecked(false);
    }
    private void closePlaceContainer() {
        il_place_demand.setVisibility(View.GONE);
        cb_place_demand.setChecked(false);
    }
    private void closePianquContainer() {
        il_pianqu_demand.setVisibility(View.GONE);
        cb_pianqu_demand.setChecked(false);
    }
    private void closeAreaContainer() {
        il_area_demand.setVisibility(View.GONE);
        cb_area_demand.setChecked(false);
    }
    private void closePriceContainer() {
        il_price_demand.setVisibility(View.GONE);
        cb_price_demand.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        boolean cb_flag;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_type_demand:
                cb_flag=!cb_type_demand.isChecked();
                cb_type_demand.setChecked(cb_flag);
                ll_type_demand.setVisibility(cb_flag ? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,ll_type_demand.getId());
                break;
            case R.id.rl_fangxing_demand:
                cb_flag=!cb_fangxing_demand.isChecked();
                cb_fangxing_demand.setChecked(cb_flag);
                il_fangxing_demand.setVisibility(cb_flag? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_fangxing_demand.getId());
                break;
            case R.id.rl_place_demand:
                cb_flag=!cb_place_demand.isChecked();
                cb_place_demand.setChecked(cb_flag);
                il_place_demand.setVisibility(cb_flag? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_place_demand.getId());
                break;
            case R.id.rl_pianqu_demand:
                cb_flag=!cb_pianqu_demand.isChecked();
                cb_pianqu_demand.setChecked(cb_flag);
                il_pianqu_demand.setVisibility(cb_flag? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_pianqu_demand.getId());
                break;
            case R.id.rl_area_demand:
                cb_flag=!cb_area_demand.isChecked();
                cb_area_demand.setChecked(cb_flag);
                il_area_demand.setVisibility(cb_flag? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_area_demand.getId());
                break;
            case R.id.rl_price_demand:
                cb_flag=!cb_price_demand.isChecked();
                cb_price_demand.setChecked(cb_flag);
                il_price_demand.setVisibility(cb_flag ? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_price_demand.getId());
                break;
            case R.id.rl_zu_demand:
                iv_zu_demand.setImageResource(R.drawable.c_manage_button_choose);
                iv_gou_demand.setImageResource(R.drawable.c_manage_button_unselected);
                cb_type_demand.setChecked(false);
                ll_type_demand.setVisibility(View.GONE);
                // 求租
                reqType = "rent";
                wv_start_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChuzuStart), CustomUtils.getWindowWidth(this)/2);
                wv_start_price_demand.setEnabled(true);
                wv_start_price_demand.setSelectItem(0);

                wv_end_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChuzuEnd), CustomUtils.getWindowWidth(this)/2);
                wv_end_price_demand.setEnabled(true);
                wv_end_price_demand.setSelectItem(0);
                break;
            case R.id.rl_gou_demand:
                iv_gou_demand.setImageResource(R.drawable.c_manage_button_choose);
                iv_zu_demand.setImageResource(R.drawable.c_manage_button_unselected);
                cb_type_demand.setChecked(false);
                ll_type_demand.setVisibility(View.GONE);
                // 求购
                reqType = "buy";
                wv_start_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChushouStart), CustomUtils.getWindowWidth(this)/2);
                wv_start_price_demand.setEnabled(true);
                wv_start_price_demand.setSelectItem(0);

                wv_end_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChushouEnd), CustomUtils.getWindowWidth(this)/2);
                wv_end_price_demand.setEnabled(true);
                wv_end_price_demand.setSelectItem(0);
                break;
            default:
                break;
        }
    }
    @Override
    public void initData() {
        intent=getIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ConstantResult.REFRESH:
                break;
        }
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
