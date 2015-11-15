package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.book.kitchen.kitchenbook.R;

/**
 * Created by User on 15/11/2015.
 */
public class MyRecipes extends Fragment implements View.OnClickListener{
    TextView addRecipe;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        addRecipe = (TextView)root.findViewById(R.id.addRecipe);
        addRecipe.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == addRecipe.getId()){
            Toast toast = Toast.makeText(v.getContext(),
                    "Add recipe clicked", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
