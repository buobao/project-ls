package com.vocinno.centanet.customermanage;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.entity.ParamCustlookList;
import com.vocinno.centanet.entity.TCmLook;
import com.vocinno.centanet.entity.TCmLookAccompany;
import com.vocinno.centanet.entity.TCmLookHouse;
import com.vocinno.centanet.housemanage.FirstHandHouseActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.model.ChoosePeople;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.CustomUtils;
import com.vocinno.utils.MethodsExtra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;


/**
 * Created by hewei26 on 2016/6/16.
 * <p/>
 * 添加带看
 * 选择一手:跳转到"添加一手"  保存回传本页面
 * 添加二手:跳转到"房源列表"  点击条目跳转到"添加二手"  保存回传本页面
 */
public class AddAccompanyActivity extends OtherBaseActivity {

    @Bind(R.id.iv_type_first)        //选择一手房源
            ImageView mIvTypeFirst;
    @Bind(R.id.iv_type_second)       //选择二手房源
            ImageView mIvTypeSecond;
    @Bind(R.id.et_confirmNum)        //带看确认书编号
            EditText mEtConfirmNum;
    @Bind(R.id.tv_startTime)         //开始时间
            TextView mTvStartTime;
    @Bind(R.id.iv_startTime)         //选择开始时间
            ImageView mIvStartTime;
    @Bind(R.id.tv_endTime)           //结束时间
            TextView mTvEndTime;
    @Bind(R.id.iv_endTime)           //选择结束时间
            ImageView mIvEndTime;
    @Bind(R.id.cb_write_back)         //是否回显
            CheckBox mCbWriteBack;
    @Bind(R.id.et_desc_house)         //文字描述
            EditText mEtDescHouse;
    @Bind(R.id.tv_addHouse)           //添加房源
            TextView mTvAddHouse;
    @Bind(R.id.lv_secondhand_house)         //二手房源列表
            ListView mLvSecondhandHouse;

    private ImageView mBack;
    private TextView mSubmit;
    private String lookType = "20074001";    //房源类型  一手&二手

    private View dialogView;
    private WheelView wv_year, wv_month, wv_day, wv_hour, wv_min;
    private MyDialog dialog;
    private boolean isStartTime = false;
    private String dayText;
    private Long startTime, endTime;

    ParamCustlookList paramCustlookList = new ParamCustlookList(); //请求参数列表

    private List<TCmLookAccompany> mTCmLookAccompanies = new ArrayList<>(); //一手带看人列表
    private List<TCmLookHouse> mTCmLookHouses = new ArrayList<>();  //一手房源列表

