package com.book.kitchen.kitchenbook.fragments;

import android.app.DownloadManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by serge_000 on 13/11/2015.
 */
public class Login extends Fragment implements  View.OnClickListener{
    TextView btnSignIn;
    TextView btnLogin;
    TextView btnForgot;
    EditText userName;
    EditText userPassword;


    String facebookEmail;
    String facebookName;

    LoginButton loginButton;
    CallbackManager callbackManager;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_login, container, false);

        btnSignIn = (TextView)root.findViewById(R.id.btnSignInLog);
        btnSignIn.setOnClickListener(this);

        btnLogin = (TextView)root.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        userName = (EditText)root.findViewById(R.id.userName);
        userPassword = (EditText)root.findViewById(R.id.password);

        btnForgot = (TextView)root.findViewById(R.id.forgot_password);
        btnForgot.setOnClickListener(this);

        loginButton = (LoginButton) root.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends","email"));
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog = ProgressDialog.show(getActivity(),"",getActivity().getResources().getString(R.string.loading));

                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {

                                    facebookName = object.getString("name");
                                    facebookEmail = object.getString("email");
                                    ParseUser user = new ParseUser();
                                    user.setUsername(facebookName);
                                    user.setPassword("facebookPassword");
                                    user.setEmail(facebookEmail);
                                    user.put("fromfacebook",true);

                                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(root.getWindowToken(), 0);

                                    user.signUpInBackground(new SignUpCallback() {
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                // Hooray! Let them use the app now.
                                                progressDialog.dismiss();
                                                KitchenBookMain kitchenBookMain = new KitchenBookMain();
                                                Utils.replaceFragment(getFragmentManager(), android.R.id.content, kitchenBookMain, false);
                                            } else {
                                                if(e.getCode() == 202){
                                                    ParseUser.logInInBackground(facebookName, "facebookPassword", new LogInCallback(){
                                                        public void done(ParseUser user, ParseException e) {
                                                            if (user != null) {
                                                                // Hooray! The user is logged in.
                                                                View view = getActivity().getCurrentFocus();
                                                                if (view != null) {
                                                                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                                                }
                                                                progressDialog.dismiss();
                                                                KitchenBookMain kitchenBookMain = new KitchenBookMain();
                                                                Utils.replaceFragment(getFragmentManager(), android.R.id.content, kitchenBookMain, false);
                                                            } else {
                                                                Utils.showAlert(getActivity(),"Error!", e.getMessage().toString());
                                                                progressDialog.dismiss();

                                                            }
                                                        }
                                                    });
                                                }else {
                                                    Utils.showAlert(getActivity(), "Error!", e.getMessage().toString());
                                                    progressDialog.dismiss();

                                                }
                                            }
                                        }
                                    });

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                // Application code
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnSignIn.getId()){
            SignIn signIn = new SignIn();
            Utils.replaceFragment(getFragmentManager(), android.R.id.content, signIn, true);
        }else if(v.getId() == btnLogin.getId()){

            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            ParseUser.logInInBackground(userName.getText().toString(), userPassword.getText().toString(), new LogInCallback(){
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    KitchenBookMain kitchenBookMain = new KitchenBookMain();
                    Utils.replaceFragment(getFragmentManager(), android.R.id.content, kitchenBookMain, false);
                } else {
                    Utils.showAlert(getActivity(),"Error!", e.getMessage().toString());
                }
            }
        });
        }else if(btnForgot.getId()==v.getId()) {
            Utils.showForgotAlert(getActivity());
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



}
