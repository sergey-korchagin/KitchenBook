package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.parse.ParseUser;

/**
 * Created by serge_000 on 13/11/2015.
 */
public class SplashScreen extends Fragment {
    ProgressBar progressBar;
    private Thread thread;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);
                    }
                } catch (InterruptedException ex) {
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    // do stuff with the user
                    KitchenBookMain kitchenBookMain = new KitchenBookMain();
                    Utils.replaceFragment(getFragmentManager(), android.R.id.content, kitchenBookMain, true);
                } else {
                    // show the signup or login screen
                    Login login = new Login();
                    Utils.replaceFragment(getFragmentManager(), android.R.id.content, login, true);
                }


            }
        };

        thread.start();
        return root;
    }
}
