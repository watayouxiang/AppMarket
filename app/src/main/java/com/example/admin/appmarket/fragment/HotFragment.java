package com.example.admin.appmarket.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.protocol.HotProtocol;
import com.example.admin.appmarket.util.DrawableUtil;
import com.example.admin.appmarket.util.ToastUtils;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.FlowLayout;
import com.example.admin.appmarket.widget.LoadingPage;

import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2016/3/23.
 */
public class HotFragment extends BaseFragment {

    private List<String> mData;

    @Override
    public View onSuccessedView() {
        Context context = UIUtils.getContext();
        ScrollView scrollView = new ScrollView(context);
        int padding = UIUtils.dip2px(10);
        scrollView.setPadding(padding, padding, padding, padding);

        FlowLayout flowLayout = new FlowLayout(context);
        flowLayout.setHorizontalSpacing(UIUtils.dip2px(6));
        flowLayout.setVerticalSpacing(UIUtils.dip2px(10));

        for (int i = 0; i < mData.size(); i++) {
            final TextView textView = new TextView(context);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);

            int textPadding = UIUtils.dip2px(10);
            textView.setPadding(textPadding, textPadding, textPadding, textPadding);
            textView.setText(mData.get(i));

            //设置背景色
            int red = 30 + new Random().nextInt(210);
            int green = 30 + new Random().nextInt(210);
            int blue = 30 + new Random().nextInt(210);
            int rgb = Color.rgb(red, green, blue);
            Drawable normalDrawable = DrawableUtil.getDrawable(rgb, UIUtils.dip2px(6));//没选中的图片
            Drawable pressDrawable = DrawableUtil.getDrawable(0xffcecece, UIUtils.dip2px(6));//选中的图片
            StateListDrawable stateListDrawable = DrawableUtil.getStateListDrawable(pressDrawable, normalDrawable);
            textView.setBackgroundDrawable(stateListDrawable);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShortToast(textView.getText().toString());
                }
            });

            flowLayout.addView(textView);
        }

        scrollView.addView(flowLayout);
        return scrollView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        HotProtocol hotProtocol = new HotProtocol();
        mData = hotProtocol.getData(0);
        return check(mData);
    }
}
