package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


/**
 * Created by serge_000 on 13/11/2015.
 */
public class SignIn extends Fragment implements View.OnClickListener {
    TextView btnSignIn;
    EditText userName;
    EditText userPassword;
    EditText userEmail;
    TextInputLayout mTilName;
    TextInputLayout mTilEmail;
    TextInputLayout mTilPassword;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);

        btnSignIn = (TextView)root.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        mTilName = (TextInputLayout)root.findViewById(R.id.til);
        mTilEmail = (TextInputLayout)root.findViewById(R.id.til2);
        mTilPassword = (TextInputLayout)root.findViewById(R.id.til3);
//        mTilName.setErrorEnabled(true);
//        mTilEmail.setErrorEnabled(true);
//        mTilPassword.setErrorEnabled(true);


        userName = (EditText)root.findViewById(R.id.userName);
        userPassword = (EditText)root.findViewById(R.id.password);
        userEmail = (EditText)root.findViewById(R.id.email);
        return root;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnSignIn.getId()) {
            if (checkEnteredData()) {
                ParseUser user = new ParseUser();
                user.setUsername(userName.getText().toString());
                user.setPassword(userPassword.getText().toString());
                user.setEmail(userEmail.getText().toString());
                user.put("fromfacebook",false);


                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.

                            KitchenBookMain kitchenBookMain = new KitchenBookMain();
                            Utils.replaceFragment(getFragmentManager(), android.R.id.content, kitchenBookMain, false);
                        } else {
                            Utils.showAlert(getActivity(),"Error!", e.getMessage().toString());
                        }
                    }
                });
            }
        }
    }

    private boolean checkEnteredData(){

//        if(userName.getText().toString().equals(""))
//        {
//            mTilName.setError(getResources().getString(R.string.empty_name));
//        }
//        if(userEmail.getText().toString().equals(""))
//        {
//            mTilName.setError(getResources().getString(R.string.empty_name));
//        }
//        if(userPassword.getText().toString().equals(""))
//        {
//            mTilName.setError(getResources().getString(R.string.empty_name));
//        }
        //NEED TO CHANGE
        if(userEmail.getText().toString().equals("") || userPassword.getText().toString().equals("") || userEmail.getText().toString().equals("") || !userEmail.getText().toString().contains("@")){
            return false;
        }
        return true;
    }
}
