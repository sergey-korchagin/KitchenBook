package com.book.kitchen.kitchenbook.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.views.TouchImageView;
import com.parse.ParseObject;

/**
 * Created by serge_000 on 20/11/2015.
 */
public class FullScreenPhoto extends Fragment {

    public FullScreenPhoto() {

    }

    @SuppressLint("ValidFragment")
    public FullScreenPhoto(Bitmap image, String title) {
        this.image = image;
        this.title = title;
    }

    Bitmap image;
    String title;
    TouchImageView touchImageView;
    TextView mTitle;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    View root = inflater.inflate(R.layout.fragment_full_screen_photo, container, false);

        touchImageView = (TouchImageView)root.findViewById(R.id.touchImageView);
        touchImageView.setImageBitmap(image);

        mTitle = (TextView)root.findViewById(R.id.fullScreenT);
        mTitle.setText(title);


        return root;
    }

}
