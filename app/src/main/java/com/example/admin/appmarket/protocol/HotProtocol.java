package com.example.admin.appmarket.protocol;

import com.example.admin.appmarket.base.BaseProtocol;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/4/5.
 */
public class HotProtocol extends BaseProtocol<List<String>> {

    private List<String> hotList = new ArrayList<String>();

    @Override
    public String getKey() {
        return "hot";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public List<String> parseJson(String result) {
        if (result == null) {
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            hotList.clear();
            for(int i=0;i<jsonArray.length();i++){
                hotList.add(jsonArray.getString(i));
            }
            return hotList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
