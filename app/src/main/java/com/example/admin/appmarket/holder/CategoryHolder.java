package com.example.admin.appmarket.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.entity.CategoryInfo;
import com.example.admin.appmarket.http.HttpHelper;
import com.example.admin.appmarket.util.BitmapHelpr;
import com.example.admin.appmarket.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by admin on 2016/4/6.
 */
public class CategoryHolder extends BaseHolder<CategoryInfo> {
    @Override
    public void refreshView() {
        CategoryInfo data = getData();
        View rootView = getRootView();

        TextView category_item_tv1 = (TextView) rootView.findViewById(R.id.category_item_tv1);
        TextView category_item_tv2 = (TextView) rootView.findViewById(R.id.category_item_tv2);
        TextView category_item_tv3 = (TextView) rootView.findViewById(R.id.category_item_tv3);
        ImageView category_item_iv1 = (ImageView) rootView.findViewById(R.id.category_item_iv1);
        ImageView category_item_iv2 = (ImageView) rootView.findViewById(R.id.category_item_iv2);
        ImageView category_item_iv3 = (ImageView) rootView.findViewById(R.id.category_item_iv3);

        category_item_tv1.setText(data.getName1());
        category_item_tv2.setText(data.getName2());
        category_item_tv3.setText(data.getName3());
        BitmapUtils bitmapUtils = BitmapHelpr.getBitmapUtils(UIUtils.getContext());
        bitmapUtils.display(category_item_iv1, HttpHelper.URL+"image?name="+data.getUrl1());
        bitmapUtils.display(category_item_iv2, HttpHelper.URL+"image?name="+data.getUrl2());
        bitmapUtils.display(category_item_iv3, HttpHelper.URL+"image?name="+data.getUrl3());
    }

    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_category_item);
    }
}
