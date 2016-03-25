package com.example.admin.appmarket.holder;

import android.view.View;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.util.UIUtils;

/**
 * Created by admin on 2016/3/25.
 */
public class HomeHolder extends BaseHolder<String> {

    //将数据设置到view的控件上
    @Override
    public void refreshView() {
        String data = getData();
        View rootView = getRootView();

        TextView tv = (TextView) rootView.findViewById(R.id.tv);
        tv.setText(data);

//        ((TextView)getRootView().findViewById(R.id.tv)).setText(getData());
    }

    //将ListView条目转换成view对象
    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_test_item);
    }
}
