package com.example.admin.appmarket.protocol;

import com.example.admin.appmarket.base.BaseProtocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class RecmmondProtocol extends BaseProtocol<List<String>> {

    @Override
    public String getKey() {
        return "recommend";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public List<String> parseJson(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            List<String> recommendList = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                recommendList.add(jsonArray.getString(i));
            }
            return recommendList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
