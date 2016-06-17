package com.vocinno.centanet.customermanage;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_Wheel_Data;
import com.vocinno.centanet.apputils.cst.CST_Wheel_Data.WheelType;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.apputils.selfdefineview.ScrollViewCanStop;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.PianQu;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.CustomUtils;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 添加客户
 *
 * @author Administrator
 */
@SuppressLint("CutPasteId")
public class AddCustomerActivity extends OtherBaseActivity implements View.OnTouchListener {
    private Map<String, String> mapPianQu = new HashMap<String, String>();
    private ModelDialog modelDialog;
    private TextView tv_addcust_time;
    private int isSameTel = 0;
    private ArrayList<String> listStr = new ArrayList<>();  //片区数据集合

    String reqType = null;  //求租求购类型

    private static enum ConnectionType {
        none, connTel, connQQ, connWeixin
    }

    private View mBackView, mSubmitView;
    private RelativeLayout /*mRyltConnectionBanner, */mRyltTypeBanner,
            mRyltPlaceBanner, mRyltAreaBanner, mRyltPriceBanner,
            mRyltPianquBanner, /*mRyltPhone,*//* mRyltQQ,*/
            mRyltQiuzu, mRyltQiumai,
            mRyltChoosePlaceContainer, mRyltChoosePriceContaner,
            mRyltChoosePianquContainer, mRyltChooseAreaContainer/*, mRyltWX*/;

    private ImageView /*mImgConnectionRight,*/ mImgTypeRight, mImgPlaceRight,
            mImgPianquRight, mImgAreaRight, mImgPriceRight/*,*/ /*mImgPhoneImage,*/
    /*mImgPhoneIcon,*/ /*mImgQQImage, *//*mImgQQIcon,*/ /*mImgWXImage, *//*mImgWXIcon*/;
    private LinearLayout /*mLlytConntectTypeContainer,*/ mLlytTypeContainer;
    private ImageView mImgQiumai, mImgQiuzu;
    private TextView mTvCustormerArea, mTvCustormerPrice, mTvCustormerPlace,

    mTvCustormerType, mTvCustormerPianqu;
    private EditText mEtCustormerNumber;
    private Button mBtnSubmitChoosePlace, mBtnSubmitChoosePianqu,
            mBtnSubmitChooseArea, mBtnSubmitChoosePrice;
    private WheelView mWheelViewChoosePlace,
            mWheelViewChoosePianqu,
            mWheelViewChooseAreaLast, mWheelViewChooseAreaTop,
            mWheelViewChoosePriceTop, mWheelViewChoosePriceLast;
    private EditText /*mEtConnectionNumber, */mEtCustormerName, mEtOtherInfo;

    private String mStrQQ, mStrWeixin;
    private ConnectionType mCurrConnType = ConnectionType.none;


    private LinearLayout ll_source_addCustomer, ll_level_addCustomer, wv_choose_source_addCustomer, wv_choose_level_addCustomer, wv_choose_fangxing_addCustomer;
    private RelativeLayout rl_choose_source_addCustomer, rl_choose_level_addCustomer, rl_fangxing_addCust, il_fangxing_addCust;
    private CheckBox cb_source_addCustomer, cb_level_addCustomer, cb_fangxing_addCustomer;
    private WheelView wv_source_addCustomer, wv_level_addCustomer, wv_fangxing_start_addCustomer, wv_fangxing_end_addCustomer;
    private Button bt_source_addCustomer, bt_level_addCustomer, bt_fangxing_addCustomer;
    private TextView tv_source_addCustomer, tv_level_addCustomer, tv_fangxing_addCust, tv_start_title, tv_end_title;
    private ScrollViewCanStop scrollView;
    private int wheelViewHeight;
    @Override
    public int setContentLayoutId() {
        return R.layout.activity_add_custormer;
    }
    @Override
    public void initView() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // 需要获取的输入控件
//		mEtConnectionNumber = (EditText) findViewById(R.id.et_connectionNumber_addCustomerActivity);
        scrollView = (ScrollViewCanStop) findViewById(R.id.scrollView1);
        tv_addcust_time = (TextView) findViewById(R.id.tv_addcust_time);
        tv_addcust_time.setText("委托日期"+MyUtils.getFormatDate(new Date()));
        mEtCustormerName = (EditText) findViewById(R.id.et_name_addCustomerActivity);
        // 需要添加点击事件的RelativeLayout
        mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mSubmitView = MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
        mSubmitView.setEnabled(false);
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.addcustomer,
                null);
        mEtOtherInfo = (EditText) findViewById(R.id.et_otherInfo_addCustomerActivity);
//		mRyltConnectionBanner = (RelativeLayout) findViewById(R.id.rlyt_connectionBanner_addCustomerActivity);
        mRyltTypeBanner = (RelativeLayout) findViewById(R.id.rlyt_typeBanner_addCustomerActivity);
        mRyltTypeBanner.setOnTouchListener(this);
        mRyltPlaceBanner = (RelativeLayout) findViewById(R.id.rlyt_placeBanner_addCustomerActivity);
        mRyltPlaceBanner.setOnTouchListener(this);
        mRyltPianquBanner = (RelativeLayout) findViewById(R.id.rlyt_pianquBanner_addCustomerActivity);
        mRyltPianquBanner.setOnTouchListener(this);
        mRyltAreaBanner = (RelativeLayout) findViewById(R.id.rlyt_areaBanner_addCustomerActivity);
        mRyltAreaBanner.setOnTouchListener(this);
        mRyltPriceBanner = (RelativeLayout) findViewById(R.id.rlyt_priceBanner_addCustomerActivity);
        mRyltPriceBanner.setOnTouchListener(this);
        // 点击Relative之后需要改变的右边的img
