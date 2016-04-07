package com.example.admin.appmarket.protocol;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ListView;

import com.example.admin.appmarket.base.BaseProtocol;
import com.example.admin.appmarket.entity.SubjectInfo;

public class SubjectProtocol extends BaseProtocol<List<SubjectInfo>> {

    @Override
    public String getKey() {
        return "subject";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public List<SubjectInfo> parseJson(String result) {
        if (result == null) {
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            List<SubjectInfo> subjectList = new ArrayList<SubjectInfo>();
            subjectList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                SubjectInfo subjectInfo = new SubjectInfo();
                subjectInfo.setDes(jsonObject.getString("des"));
                subjectInfo.setUrl(jsonObject.getString("url"));

                subjectList.add(subjectInfo);
            }
            return subjectList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
