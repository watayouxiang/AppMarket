package com.example.admin.appmarket.holder;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.entity.AppInfo;
import com.example.admin.appmarket.http.HttpHelper;
import com.example.admin.appmarket.util.BitmapHelpr;
import com.example.admin.appmarket.util.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class DetailSafeHolder extends BaseHolder<AppInfo> {

    private boolean isOpen = false;
    private LayoutParams layoutParams;
    private LinearLayout ll_root;
    private ImageView arrow_iv;

    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_detail_safe);
    }

    @Override
    public void refreshView() {
        AppInfo data = getData();
        View rootView = getRootView();

        /** 初始化控件 */
        ImageView[] imageView = new ImageView[4];
        imageView[0] = (ImageView) rootView.findViewById(R.id.app_safe1);
        imageView[1] = (ImageView) rootView.findViewById(R.id.app_safe2);
        imageView[2] = (ImageView) rootView.findViewById(R.id.app_safe3);
        imageView[3] = (ImageView) rootView.findViewById(R.id.app_safe4);

        LinearLayout[] linearLayouts = new LinearLayout[4];
        linearLayouts[0] = (LinearLayout) rootView.findViewById(R.id.ll_des1);
        linearLayouts[1] = (LinearLayout) rootView.findViewById(R.id.ll_des2);
        linearLayouts[2] = (LinearLayout) rootView.findViewById(R.id.ll_des3);
        linearLayouts[3] = (LinearLayout) rootView.findViewById(R.id.ll_des4);

        ImageView[] imageIcon = new ImageView[4];
        imageIcon[0] = (ImageView) rootView.findViewById(R.id.app_des_url1);
        imageIcon[1] = (ImageView) rootView.findViewById(R.id.app_des_url2);
        imageIcon[2] = (ImageView) rootView.findViewById(R.id.app_des_url3);
        imageIcon[3] = (ImageView) rootView.findViewById(R.id.app_des_url4);

        TextView[] textViews = new TextView[4];
        textViews[0] = (TextView) rootView.findViewById(R.id.app_des1);
        textViews[1] = (TextView) rootView.findViewById(R.id.app_des2);
        textViews[2] = (TextView) rootView.findViewById(R.id.app_des3);
        textViews[3] = (TextView) rootView.findViewById(R.id.app_des4);

        LinearLayout ll_parent = (LinearLayout) rootView.findViewById(R.id.ll_parent);
        arrow_iv = (ImageView) rootView.findViewById(R.id.arrow_iv);
        ll_root = (LinearLayout) rootView.findViewById(R.id.ll_root);

        /** 初始化控件数据 */
        layoutParams = ll_root.getLayoutParams();
        layoutParams.height = 0;
        ll_root.setLayoutParams(layoutParams);

        BitmapUtils bitmapUtils = BitmapHelpr.getBitmapUtils(UIUtils.getContext());
        for (int i = 0; i < 4; i++) {
            if (i < data.getSafeUrlList().size()) {
                imageView[i].setVisibility(View.VISIBLE);
                bitmapUtils.display(imageView[i], HttpHelper.URL + "image?name=" + data.getSafeUrlList().get(i));
            } else {
                imageView[i].setVisibility(View.GONE);
            }
        }

        for (int i = 0; i < 4; i++) {
            if (i < data.getSafeDesUrlList().size()) {
                linearLayouts[i].setVisibility(View.VISIBLE);
                textViews[i].setText(data.getSafeDesList().get(i));
                bitmapUtils.display(imageIcon[i], HttpHelper.URL + "image?name=" + data.getSafeDesUrlList().get(i));
            } else {
                linearLayouts[i].setVisibility(View.GONE);
            }
        }

        /** 设置适配器 */
        ll_parent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //收缩扩展相应方法
                expend();
            }
        });
    }

    private void expend() {
        int shortHeight = 0;
        ll_root.measure(0, 0);
        int longHeight = ll_root.getMeasuredHeight();

        ValueAnimator animator;
        if (!isOpen) {
            isOpen = true;
            //由短(0)变长(ll_root高度)
            animator = ValueAnimator.ofInt(shortHeight, longHeight);
        } else {
            isOpen = false;
            animator = ValueAnimator.ofInt(longHeight, shortHeight);
        }

        //监听由短变长,或者由长变短的过程
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams.height = (Integer) animation.getAnimatedValue();
                ll_root.setLayoutParams(layoutParams);
            }
        });

        //监听动画完成
        animator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen) {
                    arrow_iv.setBackgroundResource(R.mipmap.arrow_up);
                } else {
                    arrow_iv.setBackgroundResource(R.mipmap.arrow_down);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });

        animator.setDuration(200);
        animator.start();
    }
}
