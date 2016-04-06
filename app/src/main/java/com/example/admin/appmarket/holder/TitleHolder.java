package com.example.admin.appmarket.holder;

import android.view.View;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.entity.CategoryInfo;
import com.example.admin.appmarket.util.UIUtils;

/**
 * Created by admin on 2016/4/6.
 */
public class TitleHolder extends BaseHolder<CategoryInfo> {

    public TitleHolder(CategoryInfo categoryInfo) {
        //因为新增的title条目没有setData(..),所以需要setData(..)
        setData(categoryInfo);
    }

    @Override
    public void refreshView() {
        CategoryInfo data = getData();
        View rootView = getRootView();

        TextView category_title_item_tv = (TextView) rootView.findViewById(R.id.category_title_item_tv);
        category_title_item_tv.setText(data.getTitle());
    }

    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_category_title);
    }
}
