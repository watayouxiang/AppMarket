package com.example.admin.appmarket.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.protocol.RecmmondProtocol;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.LoadingPage;
import com.example.admin.appmarket.widget.randomLayout.StellarMap;

import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2016/3/23.
 */
public class RecommendFragment extends BaseFragment {

    private List<String> mData;

    @Override
    public View onSuccessedView() {
        StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        stellarMap.setAdapter(new MyAdapter());

        int padding = UIUtils.dip2px(10);
        stellarMap.setInnerPadding(padding, padding, padding, padding);

        //setRegularity的两个参数之必须等于getCount(),这里也就是15
        stellarMap.setRegularity(6, 9);

        //arg0: 从哪组动画开始执行
        stellarMap.setGroup(0, true);

        return stellarMap;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        RecmmondProtocol recmmondProtocol = new RecmmondProtocol();
        mData = recmmondProtocol.getData(0);
        return check(mData);
    }

    class MyAdapter implements StellarMap.Adapter {

        @Override
        public int getGroupCount() {
            //两组动画
            return 2;
        }

        @Override
        public int getCount(int group) {
            //标签的条数
            return 15;
        }

        @Override
        public View getView(int group, int position, View convertView) {
            TextView textView = new TextView(UIUtils.getContext());

            int red = 30 + new Random().nextInt(210);
            int green = 30 + new Random().nextInt(210);
            int blue = 30 + new Random().nextInt(210);
            int rgb = Color.rgb(red, green, blue);
            textView.setTextColor(rgb);

            float textSize = 10 + new Random().nextInt(17);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

            //判断一下请求网络返回数据的集合,避免角标越界
            if (mData.size() > 14) {
                textView.setText(mData.get(position));
            }

            return textView;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            // 不断的替换上一组展示效果
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            // 不断的替换下一组展示效果
            return 1;
        }
    }
}
