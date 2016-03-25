package com.example.admin.appmarket.base;

import android.view.View;

/**
 * Created by admin on 2016/3/25.
 */
public abstract class BaseHolder<T> {

    private View mView;
    private T mData;

    //2, 创建一个holder对象
    public BaseHolder(){
        //1, 将ListView条目转换成view对象
        mView = initView();
        //4, 给view对象设置Tag
        mView.setTag(this);

    }

    //3, 给holder中的字段赋值
    public void setData(T data){
        this.mData = data;
        //将数据设置到view的控件上
        refreshView();
    }

    public T getData(){
        return mData;
    }

    public View getRootView(){
        return mView;
    }

    //将数据设置到view的控件上
    public abstract void refreshView();

    //将ListView条目转换成view对象
    public abstract View initView();

}
