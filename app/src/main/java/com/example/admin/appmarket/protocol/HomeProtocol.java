package com.example.admin.appmarket.protocol;

import com.example.admin.appmarket.base.BaseProtocol;
import com.example.admin.appmarket.entity.AppInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/31.
 */
public class HomeProtocol extends BaseProtocol<List<AppInfo>> {

    private List<AppInfo> mAppInfoList = new ArrayList<AppInfo>();
    private List<String> mPicList = new ArrayList<String>();

    public List<String> getPicList() {
        return mPicList;
    }

    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public List<AppInfo> parseJson(final String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("list")) {
                mAppInfoList.clear();
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    AppInfo appInfo = new AppInfo();
                    appInfo.setDes(jsonObject2.getString("des"));
                    appInfo.setDownloadUrl(jsonObject2.getString("downloadUrl"));
                    appInfo.setIconUrl(jsonObject2.getString("iconUrl"));
                    appInfo.setId(jsonObject2.getInt("id"));
                    appInfo.setName(jsonObject2.getString("name"));
                    appInfo.setPackageName(jsonObject2.getString("packageName"));
                    appInfo.setSize(jsonObject2.getInt("size"));
                    appInfo.setStars((float) jsonObject2.getDouble("stars"));
                    mAppInfoList.add(appInfo);
                }
            }

            if (jsonObject.has("picture")) {
                mPicList.clear();
                JSONArray jsonArray = jsonObject.getJSONArray("picture");
                for (int i = 0; i < jsonArray.length(); i++) {
                    mPicList.add(jsonArray.getString(i));
                }
            }

            return mAppInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
