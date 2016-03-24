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

    /**
     * 由BaseFragment去统一管理没一个子类Fragment的界面显示
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1,正在加载view
        // 2,加载数据失败view
        // 3,加载成功但是数据为空view
        // 4,加载成功且有数据view
        // LoadingPage继承至FrameLayout,默认将多种界面展示情况添加至此帧布局中,
        // 后期通过具体的网络请求结果对应的状态,去维护那个界面显示以及隐藏

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

    /**
     * 子类的Fragment布局转换成view对象的抽象方法
     *
     * @return
     */
    public abstract View onSuccessedView();

    /**
     * 子类Fragment中请求网络的抽象方法
     *
     * @return
     */
    public abstract LoadingPage.ResultState onLoad();

    /**
     * 调用LoadingPage中的show方法
     */
    public void baseShow() {
        if (mLoadingPage != null) {
            mLoadingPage.show();
        }
    }
}
