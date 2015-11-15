package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.parse.ParseUser;

import java.io.BufferedInputStream;

/**
 * Created by User on 15/11/2015.
 */
public class PublicRecipes extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_public_recipes, container, false);

        return root;
    }

    }
