package com.example.admin.appmarket.manager;

import android.content.Intent;
import android.net.Uri;

import com.example.admin.appmarket.entity.AppInfo;
import com.example.admin.appmarket.entity.DownloadInfo;
import com.example.admin.appmarket.http.HttpHelper;
import com.example.admin.appmarket.util.IOUtils;
import com.example.admin.appmarket.util.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 2016/4/12.
 */
public class DownloadManager {

    public static final int STATE_UNDOWNLOAD = 1;//未下载
    public static final int STATE_WAITTING = 2;//等待中
    public static final int STATE_DOWNLOAD = 3;//下载中
    public static final int STATE_DOWNLOAD_ERROR = 4;//下载失败
    public static final int STATE_DOWNLOADED = 5;//下载完成
    public static final int STATE_PAUSE = 6;//暂停

    private List<DownloadObserver> observerList = new ArrayList<DownloadObserver>();
    private Map<Integer,DownloadInfo> downloadInfoMap = new ConcurrentHashMap<Integer,DownloadInfo>();
    private Map<Integer,DownloadTask> downloadTaskMap = new ConcurrentHashMap<Integer, DownloadTask>();

    //单例模式
    private DownloadManager() {}
    private static DownloadManager downloadManager = new DownloadManager();
    public static DownloadManager getInstance() {
        return downloadManager;
    }

    public DownloadInfo getDownloadInfo(AppInfo appInfo){
        return downloadInfoMap.get(appInfo.getId());
    }

    public void download(AppInfo appInfo) {
        if (appInfo != null) {
            //将下载对象维护在downloadInfoMap中
            DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
            if (downloadInfo == null) {
                downloadInfo = DownloadInfo.clone(appInfo);
                downloadInfoMap.put(downloadInfo.getId(), downloadInfo);
            }
            downloadInfo.setCurrentState(STATE_WAITTING);
            notifyStateChange(downloadInfo);

            DownloadTask downloadTask = new DownloadTask(downloadInfo);
            ThreadManager.getThreadProxyPool().execute(downloadTask);
            //将下载任务维护在downloadTaskMap中
            downloadTaskMap.put(downloadInfo.getId(),downloadTask);
        }
    }

    private class DownloadTask implements Runnable{
        private DownloadInfo downloadInfo;
        private HttpHelper.HttpResult httpResult;
        private InputStream inputStream;
        private FileOutputStream fileOutputStream;

        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            downloadInfo.setCurrentState(STATE_DOWNLOAD);
            notifyStateChange(downloadInfo);

            //断点续传下载逻辑
            File file = new File(downloadInfo.getPath());
            if(!file.exists() //没下载过
                    || file.length()!=downloadInfo.getCurrentPosition() //下载失败
                    || downloadInfo.getCurrentPosition()==0//新建下载任务
                    ){//从头开始下载
                httpResult = HttpHelper.download(HttpHelper.URL + "download?name=" + downloadInfo.getDownloadUrl());

                //确保文件已经删除,并且下载位置归0
                file.delete();
                downloadInfo.setCurrentPosition(0);
            }else{//断点续传
                httpResult = HttpHelper.download(HttpHelper.URL+"download?name="+downloadInfo.getDownloadUrl()
                        +"&range="+downloadInfo.getCurrentPosition());
            }

            if(httpResult!=null && (inputStream=httpResult.getInputStream())!=null){//网络请求成功
                //下载
                try {
                    fileOutputStream = new FileOutputStream(file,true);
                    byte[] buffer = new byte[1024];
                    int temp = -1;
                    while ((temp = inputStream.read(buffer)) != -1 && downloadInfo.getCurrentState()==STATE_DOWNLOAD){
                        fileOutputStream.write(buffer, 0, temp);
                        fileOutputStream.flush();

                        //告知UI下载进度条发生变化了
                        downloadInfo.setCurrentPosition(downloadInfo.getCurrentPosition()+temp);
                        notifyProgressChange(downloadInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    downloadInfo.setCurrentState(STATE_DOWNLOAD_ERROR);
                    notifyStateChange(downloadInfo);

                    //确保文件已经删除,并且下载位置归0
                    file.delete();
                    downloadInfo.setCurrentPosition(0);
                }finally {
                    if(httpResult!=null){
                        httpResult.close();
                    }
                    IOUtils.close(inputStream);
                    IOUtils.close(fileOutputStream);
                }

                if(downloadInfo.getCurrentPosition()==downloadInfo.getSize()){
                    //完成下载
                    downloadInfo.setCurrentState(STATE_DOWNLOADED);
                    notifyStateChange(downloadInfo);
                }else if(downloadInfo.getCurrentState() == STATE_PAUSE){
                    //暂停
                    notifyStateChange(downloadInfo);
                }
            }else{//网络请求失败
                downloadInfo.setCurrentState(STATE_DOWNLOAD_ERROR);
                notifyStateChange(downloadInfo);

                //确保文件已经删除,并且下载位置归0
                file.delete();
                downloadInfo.setCurrentPosition(0);
            }

            //将下载任务从任务集合中移除
            downloadTaskMap.remove(downloadInfo.getId());
        }
    }

    public void pause(AppInfo appInfo){
        if(appInfo!=null){
            DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
            if(downloadInfo!=null){
                stopDownload(downloadInfo);
                downloadInfo.setCurrentState(STATE_PAUSE);
                notifyStateChange(downloadInfo);
            }
        }
    }

    public synchronized void install(AppInfo appInfo){
        DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
        stopDownload(downloadInfo);

        if(downloadInfo!=null){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://"+downloadInfo.getPath()),"application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);
        }
    }

    private void stopDownload(DownloadInfo downloadInfo) {
        if(downloadInfo!=null){
            DownloadTask downloadTask = downloadTaskMap.get(downloadInfo.getId());
            if(downloadTask!=null){
                //将下载任务从线程池中移除
                ThreadManager.getThreadProxyPool().cancel(downloadTask);
            }
        }
    }

    //告知状态发生改变
    public void notifyStateChange(DownloadInfo downloadInfo) {
        if (downloadInfo != null) {
            for (DownloadObserver downloadObserver : observerList) {
                downloadObserver.onDownloadStateChange(downloadInfo);
            }
        }
    }

    //告知进度条发生改变
    public void notifyProgressChange(DownloadInfo downloadInfo) {
        if (downloadInfo != null) {
            for (DownloadObserver downloadObserver : observerList) {
                downloadObserver.onDownloadProgressChange(downloadInfo);
            }
        }
    }

    public void registerObserver(DownloadObserver downloadObserver) {
        if (downloadObserver != null) {
            if (!observerList.contains(downloadObserver)) {
                observerList.add(downloadObserver);
            }
        }
    }

    public void unRegisterObserver(DownloadObserver downloadObserver) {
        if (downloadObserver != null) {
            if (observerList.contains(downloadObserver)) {
                observerList.remove(downloadObserver);
            }
        }
    }

    //观察者模式
    public interface DownloadObserver {
        public void onDownloadProgressChange(DownloadInfo downloadInfo);

        public void onDownloadStateChange(DownloadInfo downloadInfo);
    }
}
