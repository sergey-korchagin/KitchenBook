package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.adapters.BookmarkRecyclerViewAdapter;
import com.book.kitchen.kitchenbook.adapters.RecyclerViewAdapter;

/**
 * Created by User on 24/11/2015.
 */
public class Bookmarks extends Fragment {
    RecyclerView rvB;
    BookmarkRecyclerViewAdapter recyclerViewAdapter;
    LinearLayoutManager mRecycleViewLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
       rvB = (RecyclerView) root.findViewById(R.id.rvB);
        mRecycleViewLayout = new LinearLayoutManager(root.getContext());
        rvB.setLayoutManager(mRecycleViewLayout);
        rvB.setHasFixedSize(true);
        recyclerViewAdapter = new BookmarkRecyclerViewAdapter();
        rvB.setAdapter(recyclerViewAdapter);
        return root;
    }
    }
