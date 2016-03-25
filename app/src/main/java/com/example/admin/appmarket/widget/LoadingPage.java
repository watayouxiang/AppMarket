package com.example.admin.appmarket.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.manager.ThreadManager;
import com.example.admin.appmarket.util.UIUtils;

public abstract class LoadingPage extends FrameLayout {

    /**
     * 四种界面:
     * 正在加载 loadingView
     * 加载数据失败 errorView
     * 加载成功但是数据为空 emptyView
     * 加载成功且有数据 successedView
     */
    private View loadingView;
    private View errorView;
    private View emptyView;
    private View successedView;

    /**
     * 五种网络状态:
     * 准备加载 STATE_UNLOAD
     * 正在加载 STATE_LOADING
     * 加载错误 STATE_LOAD_ERROR
     * 加载成功但数据为空 STATE_LOAD_EMPTY
     * 加载成功且有数据 STATE_LOAD_SUCCESSED
     */
    private int STATE_UNLOAD = 0;
    private int STATE_LOADING = 1;
    private int STATE_LOAD_ERROR = 2;
    private int STATE_LOAD_EMPTY = 3;
    private int STATE_LOAD_SUCCESSED = 4;

    private int CURRENTSTATE = STATE_UNLOAD;
    private LayoutParams mLayoutParams;

    public LoadingPage(Context context) {
        super(context);
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        // 初始化4种界面
        initLoadingPage();
    }

    private void initLoadingPage() {
        if (loadingView == null) {
            loadingView = UIUtils.inflate(R.layout.layout_loading);
            addView(loadingView, mLayoutParams);
        }

        if (errorView == null) {
            errorView = UIUtils.inflate(R.layout.layout_error);
            addView(errorView, mLayoutParams);
        }

        if (emptyView == null) {
            emptyView = UIUtils.inflate(R.layout.layout_empty);
            addView(emptyView, mLayoutParams);
        }
        // 根据网络状态(CURRENTSTATE)决定显示哪一个界面(view)
        showSafePage();
    }

    // 在主线程中调用showPage()方法
    private void showSafePage() {
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                showPage();
            }
        });
    }

    // 根据网络状态(CURRENTSTATE)决定显示哪一个界面(view)
    protected void showPage() {
        if (loadingView != null) {
            loadingView.setVisibility((CURRENTSTATE == STATE_UNLOAD
                    || CURRENTSTATE == STATE_LOADING) ? View.VISIBLE : View.GONE);
        }

        if (errorView != null) {
            errorView.setVisibility(CURRENTSTATE == STATE_LOAD_ERROR ? View.VISIBLE : View.GONE);
        }

        if (emptyView != null) {
            emptyView.setVisibility(CURRENTSTATE == STATE_LOAD_EMPTY ? View.VISIBLE : View.GONE);
        }

        // 加载成功页面的创建
        if (successedView == null && CURRENTSTATE == STATE_LOAD_SUCCESSED) {
            //加载成功的页面是未知的,所以抽象出来让具体子类去实现
            successedView = onCreateSuccessedView();
            addView(successedView, mLayoutParams);
        }

        if (successedView != null && CURRENTSTATE == STATE_LOAD_SUCCESSED) {
            successedView.setVisibility(View.VISIBLE);
        }
    }

    // 获取网络请求状态CURRENTSTATE,然后根据网络状态显示不同的view
    public void show() {
        //状态归位
        if (CURRENTSTATE == STATE_LOAD_SUCCESSED
                || CURRENTSTATE == STATE_LOAD_ERROR || CURRENTSTATE == STATE_LOAD_EMPTY) {
            CURRENTSTATE = STATE_UNLOAD;
        }

        if (CURRENTSTATE == STATE_UNLOAD) {
            //网络请求过程
            //网络请求过程必须走子线程
            Thread thread = new Thread() {
                public void run() {
                    //请求网络操作是未知的,所以抽象出来让具体子类去实现(url都不一样),onLoad()方法
                    //由onLoad()方法返回网络请求状态
                    //网络请求状态只可能是(成功,为空,失败)其中一种,所以定义枚举ResultState
                    final ResultState loadState = onLoad();
                    if (loadState != null) {
                        CURRENTSTATE = loadState.getState();
                    }

                    //根据网络请求状态决定显示哪一个界面
                    //UI操作,必须走主线程
                    showSafePage();
                }
            };

            //用线程池来管理子线程
            ThreadManager.getThreadProxyPool().execute(thread);
        }
    }

    //网络请求操作的结果只可能是(成功,为空,失败)其中一种,所以定义枚举ResultState
    public enum ResultState {
        STATE_ERROR(2),
        STATE_EMPTY(3),
        STATE_SUCCESSED(4);

        private int state;

        private ResultState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

    //请求网络操作是未知的,所以抽象出来让具体子类去实现
    public abstract ResultState onLoad();

    //加载成功的页面是未知的,所以抽象出来让具体子类去实现
    public abstract View onCreateSuccessedView();

}
