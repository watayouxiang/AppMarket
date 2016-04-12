package com.example.admin.appmarket.entity;

import android.os.Environment;

import com.example.admin.appmarket.manager.DownloadManager;

import java.io.File;

/**
 * Created by admin on 2016/4/12.
 */
public class DownloadInfo {
    private int id;
    private int currentState;//当前状态
    private int currentPosition;//下载位置
    private int size;//总大小
    private String downloadUrl;//下载url
    private float progress;//进度条百分比
    private String path;//存储路径
    private String name;//下载完成后的名称

    private static String APPMARKET = "appmarket";
    private static String DOWNLOAD = "download";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public float getProgress() {
        if (size == 0) {
            return 0;
        }
        return (currentPosition + 0.0f) / size;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static DownloadInfo clone(AppInfo appInfo) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setId(appInfo.getId());
        downloadInfo.setDownloadUrl(appInfo.getDownloadUrl());
        downloadInfo.setSize(appInfo.getSize());
        downloadInfo.setName(appInfo.getName());
        downloadInfo.setCurrentPosition(0);
        downloadInfo.setCurrentState(DownloadManager.STATE_UNDOWNLOAD);
        downloadInfo.setProgress(0);

        String filePath = getFilePath();
        if (filePath != null) {
            downloadInfo.setPath(filePath + downloadInfo.getName() + ".apk");
        }

        return downloadInfo;
    }

    private static String getFilePath() {
        StringBuffer buffer = new StringBuffer();
        File sdCardPath = Environment.getExternalStorageDirectory().getAbsoluteFile();
        buffer.append(sdCardPath);
        buffer.append(File.separator);
        buffer.append(APPMARKET);
        buffer.append(File.separator);
        buffer.append(DOWNLOAD);
        buffer.append(File.separator);

        if (createFile(buffer.toString())) {
            return buffer.toString();
        } else {
            return null;
        }
    }

    private static boolean createFile(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }
}
