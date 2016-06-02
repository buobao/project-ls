package com.vocinno.centanet.tools.photo.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.vocinno.centanet.R;
import com.vocinno.centanet.tools.MyUtils;

import java.util.ArrayList;

/**
 * PhotoWall中GridView的适配器
 *
 * @author hanj
 */

public class PhotoWallAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList = null;
    private int selectCount;
    //记录是否被选择
    private SparseBooleanArray selectionMap;
    private int imgWidth;

    public PhotoWallAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;
        selectionMap = new SparseBooleanArray();
    }
    public void clearList(){
        selectCount=0;
        selectionMap.clear();
    }
    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String filePath = (String) getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_wall_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.photo_wall_item_photo);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.photo_wall_item_cb);
            imgWidth = MyUtils.px2dip(context, MyUtils.getWidth(context));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RelativeLayout.LayoutParams para= (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
        para.height = imgWidth;
        para.width = imgWidth;
        holder.imageView.setLayoutParams(para);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = !selectionMap.get(position);
                if(flag){
                    if(selectCount>=9){
                        MyUtils.showToast(context,"最多选择9张图片");
                        return;
                    }
                    selectCount++;
                }else{
                    selectCount--;
                }
                selectionMap.put(position,flag);
                holder.checkBox.setChecked(selectionMap.get(position));
                if (selectionMap.get(position)) {
                    holder.imageView.setColorFilter(context.getResources().getColor(R.color.image_checked_bg));
                } else {
                    holder.imageView.setColorFilter(null);
                }
                notifyDataSetChanged();
            }
        });
        holder.checkBox.setChecked(selectionMap.get(position));
        if(selectionMap.get(position)){
            holder.imageView.setColorFilter(context.getResources().getColor(R.color.image_checked_bg));
        }else{
            holder.imageView.setColorFilter(null);
        }
//        holder.imageView.setMaxWidth(imgWidth);
       /* //tag的key必须使用id的方式定义以保证唯一，否则会出现IllegalArgumentException.
        holder.checkBox.setTag(R.id.tag_first, position);
        holder.checkBox.setTag(R.id.tag_second, holder.imageView);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Integer position = (Integer) buttonView.getTag(R.id.tag_first);
                ImageView image = (ImageView) buttonView.getTag(R.id.tag_second);

                selectionMap.put(position, isChecked);
                if (isChecked) {
                    image.setColorFilter(context.getResources().getColor(R.color.image_checked_bg));
                } else {
                    image.setColorFilter(null);
                }
            }
        });

        holder.checkBox.setChecked(selectionMap.get(position));
        holder.imageView.setTag(filePath);*/
//        loader.loadImage(4, filePath, holder.imageView);
        Glide.with(context).load(filePath).centerCrop().crossFade().into(holder.imageView);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }

    public SparseBooleanArray getSelectionMap() {
        return selectionMap;
    }

    public void clearSelectionMap() {
        selectionMap.clear();
    }
}
