package com.example.admin.appmarket.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by admin on 2016/3/24.
 */
public abstract class MyBaseAdapter <T> extends BaseAdapter{

    public List<T> mList;
    private BaseHolder mHolder;

    public MyBaseAdapter(List<T> list){
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            //1, 将listview的条目转化成view对象
            //2, 创建hoder对象
            //3, 给holder中的字段赋值
            //4, 给view对象设置Tag
            mHolder = getHolder();
        }else{
            mHolder = (BaseHolder) convertView.getTag();
        }
        //5, 将数据设置到view的控件上
        mHolder.setData(mList.get(position));

        //6, 返回view对象
        return mHolder.getRootView();
    };

    public abstract BaseHolder getHolder();
}
