package com.book.kitchen.kitchenbook.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16/11/2015.
 */
public class RecipeFullScreen extends Fragment implements View.OnClickListener{


    public RecipeFullScreen() {

    }

    @SuppressLint("ValidFragment")
    public RecipeFullScreen(ParseObject object) {
        parseObject = object;
    }

    Bitmap bmp;
    ParseObject parseObject;
    TextView title;
    TextView description;
    ImageView icon;
    TextView ingredients;
    String mTitle;
    TextView longDescription;
    LinearLayout photoLayout;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipe_full_screen, container, false);

        title = (TextView) root.findViewById(R.id.fullScreenTitle);
        description = (TextView) root.findViewById(R.id.fulScreenDescription);

        mTitle = parseObject.get("title").toString();
        title.setText(mTitle);
        description.setText(parseObject.get("description").toString());

        icon = (ImageView) root.findViewById(R.id.largeIcon);
        icon.setOnClickListener(this);
        ingredients = (TextView)root.findViewById(R.id.ingredientsTv);

        ArrayList<String> ingrArray = (ArrayList<String>)parseObject.get("ingredients");
        ArrayList<String> qtyArray = (ArrayList<String>)parseObject.get("quantity");


        String ingrString = "";
        for (int  i = 0 ; i<ingrArray.size(); i++){
           ingrString = ingrString + createStringLine(i, ingrArray.get(i).toString(), qtyArray.get(i).toString());
        }

         ingredients.setText(ingrString);

        if (parseObject.get("mainImage") != null) {
            ParseFile applicantResume = (ParseFile) parseObject.get("mainImage");
            applicantResume.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        icon.setImageBitmap(bmp);
                    } else {
                        e.printStackTrace();
                    }
                }
            });

        }

        longDescription = (TextView)root.findViewById(R.id.lDescription);
        longDescription.setText(Utils.capitalizeFirstLetter(parseObject.get("longDescription").toString()));

        photoLayout = (LinearLayout)root.findViewById(R.id.photoLine);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
       ViewGroup.LayoutParams params = photoLayout.getLayoutParams();

        params.height = (width/4)-26;



            return root;
    }

    public String createStringLine(int ind, String l,String q){
        String line ="";
        line = (ind+1)+". " + l +"  "+ q +"\n";
        return Utils.capitalizeFirstLetter(line);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == icon.getId()){
            if(bmp != null) {
                FullScreenPhoto fullScreenPhoto = new FullScreenPhoto(bmp, mTitle);
                Utils.replaceFragment(getFragmentManager(), android.R.id.content, fullScreenPhoto, true);
            }else{
                Utils.showAlert(getActivity(),"","No picture!");
            }
        }
    }
}
