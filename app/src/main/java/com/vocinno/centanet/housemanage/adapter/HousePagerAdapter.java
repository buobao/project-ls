package com.vocinno.centanet.housemanage.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vocinno.centanet.R;
import com.vocinno.centanet.model.Image;

import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class HousePagerAdapter extends PagerAdapter {
    private List<ImageView> imageList;
    private List<Image> imageUrlList;
    private Context context;

    public HousePagerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ImageView> list){
        imageList=list;
    }
    public void setUrlList(List<Image> list){
        imageUrlList=list;
    }
    // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
    @Override
    public int getCount() {
        return imageList==null?0:imageList.size();
    }
// 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
// PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView(imageList.get(position));
    }

// 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        String url=imageUrlList.get(position).getUrl();
        if (url!= null&&url.length()!= 0) {
            Glide.with(context).load(url).centerCrop()
                    .crossFade()
                    .error(R.drawable.default_img)
                    .into(imageList.get(position));
        }
        view.addView(imageList.get(position));
        return imageList.get(position);
    }
}