//		mImgConnectionRight = (ImageView) findViewById(R.id.img_connectionRight_addCustomerActivity);
        mImgTypeRight = (ImageView) findViewById(R.id.img_chooseType_addCustomerActivity);
        mImgPlaceRight = (ImageView) findViewById(R.id.img_choosePlace_addCustomerActivity);
        mImgPianquRight = (ImageView) findViewById(R.id.img_choosePianqu_addCustomerActivity);
        mImgAreaRight = (ImageView) findViewById(R.id.img_chooseArea_addCustomerActivity);
        mImgPriceRight = (ImageView) findViewById(R.id.img_choosePrice_addCustomerActivity);
        // 点击Relative之后需要改变的textview
        mEtCustormerNumber = (EditText) findViewById(R.id.tv_connect_addCustomerActivity);
        mEtCustormerNumber.setOnClickListener(this);
        mEtCustormerNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String tel = mEtCustormerNumber.getText().toString().trim();
                if (tel.length() >= 11) {
                    checkPhoneNum(tel);
                }
            }
        });
        mTvCustormerType = (TextView) findViewById(R.id.tv_type_addCustomerActivity);
        mTvCustormerPlace = (TextView) findViewById(R.id.tv_changePlace_addCustomerActivity);
        mTvCustormerPianqu = (TextView) findViewById(R.id.tv_changePianqu_addCustomerActivity);
        mTvCustormerArea = (TextView) findViewById(R.id.tv_changeArea_addCustomerActivity);
        mTvCustormerPrice = (TextView) findViewById(R.id.tv_changePrice_addCustomerActivity);
        // 点击Relative之后需要显示的控件
