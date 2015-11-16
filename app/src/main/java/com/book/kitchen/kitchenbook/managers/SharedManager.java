package com.book.kitchen.kitchenbook.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by use on 10/08/2015.
 */
public class SharedManager {
    private static SharedManager ourInstance = new SharedManager();


    private SharedPreferences mSharedPref;

    public static SharedManager getInstance() {
        return ourInstance;
    }

    private SharedManager() {
    }


    public void init(Context context) {

        mSharedPref = context.getSharedPreferences(context.getPackageName(),0);
    }

    public void put(String key,String value) {
        
        mSharedPref.edit().putString(key,value).apply();

    }

    public void put(String key,boolean value) {

        mSharedPref.edit().putBoolean(key, value).apply();

    }

    public void put(String key,int value) {

        mSharedPref.edit().putInt(key, value).apply();

    }
    public int getInt(String key){
        return mSharedPref.getInt(key,-1);
    }

    public boolean getBoolean(String key) {
        return mSharedPref.getBoolean(key,false);
    }

    public String getString(String key) {

        return mSharedPref.getString(key,null);
    }






}
