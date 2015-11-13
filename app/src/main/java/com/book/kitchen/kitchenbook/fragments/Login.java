package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by serge_000 on 13/11/2015.
 */
public class Login extends Fragment implements  View.OnClickListener{
    TextView btnSignIn;
    TextView btnLogin;

    EditText userName;
    EditText userPassword;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        btnSignIn = (TextView)root.findViewById(R.id.btnSignInLog);
        btnSignIn.setOnClickListener(this);

        btnLogin = (TextView)root.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        userName = (EditText)root.findViewById(R.id.userName);
        userPassword = (EditText)root.findViewById(R.id.password);
        return root;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnSignIn.getId()){
            SignIn signIn = new SignIn();
            Utils.replaceFragment(getFragmentManager(), android.R.id.content, signIn, true);
        }else if(v.getId() == btnLogin.getId()){

            ParseUser.logInInBackground(userName.getText().toString(), userPassword.getText().toString(), new LogInCallback(){
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    KitchenBookMain kitchenBookMain = new KitchenBookMain();
                    Utils.replaceFragment(getFragmentManager(), android.R.id.content, kitchenBookMain, true);
                } else {
                    Utils.showAlert(getActivity(),"Error!", e.getMessage().toString());
                }
            }
        });
        }

    }


}
