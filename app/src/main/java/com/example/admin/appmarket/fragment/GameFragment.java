package com.example.admin.appmarket.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;

/**
 * Created by admin on 2016/3/23.
 */
public class GameFragment extends BaseFragment {
    @Override
    public View onSuccessedView() {
        TextView textView = new TextView(UIUtils.getContext());
        textView.setText("游戏");
        textView.setTextColor(UIUtils.getResources().getColor(R.color.text_color));
        return textView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        return LoadingPage.ResultState.STATE_SUCCESSED;
    }
}
