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

    @Override
    public View onSuccessedView() {
        ListView listView = new ListView(UIUtils.getContext());

        List<String> list = new ArrayList<String>();
        for (AppInfo appInfo : mAppInfoList) {
            list.add(appInfo.getName());
        }

        listView.setAdapter(new MyAdapter(list));

        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        HomeProtocol homeProtocol = new HomeProtocol();
        mAppInfoList = homeProtocol.getData(0);

        return LoadingPage.ResultState.STATE_SUCCESSED;
    }

    class MyAdapter extends MyBaseAdapter<String> {

        public MyAdapter(List<String> list) {
            super(list);
        }

        @Override
        public List<String> onLoadMore() {
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < 19; i++) {
                list.add("More " + i);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        public BaseHolder getHolder() {
            return new HomeHolder();
        }
    }

}
