package com.example.admin.appmarket.base;

import android.text.TextUtils;

import com.example.admin.appmarket.http.HttpHelper;
import com.example.admin.appmarket.util.IOUtils;
import com.example.admin.appmarket.util.ToastUtils;
import com.example.admin.appmarket.util.UIUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by admin on 2016/3/30.
 */
public abstract class BaseProtocol<T> {

    public T getData(int index) {

        //1.给缓存数据设置有效时长
        //2.从缓存中获取数据, 如果有数据并且没有超出有效时长, 展示出来
        //3.从缓存中获取数据, 如果没有数据, 请求数据
        //4.将数据进行json解析

        String data = getDataFromLocal(index);
        String result = null;
        if (!TextUtils.isEmpty(data)) {
            result = data;
        } else {
            result = getDataFromNet(index);
        }
        return parseJson(result);
    }

    /**
     * 从网络获取数据
     *
     * @param index
     * @return
     */
    private String getDataFromNet(int index) {
        //http://www.ooxx.com/index.jsp?index=1&name=12312&psd=fdfsa
        //getKey(): index.jsp
        //getParams(): &name=12312&psd=fdfsa
        HttpHelper.HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey() + "?index=" + index + getParams());
        String result = httpResult.getString();
        if (!TextUtils.isEmpty(result)) {
            writeToLocal(result, index);
        }
        return result;
    }

    /**
     * 缓存数据
     *
     * @param result
     * @param index
     */
    private void writeToLocal(String result, int index) {
        BufferedWriter bufferedWriter = null;
        File cacheDir = UIUtils.getContext().getCacheDir();
        File file = new File(cacheDir, getKey() + index + getParams());
        // 数据有效设置成30min
        Long validTime = System.currentTimeMillis() + 30 * 60 * 1000;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            bufferedWriter.write(validTime + "\r\n");
            bufferedWriter.write(result.toCharArray());
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bufferedWriter);
        }
    }

    /**
     * 从本地获取数据
     *
     * @param index
     * @return
     */
    private String getDataFromLocal(int index) {
        BufferedReader bufferedReader = null;
        File cacheDir = UIUtils.getContext().getCacheDir();

        //http://www.ooxx.com/index.jsp?index=1&name=12312&psd=fdfsa
        //index.jsp + index=1 + &name=12312&psd=fdfsa
        File file = new File(cacheDir, getKey() + index + getParams());

        try {
            bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String firstLine = bufferedReader.readLine();
            Long validTime = Long.valueOf(firstLine);
            if (System.currentTimeMillis() < validTime) { //数据并且没有超出有效时长
                StringBuffer stringBuffer = new StringBuffer();
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuffer.append(temp);
                }
                return stringBuffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bufferedReader);
        }
        return null;
    }

    //http://www.ooxx.com/index.jsp?index=1&name=12312&psd=fdfsa
    //index.jsp
    public abstract String getKey();

    //http://www.ooxx.com/index.jsp?index=1&name=12312&psd=fdfsa
    //&name=12312&psd=fdfsa
    public abstract String getParams();

    /**
     * 解析缓存数据(String -> json)
     *
     * @param result
     * @return
     */
    public abstract T parseJson(String result);
}
