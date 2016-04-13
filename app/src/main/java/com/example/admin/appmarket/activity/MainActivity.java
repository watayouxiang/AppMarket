package com.example.admin.appmarket.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.base.BaseActivity;
import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.factory.FragmentFactory;
import com.example.admin.appmarket.util.UIUtils;
import com.example.admin.appmarket.widget.PagerTab;

public class MainActivity extends BaseActivity {

    private PagerTab mTab;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPagerTab();
        initActionBar();
    }

    private void initActionBar() {
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        ActionBar supportActionBar = getSupportActionBar();
        //设置按钮右侧的文字说明
        supportActionBar.setTitle(UIUtils.getString(R.string.app_name));
        //设置actionbar左上角按钮可以去点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        //设置actionbar左上角按钮可以去设置图片
        supportActionBar.setHomeButtonEnabled(true);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerlayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        //将按钮的点击状态和侧拉栏目的扩展或者收缩做同步
        mActionBarDrawerToggle.syncState();
    }

    private void initPagerTab() {
        mTab = (PagerTab) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        //指针控件绑定ViewPager
        mTab.setViewPager(mViewPager);
        mTab.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //Fragment工厂类获取Fragment对象
            BaseFragment fragment = FragmentFactory.createFragment(position);
            //根据不同的网络状态显示不同的界面
            fragment.baseShow();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private String[] tabNames;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            tabNames = UIUtils.getStringArray(R.array.tab_names);
        }

        @Override
        public Fragment getItem(int position) {
            // 根据不同的索引生成不同的Fragment
            return FragmentFactory.createFragment(position);
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        //指定指针说明文字方法
        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item)|| mActionBarDrawerToggle.onOptionsItemSelected(item);
    }
}
