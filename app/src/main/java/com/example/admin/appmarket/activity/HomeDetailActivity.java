package com.example.admin.appmarket.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseActivity;
import com.example.admin.appmarket.entity.AppInfo;
import com.example.admin.appmarket.holder.DetailInfoHolder;
import com.example.admin.appmarket.holder.DetailSafeHolder;
import com.example.admin.appmarket.protocol.DetailProtocol;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;


/**
 * Created by admin on 2016/4/7.
 */
public class HomeDetailActivity extends BaseActivity {

    private String packageName;
    private AppInfo mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        packageName = getIntent().getStringExtra("packageName");

        LoadingPage loadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public ResultState onLoad() {
                return HomeDetailActivity.this.onLoad();
            }

            @Override
            public View onCreateSuccessedView() {
                return HomeDetailActivity.this.onCreateSuccessedView();
            }
        };

        setContentView(loadingPage);

        if (loadingPage != null) {
            loadingPage.show();
        }
    }

    private View onCreateSuccessedView() {
        View view = UIUtils.inflate(R.layout.layout_detail);

        FrameLayout app_info_fl = (FrameLayout) view.findViewById(R.id.app_info_fl);
        FrameLayout app_safe_fl = (FrameLayout) view.findViewById(R.id.app_safe_fl);
        HorizontalScrollView app_pic_hsv = (HorizontalScrollView) view.findViewById(R.id.app_pic_hsv);
        FrameLayout app_des_fl = (FrameLayout) view.findViewById(R.id.app_des_fl);

        DetailInfoHolder detailInfoHolder = new DetailInfoHolder();
        detailInfoHolder.setData(mData);
        app_info_fl.addView(detailInfoHolder.getRootView());

        DetailSafeHolder detailSafeHolder = new DetailSafeHolder();
        detailSafeHolder.setData(mData);
        app_safe_fl.addView(detailSafeHolder.getRootView());

        return view;
    }

    private LoadingPage.ResultState onLoad() {
        DetailProtocol detailProtocol = new DetailProtocol();
        detailProtocol.setPackageName(packageName);
        mData = detailProtocol.getData(0);

        if (mData == null) {
            return LoadingPage.ResultState.STATE_ERROR;
        } else {
            return LoadingPage.ResultState.STATE_SUCCESSED;
        }
    }
}

