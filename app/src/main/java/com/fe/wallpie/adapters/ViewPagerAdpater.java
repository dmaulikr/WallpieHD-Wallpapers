package com.fe.wallpie.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farmaan-PC on 22-01-2017.
 */

public class ViewPagerAdpater extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mFragmentTitle;

    public ViewPagerAdpater(FragmentManager fm) {
        super(fm);
        mFragmentTitle = new ArrayList<>();
        mFragments = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitle.get(position);
    }
}
