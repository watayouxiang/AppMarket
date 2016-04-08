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

public class DetailInfoHolder extends BaseHolder<AppInfo> {
    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_detail_info);
    }

    @Override
    public void refreshView() {
        AppInfo data = getData();
        View rootView = getRootView();

        TextView app_data_tv = (TextView) rootView.findViewById(R.id.app_data_tv);
        TextView app_download_tv = (TextView) rootView.findViewById(R.id.app_download_tv);
        TextView app_version_tv = (TextView) rootView.findViewById(R.id.app_version_tv);
        TextView app_size_tv = (TextView) rootView.findViewById(R.id.app_size_tv);
        ImageView app_icon_iv = (ImageView) rootView.findViewById(R.id.app_icon_iv);
        TextView app_name_tv = (TextView) rootView.findViewById(R.id.app_name_tv);
        RatingBar app_star = (RatingBar) rootView.findViewById(R.id.app_star);

        app_data_tv.setText(data.getDate());
        app_download_tv.setText(data.getDownloadNum());
        app_version_tv.setText(data.getVersion());
        app_size_tv.setText(data.getSize() + "");
        app_name_tv.setText(data.getName());
        app_star.setRating(data.getStars());
        BitmapUtils bitmapUtils = BitmapHelpr.getBitmapUtils(UIUtils.getContext());
        bitmapUtils.display(app_icon_iv, HttpHelper.URL + "image?name=" + data.getIconUrl());
    }
}
