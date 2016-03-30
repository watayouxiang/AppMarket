package com.example.admin.appmarket.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.appmarket.base.MyBaseAdapter;
import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.holder.HomeHolder;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/23.
 */
public class HomeFragment extends BaseFragment {

    @Override
    public View onSuccessedView() {
        ListView listView = new ListView(UIUtils.getContext());
        List<String> list = new ArrayList<String>();
        for(int i=0;i<20;i++){
            list.add("Data "+ i);
        }
        listView.setAdapter(new MyAdapter(list));

        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        return LoadingPage.ResultState.STATE_SUCCESSED;
    }

    class MyAdapter extends MyBaseAdapter<String> {

        public MyAdapter(List<String> list) {
            super(list);
        }

        @Override
        public List<String> onLoadMore() {
            List<String> list = new ArrayList<String>();
            for(int i=0;i<20;i++){
                list.add("More "+ i);
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
