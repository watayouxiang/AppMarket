package com.example.admin.appmarket.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseHolder;
import com.example.admin.appmarket.entity.AppInfo;
import com.example.admin.appmarket.util.UIUtils;

/**
 * Created by admin on 2016/4/11.
 */
public class DetailDesHolder extends BaseHolder<AppInfo> {

    private AppInfo data;
    private TextView app_des_tv, app_author_tv;
    private ImageView arrow_iv;
    private LinearLayout ll_root;
    private boolean isOpen = false;
    private ValueAnimator valueAnimator;
    private ViewGroup.LayoutParams layoutParams;

    @Override
    public View initView() {
        return UIUtils.inflate(R.layout.layout_detail_des);
    }

    @Override
    public void refreshView() {
        View rootView = getRootView();
        data = getData();

        app_des_tv = (TextView) rootView.findViewById(R.id.app_des_tv);
        app_author_tv = (TextView) rootView.findViewById(R.id.app_author_tv);
        arrow_iv = (ImageView) rootView.findViewById(R.id.arrow_iv);
        ll_root = (LinearLayout) rootView.findViewById(R.id.ll_root);

        app_des_tv.setText(data.getDes());
        app_author_tv.setText(data.getAuthor());

        //初始化7行高度
        layoutParams = app_des_tv.getLayoutParams();
        layoutParams.height = getShortHeight();
        app_des_tv.setLayoutParams(layoutParams);

        ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expend();
            }
        });
    }

    private void expend() {
        int shortHeight = getShortHeight();
        int longHeight = getLongHeight();

        //收缩展开逻辑
        if (!isOpen) {
            isOpen = true;
            if (shortHeight < longHeight) {
                valueAnimator = ValueAnimator.ofInt(shortHeight, longHeight);
            }
        } else {
            isOpen = false;
            if (shortHeight < longHeight) {
                valueAnimator = ValueAnimator.ofInt(longHeight, shortHeight);
            }
        }

        //设置收缩展开时的控件高度变化, 动画结束后改变箭头方向
        if (valueAnimator != null) {
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    layoutParams.height = (int) animation.getAnimatedValue();
                    app_des_tv.setLayoutParams(layoutParams);
                }
            });

            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isOpen) {
                        arrow_iv.setBackgroundResource(R.mipmap.arrow_up);
                    } else {
                        arrow_iv.setBackgroundResource(R.mipmap.arrow_down);
                    }
                    //动画执行完毕后让整个界面滚动起来,循环查找对应的scrollView的夫控件,直到找到为止
                    final ScrollView scrollView = (ScrollView) getScrollView();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            valueAnimator.setDuration(300);
            valueAnimator.start();
        }
    }

    private View getScrollView() {
        View parent = (View) ll_root.getParent();
        while (!(parent instanceof ScrollView)) {
            parent = (View) parent.getParent();
        }
        return parent;
    }

    //模拟控件,获取textview的7行时的高度
    private int getShortHeight() {
        int width = app_des_tv.getMeasuredWidth();
        //宽是matchParent是精确的(32 2 30)
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        //高不是精确的,所以设置成至多2000像素
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);

        TextView textView = new TextView(UIUtils.getContext());
        textView.setMaxLines(7);
        textView.setLines(7);
        textView.setText(data.getDes());
        textView.measure(widthMeasureSpec, heightMeasureSpec);

        return textView.getMeasuredHeight();
    }

    private int getLongHeight() {
        int width = app_des_tv.getMeasuredWidth();
        //宽是matchParent是精确的(32 2 30)
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        //高不是精确的,所以设置成至多2000像素
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);

        TextView textView = new TextView(UIUtils.getContext());
        textView.setText(data.getDes());
        textView.measure(widthMeasureSpec, heightMeasureSpec);

        return textView.getMeasuredHeight();
    }
}
