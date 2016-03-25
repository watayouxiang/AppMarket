package com.example.admin.appmarket.factory;

import com.example.admin.appmarket.base.BaseFragment;
import com.example.admin.appmarket.fragment.AppFragment;
import com.example.admin.appmarket.fragment.CategoryFragment;
import com.example.admin.appmarket.fragment.GameFragment;
import com.example.admin.appmarket.fragment.HomeFragment;
import com.example.admin.appmarket.fragment.HotFragment;
import com.example.admin.appmarket.fragment.RecommendFragment;
import com.example.admin.appmarket.fragment.SubjectFragment;

import java.util.HashMap;

/**
 * Fragment的工厂类,用于缓存以及生成Fragment对象
 * Created by admin on 2016/3/23.
 */
public class FragmentFactory {

    private static HashMap<Integer, BaseFragment> mHashMap = new HashMap<Integer, BaseFragment>();

    public static BaseFragment createFragment(int index){

        BaseFragment fragment = mHashMap.get(index);
        if(fragment!=null){
            return fragment;
        }else{
            switch (index){
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new GameFragment();
                    break;
                case 3:
                    fragment = new SubjectFragment();
                    break;
                case 4:
                    fragment = new RecommendFragment();
                    break;
                case 5:
                    fragment = new CategoryFragment();
                    break;
                case 6:
                    fragment = new HotFragment();
                    break;
            }

            mHashMap.put(index,fragment);
            return fragment;
        }
    }

}
