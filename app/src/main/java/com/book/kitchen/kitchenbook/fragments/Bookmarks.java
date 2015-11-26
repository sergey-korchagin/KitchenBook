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
import com.book.kitchen.kitchenbook.adapters.MyRecipesRecyclerViewAdapter;
import com.book.kitchen.kitchenbook.adapters.RecyclerViewAdapter;
import com.book.kitchen.kitchenbook.interfaces.OnItemClickListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24/11/2015.
 */
public class Bookmarks extends Fragment {
    RecyclerView rvB;
    BookmarkRecyclerViewAdapter recyclerViewAdapter;
    LinearLayoutManager mRecycleViewLayout;
    OnItemClickListener onItemClickListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        rvB = (RecyclerView) root.findViewById(R.id.rvB);
        mRecycleViewLayout = new LinearLayoutManager(root.getContext());
        rvB.setLayoutManager(mRecycleViewLayout);
        rvB.setHasFixedSize(true);


        getCategories();

        return root;
    }


    public void getCategories() {
       /* ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<String> bookmarksArray = (ArrayList<String>) currentUser.get("bookmarks");
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        if(bookmarksArray !=null) {
            if (bookmarksArray.size() != 0) {

                for (String s : bookmarksArray) {
                    ParseQuery query = new ParseQuery("recipe");
                    query.whereEqualTo("objectId", s);
                    queries.add(query);
                }
                ParseQuery mainQuery = ParseQuery.or(queries);
                mainQuery.findInBackground(new FindCallback() {
                    @Override
                    public void done(List objects, ParseException e) {

                    }

                    @Override
                    public void done(Object o, Throwable throwable) {
                        if (o instanceof List) {
                            List<ParseObject> categories = (List<ParseObject>) o;
                            recyclerViewAdapter = new BookmarkRecyclerViewAdapter(categories, onItemClickListener);
                            rvB.setAdapter(recyclerViewAdapter);

                        }
                    }
                });

            }
        }*/
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery query = new ParseQuery("recipe");
        query.whereEqualTo("userId", currentUser.getObjectId()).whereEqualTo("isBookmark",true);

        query.addDescendingOrder("createdAt");
        //     query.whereEqualTo("public","public");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List objects, ParseException e) {
            }

            @Override
            public void done(Object o, Throwable throwable) {
                if (o instanceof List) {
                    List<ParseObject> categories = (List<ParseObject>) o;
                    recyclerViewAdapter = new BookmarkRecyclerViewAdapter(categories, onItemClickListener);
                    rvB.setAdapter(recyclerViewAdapter);

                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