//		mLlytConntectTypeContainer = (LinearLayout) findViewById(R.id.llyt_connectionType_addCustomerActivity);
        mLlytTypeContainer = (LinearLayout) findViewById(R.id.llyt_chooseCustormerType_addCustomerActivity);
        mRyltChoosePlaceContainer = (RelativeLayout) findViewById(R.id.rlyt_choosePlace_addCustomerActivity);
        mRyltChoosePianquContainer = (RelativeLayout) findViewById(R.id.rlyt_choosePianqu_addCustomerActivity);
        mRyltChooseAreaContainer = (RelativeLayout) findViewById(R.id.rlyt_chooseArea_addCustomerActivity);
        ((TextView) mRyltChooseAreaContainer
                .findViewById(R.id.tv_startTitle_modelTwoWheelView))
                .setText(R.string.minarea);
        ((TextView) mRyltChooseAreaContainer
                .findViewById(R.id.tv_endTitle_modelTwoWheelView))
                .setText(R.string.maxarea);
        mRyltChoosePriceContaner = (RelativeLayout) findViewById(R.id.rlyt_choosePrice_addCustomerActivity);

        mRyltQiuzu = (RelativeLayout) findViewById(R.id.rlyt_isChooseQiuzu_addCustomerActivity);
        mRyltQiumai = (RelativeLayout) findViewById(R.id.rlyt_isChooseQiumai_addCustomerActivity);
        mWheelViewChoosePlace = (WheelView) mRyltChoosePlaceContainer
                .findViewById(R.id.wheelview_modelOneWheelView);
        mWheelViewChoosePianqu = (WheelView) mRyltChoosePianquContainer
                .findViewById(R.id.wheelview_modelOneWheelView);
        mWheelViewChooseAreaLast = (WheelView) mRyltChooseAreaContainer
                .findViewById(R.id.wheelview_end_modelTwoWheelView);
        mWheelViewChooseAreaTop = (WheelView) mRyltChooseAreaContainer
                .findViewById(R.id.wheelview_start_modelTwoWheelView);
        mWheelViewChoosePriceLast = (WheelView) mRyltChoosePriceContaner
                .findViewById(R.id.wheelview_end_modelTwoWheelView);
        mWheelViewChoosePriceTop = (WheelView) mRyltChoosePriceContaner
                .findViewById(R.id.wheelview_start_modelTwoWheelView);
        mBtnSubmitChoosePlace = (Button) mRyltChoosePlaceContainer
                .findViewById(R.id.btn_submit_modelOneWheelView);
        mBtnSubmitChoosePianqu = (Button) mRyltChoosePianquContainer
                .findViewById(R.id.btn_submit_modelOneWheelView);
        mBtnSubmitChooseArea = (Button) mRyltChooseAreaContainer
                .findViewById(R.id.btn_submit_modelTwoWheelView);
        mBtnSubmitChoosePrice = (Button) mRyltChoosePriceContaner
                .findViewById(R.id.btn_submit_modelTwoWheelView);

        // 原本隐藏起来的控件里需要被改变的
        mImgQiuzu = (ImageView) findViewById(R.id.img_isChooseQiuzu_addCustomerActivity);
        mImgQiumai = (ImageView) findViewById(R.id.img_isChooseQiumai_addCustomerActivity);

        tv_source_addCustomer = (TextView) findViewById(R.id.tv_source_addCustomer);
        tv_level_addCustomer = (TextView) findViewById(R.id.tv_level_addCustomer);
        tv_fangxing_addCust = (TextView) findViewById(R.id.tv_fangxing_addCust);

        wv_choose_source_addCustomer = (LinearLayout) findViewById(R.id.wv_choose_source_addCustomer);
        wv_choose_level_addCustomer = (LinearLayout) findViewById(R.id.wv_choose_level_addCustomer);
        wv_choose_fangxing_addCustomer = (LinearLayout) findViewById(R.id.wv_choose_fangxing_addCustomer);

        ll_source_addCustomer = (LinearLayout) findViewById(R.id.ll_source_addCustomer);
        ll_source_addCustomer.setOnClickListener(this);
        ll_source_addCustomer.setOnTouchListener(this);

        ll_level_addCustomer = (LinearLayout) findViewById(R.id.ll_level_addCustomer);
        ll_level_addCustomer.setOnClickListener(this);
        ll_level_addCustomer.setOnTouchListener(this);

        cb_source_addCustomer = (CheckBox) findViewById(R.id.cb_source_addCustomer);
        cb_source_addCustomer.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_level_addCustomer = (CheckBox) findViewById(R.id.cb_level_addCustomer);
        cb_level_addCustomer.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_fangxing_addCustomer = (CheckBox) findViewById(R.id.cb_fangxing_addCustomer);
        cb_fangxing_addCustomer.setOnCheckedChangeListener(getCheckBoxChangeListener());

        rl_choose_source_addCustomer = (RelativeLayout) findViewById(R.id.rl_choose_source_addCustomer);
        rl_choose_level_addCustomer = (RelativeLayout) findViewById(R.id.rl_choose_level_addCustomer);
        rl_fangxing_addCust = (RelativeLayout) findViewById(R.id.rl_fangxing_addCust);
        il_fangxing_addCust = (RelativeLayout) findViewById(R.id.il_fangxing_addCust);
        rl_fangxing_addCust.setOnClickListener(this);
        rl_fangxing_addCust.setOnTouchListener(this);
        wv_source_addCustomer = (WheelView) rl_choose_source_addCustomer.findViewById(R.id.wheelview_modelOneWheelView);
        wv_level_addCustomer = (WheelView) rl_choose_level_addCustomer.findViewById(R.id.wheelview_modelOneWheelView);
        wv_fangxing_start_addCustomer = (WheelView) il_fangxing_addCust.findViewById(R.id.wheelview_start_modelTwoWheelView);
        wv_fangxing_end_addCustomer = (WheelView) il_fangxing_addCust.findViewById(R.id.wheelview_end_modelTwoWheelView);
        tv_start_title = (TextView) il_fangxing_addCust.findViewById(R.id.tv_startTitle_modelTwoWheelView);
        tv_start_title.setText("最小房型");
        tv_end_title = (TextView) il_fangxing_addCust.findViewById(R.id.tv_endTitle_modelTwoWheelView);
        tv_end_title.setText("最大房型");
        bt_fangxing_addCustomer = (Button) il_fangxing_addCust.findViewById(R.id.btn_submit_modelTwoWheelView);
        bt_fangxing_addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fangxing = wv_fangxing_start_addCustomer.getSelectedText();
                String maxFangxing = wv_fangxing_end_addCustomer.getSelectedText();
                if (Integer.parseInt(fangxing.replace("室", "")) >= Integer.parseInt(maxFangxing.replace("室", ""))) {
                    MyToast.showToast("最大房型必须大于最小房型");
                    return;
                } else {
                    tv_fangxing_addCust.setText(fangxing + "-" + maxFangxing + "");
                    wv_choose_fangxing_addCustomer.setVisibility(View.GONE);
                    cb_fangxing_addCustomer.setChecked(!cb_fangxing_addCustomer.isChecked());
                }
            }
        });


        bt_source_addCustomer = (Button) rl_choose_source_addCustomer.findViewById(R.id.btn_submit_modelOneWheelView);
        bt_source_addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = wv_source_addCustomer.getSelectedText();
                tv_source_addCustomer.setText(source);
                wv_choose_source_addCustomer.setVisibility(View.GONE);
                cb_source_addCustomer.setChecked(!cb_source_addCustomer.isChecked());
            }
        });
        bt_level_addCustomer = (Button) rl_choose_level_addCustomer.findViewById(R.id.btn_submit_modelOneWheelView);
        bt_level_addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String level = wv_level_addCustomer.getSelectedText();
                tv_level_addCustomer.setText(level);
                wv_choose_level_addCustomer.setVisibility(View.GONE);
                cb_level_addCustomer.setChecked(!cb_level_addCustomer.isChecked());
            }
        });

        mRyltChoosePlaceContainer.setVisibility(View.GONE);
        mRyltChoosePianquContainer.setVisibility(View.GONE);
        mRyltChooseAreaContainer.setVisibility(View.GONE);
        mRyltChoosePriceContaner.setVisibility(View.GONE);

        mWheelViewChoosePlace.postDelayed(new Runnable() {
            @Override
            public void run() {
                wheelViewHeight = mWheelViewChoosePlace.getHeight();
            }
        },100);
        setListener();
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getCheckBoxChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.cb_source_addCustomer:
                        if (isChecked) {
                            wv_choose_source_addCustomer.setVisibility(View.VISIBLE);
//							wv_choose_level_addCustomer.setVisibility(View.GONE);
//							cb_level_addCustomer.setChecked(false);
                        } else {
                            wv_choose_source_addCustomer.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.cb_level_addCustomer:
                        if (isChecked) {
                            wv_choose_level_addCustomer.setVisibility(View.VISIBLE);
//							wv_choose_source_addCustomer.setVisibility(View.GONE);
//							cb_source_addCustomer.setChecked(false);
                        } else {
                            wv_choose_level_addCustomer.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.cb_fangxing_addCustomer:
                        if (isChecked) {
                            wv_choose_fangxing_addCustomer.setVisibility(View.VISIBLE);
                        } else {
                            wv_choose_fangxing_addCustomer.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        };
    }

    public void setListener() {

        mRyltQiuzu.setOnClickListener(this);
        mRyltQiumai.setOnClickListener(this);
        mBtnSubmitChoosePlace.setOnClickListener(this);
        mBtnSubmitChoosePianqu.setOnClickListener(this);
        mBtnSubmitChooseArea.setOnClickListener(this);
        mBtnSubmitChoosePrice.setOnClickListener(this);

        mBackView.setOnClickListener(this);
        mSubmitView.setOnClickListener(this);
        mRyltTypeBanner.setOnClickListener(this);
        mRyltPlaceBanner.setOnClickListener(this);
        mRyltPianquBanner.setOnClickListener(this);
        mRyltAreaBanner.setOnClickListener(this);
        mRyltPriceBanner.setOnClickListener(this);

        mSubmitView.setClickable(false);

        mEtCustormerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                checkIsFinish();
            }
        });
    }

    @Override
    public void initData() {
        wv_source_addCustomer.setData(CST_Wheel_Data.getListDatas(WheelType.source), CustomUtils.getWindowWidth(this));
        wv_source_addCustomer.setEnabled(true);
        wv_source_addCustomer.setSelectItem(0);

        wv_level_addCustomer.setData(CST_Wheel_Data.getListDatas(WheelType.level), CustomUtils.getWindowWidth(this));
        wv_level_addCustomer.setEnabled(true);
        wv_level_addCustomer.setSelectItem(0);

        wv_fangxing_start_addCustomer.setData(CST_Wheel_Data.getListDatas(WheelType.fangxing), CustomUtils.getWindowWidth(this) / 2);
        wv_fangxing_start_addCustomer.setEnabled(true);
        wv_fangxing_start_addCustomer.setSelectItem(0);

        wv_fangxing_end_addCustomer.setData(CST_Wheel_Data.getListDatas(WheelType.maxfangxing), CustomUtils.getWindowWidth(this) / 2);
        wv_fangxing_end_addCustomer.setEnabled(true);
        wv_fangxing_end_addCustomer.setSelectItem(0);

        mWheelViewChoosePlace.setData(CST_Wheel_Data
                .getListDatas(WheelType.area), CustomUtils.getWindowWidth(this));
        mWheelViewChoosePianqu.setData(new ArrayList<String>(), CustomUtils.getWindowWidth(this));
        mWheelViewChooseAreaLast.setData(CST_Wheel_Data
                .getListDatas(WheelType.squareEnd), CustomUtils.getWindowWidth(this) / 2);
        mWheelViewChooseAreaTop.setData(CST_Wheel_Data
                .getListDatas(WheelType.squareStart), CustomUtils.getWindowWidth(this) / 2);
        mWheelViewChoosePriceLast.setData(CST_Wheel_Data
                .getListDatas(WheelType.priceChushouEnd), CustomUtils.getWindowWidth(this) / 2);
        mWheelViewChoosePriceTop.setData(CST_Wheel_Data
                .getListDatas(WheelType.priceChushouStart), CustomUtils.getWindowWidth(this) / 2);
        mWheelViewChoosePlace.setEnable(true);
        mWheelViewChoosePlace.setSelectItem(0);
        mWheelViewChoosePianqu.setEnable(true);
        mWheelViewChooseAreaLast.setEnable(true);
        mWheelViewChooseAreaTop.setEnable(true);
        mWheelViewChoosePriceLast.setEnable(true);
        mWheelViewChoosePriceTop.setEnable(true);
        mWheelViewChooseAreaLast.setSelectItem(0);
        mWheelViewChooseAreaTop.setSelectItem(0);
        mWheelViewChoosePriceLast.setSelectItem(0);
        mWheelViewChoosePriceTop.setSelectItem(0);
    }

    private void setScrollView() {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
//                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }

    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_fangxing_addCust:
                setScrollView();
                cb_fangxing_addCustomer.setChecked(!cb_fangxing_addCustomer.isChecked());
                if (cb_fangxing_addCustomer.isChecked()) {
                    checkOpenOrClose(rl_fangxing_addCust.getId());
                }
                checkIsFinish();
                break;
            case R.id.ll_source_addCustomer:
                setScrollView();
                cb_source_addCustomer.setChecked(!cb_source_addCustomer.isChecked());
                if (cb_source_addCustomer.isChecked()) {
                    checkOpenOrClose(ll_source_addCustomer.getId());
                }
                checkIsFinish();
                break;
            case R.id.ll_level_addCustomer:
                setScrollView();
                cb_level_addCustomer.setChecked(!cb_level_addCustomer.isChecked());
                if (cb_level_addCustomer.isChecked()) {
                    checkOpenOrClose(ll_level_addCustomer.getId());
                }
                checkIsFinish();
                break;
            case R.id.img_left_mhead1:  //标题栏左侧箭头
                finish();
                break;
            case R.id.tv_right_mhead1:  //标题栏右侧 "保存"文字
                if (mEtCustormerNumber.getText() == null || mEtCustormerNumber.getText().toString().trim().length() <= 0) {
                    MethodsExtra.toast(mContext, "手机号码不能为空");
                    return;
                } else {
                    if (!isMobileNO(mEtCustormerNumber.getText().toString())) {
                        MethodsExtra.toast(mContext, "手机号码有误，请重新输入！");
                        mEtCustormerNumber.setFocusable(true);
                        mEtCustormerNumber.setFocusableInTouchMode(true);
                        return;
                    }
                }
                if (isSameTel == 0) {
                    MethodsExtra.toast(mContext, "手机号码请查重");
                    return;
                } else if (isSameTel == 2) {
                    MethodsExtra.toast(mContext, "手机号码重复");
                    return;
                }
                showDialog();

                if (mTvCustormerType.getText().toString().equals("求租")) {
                    reqType = "rent";
                } else {
                    reqType = "buy";
                }
                String price = null;
                String oldP = mTvCustormerPrice.getText().toString()
                        .replace("万", "").replace("元", "");
                if ("buy".equals(reqType)) {
                    price = oldP.substring(0, oldP.indexOf("-")) + "0000" + oldP.substring(oldP.indexOf("-"), oldP.length());
                    if (!price.endsWith("不限")) {
                        price += "0000";
                    }
                } else {
                    price = oldP;
                }
                addCust(reqType, price);
                break;
            case R.id.rlyt_isChooseQiuzu_addCustomerActivity:
                // 求租
                mImgQiumai
                        .setBackgroundResource(R.drawable.c_manage_button_unselected);
                mImgQiuzu.setBackgroundResource(R.drawable.c_manage_button_choose);
                mTvCustormerType.setText("求租");
                closeTypeContainer();
                if (mTvCustormerPrice.getText() != null
                        && mTvCustormerPrice.getText().toString().contains("万")) {
                    mTvCustormerPrice.setText("");
                }
                mWheelViewChoosePriceTop.setData(CST_Wheel_Data
                        .getListDatas(WheelType.priceChuzuStart));
                mWheelViewChoosePriceLast.setData(CST_Wheel_Data
                        .getListDatas(WheelType.priceChuzuEnd));
                mWheelViewChoosePriceTop.setSelectItem(0);
                mWheelViewChoosePriceLast.setSelectItem(0);
                mWheelViewChoosePriceTop.invalidate();
                mWheelViewChoosePriceLast.invalidate();
                checkIsFinish();
                break;
            case R.id.rlyt_isChooseQiumai_addCustomerActivity:
                // 求购
                mImgQiumai.setBackgroundResource(R.drawable.c_manage_button_choose);
                mImgQiuzu
                        .setBackgroundResource(R.drawable.c_manage_button_unselected);
                mTvCustormerType.setText("求购");
                closeTypeContainer();
                if (mTvCustormerPrice.getText() != null
                        && mTvCustormerPrice.getText().toString().contains("元")) {
                    mTvCustormerPrice.setText("");
                }
                mWheelViewChoosePriceTop.setData(CST_Wheel_Data
                        .getListDatas(WheelType.priceChushouStart));
                mWheelViewChoosePriceLast.setData(CST_Wheel_Data
                        .getListDatas(WheelType.priceChushouEnd));
                mWheelViewChoosePriceTop.setSelectItem(0);
                mWheelViewChoosePriceLast.setSelectItem(0);
                mWheelViewChoosePriceTop.invalidate();
                mWheelViewChoosePriceLast.invalidate();
                checkIsFinish();
                break;
            // 如果六个选择全选了 才给提交
            // 分别是 名字 电话 类型 地点 面积 价格
//			case R.id.et_connectionNumberOK_addCustomerActivity:
			/*case R.id.rlyt_connectionBanner_addCustomerActivity:
				if (mLlytConntectTypeContainer.getVisibility() == View.GONE) {
					mLlytConntectTypeContainer.setVisibility(View.VISIBLE);
//					mImgConnectionRight.setBackgroundResource(R.drawable.h_manage_icon_up);
					checkOpenOrClose(mLlytConntectTypeContainer.getId());
					if (mCurrConnType == ConnectionType.none) {
						addPhoneNumber();
					}
				} else {
					if (isMobileNO(mStrTel) || TextUtils.isEmpty(mStrTel)) {
						String phoneJson = CST_JS.getJsonStringForCheckPhoneNORepeated(mStrTel);
						MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
								CST_JS.JS_Function_CustomerList_addCustomer_checkPhoneNORepeated, phoneJson);
						//closeConnectionTypeContainer();
					} else {
						MethodsExtra.toast(mContext, "手机号码有误，请重新输入！");
					}
				}
				break;*/
            case R.id.rlyt_typeBanner_addCustomerActivity:
                setScrollView();
                setLoseFocus();
                if (mLlytTypeContainer.getVisibility() == View.GONE) {
                    mLlytTypeContainer.setVisibility(View.VISIBLE);
                    mImgTypeRight
                            .setBackgroundResource(R.drawable.h_manage_icon_up);
                    checkOpenOrClose(mLlytTypeContainer.getId());
                } else {
                    closeTypeContainer();
                }
                MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
                checkIsFinish();
                break;
            case R.id.rlyt_placeBanner_addCustomerActivity:
                setScrollView();
                setLoseFocus();
                if (mRyltChoosePlaceContainer.getVisibility() == View.GONE) {
                    mRyltChoosePlaceContainer.setVisibility(View.VISIBLE);
                    mImgPlaceRight
                            .setBackgroundResource(R.drawable.h_manage_icon_up);
                    checkOpenOrClose(mRyltChoosePlaceContainer.getId());
                } else {
                    closePlaceContainer();
                }
                MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
                checkIsFinish();
                break;
            case R.id.rlyt_pianquBanner_addCustomerActivity:    //点击展开片区WheelView
                setScrollView();
                setLoseFocus();
                if (mRyltChoosePianquContainer.getVisibility() == View.GONE){
                    if(!TextUtils.isEmpty(mTvCustormerPlace.getText().toString())){
                        mRyltChoosePianquContainer.setVisibility(View.VISIBLE);
                        mImgPianquRight.setBackgroundResource(R.drawable.h_manage_icon_up);
                    }else{
                        MethodsExtra.toast(mContext,"请先选择城区!");
                    }
                    checkOpenOrClose(mRyltChoosePianquContainer.getId());
                }  else {
                    closePianquContainer();
                }
                MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
                checkIsFinish();
                break;
            case R.id.rlyt_areaBanner_addCustomerActivity:      //点击展开区域WheelView
                setScrollView();
                setLoseFocus();
                if (mRyltChooseAreaContainer.getVisibility() == View.GONE) {
                    mRyltChooseAreaContainer.setVisibility(View.VISIBLE);
                    mImgAreaRight
                            .setBackgroundResource(R.drawable.h_manage_icon_up);
                    checkOpenOrClose(mRyltChooseAreaContainer.getId());
                } else {
                    closeAreaContainer();
                }
                MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
                checkIsFinish();
                break;
            case R.id.rlyt_priceBanner_addCustomerActivity:
                setScrollView();
                setLoseFocus();
                if (mRyltChoosePriceContaner.getVisibility() == View.GONE ) {
                //选择价格前 必须先选择类型
                    if(TextUtils.isEmpty(mTvCustormerType.getText())){
                        MethodsExtra.toast(this,"请先选择交易类型!");
                        return;
                    }
                    mRyltChoosePriceContaner.setVisibility(View.VISIBLE);
                    mImgPriceRight.setBackgroundResource(R.drawable.h_manage_icon_up);
                    checkOpenOrClose(mRyltChoosePriceContaner.getId());
                } else {
                    closePriceContainer();
                }
                MethodsExtra.hideSoftInput1(mContext, mEtCustormerName);
                checkIsFinish();
                break;
			/*case R.id.rlyt_phone_addCustomerActivity:
				addPhoneNumber();
				break;*/
//			case R.id.rlyt_qq_addCustomerActivity:
//				addQQNumber();
//				break;
//			case R.id.rlyt_wx_addCustomerActivity:
//				addWXNumber();
//				break;
            case R.id.btn_submit_modelOneWheelView:
                /**************************区域WheelView选择****************************/
                if (mRyltChoosePlaceContainer.getVisibility() == View.VISIBLE) {
                    mWheelViewChoosePianqu.setData(new ArrayList<String>());
                    listStr.clear();
                    if (mWheelViewChoosePlace.getSelected() == 0 ) {
                        // 选择所有   默认片区为全部
                        listStr.add("全部");
                        mWheelViewChoosePianqu.resetData(listStr,CustomUtils.getWindowWidth(AddCustomerActivity.this));
                        mWheelViewChoosePianqu.setSelectItem(0);
                        mTvCustormerPianqu.setText("全部");
                    }else{
                        // 选择其他区域 默认片区为全部
                        String strCode = CST_Wheel_Data.getCodeForArea(mWheelViewChoosePlace.getSelectedText());
                        getPianQu(strCode); //获得片区数据
                        listStr.add(0,"全部");
                        mWheelViewChoosePianqu.resetData(listStr, CustomUtils.getWindowWidth(AddCustomerActivity.this));
                        mWheelViewChoosePianqu.setSelectItem(0);
                        mTvCustormerPianqu.setText("全部");
                    }
                    mTvCustormerPlace.setText(mWheelViewChoosePlace
                            .getSelectedText());
                    closePlaceContainer();
                /**************************片区WheelView选择****************************/
                } else if (mRyltChoosePianquContainer.getVisibility() == View.VISIBLE) {    //设置片区
                    mTvCustormerPianqu.setText(mWheelViewChoosePianqu.getSelectedText());
                    closePianquContainer();
                }
                checkIsFinish();
                break;
            case R.id.btn_submit_modelTwoWheelView:
                if (mRyltChooseAreaContainer.getVisibility() == View.VISIBLE) {
                    // 面积
                    if (mWheelViewChooseAreaLast.getSelectedText().trim()
                            .equals("不限")
                            || Integer.parseInt(mWheelViewChooseAreaTop
                            .getSelectedText().trim().replaceAll("平米", "")) < Integer
                            .parseInt(mWheelViewChooseAreaLast
                                    .getSelectedText().trim()
                                    .replaceAll("平米", ""))) {
                        mTvCustormerArea.setText(mWheelViewChooseAreaTop
                                .getSelectedText()
                                + "-"
                                + mWheelViewChooseAreaLast.getSelectedText());
                        closeAreaContainer();
                    } else {
                        MethodsExtra.toast(mContext, "最大面积应大于最小面积");
                    }
                } else if (mRyltChoosePriceContaner.getVisibility() == View.VISIBLE) {
                    // 价格
                    if (mWheelViewChoosePriceLast.getSelectedText().trim()
                            .equals("不限")
                            || Integer.parseInt(mWheelViewChoosePriceTop
                            .getSelectedText().trim().replaceAll("万", "")
                            .replaceAll("元", "")) < Integer
                            .parseInt(mWheelViewChoosePriceLast
                                    .getSelectedText().trim()
                                    .replaceAll("万", "")
                                    .replaceAll("元", ""))) {
                        mTvCustormerPrice.setText(mWheelViewChoosePriceTop
                                .getSelectedText()
                                + "-"
                                + mWheelViewChoosePriceLast.getSelectedText());
                        closePriceContainer();
                    } else {
                        MethodsExtra.toast(mContext, "最高价格应大于最低价格");
                    }
                }
                checkIsFinish();
                break;
            default:
                break;
        }
    }
    /**************************获得片区数据****************************/
    private void getPianQu(String strCode) {
        Map<String, String> map = new HashMap<String, String>();
        URL = NetWorkConstant.PORT_URL + NetWorkMethod.areas;
        map.put(NetWorkMethod.districtCode, strCode);
        showDialog();
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                dismissDialog();
            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, PianQu.class);
                List<PianQu> listPianQu = jsReturn.getListDatas();
                mapPianQu.clear();
                if (jsReturn.isSuccess() && listPianQu != null) {
                    for (int i = 0; i < listPianQu.size(); i++) {
                        PianQu pq = listPianQu.get(i);
                        mapPianQu.put(pq.getAreaName(), "" + pq.getAreaCode());
                        listStr.add(pq.getAreaName());
                    }
                    mWheelViewChoosePianqu.resetData(listStr, CustomUtils.getWindowWidth(AddCustomerActivity.this));
                    mWheelViewChoosePianqu.setSelectItem(0);
                }
            }
        });
    }

    private void addCust(String reqType, String price) {
        Map<String, String> map = new HashMap<String, String>();
        URL = NetWorkConstant.PORT_URL + NetWorkMethod.addCust;
        String source = tv_source_addCustomer.getText().toString();
        String sourceCode = CST_Wheel_Data.sourceMap.get(source);
        String level = tv_level_addCustomer.getText().toString();
        String fromToRoom = tv_fangxing_addCust.getText().toString().replace("室", "");
        map.put(NetWorkMethod.name, mEtCustormerName.getText().toString());
        map.put(NetWorkMethod.phone, mEtCustormerNumber.getText().toString());
        map.put(NetWorkMethod.reqType, reqType);

        String districtCode= CST_Wheel_Data.getCodeForArea(mWheelViewChoosePlace.getSelectedText());
        map.put(NetWorkMethod.districtCode, districtCode==null?"":districtCode);
        String pianQu=mapPianQu.get(mTvCustormerPianqu.getText().toString());
        map.put(NetWorkMethod.area, pianQu==null?"":pianQu);
        map.put(NetWorkMethod.acreage, mTvCustormerArea.getText().toString().replace("平米", ""));
        map.put(NetWorkMethod.price, price);
        map.put(NetWorkMethod.other, mEtOtherInfo.getText().toString());
        map.put(NetWorkMethod.origin, sourceCode);
        map.put(NetWorkMethod.rank, level);
        map.put(NetWorkMethod.fromToRoom, fromToRoom);

        showDialog();
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissDialog();
            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, Object.class);
                if (jsReturn.isSuccess()) {
                    MethodsExtra.toast(mContext, jsReturn.getMsg());
                    setResult(MyConstant.REFRESH);
                    finish();
                } else {
                    MethodsExtra.toast(mContext, jsReturn.getMsg());
                }
            }
        });
    }
    
    /**************************判断号码是否重复后  隐藏dialog框****************************/
    private void checkPhoneNum(String tel) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(NetWorkMethod.phone, tel);
        URL = NetWorkConstant.PORT_URL + NetWorkMethod.checkMpNo;
        //显示dialog
        showDialog();
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissDialog();
            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, JSONObject.class);
                if (jsReturn.isSuccess()) {
                    isSameTel = 1;
                } else {
                    isSameTel = 2;
                    MethodsExtra.toast(mContext, jsReturn.getMsg());
                }
            }
        });
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

    /**************************检测信息是否录入完整****************************/
    private void checkIsFinish() {
        boolean isFinish = true;
        if (mEtCustormerName.getText() == null || mEtCustormerName.getText().toString().length() == 0) {
            isFinish = false;
        } /*else if (mStrTel == null) {
			isFinish = false;
		} */ else if (tv_source_addCustomer == null || tv_source_addCustomer.getText().toString().length() == 0) {
            isFinish = false;
        } else if (tv_level_addCustomer == null || tv_level_addCustomer.getText().toString().length() == 0) {
            isFinish = false;
        } else if (tv_fangxing_addCust == null || tv_fangxing_addCust.getText().toString().length() == 0) {
            isFinish = false;
        } else if (mTvCustormerType.getText() == null || mTvCustormerType.getText().toString().length() == 0) {
            isFinish = false;
        }/* else if (mEtCustormerNumber.getText() == null || mEtCustormerNumber.getText().toString().length() == 0) {//mEtCustormerNumber
			isFinish = false;
		} */ else if (mTvCustormerPlace.getText() == null || mTvCustormerPlace.getText().toString().length() == 0) {
            isFinish = false;
        } else if (mTvCustormerPianqu.getText() == null || mTvCustormerPianqu.getText().toString().length() == 0) {
            isFinish = false;
        } else if (mTvCustormerArea.getText() == null || mTvCustormerArea.getText().toString().length() == 0) {
            isFinish = false;
        } else if (mTvCustormerPrice.getText() == null || mTvCustormerPrice.getText().toString().length() == 0) {
            isFinish = false;
        }
		/*if(!(isMobileNO(mStrTel) || TextUtils.isEmpty(mStrTel))) {
			isFinish = false;
		}*/
        mSubmitView = MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
        if (isFinish) {
            mSubmitView.setClickable(true);
            mSubmitView.setEnabled(true);
        } else {
            mSubmitView.setClickable(false);
            mSubmitView.setEnabled(false);
        }
    }

    private void checkOpenOrClose(int intId) {
        if (intId != mRyltChoosePlaceContainer.getId()) {
            closePlaceContainer();
        }

        if (intId != mRyltChoosePianquContainer.getId()) {
            closePianquContainer();
        }

        if (intId != mRyltChoosePriceContaner.getId()) {
            closePriceContainer();
        }

        if (intId != mRyltChooseAreaContainer.getId()) {
            closeAreaContainer();
        }

		/*if (intId != mLlytConntectTypeContainer.getId()) {
			closeConnectionTypeContainer();
		}*/

        if (intId != mLlytTypeContainer.getId()) {
            closeTypeContainer();
        }
        if (intId != ll_source_addCustomer.getId()) {
            closeSourceContainer();
        }
        if (intId != ll_level_addCustomer.getId()) {
            closeLevelContainer();
        }
        if (intId != rl_fangxing_addCust.getId()) {
            closeFangXingContainer();
        }
    }


    private void closeFangXingContainer() {
        wv_choose_fangxing_addCustomer.setVisibility(View.GONE);
        cb_fangxing_addCustomer.setChecked(false);
    }

    private void closeSourceContainer() {
        wv_choose_source_addCustomer.setVisibility(View.GONE);
        cb_source_addCustomer.setChecked(false);
    }

    private void closeLevelContainer() {
        wv_choose_level_addCustomer.setVisibility(View.GONE);
        cb_level_addCustomer.setChecked(false);
    }

    private void closePlaceContainer() {
        mRyltChoosePlaceContainer.setVisibility(View.GONE);
        mImgPlaceRight.setBackgroundResource(R.drawable.h_manage_icon_down);
    }

    private void closePianquContainer() {
        mRyltChoosePianquContainer.setVisibility(View.GONE);
        mImgPianquRight.setBackgroundResource(R.drawable.h_manage_icon_down);
    }

    private void closePriceContainer() {
        mRyltChoosePriceContaner.setVisibility(View.GONE);
        mImgPriceRight.setBackgroundResource(R.drawable.h_manage_icon_down);
    }

    private void closeAreaContainer() {
        mRyltChooseAreaContainer.setVisibility(View.GONE);
        mImgAreaRight.setBackgroundResource(R.drawable.h_manage_icon_down);
    }

    private void closeTypeContainer() {
        mLlytTypeContainer.setVisibility(View.GONE);
        mImgTypeRight.setBackgroundResource(R.drawable.h_manage_icon_down);
    }


    public static boolean isMobileNO(String mobiles) {
        if (mobiles != null) {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0,7]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        }
        return false;
    }

    public void notifCallBack(String name, String className, Object data) {

    }

    public void setLoseFocus() {
		/*if(mStrTel!=null){
			if(isMobileNO(mStrTel) && !TextUtils.isEmpty(mStrTel)){
				if(mEtCustormerNumber.isFocusable()){
					mEtCustormerNumber.setFocusable(false);
				}
			}else{
				if(mStrTel.toString().trim()!=null&&mStrTel.toString().trim().length()>0){
					MethodsExtra.toast(mContext, "手机号码有误，请重新输入！");
					mEtCustormerNumber.setFocusable(true);
					mEtCustormerNumber.setFocusableInTouchMode(true);
				}
			}
		}*/
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_UP) {
        final int y= (int)event.getRawY();
        int totalHeight=y+MyUtils.dip2px(this,230);
        int phoneHeight = MyUtils.getHeight(this);
        if(totalHeight>=phoneHeight){
            scrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, y/2);
                }
            },100);
        }
            /*switch (event.getAction()) {
                case R.id.ll_source_addCustomer:
                break;
                case R.id.rlyt_areaBanner_addCustomerActivity:
                break;
            }*/
        }
        return false;
    }
}
