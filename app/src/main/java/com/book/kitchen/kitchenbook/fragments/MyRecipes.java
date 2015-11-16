package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.adapters.MyRecipesRecyclerViewAdapter;
import com.book.kitchen.kitchenbook.adapters.RecyclerViewAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by User on 15/11/2015.
 */
public class MyRecipes extends Fragment implements View.OnClickListener{
    TextView addRecipe;

    RecyclerView rv;
    MyRecipesRecyclerViewAdapter recyclerViewAdapter;
    LinearLayoutManager mRecycleViewLayout;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        addRecipe = (TextView)root.findViewById(R.id.addRecipe);
        addRecipe.setOnClickListener(this);

        rv = (RecyclerView) root.findViewById(R.id.rvMy);
        mRecycleViewLayout = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(mRecycleViewLayout);
        rv.setHasFixedSize(true);

        getCategories();


        return root;
    }

    public void getCategories() {
       ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery query = new ParseQuery("recipe");
        query.whereEqualTo("userId",currentUser.getObjectId());
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List objects, ParseException e) {

            }

            @Override
            public void done(Object o, Throwable throwable) {
                if (o instanceof List) {
                    List<ParseObject> categories = (List<ParseObject>) o;
                    recyclerViewAdapter = new MyRecipesRecyclerViewAdapter(categories);
                    rv.setAdapter(recyclerViewAdapter);

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == addRecipe.getId()){
            AddRecipe addRecipe = new AddRecipe();
            Utils.replaceFragment(getParentFragment().getFragmentManager(), android.R.id.content, addRecipe, true);
        }
    }
}
