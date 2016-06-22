package com.vocinno.centanet.housemanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.entity.TCmLookAccompany;
import com.vocinno.centanet.entity.TCmLookHouse;

import java.util.List;

/**
 * Created by hewei26 on 2016/6/21.
 */
public class SecondHandHouseAdapter extends BaseAdapter {

    private Context mContext;
    private List<TCmLookHouse> mLookHouses;
    List<TCmLookAccompany> mLookAccompanies;

    public SecondHandHouseAdapter(Context context, List<TCmLookHouse> tCmLookHouses2, List<TCmLookAccompany> tCmLookAccompanies2) {
        mContext = context;
        mLookHouses = tCmLookHouses2;
        mLookAccompanies = tCmLookAccompanies2;
    }

    @Override
    public int getCount() {
        return mLookHouses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_secondhand_house, null);
            holder.mIvDeleteSecond = (ImageView) convertView.findViewById(R.id.iv_delete_second);
            holder.mTvHouseId = (TextView)convertView.findViewById(R.id.tv_houseId);
            holder.mTvHouseAddr = (TextView)convertView.findViewById(R.id.tv_houseAddr);
            holder.mTvAccompanyPeople = (TextView) convertView.findViewById(R.id.tv_accompany_people);
            holder.mTvAccompanyPromise = (TextView) convertView.findViewById(R.id.tv_accompany_promise);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //取出对应的值
        TCmLookHouse tCmLookHouse = mLookHouses.get(position);
        TCmLookAccompany tCmLookAccompany = mLookAccompanies.get(position);
        //给控件赋值
        holder.mTvHouseId.setText(tCmLookHouse.getHouseId()+"");  //房源ID
        holder.mTvHouseAddr.setText(tCmLookHouse.getHouAddr());   //房源地址
        holder.mTvAccompanyPeople.setText(tCmLookAccompany.getAccompanyName());  //陪看人
        holder.mTvAccompanyPromise.setText(tCmLookAccompany.getAccompanyPromise().equals("0")?"否":"是"); //经理陪看
        //点击删除条目
        holder.mIvDeleteSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLookHouses.remove(position);
                mLookAccompanies.remove(position);
                notifyDataSetChanged(); //刷新列表
            }
        });
        return convertView;
    }


    static class ViewHolder{
        ImageView mIvDeleteSecond;
        TextView mTvHouseId;
        TextView mTvHouseAddr;
        TextView mTvAccompanyPeople;
        TextView mTvAccompanyPromise;
    }
}
