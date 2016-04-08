package com.example.admin.appmarket.protocol;

import com.example.admin.appmarket.base.BaseProtocol;
import com.example.admin.appmarket.entity.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/4/7.
 */
public class DetailProtocol extends BaseProtocol<AppInfo> {

    private String packageName;

    @Override
    public String getKey() {
        return "detail";
    }

    @Override
    public String getParams() {
        return "&packageName=" + packageName;
    }

    @Override
    public AppInfo parseJson(String result) {
        if(result==null){
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(result);
            AppInfo appInfo = new AppInfo();
            appInfo.setDes(jsonObject.getString("des"));
            appInfo.setDownloadUrl(jsonObject.getString("downloadUrl"));
            appInfo.setIconUrl(jsonObject.getString("iconUrl"));
            appInfo.setId(jsonObject.getInt("id"));
            appInfo.setName(jsonObject.getString("name"));
            appInfo.setPackageName(jsonObject.getString("packageName"));
            appInfo.setSize(jsonObject.getInt("size"));
            appInfo.setStars((float)jsonObject.getDouble("stars"));

            appInfo.setAuthor(jsonObject.getString("author"));
            appInfo.setDownloadNum(jsonObject.getString("downloadNum"));
            appInfo.setVersion(jsonObject.getString("version"));
            appInfo.setDate(jsonObject.getString("date"));

            appInfo.getScreenList().clear();
            if(jsonObject.has("screen")){
                JSONArray jsonArray = jsonObject.getJSONArray("screen");
                for(int i=0;i<jsonArray.length();i++){
                    appInfo.getScreenList().add(jsonArray.getString(i));
                }
            }

            appInfo.getSafeUrlList().clear();
            appInfo.getSafeDesUrlList().clear();
            appInfo.getSafeDesList().clear();
            if(jsonObject.has("safe")){
                JSONArray jsonArray = jsonObject.getJSONArray("safe");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                    appInfo.getSafeUrlList().add(jsonObject2.getString("safeUrl"));
                    appInfo.getSafeDesUrlList().add(jsonObject2.getString("safeDesUrl"));
                    appInfo.getSafeDesList().add(jsonObject2.getString("safeDes"));
                }
            }
            return appInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
