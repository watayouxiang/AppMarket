package com.example.admin.appmarket.protocol;

import com.example.admin.appmarket.base.BaseProtocol;
import com.example.admin.appmarket.entity.CategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/4/6.
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryInfo>> {

    private List<CategoryInfo> categoryInfoList = new ArrayList<CategoryInfo>();

    @Override
    public String getKey() {
        return "category";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public List<CategoryInfo> parseJson(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            categoryInfoList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("title")) {
                    CategoryInfo categoryInfo = new CategoryInfo();
                    categoryInfo.setTitle(jsonObject.getString("title"));
                    categoryInfo.setTitle(true);
                    categoryInfoList.add(categoryInfo);
                }

                if (jsonObject.has("infos")) {
                    JSONArray jsonArray2 = jsonObject.getJSONArray("infos");
                    for (int j = 0; j < jsonArray2.length(); j++) {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                        CategoryInfo categoryInfo = new CategoryInfo();
                        categoryInfo.setName1(jsonObject2.getString("name1"));
                        categoryInfo.setName2(jsonObject2.getString("name2"));
                        categoryInfo.setName3(jsonObject2.getString("name3"));
                        categoryInfo.setUrl1(jsonObject2.getString("url1"));
                        categoryInfo.setUrl2(jsonObject2.getString("url2"));
                        categoryInfo.setUrl3(jsonObject2.getString("url3"));
                        categoryInfoList.add(categoryInfo);
                    }
                }
            }
            return categoryInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
