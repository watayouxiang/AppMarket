package com.example.admin.appmarket.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.appmarket.base.MyBaseAdapter;
import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.entity.AppInfo;
import com.example.admin.appmarket.holder.HeaderHolder;
import com.example.admin.appmarket.holder.HomeHolder;
import com.example.admin.appmarket.protocol.HomeProtocol;
import com.example.admin.appmarket.util.LogUtils;
import com.example.admin.appmarket.util.ToastUtils;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/23.
 */
public class HomeFragment extends BaseFragment {

    private List<AppInfo> mAppInfoList;
    private HomeProtocol mHomeProtocol;

    @Override
    public View onSuccessedView() {
        ListView listView = new ListView(UIUtils.getContext());

        //首页添加轮播图的逻辑
        if (listView.getHeaderViewsCount() < 1) {
            //UI操作都放在Holder中做处理
            HeaderHolder headerHolder = new HeaderHolder(mHomeProtocol.getPicList());
            listView.addHeaderView(headerHolder.getRootView());
        }

        listView.setAdapter(new MyAdapter(mAppInfoList));
        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        mHomeProtocol = new HomeProtocol();
        mAppInfoList = mHomeProtocol.getData(0);
        return check(mAppInfoList);
    }

    class MyAdapter extends MyBaseAdapter<AppInfo> {

        public MyAdapter(List<AppInfo> list) {
            super(list);
        }

        @Override
        public List<AppInfo> onLoadMore() {
            HomeProtocol homeProtocol = new HomeProtocol();
            List<AppInfo> moreData = homeProtocol.getData(getListSize());
            return moreData;
        }

        @Override
        public BaseHolder getHolder() {
            return new HomeHolder();
        }
    }

}
