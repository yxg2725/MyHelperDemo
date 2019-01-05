package com.example.myhelper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import com.example.myhelper.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/1/2.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles;
    private ArrayList<Fragment> mFragments;

    public MyPagerAdapter(FragmentManager fm, String[] titles, ArrayList<Fragment> fragments) {
        super(fm);
        this.mTitles = titles;
        this.mFragments = fragments;
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
