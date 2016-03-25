package com.example.admin.appmarket.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.adapter.MyBaseAdapter;
import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/23.
 */
public class HomeFragment extends BaseFragment {

    private List<String> mArrayList;

    @Override
    public View onSuccessedView() {
        ListView listView = new ListView(UIUtils.getContext());
        mArrayList = new ArrayList<String>();
        for(int i=0;i<100;i++){
            mArrayList.add("Data "+ i);
        }
        listView.setAdapter(new MyAdapter(mArrayList));

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = UIUtils.inflate(R.layout.layout_test_item);
                holder = new ViewHolder();
                holder.tv = (TextView) convertView.findViewById(R.id.tv);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv.setText(mList.get(position));
            return convertView;
        }
    }

    class ViewHolder{
        TextView tv;
    }

}
