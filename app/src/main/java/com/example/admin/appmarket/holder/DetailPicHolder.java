package com.example.admin.appmarket.holder;

import android.view.View;
import android.widget.ImageView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.entity.AppInfo;
import com.example.admin.appmarket.http.HttpHelper;
import com.example.admin.appmarket.util.BitmapHelpr;
import com.example.admin.appmarket.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by admin on 2016/4/11.
 */
public class DetailPicHolder extends BaseHolder<AppInfo>{
    @Override
    public void refreshView() {
        View rootView = getRootView();
        AppInfo data = getData();

        ImageView[] imageViews = new ImageView[5];
        imageViews[0] = (ImageView) rootView.findViewById(R.id.image1);
        imageViews[1] = (ImageView) rootView.findViewById(R.id.image2);
        imageViews[2] = (ImageView) rootView.findViewById(R.id.image3);
        imageViews[3] = (ImageView) rootView.findViewById(R.id.image4);
        imageViews[4] = (ImageView) rootView.findViewById(R.id.image5);

        BitmapUtils bitmapUtils = BitmapHelpr.getBitmapUtils(UIUtils.getContext());
        for(int i=0; i<5; i++){
            if(i<data.getScreenList().size()){
                imageViews[i].setVisibility(View.VISIBLE);
                bitmapUtils.display(imageViews[i], HttpHelper.URL+"image?name="+data.getScreenList().get(i));
            }else{
                imageViews[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_detail_pic);
    }
}
