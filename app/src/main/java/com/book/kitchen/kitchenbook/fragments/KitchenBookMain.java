package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.adapters.MyPagerAdapter;
import com.book.kitchen.kitchenbook.interfaces.OnItemClickListener;
import com.book.kitchen.kitchenbook.managers.SharedManager;
import com.parse.ParseObject;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by serge_000 on 13/11/2015.
 */
public class KitchenBookMain extends Fragment implements OnItemClickListener{
    PublicRecipes publicRecipes;
    MyRecipes myRecipes;
    Settings settings;
    Bookmarks bookmarks;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);



        pager = (ViewPager)root.findViewById(R.id.pager);
        pagerAdapter = new MyPagerAdapter(getChildFragmentManager(),initFragments(), getActivity());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return root;
    }

    private List<Fragment> initFragments() {


        myRecipes = new MyRecipes();
        publicRecipes = new PublicRecipes();
        settings = new Settings();
        bookmarks = new Bookmarks();


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(publicRecipes);
        fragments.add(myRecipes);
        fragments.add(bookmarks);
        fragments.add(settings);


        publicRecipes.setOnItemClickListener(this);
        myRecipes.setOnItemClickListener(this);
        bookmarks.setOnItemClickListener(this);


        return fragments;
    }

    @Override
    public void onCardClickListener(ParseObject object, boolean isMy) {
        RecipeFullScreen recipeFullScreen = new RecipeFullScreen(object, isMy);
        Utils.replaceFragment(getFragmentManager(), android.R.id.content, recipeFullScreen, true);
    }
}
