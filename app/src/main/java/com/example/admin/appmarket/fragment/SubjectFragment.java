package com.example.admin.appmarket.fragment;

import android.view.View;
import android.widget.ListView;

import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.base.MyBaseAdapter;
import com.example.admin.appmarket.entity.SubjectInfo;
import com.example.admin.appmarket.holder.SubjectHolder;
import com.example.admin.appmarket.protocol.SubjectProtocol;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;

import java.util.List;

/**
 * Created by admin on 2016/3/23.
 */
public class SubjectFragment extends BaseFragment {

    private List<SubjectInfo> mData;

    @Override
    public View onSuccessedView() {
        ListView listView = new ListView(UIUtils.getContext());
        listView.setAdapter(new MyAdapter(mData));
        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        SubjectProtocol subjectProtocol = new SubjectProtocol();
        mData = subjectProtocol.getData(0);
        return check(mData);
    }

    class MyAdapter extends MyBaseAdapter<SubjectInfo> {

        public MyAdapter(List<SubjectInfo> list) {
            super(list);
        }

        @Override
        public BaseHolder getHolder() {
            return new SubjectHolder();
        }

        @Override
        public List<SubjectInfo> onLoadMore() {
            SubjectProtocol subjectProtocol = new SubjectProtocol();
            List<SubjectInfo> moreData = subjectProtocol.getData(getListSize());
            return moreData;
        }
    }
}
