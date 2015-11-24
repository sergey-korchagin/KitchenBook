package com.book.kitchen.kitchenbook;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.fragments.Login;
import com.book.kitchen.kitchenbook.fragments.SplashScreen;
import com.book.kitchen.kitchenbook.managers.SharedManager;
import com.book.kitchen.kitchenbook.managers.VolleyManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class KitchenBookActivity extends AppCompatActivity {
    private String[] mMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_book);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Cqa9bDcaRLejwe6hipEpT8G7K5QdFZYdrCY3MQuS", "qY5lFHoDXUUsWy3jZMBXeVlksbCXHIouRzsukSGM");

        SharedManager.getInstance().init(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

        VolleyManager.getInstance().init(this);



        SplashScreen splashScreen = new SplashScreen();
        Utils.replaceFragment(getFragmentManager(), android.R.id.content, splashScreen, false);

    }



    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        getFragmentManager().popBackStackImmediate();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


}
