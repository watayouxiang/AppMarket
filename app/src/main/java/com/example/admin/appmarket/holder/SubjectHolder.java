package com.example.admin.appmarket.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.entity.SubjectInfo;
import com.example.admin.appmarket.http.HttpHelper;
import com.example.admin.appmarket.util.BitmapHelpr;
import com.example.admin.appmarket.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class SubjectHolder extends BaseHolder<SubjectInfo> {

    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_subject_item);
    }

    @Override
    public void refreshView() {
        SubjectInfo data = getData();
        View rootView = getRootView();

        ImageView subject_item_iv = (ImageView) rootView.findViewById(R.id.subject_item_iv);
        TextView subject_item_tv = (TextView) rootView.findViewById(R.id.subject_item_tv);

        subject_item_tv.setText(data.getDes());
        //三级缓存
        BitmapUtils bitmapUtils = BitmapHelpr.getBitmapUtils(UIUtils.getContext());
        bitmapUtils.display(subject_item_iv, HttpHelper.URL + "image?name=" + data.getUrl());
    }
}
