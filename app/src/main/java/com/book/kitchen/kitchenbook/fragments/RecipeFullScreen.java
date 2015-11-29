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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Constants;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.adapters.CommentsListAdapter;
import com.book.kitchen.kitchenbook.adapters.MyRecipesRecyclerViewAdapter;
import com.book.kitchen.kitchenbook.adapters.RecyclerViewAdapter;
import com.book.kitchen.kitchenbook.managers.SharedManager;
import com.book.kitchen.kitchenbook.managers.VolleyManager;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    public RecipeFullScreen(ParseObject object, boolean isMyRecipe) {
        parseObject = object;
        this.isMyyRecipe = isMyRecipe;
    }

    int nullCounter = 0;
    Bitmap bmp;
    Bitmap bmp1;
    Bitmap bmp2;
    Bitmap bmp3;
    Bitmap bmp4;

    ParseObject parseObject;
    TextView title;
    TextView description;
    ImageView icon;
    TextView ingredients;
    String mTitle;
    TextView longDescription;
    LinearLayout photoLayout;
    ImageView mImage1;
    ImageView mImage2;
    ImageView mImage3;
    ImageView mImage4;
    VolleyManager volleyManager;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;
    ProgressBar progressBar4;
    String objectId;
    TextView morePhotos;
    ImageView star;
    ParseUser currentUser;
    boolean isMyyRecipe;
    LinearLayout commentsLayout;
    TextView btnAddComment;
    EditText commentText;
    ListView commentsList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipe_full_screen, container, false);
        currentUser = ParseUser.getCurrentUser();
        objectId = parseObject.getObjectId();

        progressBar1 = (ProgressBar)root.findViewById(R.id.p1);
        progressBar2 = (ProgressBar)root.findViewById(R.id.p2);
        progressBar3 = (ProgressBar)root.findViewById(R.id.p3);
        progressBar4 = (ProgressBar)root.findViewById(R.id.p4);

        morePhotos = (TextView)root.findViewById(R.id.photosText);
        title = (TextView) root.findViewById(R.id.fullScreenTitle);
        description = (TextView) root.findViewById(R.id.fulScreenDescription);

        mTitle = parseObject.get("title").toString();
        title.setText(mTitle);
        description.setText(Utils.capitalizeFirstLetter(parseObject.get("description").toString()));

        icon = (ImageView) root.findViewById(R.id.largeIcon);
        icon.setOnClickListener(this);
        ingredients = (TextView)root.findViewById(R.id.ingredientsTv);

        ArrayList<String> ingrArray = (ArrayList<String>)parseObject.get("ingredients");
        ArrayList<String> qtyArray = (ArrayList<String>)parseObject.get("quantity");

        volleyManager = VolleyManager.getInstance();


        String ingrString = "";
        for (int  i = 0 ; i<ingrArray.size(); i++){
           ingrString = ingrString + createStringLine(i, ingrArray.get(i).toString(), qtyArray.get(i).toString());
        }

         ingredients.setText(ingrString);
        mImage1 = (ImageView)root.findViewById(R.id.imageView1);
        mImage1.setOnClickListener(this);
        mImage2 = (ImageView)root.findViewById(R.id.imageView2);
        mImage2.setOnClickListener(this);
        mImage3 = (ImageView)root.findViewById(R.id.imageView3);
        mImage3.setOnClickListener(this);
        mImage4 = (ImageView)root.findViewById(R.id.imageView4);
        mImage4.setOnClickListener(this);

        photoLayout = (LinearLayout)root.findViewById(R.id.photoLine);
        commentsLayout = (LinearLayout)root.findViewById(R.id.commentsLayout);

        btnAddComment = (TextView)root.findViewById(R.id.btnAddComment);
        btnAddComment.setOnClickListener(this);
        commentText = (EditText)root.findViewById(R.id.commentText);
        if(parseObject.get("public").equals("private")){
            commentsLayout.setVisibility(View.GONE);
        }

        fillPhotos();

        longDescription = (TextView)root.findViewById(R.id.lDescription);
        longDescription.setText(Utils.capitalizeFirstLetter(parseObject.get("longDescription").toString()));


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        ViewGroup.LayoutParams params = photoLayout.getLayoutParams();
        params.height = (width/4)-26;

        star = (ImageView)root.findViewById(R.id.starBtn);
        star.setOnClickListener(this);
        if(parseObject.getBoolean("isBookmark") || currentUser.getObjectId().equals(parseObject.get("userId"))){
            star.setVisibility(View.INVISIBLE);
        }
        commentsList = (ListView) root.findViewById(R.id.comentList);
        if(!parseObject.getBoolean("isBookmark")){
            getComments();
        }
        return root;
    }


        public void getComments(){
            ParseQuery query = new ParseQuery("comment");
            query.whereEqualTo("recipeId", parseObject.getObjectId());
            query.addDescendingOrder("createdAt");
            query.findInBackground(new FindCallback() {
                @Override
                public void done(List objects, ParseException e) {

                }

                @Override
                public void done(Object o, Throwable throwable) {
                    if (o instanceof List) {
                        List<ParseObject> categories = (List<ParseObject>) o;
                        CommentsListAdapter adapter = new CommentsListAdapter(getActivity(),categories);
                        commentsList.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(commentsList);

                    }
                }
            });
        }

    public void fillPhotos(){
        if (parseObject.get("mainImage") != null) {
            ParseFile applicantResume = (ParseFile) parseObject.get("mainImage");
            volleyManager.addToRequestQueue(volleyManager.createImageRequest(applicantResume.getUrl(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    icon.setImageBitmap(response);
                    bmp = response;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    error.toString();
                }
            }));

        }else{
            icon.setImageDrawable(getResources().getDrawable(R.drawable.no_photo));
        }
        if (parseObject.get("image1") != null) {
            ParseFile applicantResume = (ParseFile) parseObject.get("image1");
            volleyManager.addToRequestQueue(volleyManager.createImageRequest(applicantResume.getUrl(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {

                    mImage1.setImageBitmap(response);
                    bmp1 = response;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    error.toString();
                }
            }));
        }else{
            mImage1.setVisibility(View.INVISIBLE);
            progressBar1.setVisibility(View.INVISIBLE);
            nullCounter++;
        }
        if (parseObject.get("image2") != null) {
            ParseFile applicantResume = (ParseFile) parseObject.get("image2");
            volleyManager.addToRequestQueue(volleyManager.createImageRequest(applicantResume.getUrl(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    mImage2.setImageBitmap(response);
                    bmp2 = response;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    error.toString();
                }
            }));
        }else{
            mImage2.setVisibility(View.INVISIBLE);
            progressBar2.setVisibility(View.INVISIBLE);
            nullCounter++;
        }
        if (parseObject.get("image3") != null) {
            ParseFile applicantResume = (ParseFile) parseObject.get("image3");
            volleyManager.addToRequestQueue(volleyManager.createImageRequest(applicantResume.getUrl(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    mImage3.setImageBitmap(response);
                    bmp3 = response;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    error.toString();
                }
            }));
        }else{
            mImage3.setVisibility(View.INVISIBLE);
            progressBar3.setVisibility(View.INVISIBLE);
            nullCounter++;
        }
        if (parseObject.get("image4") != null) {
            ParseFile applicantResume = (ParseFile) parseObject.get("image4");
            volleyManager.addToRequestQueue(volleyManager.createImageRequest(applicantResume.getUrl(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    mImage4.setImageBitmap(response);
                    bmp4 = response;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    error.toString();
                }
            }));
        }else{
            mImage4.setVisibility(View.INVISIBLE);
            progressBar4.setVisibility(View.INVISIBLE);
            nullCounter++;
        }

        if(nullCounter == 4){
            morePhotos.setVisibility(View.GONE);
            photoLayout.setVisibility(View.GONE);
            nullCounter = 0;
        }
    }

    public String createStringLine(int ind, String l,String q){
        String line ="";
        line = (ind+1)+". " + Utils.capitalizeFirstLetter(l) +"  "+ Utils.capitalizeFirstLetter(q) +"\n";
        return line;
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
     else   if(v.getId() == mImage1.getId()){
            if(bmp1 != null) {
                FullScreenPhoto fullScreenPhoto = new FullScreenPhoto(bmp1, "");
                Utils.replaceFragment(getFragmentManager(), android.R.id.content, fullScreenPhoto, true);
            }else{
                Utils.showAlert(getActivity(),"","No picture!");
            }
        }
     else   if(v.getId() == mImage2.getId()){
            if(bmp2 != null) {
                FullScreenPhoto fullScreenPhoto = new FullScreenPhoto(bmp2, "");
                Utils.replaceFragment(getFragmentManager(), android.R.id.content, fullScreenPhoto, true);
            }else{
                Utils.showAlert(getActivity(),"","No picture!");
            }
        }
    else    if(v.getId() == mImage3.getId()){
            if(bmp3 != null) {
                FullScreenPhoto fullScreenPhoto = new FullScreenPhoto(bmp3, "");
                Utils.replaceFragment(getFragmentManager(), android.R.id.content, fullScreenPhoto, true);
            }else{
                Utils.showAlert(getActivity(),"","No picture!");
            }
        }
     else   if(v.getId() == mImage4.getId()){
            if(bmp4 != null) {
                FullScreenPhoto fullScreenPhoto = new FullScreenPhoto(bmp4, "");
                Utils.replaceFragment(getFragmentManager(), android.R.id.content, fullScreenPhoto, true);
            }else{
                Utils.showAlert(getActivity(),"","No picture!");
            }
        }
        else if(star.getId() == v.getId()){
          // ArrayList<String> bookmarksArray = (ArrayList<String>)currentUser.get("bookmarks");

            Utils.cloneObjectforBookmark(getActivity(), currentUser, parseObject);
            star.setVisibility(View.INVISIBLE);

        }
        else if(btnAddComment.getId() == v.getId()){
            if(!commentText.getText().toString().equals(""))
            {
                ParseObject comment = new ParseObject("comment");
                comment.put("userName", currentUser.get("username"));
                comment.put("comment",commentText.getText().toString());
                comment.put("recipeId", parseObject.getObjectId());
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Utils.showAlert(getActivity(), "", "Comment was added");
                            commentText.setText("");
                            getComments();
                        } else {

                        }
                    }
                });
            }else {
                Utils.showAlert(getActivity(), "", "Cant send empty filed");
            }

        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
