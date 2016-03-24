package com.example.admin.appmarket.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseActivity;
import com.example.admin.appmarket.util.UIUtils;

public class PagerTab extends ViewGroup {

    private ViewPager mViewPager;
    private PageListener mPageListener = new PageListener();
    private OnPageChangeListener mDelegatePageListener;
    private BaseActivity mActivity;

    private int mDividerPadding = 12;
    private int mDividerWidth = 1;
    private int mDividerColor = 0x1A000000;
    private Paint mDividerPaint;

    private int mIndicatorHeight = 4;
    private int mIndicatorWidth;
    private int mIndicatorLeft;
    private int mIndicatorColor = 0xFF0084FF;
    private Paint mIndicatorPaint;

    private int mContentWidth;
    private int mContentHeight;

    private int mTabPadding = 24;
    private int mTabTextSize = 16;

    private int mTabBackgroundResId = R.drawable.bg_tab_text;
    private int mTabTextColorResId = R.color.tab_text_color;
    private int mTabCount;

    private int mCurrentPosition = 0;
    private float mCurrentOffsetPixels;
    private int mSelectedPosition = 0;

    private boolean mIsBeingDragged = false;
    private float mLastMotionX;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mTouchSlop;

    private ScrollerCompat mScroller;
    private int mLastScrollX;

    private int mMaxScrollX = 0;
    private int mSplitScrollX = 0;

    private EdgeEffectCompat mLeftEdge;
    private EdgeEffectCompat mRightEdge;

    public PagerTab(Context context) {
        this(context, null);
    }

