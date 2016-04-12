package com.example.admin.appmarket.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.entity.AppInfo;
import com.example.admin.appmarket.entity.DownloadInfo;
import com.example.admin.appmarket.manager.DownloadManager;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.ProgressHorizontal;

/**
 * Created by admin on 2016/4/12.
 */
public class DownloadHolder extends BaseHolder<AppInfo> implements View.OnClickListener,DownloadManager.DownloadObserver {

    private ProgressHorizontal progressHorizontal;
    private Button download_bt;
    private int state;
    private float progress;
    private DownloadManager downloadManager;
    private AppInfo data;

    @Override
    public void setData(AppInfo data) {
        downloadManager = DownloadManager.getInstance();
        DownloadInfo downloadInfo = downloadManager.getDownloadInfo(data);
        if(downloadInfo == null){
            state = DownloadManager.STATE_UNDOWNLOAD;
            progress = 0;
        }else{
            state = downloadInfo.getCurrentState();
            progress = downloadInfo.getProgress();
        }

        super.setData(data);
    }

    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_detail_download);
    }

    @Override
    public void refreshView() {
        View rootView = getRootView();
        data = getData();

        download_bt = (Button) rootView.findViewById(R.id.download_bt);

        //获取放置进度条的自定义控件所在的帧布局
        FrameLayout download_fl = (FrameLayout) rootView.findViewById(R.id.download_fl);
        //水平递增进度条,文字自定义控件
        progressHorizontal = new ProgressHorizontal(UIUtils.getContext());
        //中间要显示文字
        progressHorizontal.setProgressTextVisible(true);
        //中间文字要白色
        progressHorizontal.setProgressTextColor(Color.WHITE);
        //告知中间文字的大小
        progressHorizontal.setProgressTextSize(UIUtils.dip2px(16));
        //设置自定义控件进度条在递增过程中的蓝色前景
        progressHorizontal.setProgressResource(R.drawable.progress_normal);
        //设置自定义控件的背景
        progressHorizontal.setBackgroundResource(R.drawable.progress_bg);
        //背景就是控件的大小
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        download_fl.addView(progressHorizontal, layoutParams);

        download_bt.setOnClickListener(this);
        download_fl.setOnClickListener(this);

        //将状态以及进度条更新到底部帧布局放置的自定义控件中
        refreshMainThreadUI(state,progress);
    }

    private void refreshMainThreadUI(int state, float progress) {
        //为了后续调用此方法的时候,对应两个参数的值会有变化
        this.state = state;
        this.progress = progress;

        switch (state) {
            case DownloadManager.STATE_UNDOWNLOAD:
                download_bt.setVisibility(View.VISIBLE);
                progressHorizontal.setVisibility(View.GONE);
                download_bt.setText(UIUtils.getString(R.string.app_state_download));
                break;
            case DownloadManager.STATE_WAITTING:
                download_bt.setVisibility(View.VISIBLE);
                progressHorizontal.setVisibility(View.GONE);
                download_bt.setText(UIUtils.getString(R.string.app_state_waiting));
                break;
            case DownloadManager.STATE_DOWNLOAD_ERROR:
                download_bt.setVisibility(View.VISIBLE);
                progressHorizontal.setVisibility(View.GONE);
                download_bt.setText(UIUtils.getString(R.string.app_state_error));
                break;
            case DownloadManager.STATE_DOWNLOADED:
                download_bt.setVisibility(View.VISIBLE);
                progressHorizontal.setVisibility(View.GONE);
                download_bt.setText(UIUtils.getString(R.string.app_state_downloaded));
                break;

            case DownloadManager.STATE_DOWNLOAD:
                progressHorizontal.setVisibility(View.VISIBLE);
                download_bt.setVisibility(View.GONE);
                //自定义控件调用此方法不仅维护蓝色进度条递增,还维护中间展示百分比的文字
                progressHorizontal.setProgress(progress);
                //此处不需要刻意维护中间显示文字,以上有做处理
                progressHorizontal.setCenterText("");
                break;
            case DownloadManager.STATE_PAUSE:
                progressHorizontal.setVisibility(View.VISIBLE);
                download_bt.setVisibility(View.GONE);
                //自定义控件调用此方法不仅维护蓝色进度条递增,还维护中间展示百分比的文字
                progressHorizontal.setProgress(progress);
                //此处不需要刻意维护中间显示文字,以上有做处理
                progressHorizontal.setCenterText("暂停");
                break;
        }
    }

    public void registerObserver() {
        downloadManager.registerObserver(this);
    }

    @Override
    public void onDownloadProgressChange(DownloadInfo downloadInfo) {
        refreshUI(downloadInfo);
    }

    @Override
    public void onDownloadStateChange(DownloadInfo downloadInfo) {
        refreshUI(downloadInfo);
    }

    private void refreshUI(final DownloadInfo downloadInfo) {
        //UI操作需运行在主线程
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                refreshMainThreadUI(downloadInfo.getCurrentState(), downloadInfo.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_bt:
            case R.id.download_fl:
                if(state == DownloadManager.STATE_UNDOWNLOAD ||
                        state == DownloadManager.STATE_PAUSE || state == DownloadManager.STATE_DOWNLOAD_ERROR){
                    downloadManager.download(data);
                }else if(state == DownloadManager.STATE_WAITTING || state == DownloadManager.STATE_DOWNLOAD){
                    downloadManager.pause(data);
                }else if(state == DownloadManager.STATE_DOWNLOADED){
                    downloadManager.install(data);
                }
                break;
        }
    }
}
