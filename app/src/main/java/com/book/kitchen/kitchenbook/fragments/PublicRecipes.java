package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15/11/2015.
 */
public class PublicRecipes extends Fragment implements AdapterView.OnItemSelectedListener{
    RecyclerView rv;
    RecyclerViewAdapter recyclerViewAdapter;
    LinearLayoutManager mRecycleViewLayout;
    OnItemClickListener onItemClickListener;
    ProgressDialog progressDialog;

    FrameLayout errorLayout;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_public_recipes, container, false);

        progressDialog = ProgressDialog.show(getActivity(),"", getActivity().getResources().getString(R.string.loading));

        rv = (RecyclerView) root.findViewById(R.id.rv);
        mRecycleViewLayout = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(mRecycleViewLayout);
        rv.setHasFixedSize(true);
        getCategories();
        errorLayout = (FrameLayout) root.findViewById(R.id.networkErrorLayout);

       intiReciever();

        return root;
    }



    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public void getCategories() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery query = new ParseQuery("recipe");
        query.whereNotEqualTo("userId", currentUser.getObjectId()).whereEqualTo("public", "public");
   //     query.whereEqualTo("public","public");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List objects, ParseException e) {
            }

            @Override
            public void done(Object o, Throwable throwable) {
                if (o instanceof List) {
                    List<ParseObject> categories = (List<ParseObject>) o;
                    progressDialog.dismiss();
                    recyclerViewAdapter = new RecyclerViewAdapter(categories, onItemClickListener);
                    rv.setAdapter(recyclerViewAdapter);

                }
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private  void intiReciever(){
        getActivity().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if(isDataConnected()){
                    // Toast.makeText( context, "Active Network Type : connected", Toast.LENGTH_SHORT ).show();
                    errorLayout.setVisibility(View.GONE);
                }else {
                    errorLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    }
    private boolean isDataConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }
}