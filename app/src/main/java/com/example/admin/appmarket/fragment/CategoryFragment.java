package com.example.admin.appmarket.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.base.MyBaseAdapter;
import com.example.admin.appmarket.entity.CategoryInfo;
import com.example.admin.appmarket.holder.CategoryHolder;
import com.example.admin.appmarket.holder.TitleHolder;
import com.example.admin.appmarket.protocol.CategoryProtocol;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;

import java.util.List;

/**
 * Created by admin on 2016/3/23.
 */
public class CategoryFragment extends BaseFragment {

    private List<CategoryInfo> mData;

    @Override
    public View onSuccessedView() {
        ListView listView = new ListView(UIUtils.getContext());
        listView.setAdapter(new MyAdapter(mData));
        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        CategoryProtocol categoryProtocol = new CategoryProtocol();
        mData = categoryProtocol.getData(0);
        return check(mData);
    }

    class MyAdapter extends MyBaseAdapter<CategoryInfo> {

        private int currentPosition;

        public MyAdapter(List<CategoryInfo> list) {
            super(list);
        }

        @Override
        public List<CategoryInfo> onLoadMore() {
            return null;
        }

        @Override
        public BaseHolder getHolder() {
            if(mList.get(currentPosition).isTitle()){
                //title类型Holder
                //因为新增的title条目没有setData(..),所以需要携带数据
                return new TitleHolder(mList.get(currentPosition));
            }else{
                //文字+图片Holder
                return new CategoryHolder();
            }
        }

        @Override
        public boolean hasMore() {
            //关闭加载更多
            return false;
        }

        /**
         * 条目总数
         */
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        /**
         * 当前索引对应的条目类型
         */
        @Override
        public int getInnerType(int position) {
            if (mList.get(position).isTitle()) {
                //title条目
                return super.getInnerType(position) + 1;
            } else {
                //文字+图片条目
                return super.getInnerType(position);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            this.currentPosition = position;
            return super.getView(position, convertView, parent);
        }
    }
}
