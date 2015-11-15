package com.book.kitchen.kitchenbook.adapters;


import android.support.v13.app.FragmentPagerAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by serge_000 on 14/11/2015.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragments;
    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 1){
            return "My recipes";
        }else if(position==2){
            return "Settings";
        }
        return "Public reciepes";
    }
}
