package com.example.admin.appmarket.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;

import java.util.List;

/**
 * Fragment生命周期:
 * onAttach -> onCreate -> onCreateView -> onActivityCreated(创建)
 * onStart(可见)
 * onResume(获取焦点)
 * onPause(失去焦点)
 * onStop(不可见)
 * onDestroyView -> onDestroy -> onDetach(销毁)
 */
public abstract class BaseFragment extends Fragment {

    private LoadingPage mLoadingPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 由BaseFragment去统一管理每一个子类Fragment的界面显示
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // LoadingPage继承至FrameLayout,默认将多种界面展示情况添加至此帧布局中,
        // 后期通过具体的网络请求结果状态,去维护哪个界面显示以及隐藏
        mLoadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public ResultState onLoad() {
                return BaseFragment.this.onLoad();
            }

            @Override
            public View onCreateSuccessedView() {
                return BaseFragment.this.onSuccessedView();
            }
        };

        return mLoadingPage;
    }

    // 调用LoadingPage中的show方法
    // mLoadingPage.show();方法会根据不同网络状态显示不同的界面
    public void baseShow() {
        if (mLoadingPage != null) {
            mLoadingPage.show();
        }
    }

    // 根据服务器返回的数据判断返回的状态
    public LoadingPage.ResultState check(Object data) {
        if (data != null && data instanceof List) {
            if (((List) data).size() > 0) {
                return LoadingPage.ResultState.STATE_SUCCESSED;
            } else if (((List) data).size() == 0) {
                return LoadingPage.ResultState.STATE_EMPTY;
            } else {
                return LoadingPage.ResultState.STATE_ERROR;
            }
        } else {
            return LoadingPage.ResultState.STATE_ERROR;
        }
    }

    //加载成功的页面是未知的,所以抽象出来让具体子类去实现
    public abstract View onSuccessedView();

    //请求网络操作是未知的,所以抽象出来让具体子类去实现
    //因为LoadingPage中的onLoad方法走的是子线程,所以这里走的也是子线程
    public abstract LoadingPage.ResultState onLoad();

}
