package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
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
import com.book.kitchen.kitchenbook.Utils.Constants;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.adapters.MyPagerAdapter;
import com.book.kitchen.kitchenbook.interfaces.OnItemClickListener;
import com.book.kitchen.kitchenbook.managers.SharedManager;
import com.google.gson.Gson;
import com.parse.ParseObject;
import com.parse.ParseUser;
import android.support.v4.app.FragmentManager;


import java.util.ArrayList;
import java.util.List;
import java.util.UnknownFormatConversionException;

/**
 * Created by serge_000 on 13/11/2015.
 */
public class KitchenBookMain extends Fragment implements OnItemClickListener{
    PublicRecipes publicRecipes;
    MyRecipes myRecipes;
    Settings settings;

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


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(publicRecipes);
        fragments.add(myRecipes);
        fragments.add(settings);
        publicRecipes.setOnItemClickListener(this);
        myRecipes.setOnItemClickListener(this);


        return fragments;
    }

    @Override
    public void onCardClickListener(ParseObject object) {
        SharedManager sharedManager = SharedManager.getInstance();

     //   sharedManager.put(Constants.CURRENT_OBJECT, id);

        RecipeFullScreen recipeFullScreen = new RecipeFullScreen(object);
        Utils.replaceFragment(getFragmentManager(), android.R.id.content, recipeFullScreen, true);
    }
}
