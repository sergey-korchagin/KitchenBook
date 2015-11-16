package com.book.kitchen.kitchenbook.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Constants;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.adapters.RecyclerViewAdapter;
import com.book.kitchen.kitchenbook.managers.SharedManager;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by User on 16/11/2015.
 */
public class RecipeFullScreen extends Fragment {


    public RecipeFullScreen() {

    }

    @SuppressLint("ValidFragment")
    public RecipeFullScreen(ParseObject object) {
        parseObject = object;
    }


    ParseObject parseObject;
    TextView title;
    TextView description;
    ImageView icon;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipe_full_screen, container, false);

        title = (TextView) root.findViewById(R.id.fullScreenTitle);
        description = (TextView) root.findViewById(R.id.fulScreenDescription);

        title.setText(parseObject.get("title").toString());
        description.setText(parseObject.get("description").toString());

        icon = (ImageView) root.findViewById(R.id.largeIcon);


        if (parseObject.get("mainImage") != null) {
            ParseFile applicantResume = (ParseFile) parseObject.get("mainImage");
            applicantResume.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        icon.setImageBitmap(bmp);
                    } else {
                        e.printStackTrace();
                    }
                }
            });

        }
            return root;
    }

}