    public PagerTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
        }
        init();
        initPaint();
    }

    private void init() {
        mIndicatorHeight = UIUtils.dip2px(mIndicatorHeight);
        mDividerPadding = UIUtils.dip2px(mDividerPadding);
        mTabPadding = UIUtils.dip2px(mTabPadding);
        mDividerWidth = UIUtils.dip2px(mDividerWidth);
        mTabTextSize = UIUtils.dip2px(mTabTextSize);

        mScroller = ScrollerCompat.create(mActivity);
        final ViewConfiguration configuration = ViewConfiguration.get(mActivity);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        mLeftEdge = new EdgeEffectCompat(mActivity);
        mRightEdge = new EdgeEffectCompat(mActivity);
    }

    /**
     * 初始化笔
     */
    private void initPaint() {
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setColor(mIndicatorColor);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth(mDividerWidth);
        mDividerPaint.setColor(mDividerColor);
    }

    /**
     * 设置ViewPager
     */
    public void setViewPager(ViewPager viewPager) {
        if (viewPager == null || viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager is null or ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        onViewPagerChanged();
    }

    private void onViewPagerChanged() {
        mViewPager.setOnPageChangeListener(mPageListener);
        mTabCount = mViewPager.getAdapter().getCount();
        for (int i = 0; i < mTabCount; i++) {
            if (mViewPager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) mViewPager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, mViewPager.getAdapter().getPageTitle(i).toString());
            }
        }
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    mCurrentPosition = mViewPager.getCurrentItem();
                    if (mDelegatePageListener != null) {
                        mDelegatePageListener.onPageSelected(mCurrentPosition);
                    }
                }
            });
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mDelegatePageListener = listener;
    }

    /**
     * 添加文字tab
     */
    private void addTextTab(final int position, String title) {
        TextView tab = new TextView(mActivity);
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
        tab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //获取对应的一个颜色选择器，xml
        tab.setTextColor(UIUtils.getColorStateList(mTabTextColorResId));
        tab.setBackgroundDrawable(UIUtils.getDrawable(mTabBackgroundResId));
        tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        addTab(position, tab);
    }

    /**
     * 添加图片icon
     */
    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(mActivity);
        tab.setImageResource(resId);
        tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position);
            }
        });
        tab.setPadding(mTabPadding, 0, mTabPadding, 0);
        addView(tab, position);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingBottom();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int totalWidth = 0;
        int highest = 0;
        int goneChildCount = 0;
        for (int i = 0; i < mTabCount; i++) {
            final View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                goneChildCount--;
                continue;
            }
            int childWidthMeasureSpec;
            int childHeightMeasureSpec;

            LayoutParams childLayoutParams = child.getLayoutParams();
            if (childLayoutParams == null) {
                childLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }

            if (childLayoutParams.width == LayoutParams.MATCH_PARENT) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            } else if (childLayoutParams.width == LayoutParams.WRAP_CONTENT) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
            } else {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.width, MeasureSpec.EXACTLY);
            }

            if (childLayoutParams.height == LayoutParams.MATCH_PARENT) {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            } else if (childLayoutParams.height == LayoutParams.WRAP_CONTENT) {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            } else {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY);
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            totalWidth += childWidth;
            highest = highest < childHeight ? childHeight : highest;
        }

        if (totalWidth <= widthSize) {
            int splitWidth = (int) (widthSize / (mTabCount - goneChildCount + 0.0f) + 0.5f);
            for (int i = 0; i < mTabCount; i++) {
                final View child = getChildAt(i);
                if (child == null || child.getVisibility() == View.GONE) {
                    continue;
                }
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(splitWidth, MeasureSpec.EXACTLY);
                int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
            mMaxScrollX = 0;
            mSplitScrollX = 0;
        } else {
            mMaxScrollX = totalWidth - widthSize;
            mSplitScrollX = (int) (mMaxScrollX / (mTabCount - goneChildCount - 1.0f) + 0.5f);
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            mContentWidth = widthSize;
        } else {
            mContentWidth = totalWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mContentHeight = heightSize;
        } else {
            mContentHeight = highest;
        }

        int measureWidth = mContentWidth + getPaddingLeft() + getPaddingRight();
        int measureHeight = mContentHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int height = b - t;
            int left = l;
            for (int i = 0; i < mTabCount; i++) {
                final View child = getChildAt(i);
                if (child == null || child.getVisibility() == View.GONE) {
                    continue;
                }
                int top = (int) ((height - child.getMeasuredHeight()) / 2.0f + 0.5f);
                int right = left + child.getMeasuredWidth();
                child.layout(left, top, right, top + child.getMeasuredHeight());
                left = right;
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int height = getHeight();
        //画指示器
        canvas.drawRect(mIndicatorLeft, height - mIndicatorHeight, mIndicatorLeft + mIndicatorWidth, height, mIndicatorPaint);

        // 画分割线
        for (int i = 0; i < mTabCount - 1; i++) {
            final View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            if (child != null) {
                canvas.drawLine(child.getRight(), mDividerPadding, child.getRight(), mContentHeight - mDividerPadding, mDividerPaint);
            }
        }

        boolean needsInvalidate = false;
        if (!mLeftEdge.isFinished()) {
            final int restoreCount = canvas.save();
            final int heightEdge = getHeight() - getPaddingTop() - getPaddingBottom();
            final int widthEdge = getWidth();
            canvas.rotate(270);
            canvas.translate(-heightEdge + getPaddingTop(), 0);
            mLeftEdge.setSize(heightEdge, widthEdge);
            needsInvalidate |= mLeftEdge.draw(canvas);
            canvas.restoreToCount(restoreCount);
        }
        if (!mRightEdge.isFinished()) {
            final int restoreCount = canvas.save();
            final int widthEdge = getWidth();
            final int heightEdge = getHeight() - getPaddingTop() - getPaddingBottom();
            canvas.rotate(90);
            canvas.translate(-getPaddingTop(), -(widthEdge + mMaxScrollX));
            mRightEdge.setSize(heightEdge, widthEdge);
            needsInvalidate |= mRightEdge.draw(canvas);
            canvas.restoreToCount(restoreCount);
        }
        if (needsInvalidate) {
            postInvalidate();
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (mIsBeingDragged && action == MotionEvent.ACTION_MOVE) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                mLastMotionX = x;
                mIsBeingDragged = !mScroller.isFinished();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                if (xDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                break;
        }
        return mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float deltaX = x - mLastMotionX;
                if (!mIsBeingDragged) {
                    if (Math.abs(deltaX) > mTouchSlop) {
                        mIsBeingDragged = true;
                    }
                }
                if (mIsBeingDragged) {
                    mLastMotionX = x;
                    onMove(deltaX);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    float velocity = velocityTracker.getXVelocity();
                    onUp(velocity);
                }
            }
            case MotionEvent.ACTION_CANCEL: {
                mIsBeingDragged = false;
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            }
        }
        return true;
    }

    private void onMove(float x) {
        if (mMaxScrollX <= 0) {
            if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
                mViewPager.fakeDragBy(x);
            }
        } else {
            int scrollByX = -(int) (x + 0.5);
            if (getScrollX() + scrollByX < 0) {
                scrollByX = 0 - getScrollX();
                mLeftEdge.onPull(Math.abs(x) / getWidth());
            }
            if (getScrollX() + scrollByX > mMaxScrollX) {
                scrollByX = mMaxScrollX - getScrollX();
                mRightEdge.onPull(Math.abs(x) / getWidth());
            }
            scrollBy(scrollByX, 0);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void onUp(float velocity) {
        if (mMaxScrollX <= 0) {
            if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
        } else {
            if (Math.abs(velocity) <= mMinimumVelocity) {
                return;
            }
            mScroller.fling(getScrollX(), 0, -(int) (velocity + 0.5), 0, 0, mMaxScrollX, 0, 0, 270, 0);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = mLastScrollX;
            mLastScrollX = mScroller.getCurrX();
            if (mLastScrollX < 0 && oldX >= 0) {
                mLeftEdge.onAbsorb((int) mScroller.getCurrVelocity());
            } else if (mLastScrollX > mMaxScrollX && oldX <= mMaxScrollX) {
                mRightEdge.onAbsorb((int) mScroller.getCurrVelocity());
            }
            int x = mLastScrollX;
            if (mLastScrollX < 0) {
                x = 0;
            } else if (mLastScrollX > mMaxScrollX) {
                x = mMaxScrollX;
            }
            scrollTo(x, 0);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void checkAndcalculate() {
        final View firstTab = getChildAt(0);
        if (mIndicatorLeft < firstTab.getLeft()) {
            mIndicatorLeft = firstTab.getLeft();
            mIndicatorWidth = firstTab.getWidth();
        }
        View lastTab = getChildAt(mTabCount - 1);
        if (mIndicatorLeft > lastTab.getLeft()) {
            mIndicatorLeft = lastTab.getLeft();
            mIndicatorWidth = lastTab.getWidth();
        }
        for (int i = 0; i < mTabCount; i++) {
            View tab = getChildAt(i);
            if (mIndicatorLeft < tab.getLeft()) {
                mCurrentPosition = i - 1;
                View currentTab = getChildAt(mCurrentPosition);
                mCurrentOffsetPixels = (mIndicatorLeft - currentTab.getLeft()) / (currentTab.getWidth() + 0.0f);
                break;
            }
        }
    }

    /**
     * 滚动到指定的child
     */
    public void scrollSelf(int position, float offset) {
        if (position >= mTabCount) {
            return;
        }
        final View tab = getChildAt(position);
        mIndicatorLeft = (int) (tab.getLeft() + tab.getWidth() * offset + 0.5);
        int rightPosition = position + 1;
        if (offset > 0 && rightPosition < mTabCount) {
            View rightTab = getChildAt(rightPosition);
            mIndicatorWidth = (int) (tab.getWidth() * (1 - offset) + rightTab.getWidth() * offset + 0.5);
        } else {
            mIndicatorWidth = tab.getWidth();
        }
        checkAndcalculate();

        int newScrollX = position * mSplitScrollX + (int) (offset * mSplitScrollX + 0.5);
        if (newScrollX < 0) {
            newScrollX = 0;
        }
        if (newScrollX > mMaxScrollX) {
            newScrollX = mMaxScrollX;
        }
        int duration = 100;
        if (mSelectedPosition != -1) {
            duration = (Math.abs(mSelectedPosition - position)) * 100;
        }
        mScroller.startScroll(getScrollX(), 0, (newScrollX - getScrollX()), 0, duration);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 选中指定位置的Tab
     */
    private void selectTab(int position) {
        for (int i = 0; i < mTabCount; i++) {
            View tab = getChildAt(i);
            if (tab != null) {
                tab.setSelected(position == i);
            }
        }
    }

    private class PageListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, final int positionOffsetPixels) {
            scrollSelf(position, positionOffset);
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                mSelectedPosition = -1;
            }
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            System.out.println("onPageSelected:" + position);
            mSelectedPosition = position;
            selectTab(position);
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageSelected(position);
            }
        }
    }

    public interface IconTabProvider {
        public int getPageIconResId(int position);

        public int getPageSelectedIconResId();
    }
}
