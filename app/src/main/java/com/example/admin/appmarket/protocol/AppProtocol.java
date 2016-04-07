package com.example.admin.appmarket.protocol;

import com.example.admin.appmarket.base.BaseProtocol;
import com.example.admin.appmarket.entity.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/4/5.
 */
public class AppProtocol extends BaseProtocol<List<AppInfo>> {
    @Override
    public String getKey() {
        return "app";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public List<AppInfo> parseJson(String result) {
        if (result == null) {
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            List<AppInfo> appInfoList = new ArrayList<AppInfo>();
            appInfoList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AppInfo appInfo = new AppInfo();
                appInfo.setDes(jsonObject.getString("des"));
                appInfo.setDownloadUrl(jsonObject.getString("downloadUrl"));
                appInfo.setIconUrl(jsonObject.getString("iconUrl"));
                appInfo.setId(jsonObject.getInt("id"));
                appInfo.setName(jsonObject.getString("name"));
                appInfo.setPackageName(jsonObject.getString("packageName"));
                appInfo.setSize(jsonObject.getInt("size"));
                appInfo.setStars((float) jsonObject.getDouble("stars"));
                appInfoList.add(appInfo);
            }
            return appInfoList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
