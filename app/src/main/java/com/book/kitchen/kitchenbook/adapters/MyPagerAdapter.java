package com.book.kitchen.kitchenbook.adapters;


import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.widget.LinearLayout;

import com.book.kitchen.kitchenbook.R;

import java.util.List;

/**
 * Created by serge_000 on 14/11/2015.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragments;
    Context mContext;
    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments, Context context) {
        super(fm);
        mFragments = fragments;
        this.mContext = context;
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
            return mContext.getResources().getString(R.string.my_recipes);
        }else if(position==2){
            return mContext.getResources().getString(R.string.settings_my);
        }
        return mContext.getResources().getString(R.string.public_recipes);
    }
}
