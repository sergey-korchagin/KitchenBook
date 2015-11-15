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

/**
 * Created by User on 15/11/2015.
 */
public class Settings extends Fragment {
    Button button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);



        button = (Button)root.findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Login login = new Login();
                Utils.replaceFragment(getParentFragment().getFragmentManager(), android.R.id.content, login, false);
            }
        });
        return root;
    }
}
