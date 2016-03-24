package com.example.admin.appmarket.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.util.UIUtils;

public abstract class LoadingPage extends FrameLayout {
    //正在加载view
    private View loadingView;
    //加载数据失败view
    private View errorView;
    //加载成功但是数据为空view
    private View emptyView;
    //加载成功且有数据view
    private View successedView;

    //页面请求网络的初始状态
    private int STATE_UNLOAD = 0;
    //正在加载
    private int STATE_LOADING = 1;
    //加载失败
    private int STATE_LOAD_ERROR = 2;
    private int STATE_LOAD_EMPTY = 3;
    private int STATE_LOAD_SUCCESSED = 4;

    //当前(请求网络)状态,决定添加在帧布局中那个view对象去做展示
    private int CURRENTSTATE = STATE_UNLOAD;
    private LayoutParams layoutParams;

    //初始状态

    public LoadingPage(Context context) {
        super(context);
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        //初始化4中类型界面
        initLoadingPage();
    }

    private void initLoadingPage() {
        //1,正在加载
        if (loadingView == null) {
            loadingView = UIUtils.inflate(R.layout.layout_loading);
            //添加到帧布局内部
            addView(loadingView, layoutParams);
        }
        //2,加载失败
        if (errorView == null) {
            errorView = UIUtils.inflate(R.layout.layout_error);
            addView(errorView, layoutParams);
        }
        //3,加载为空
        if (emptyView == null) {
            emptyView = UIUtils.inflate(R.layout.layout_empty);
            addView(emptyView, layoutParams);
        }
        //根据初始状态去决定哪个界面显示,哪个隐藏
        showSafePage();
    }

    //将展示界面的操作,封装到主线程中运行
    private void showSafePage() {
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                //根据当前状态去展示界面的方法
                showPage();
            }
        });
    }

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
        //添加获取数据成功的界面到帧布局中
        if (successedView == null && CURRENTSTATE == STATE_LOAD_SUCCESSED) {
            //构建成功的View对象,因为每一个Fragment中成功获取数据放置的界面效果都不一样,也就是不知道如果去构建这个successedView
            successedView = onCreateSuccessedView();
            addView(successedView, layoutParams);
        }

        if (successedView != null && CURRENTSTATE == STATE_LOAD_SUCCESSED) {
            successedView.setVisibility(View.VISIBLE);
        }
    }

    //只有在选中对应的Fragment对象的时候才去调用此请求网络方法

    //编写网络请求过程,然后获取请求结果,调用showPage方法,用于判断界面的显示隐藏
    public void show() {
        //状态归位
        if (CURRENTSTATE == STATE_LOAD_SUCCESSED
                || CURRENTSTATE == STATE_LOAD_ERROR || CURRENTSTATE == STATE_LOAD_EMPTY) {
            CURRENTSTATE = STATE_UNLOAD;
        }

        if (CURRENTSTATE == STATE_UNLOAD) {
            //网络请求过程
            new Thread() {
                public void run() {
                    //每一个Fragment的请求网络操作,都在此处做触发
                    //url 参数  请求 对于每一个Fragment而言,都不一致,所以没法在此次做具体实现,所以抽象
                    //返回请求网络最终得到的状态
                    final ResultState onLoad = onLoad();
                    //将获取网络请求状态的过程封装到主线程中,根据此状态决定页面的展示UI操作情况
                    UIUtils.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (onLoad != null) {
                                CURRENTSTATE = onLoad.getState();
                            }
                            //在主线程中,调用根据状态决定显示界面的方法
                            showPage();
                        }
                    });
                }

                ;
            }.start();
        }
    }

    //请求网络的抽象方法
    public abstract ResultState onLoad();

    //未知的构建成功界面的过程,方法留给具体的Fragment子类界面去实现
    public abstract View onCreateSuccessedView();

    //定义一个枚举,去圈定请求网络的返回状态只能是(成功,为空,失败)其中一种
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
}
