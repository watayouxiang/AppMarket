package com.example.admin.appmarket.fragment;

import android.view.View;
import android.widget.ListView;

import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.base.MyBaseAdapter;
import com.example.admin.appmarket.entity.AppInfo;
import com.example.admin.appmarket.holder.AppHolder;
import com.example.admin.appmarket.protocol.AppProtocol;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;

import java.util.List;

/**
 * Created by admin on 2016/3/23.
 */
public class AppFragment extends BaseFragment {

    private List<AppInfo> mData;

    @Override
    public View onSuccessedView() {
        ListView listView = new ListView(UIUtils.getContext());
        listView.setAdapter(new MyAdapter(mData));
        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        AppProtocol appProtocol = new AppProtocol();
        mData = appProtocol.getData(0);
        return check(mData);
    }

    class MyAdapter extends MyBaseAdapter{

        public MyAdapter(List list) {
            super(list);
        }

        @Override
        public List onLoadMore() {
            AppProtocol appProtocol = new AppProtocol();
            List<AppInfo> moreData = appProtocol.getData(getListSize());
            return moreData;
        }

        @Override
        public BaseHolder getHolder() {
            return new AppHolder();
        }
    }
}
