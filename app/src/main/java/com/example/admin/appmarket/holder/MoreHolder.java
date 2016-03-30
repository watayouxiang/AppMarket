package com.example.admin.appmarket.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.base.MyBaseAdapter;
import com.example.admin.appmarket.util.UIUtils;

/**
 * Created by admin on 2016/3/29.
 * 加载更多Holder
 */
public class MoreHolder extends BaseHolder<Integer> {

    //加载更多Holder状态
    public static int HAS_MORE = 1;
    public static int NO_MORE = 2;
    public static int LOAD_MORE_ERROR = 3;

    private MyBaseAdapter mAdapter;
    private LinearLayout ll_load_more;
    private TextView tv_load_more_error;

    public <T> MoreHolder(boolean hasMore, MyBaseAdapter<T> adapter) {
        setData(hasMore ? HAS_MORE : NO_MORE);
        this.mAdapter = adapter;
    }

    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.load_more);
    }

    @Override
    public void refreshView() {
        //调用getRootView方法前必须调用完成initView的view的返回
        View view = getRootView();
        tv_load_more_error = (TextView) view.findViewById(R.id.tv_load_more_error);
        ll_load_more = (LinearLayout) view.findViewById(R.id.ll_load_more);

        Integer data = getData();
        if (data == HAS_MORE) {
            ll_load_more.setVisibility(View.VISIBLE);
            tv_load_more_error.setVisibility(View.GONE);
        }

        if (data == NO_MORE) {
            ll_load_more.setVisibility(View.GONE);
            tv_load_more_error.setVisibility(View.GONE);
        }

        if (data == LOAD_MORE_ERROR) {
            ll_load_more.setVisibility(View.GONE);
            tv_load_more_error.setVisibility(View.VISIBLE);
        }
    }

    //重写父类的getRootView方法,加载加载更多条目特有的加载更多数据的逻辑
    @Override
    public View getRootView() {
        if (getData() == HAS_MORE && mAdapter != null) {
            mAdapter.loadMore();
        }
        return super.getRootView();
    }
}
