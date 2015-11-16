package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.adapters.RecyclerViewAdapter;
import com.book.kitchen.kitchenbook.interfaces.OnItemClickListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.BufferedInputStream;
import java.util.List;

/**
 * Created by User on 15/11/2015.
 */
public class PublicRecipes extends Fragment{
    RecyclerView rv;
    RecyclerViewAdapter recyclerViewAdapter;
    LinearLayoutManager mRecycleViewLayout;
    OnItemClickListener onItemClickListener;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_public_recipes, container, false);
        rv = (RecyclerView) root.findViewById(R.id.rv);
        mRecycleViewLayout = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(mRecycleViewLayout);
        rv.setHasFixedSize(true);

        getCategories();


        return root;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public void getCategories() {

        ParseQuery query = new ParseQuery("recipe");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List objects, ParseException e) {

            }
            @Override
            public void done(Object o, Throwable throwable) {
                if (o instanceof List) {
                    List<ParseObject> categories = (List<ParseObject>) o;
                    recyclerViewAdapter = new RecyclerViewAdapter(categories,onItemClickListener);
                    rv.setAdapter(recyclerViewAdapter);

                }
            }
        });

    }


}