    private List<TCmLookAccompany> mTCmLookAccompanies2 = new ArrayList<>(); //二手带看人列表
    private List<TCmLookHouse> mTCmLookHouses2 = new ArrayList<>();  //二手房源列表

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_add_accompany;
    }

    @Override
    public void initView() {
        //禁用侧滑
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置标题栏
        mBack = (ImageView) MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mSubmit = (TextView) MethodsExtra.findHeadRightView1(mContext, baseView, R.string.save, 0);
        MethodsExtra.findHeadTitle1(mContext, baseView, R.string.add_accompany, null);
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        //选择时间
        dialogView = getLayoutInflater().inflate(R.layout.time_dialog, null);
        wv_year = (WheelView) dialogView.findViewById(R.id.wv_year);
        wv_month = (WheelView) dialogView.findViewById(R.id.wv_month);
        wv_day = (WheelView) dialogView.findViewById(R.id.wv_day);
        wv_hour = (WheelView) dialogView.findViewById(R.id.wv_hour);
        wv_min = (WheelView) dialogView.findViewById(R.id.wv_min);

        mIvTypeFirst.setOnClickListener(this);
        mIvTypeSecond.setOnClickListener(this);
        mTvAddHouse.setOnClickListener(this);
        mIvStartTime.setOnClickListener(this);
        mIvEndTime.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //二手 : 封装数据到请求体
        String delCode = intent.getStringExtra("delCode");
        String addr = intent.getStringExtra("addr");
        Long houseId = intent.getLongExtra("houseId",-1);
        TCmLookHouse tCmLookHouse = new TCmLookHouse();
        tCmLookHouse.setHouseId(houseId);
        tCmLookHouse.setHouAddr(addr);
        tCmLookHouse.setHousedelCode(delCode);
        mTCmLookHouses2.add(0,tCmLookHouse);

        ChoosePeople people = (ChoosePeople) intent.getSerializableExtra(MyConstant.peiKan);
        String accompanyPromise = intent.getStringExtra("isManager");
        TCmLookAccompany tCmLookAccompany = new TCmLookAccompany();
        tCmLookAccompany.setAccompanyGroup(people.getOrgId());
        tCmLookAccompany.setAccompanyPromise(accompanyPromise);
        tCmLookAccompany.setAccompanyUser(people.getUserId());
        tCmLookAccompany.setAccompanyRole(people.getJobCode());
        tCmLookAccompany.setAccompanyName(people.getRealName());
        mTCmLookAccompanies2.add(0,tCmLookAccompany);

    }

    @Override
    public void initData() {
        Log.i("","");
;    }

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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_type_first:    //选择一手房源
                mIvTypeFirst.setImageResource(R.drawable.c_manage_button_choose);
                mIvTypeSecond.setImageResource(R.drawable.c_manage_button_unselected);
                lookType = "20074002";
                break;
            case R.id.iv_type_second:   //选择二手房源
                mIvTypeSecond.setImageResource(R.drawable.c_manage_button_choose);
                mIvTypeFirst.setImageResource(R.drawable.c_manage_button_unselected);
                lookType = "20074001";
                break;
            case R.id.tv_addHouse:      //添加房源
                if (lookType == "20074002") {
                    //添加一手 -->回传本页面
                    intent = new Intent(this, FirstHandHouseActivity.class);
                    startActivityForResult(intent, MyConstant.REQUEST_ADDFIRST);
                } else if (lookType == "20074001") {
                    //房源列表 --> 添加二手 --> 回传本页面
                    intent = new Intent(this, HouseManageActivity.class);
                    intent.putExtra(MyConstant.isIntoHouseDetail, 1);
                    startActivity(intent);
                }
                break;
            case R.id.iv_startTime:     //选择开始时间
                wheelViewSetData();
                if (dialog == null) {
                    dialog = new MyDialog(this);
                }
                isStartTime = true;
                dialogView.findViewById(R.id.bt_cancel).setOnClickListener(this);
                dialogView.findViewById(R.id.bt_submit).setOnClickListener(this);
                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(false);
                setDialogFullWidth();
                dialog.show();
                break;
            case R.id.iv_endTime:       //选择结束时间
                wheelViewSetData();
                if (dialog == null) {
                    dialog = new MyDialog(this);
                }
                isStartTime = false;
                dialogView.findViewById(R.id.bt_cancel).setOnClickListener(this);
                dialogView.findViewById(R.id.bt_submit).setOnClickListener(this);
                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(false);
                setDialogFullWidth();
                dialog.show();
                break;
            case R.id.bt_submit:        //确定选择时间
                setDate();
                break;
            case R.id.bt_cancel:        //取消选择时间
                dialog.dismiss();
                break;
            case R.id.img_left_mhead1:  //Back
                finish();
                break;
            case R.id.tv_right_mhead1:  //保存
                //显示Loading
                Loading.show(this);
                String custCode = getIntent().getStringExtra(MyConstant.custCode);  //客户编码
                String mConfirmationNumber = mEtConfirmNum.getText().toString();   //带看确认书编号
                String startTime = mTvStartTime.getText().toString();   //开始时间
                String endTime = mTvEndTime.getText().toString();       //结束时间
                String custlookTrackType = mCbWriteBack.isChecked() ? "1" : "0";    //是否回写 0&1
                String mRemark = mEtDescHouse.getText().toString();   //文字描述
                //页面内容(必传)
                TCmLook tCmLook = new TCmLook();
                tCmLook.setRemark(mRemark);
                tCmLook.setStartTime(startTime);
                tCmLook.setEndTime(endTime);
                tCmLook.setLookType(lookType);
                tCmLook.setCustCode(custCode);
                tCmLook.setConfirmationNumber(mConfirmationNumber);
                //设置请求参数
                paramCustlookList.settCmLook(tCmLook);
                paramCustlookList.setCustlookTrackType(custlookTrackType); //是否回写:"0"&"1"
                if(lookType == "20074002"){         //一手
                    mTCmLookHouses.get(0).setFeedback(mConfirmationNumber+mRemark);
                    paramCustlookList.settCmLookHouseList(mTCmLookHouses);
                    paramCustlookList.settCmLookAccompanyList(mTCmLookAccompanies);
                }else if (lookType == "20074001"){  //二手
                    mTCmLookHouses2.get(0).setFeedback(mConfirmationNumber+mRemark);
                    paramCustlookList.settCmLookHouseList(mTCmLookHouses2);
                    paramCustlookList.settCmLookAccompanyList(mTCmLookAccompanies2);
                }

                URL = NetWorkConstant.PORT_URL + NetWorkMethod.custLookAdd;
                OkHttpClientManager.postJsonAsyn(URL, paramCustlookList, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Loading.dismissLoading();
                    }

                    @Override
                    public void onResponse(String response) {
                        //隐藏Loading
                        Loading.dismissLoading();
                        MyUtils.LogI("------", response.toString());

                        //返回客户信息页面
                        setResult(MyConstant.accompanyCode);
                        finish();
                    }
                });
                break;
        }
    }

    /************************** 添加一手回传 **************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MyConstant.RESULT_ADDFIRST) {
            //封装数据到请求体
            TCmLookHouse tCmLookHouse = (TCmLookHouse) data.getSerializableExtra(MyConstant.addFirstHouse);
            mTCmLookHouses.add(0,tCmLookHouse);
            ChoosePeople people = (ChoosePeople) data.getSerializableExtra(MyConstant.peiKan);
            String accompanyPromise = data.getStringExtra("isManager");
            TCmLookAccompany tCmLookAccompany = new TCmLookAccompany();
            tCmLookAccompany.setAccompanyGroup(people.getOrgId());
            tCmLookAccompany.setAccompanyPromise(accompanyPromise);
            tCmLookAccompany.setAccompanyUser(people.getUserId());
            tCmLookAccompany.setAccompanyRole(people.getJobCode());
            tCmLookAccompany.setAccompanyName(people.getRealName());
            mTCmLookAccompanies.add(0,tCmLookAccompany);
            //封装数据到ListView集合
            ArrayList<String> imgList = data.getStringArrayListExtra(MyConstant.imgPathList); //本地图片路径



        }
    }

    /*************************  设置时间  **************************/
    /**
     * WheelView选择时间
     */
    private void wheelViewSetData() {
        int wvWidth = (CustomUtils.getWindowWidth(this) - 120) / 5;
        final int wvWidth2 = (CustomUtils.getWindowWidth(this) - 250) / 5;
        final Calendar c = Calendar.getInstance();

        wv_year.setWvWidth(wvWidth);
        wv_year.setData(getYear(), (CustomUtils.getWindowWidth(this) - 150) / 5);
        wv_year.setSelectItem(1);

        wv_month.setWvWidth(wvWidth2);
        wv_month.setData(getMonth(), (CustomUtils.getWindowWidth(this) - 150) / 5);
        wv_month.setSelectText((c.get(Calendar.MONTH) + 1) + "", 0);

        wv_day.setWvWidth(wvWidth2);
        wv_day.setData(getDay(wv_year.getSelectedText(), wv_month.getSelectedText()), wvWidth2);
        wv_day.setSelectText(c.get(Calendar.DAY_OF_MONTH) + "", 0);
        dayText = c.get(Calendar.DAY_OF_MONTH) + "";

        wv_hour.setWvWidth(wvWidth2);
        wv_hour.setData(getHour(), wvWidth2);
        wv_hour.setSelectText(c.get(Calendar.HOUR_OF_DAY) + "", 0);

        wv_min.setWvWidth(wvWidth2);
        wv_min.setData(getMin(), wvWidth2);
        if (c.get(Calendar.MINUTE) <= 9) {
            wv_min.setSelectText("0" + c.get(Calendar.MINUTE), 0);
        } else {
            wv_min.setSelectText(c.get(Calendar.MINUTE) + "", 0);
        }
        wv_year.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (wv_month.getSelectedText().equals("2")) {
                    wv_day.setData(getDay(text, wv_month.getSelectedText()), wvWidth2);
                    int y = Integer.parseInt(text);
                    if (Integer.parseInt(dayText) <= 28) {
                        wv_day.setSelectText(dayText, 0);

                    } else {
                        if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
                            wv_day.setSelectText(dayText, 0);
                        } else {
                            wv_day.setSelectText("28", 0);
                            dayText = "28";
                        }
                    }
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        wv_month.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                wv_day.setData(getDay(wv_year.getSelectedText(), text), wvWidth2);
                int m = Integer.parseInt(text);
                int y = Integer.parseInt(wv_year.getSelectedText());
                if (Integer.parseInt(dayText) <= 28) {
                    wv_day.setSelectText(dayText, 0);
                } else {
                    if (m == 2) {
                        if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
                            if (Integer.parseInt(dayText) > 29) {
                                wv_day.setSelectText("29", 0);
                                dayText = "29";
                            } else {
                                wv_day.setSelectText(dayText, 0);
                            }
                        } else {
                            wv_day.setSelectText("28", 0);
                            dayText = "28";
                        }
                    } else if (m == 4 || m == 6 || m == 9 || m == 11) {
                        if (Integer.parseInt(dayText) > 30) {
                            wv_day.setSelectText("30", 0);
                            dayText = "30";
                        } else {
                            wv_day.setSelectText(dayText, 0);
                        }
                    } else {
                        wv_day.setSelectText(dayText, 0);
                    }
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        wv_day.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                dayText = text;
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
    }

    /**
     * 设置时间到界面
     */
    public Date setDate() {
        String year = wv_year.getSelectedText();
        String month = wv_month.getSelectedText();
        String day = wv_day.getSelectedText();
        String hour = wv_hour.getSelectedText();
        String min = wv_min.getSelectedText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date parseDate = sdf.parse(year + "-" + month + "-" + day + " " + hour + ":" + min);
            String dateFormat = sdf.format(parseDate);
            if (isStartTime) {

                if (CompareTimeSize(parseDate.getTime())) {
                    mTvStartTime.setText(dateFormat);
                    startTime = parseDate.getTime();
                    Log.i("startTime=========", "startTime" + startTime);
                    //iv_start_time_clear.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            } else {
                if (CompareTimeSize(parseDate.getTime())) {
                    mTvEndTime.setText(dateFormat);
                    endTime = parseDate.getTime();
                    //iv_end_time_clear.setVisibility(View.VISIBLE);
                    Log.i("endTime=========", "endTime" + endTime);
                    dialog.dismiss();
                }
            }
            return parseDate;
        } catch (ParseException e) {
            e.printStackTrace();
            dialog.dismiss();
            return null;
        }
    }

    /**
     * 比较开始和结束时间
     */
    private boolean CompareTimeSize(long time) {
        if (isStartTime) {
            if (endTime != null) {//选择开始时间并且之前已经选了结束时间
                if (endTime - time <= 0) {
                    MethodsExtra.toast(this, "开始时间应小于结束时间");
                    return false;
                }
            }
        } else {
            if (startTime != null) {//选择结束时间并且之前已经选了开始时间
                if (time - startTime <= 0) {
                    MethodsExtra.toast(this, "结束时间应大于开始时间");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 设置Dialog宽度 = 屏幕宽度
     */
    private void setDialogFullWidth() {
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }


    /**
     * 选择年 月 日 时 分
     */
    public ArrayList<String> getYear() {
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        ArrayList<String> list = new ArrayList<String>();
        list.add(mYear + 1 + "");
        list.add(mYear + "");
        list.add(mYear - 1 + "");
        list.add(mYear - 2 + "");
        list.add(mYear - 3 + "");
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        Log.i("=====", "mDay" + mDay);
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        return list;
    }

    public ArrayList<String> getMonth() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            list.add(i + "");
        }
        return list;
    }

    public ArrayList<String> getDay(String year, String month) {
        ArrayList<String> list = new ArrayList<String>();
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        for (int i = 1; i <= 28; i++) {
            list.add(i + "");
        }
        if (m == 2) {
            if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
                list.add("29");
            }
        } else if (m == 4 || m == 6 || m == 9 || m == 11) {
            list.add("29");
            list.add("30");
        } else {
            list.add("29");
            list.add("30");
            list.add("31");
        }
        return list;
    }

    public ArrayList<String> getHour() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= 24; i++) {
            list.add(i + "");
        }
        return list;
    }

    public ArrayList<String> getMin() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i <= 59; i++) {
            if (i <= 9) {
                list.add("0" + i);
            } else {
                list.add(i + "");
            }
        }
        return list;
    }

}
