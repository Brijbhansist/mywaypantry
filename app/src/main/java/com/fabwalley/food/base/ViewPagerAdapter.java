package com.fabwalley.food.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager, List<BaseFragment> mFragmentList) {
        super(manager);
        this.mFragmentList = mFragmentList;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}