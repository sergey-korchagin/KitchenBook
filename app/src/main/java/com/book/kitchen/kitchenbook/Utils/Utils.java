package com.book.kitchen.kitchenbook.Utils;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;

import com.book.kitchen.kitchenbook.KitchenBookActivity;

/**
 * Created by serge_000 on 13/11/2015.
 */
public class Utils {

    public static void replaceFragment(FragmentManager fragmentManager, int container, Fragment fragment, boolean AddToBackStack) {

        //Enter Animations Later
        if(fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (AddToBackStack)
                fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(container, fragment).commitAllowingStateLoss();

        }
    }

    public static void showAlert(Context context, String title,  String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(true);
//                .setNegativeButton("ОК, иду на кухню",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
                 //       });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
