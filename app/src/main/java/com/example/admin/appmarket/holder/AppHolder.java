package com.example.admin.appmarket.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.entity.AppInfo;
import com.example.admin.appmarket.http.HttpHelper;
import com.example.admin.appmarket.util.BitmapHelpr;
import com.example.admin.appmarket.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by admin on 2016/4/5.
 */
public class AppHolder extends BaseHolder<AppInfo> {

    //将数据设置到view的控件上
    @Override
    public void refreshView() {
        AppInfo data = getData();
        View rootView = getRootView();

        TextView app_name_tv = (TextView) rootView.findViewById(R.id.app_name_tv);
        ImageView app_icon_iv = (ImageView) rootView.findViewById(R.id.app_icon_iv);
        TextView app_size_tv = (TextView) rootView.findViewById(R.id.app_size_tv);
        TextView app_des_tv = (TextView) rootView.findViewById(R.id.app_des_tv);
        RatingBar app_star_bar = (RatingBar) rootView.findViewById(R.id.app_star_bar);

        app_name_tv.setText(data.getName());
        app_size_tv.setText(data.getSize() + "");
        app_star_bar.setRating(data.getStars());
        app_des_tv.setText(data.getDes());

        //加载一张网络图片(三级缓存(1,内存(维护了LRU算法的Map<imgUrl,图片Bitmap对象>)2,本地文件缓存(文件名称唯一性url地址))3,网络)
        //XUtils中BitmapUtils维护了一个三级缓存的对象
        //因为每new一个XUtils对象就申请了一个100M的内存空间,所以XUtils对象要设置成单例
        BitmapUtils bitmapUtils = BitmapHelpr.getBitmapUtils(UIUtils.getContext());
        bitmapUtils.display(app_icon_iv, HttpHelper.URL + "image?name=" + data.getIconUrl());
        //http://127.0.0.1:8090/image?name=app/com.youyuan.yyhl/icon.jpg

    }

    //将ListView条目转换成view对象
    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_home_item);
    }
}
