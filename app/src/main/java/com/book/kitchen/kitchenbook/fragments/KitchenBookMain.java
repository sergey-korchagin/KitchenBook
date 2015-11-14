package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.adapters.MyPagerAdapter;
import com.parse.ParseUser;
import android.support.v4.app.FragmentManager;


import java.util.UnknownFormatConversionException;

/**
 * Created by serge_000 on 13/11/2015.
 */
public class KitchenBookMain extends Fragment {
    Button button;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);


//        pager = (ViewPager)root.findViewById(R.id.pager);
//        pagerAdapter = new MyPagerAdapter(getSupportFragment.....);
//        pager.setAdapter(pagerAdapter);
//
//        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.d("", "onPageSelected, position = " + position);
//            }
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset,
//                                       int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });



        button = (Button)root.findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Login login = new Login();
                Utils.replaceFragment(getFragmentManager(), android.R.id.content, login, false);
            }
        });
        return root;
    }
}
