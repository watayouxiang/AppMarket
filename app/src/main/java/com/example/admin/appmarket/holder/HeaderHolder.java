package com.example.admin.appmarket.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.http.HttpHelper;
import com.example.admin.appmarket.util.BitmapHelpr;
import com.example.admin.appmarket.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/4/6.
 */
public class HeaderHolder extends BaseHolder<List<String>> {

    private List<String> mData;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private List<View> mViewList = new ArrayList<View>();

    public HeaderHolder(List<String> picList) {
        //将数据设置给BaseHolder, 后面getData()才有值
        setData(picList);
    }

    @Override
    public View initView() {
        //创建一个RelativeLayout对象
        AbsListView.LayoutParams absParams = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                UIUtils.getDimenPx(R.dimen.list_header_height));
        mRelativeLayout = new RelativeLayout(UIUtils.getContext());
        mRelativeLayout.setLayoutParams(absParams);

        //创建一个ViewPager对象, 并添加到RelativeLayout中
        mViewPager = new ViewPager(UIUtils.getContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mRelativeLayout.addView(mViewPager, rlParams);

        return mRelativeLayout;
    }

    @Override
    public void refreshView() {
        mData = getData();

        mViewPager.setAdapter(new MyPagerAdapter());

        /** 设添加轮播图指示器 */
        //创建一个linearLayout对象, 并添加到mRelativeLayout
        LinearLayout linearLayout = new LinearLayout(UIUtils.getContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mRelativeLayout.addView(linearLayout, rlParams);

        mViewList.clear();
        linearLayout.removeAllViews();
        for (int i = 0; i < mData.size(); i++) {
            View view = new View(UIUtils.getContext());
            if (i == 0) {
                view.setBackgroundResource(R.mipmap.indicator_selected);
            } else {
                view.setBackgroundResource(R.mipmap.indicator_normal);
            }

            //创建view对象, 并添加到linearLayout
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                    UIUtils.dip2px(6),
                    UIUtils.dip2px(6));
            llParams.setMargins(0, 0, UIUtils.dip2px(6), UIUtils.dip2px(6));
            linearLayout.addView(view, llParams);

            mViewList.add(view);
        }

        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setCurrentItem(1000 * mData.size());
        new RunnableTask().start();
    }

    class RunnableTask implements Runnable{
        public void start(){
            UIUtils.removeCallBack(this);
            UIUtils.postDelayed(this,1000);
        }

        @Override
        public void run() {
            int position = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(position+1);

            //移除前一个任务,再添加后一个任务
            UIUtils.removeCallBack(this);
            UIUtils.postDelayed(this,1000);
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int index = position % mData.size();
            for (int i = 0; i < mViewList.size(); i++) {
                View view = mViewList.get(i);
                if (i == index) {
                    view.setBackgroundResource(R.mipmap.indicator_selected);
                } else {
                    view.setBackgroundResource(R.mipmap.indicator_normal);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
//            return mData.size();
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(UIUtils.getContext());
            BitmapUtils bitmapUtils = BitmapHelpr.getBitmapUtils(UIUtils.getContext());
            bitmapUtils.display(imageView, HttpHelper.URL + "image?name=" + mData.get(position % mData.size()));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

            //这句代码就是抛出异常, 所以不需要调用
            //super.destroyItem(container, position, object);
        }
    }
}
