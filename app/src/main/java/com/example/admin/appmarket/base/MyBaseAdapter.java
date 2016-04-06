package com.example.admin.appmarket.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.admin.appmarket.holder.MoreHolder;
import com.example.admin.appmarket.manager.ThreadManager;
import com.example.admin.appmarket.util.UIUtils;

import java.util.List;

/**
 * Created by admin on 2016/3/24.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    /** 存放数据的集合 */
    public List<T> mList;
    /** 一般条目的Holder */
    private BaseHolder mHolder;
    /** 更多条目的Holder */
    private MoreHolder mMoreHolder;
    private int LOAD_MORE = 0;
    private int LIST_ITEM = 1;

    public MyBaseAdapter(List<T> list) {
        this.mList = list;
    }

    //两种Type的Item
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    //Item的类型有LOAD_MORE和LIST_ITEM两种
    @Override
    public int getItemViewType(int position) {
        if (getCount() - 1 == position) {
            return LOAD_MORE;
        } else {
            return getInnerType(position);
        }
    }

    public int getInnerType(int position) {
        return LIST_ITEM;
    }

    @Override
    public int getCount() {
        return mList.size() + 1;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //1, 将listview的条目转化成view对象
            //2, 创建hoder对象
            //3, 给holder中的字段赋值
            //4, 给view对象设置Tag
            if (getItemViewType(position) == LOAD_MORE) {
                mHolder = getMoreHolder();
            } else {
                mHolder = getHolder();
            }
        } else {
            mHolder = (BaseHolder) convertView.getTag();
        }
        //5, 将数据设置到view的控件上
        if (getItemViewType(position) == LIST_ITEM) {
            mHolder.setData(mList.get(position));
        }

        //6, 返回view对象
        return mHolder.getRootView();
    }

    private BaseHolder getMoreHolder() {
        if (mMoreHolder == null) {
            mMoreHolder = new MoreHolder(hasMore(), this);
        }
        return mMoreHolder;
    }

    //是否开开启加载更多, 默认开启
    public boolean hasMore() {
        return true;
    }

    //加载更多方法供给MoreHolder调用
    public void loadMore() {
        //请求网络加载更多是耗时操作
        ThreadManager.getThreadProxyPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<T> list = onLoadMore();

                //加载更多数据后刷新列表是UI操作
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list != null) {
                            if (list.size() == 20) {
                                mMoreHolder.setData(MoreHolder.HAS_MORE);
                            } else if (list.size() < 20) {
                                mMoreHolder.setData(MoreHolder.NO_MORE);
                            }
                        } else {
                            mMoreHolder.setData(MoreHolder.LOAD_MORE_ERROR);
                        }

                        if (list != null) {
                            mList.addAll(list);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    public int getListSize(){
        return mList.size();
    }

    //加载更多返回的数据未知,抽象给子类实现
    public abstract List<T> onLoadMore();

    public abstract BaseHolder getHolder();
}
