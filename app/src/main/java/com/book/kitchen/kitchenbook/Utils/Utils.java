package com.book.kitchen.kitchenbook.Utils;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.book.kitchen.kitchenbook.KitchenBookActivity;
import com.book.kitchen.kitchenbook.R;
import com.parse.GetCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

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

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showForgotAlert(final Context context) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        final TextView dialogTitle = (TextView)promptsView.findViewById(R.id.title);
        dialogTitle.setText("Please enter your email");
        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                if (!userInput.getText().toString().equals("")) {


                                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                                    query.whereEqualTo("email", userInput.getText().toString());
                                    query.findInBackground(new FindCallback<ParseUser>() {
                                        @Override
                                        public void done(List<ParseUser> objects, ParseException e) {
                                            if (e == null) {
                                                if ((boolean) objects.get(0).get("fromfacebook")) {
                                                    showAlert(context,"", "User was registered with facebook, please go to facebook to change password!");
                                                }else {
                                                    newPassword(userInput.getText().toString(), context);

                                                }

                                            } else {
                                                showAlert(context, "Error!", e.getMessage().toString());
                                            }
                                        }
                                    });


                                }
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public static void newPassword(String user,final Context context){
        ParseUser.requestPasswordResetInBackground(user,
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            showAlert(context, "Success", "An email was successfully sent with reset instructions");
                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            showAlert(context, "Error!", e.getMessage().toString());
                        }
                    }
                });
    }

    public static String capitalizeFirstLetter(String capitalizeMe){
        if(!capitalizeMe.equals("") ) {
            capitalizeMe =  capitalizeMe.substring(0, 1).toUpperCase() + capitalizeMe.substring(1);
        }
        return capitalizeMe;
    }


    public static String getCategoryFromCode(Context context, int code){
        String category =context.getResources().getString(R.string.choice_category);
        switch (code){
            case 0:
                category = context.getResources().getString(R.string.choice_category);
                break;
            case 1:
                category = context.getResources().getString(R.string.beef);
                break;
            case 2:
                category = context.getResources().getString(R.string.chicken);
                break;
            case 3:
                category = context.getResources().getString(R.string.baking);
                break;
            case 4:
                category = context.getResources().getString(R.string.drinks);
                break;
            case 5:
                category = context.getResources().getString(R.string.soup);
                break;

        }

        return category;
    }


    public static void cloneObjectforBookmark(final Context context, ParseUser currentUser, ParseObject clone){

        ParseObject recipe1 = new ParseObject("recipe");
        recipe1.put("userId", currentUser.getObjectId());
        recipe1.put("title", clone.get("title"));
        recipe1.put("description", clone.get("description"));
        recipe1.put("userName", clone.get("userName"));
        recipe1.put("category", clone.get("category"));
        recipe1.put("ingredients", clone.get("ingredients"));
        recipe1.put("quantity", clone.get("quantity"));
        recipe1.put("longDescription", clone.get("longDescription"));
        if(clone.get("mainImage") != null){
            recipe1.put("mainImage", clone.get("mainImage"));
        }
        if(clone.get("image1") !=null){
            recipe1.put("image1", clone.get("image1"));
        }
        if(clone.get("image2")!= null){
            recipe1.put("image2", clone.get("image2"));
        }
        if(clone.get("image3")!= null){
            recipe1.put("image3", clone.get("image3"));
        }
        if(clone.get("image4")!= null){
            recipe1.put("image4", clone.get("image4"));
        }

        recipe1.put("public", "private");
        recipe1.put("cookingTime", clone.get("cookingTime"));
        recipe1.put("isBookmark", true);
        recipe1.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    showAlert(context, "", "Added to bookmarks");

                } else {
                }
            }
        });

    }
}
