package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by User on 15/11/2015.
 */
public class AddRecipe extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageView  mMainImage;
    LinearLayout mImageFrom;
    boolean imageClicked = false;
    TextView buttonCamera;
    TextView buttonGallery;
    TextView btnPost;
    ParseUser currentUser;
    ImageView btnPlus;
LinearLayout recipeLayout;
    EditText title;
    String titleText;
    EditText description;
    String descriptionText;
    ParseFile file;
    CheckBox mIsPublic;
    Spinner spinner;
    int category;

    ProgressDialog progressDialog;

    private static final int CAMERA_REQUEST = 1888;
    public static final int ACTIVITY_SELECT_IMAGE = 1889;
    View root;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        root = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        mMainImage = (ImageView)root.findViewById(R.id.mainImage);
        mMainImage.setOnClickListener(this);

        mImageFrom = (LinearLayout)root.findViewById(R.id.choiseFrom);

        buttonCamera = (TextView)root.findViewById(R.id.btnCamera);
        buttonCamera.setOnClickListener(this);

        buttonGallery = (TextView)root.findViewById(R.id.btnGallery);
        buttonGallery.setOnClickListener(this);

        btnPost = (TextView)root.findViewById(R.id.post);
        btnPost.setOnClickListener(this);

        title = (EditText)root.findViewById(R.id.recipe_title);
        description = (EditText)root.findViewById(R.id.recipe_description);

        mIsPublic = (CheckBox)root.findViewById(R.id.isPublic);
        mIsPublic.setOnClickListener(this);

        currentUser = ParseUser.getCurrentUser();

        btnPlus = (ImageView)root.findViewById(R.id.plusButton);
        btnPlus.setOnClickListener(this);

        recipeLayout = (LinearLayout)root.findViewById(R.id.recipeLayout);
        spinner = (Spinner)root.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        spinner.setPrompt("Category");
        List<String> elements = new ArrayList<String>();
        elements.add(getResources().getString(R.string.choice_category)); // 0 position
        elements.add(getResources().getString(R.string.beef));
        elements.add(getResources().getString(R.string.chicken));
        elements.add(getResources().getString(R.string.baking));
        elements.add(getResources().getString(R.string.drinks));
        elements.add(getResources().getString(R.string.soup));

        //Создаем для spinner адаптер:
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,elements);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);


        return root;

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mMainImage.getId()){
            if(!imageClicked){
                mImageFrom.setVisibility(View.VISIBLE);
                imageClicked = true;
            }else{
                mImageFrom.setVisibility(View.GONE);
                imageClicked = false;
            }
        }else if(v.getId() == buttonCamera.getId()){
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }else if(v.getId() == buttonGallery.getId()){
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
        }else if(btnPost.getId() == v.getId()){

           if( getTextFromFields()){
               progressDialog = ProgressDialog.show(getActivity(),"",getActivity().getResources().getString(R.string.loading));
               ParseObject recipe1 = new ParseObject("recipe");
               recipe1.put("userId",currentUser.getObjectId() );
               recipe1.put("title",titleText);
               recipe1.put("description", descriptionText);
               recipe1.put("userName",currentUser.get("username"));
               recipe1.put("category", category);
               if(file!=null) {
                   recipe1.put("mainImage", file);
               }
               if(((CheckBox)mIsPublic).isChecked()){
                   recipe1.put("public","public");
               }else{
                   recipe1.put("public","private");

               }
               recipe1.saveInBackground(new SaveCallback() {
                   @Override
                   public void done(ParseException e) {
                       if (e == null) {
                           getFragmentManager().popBackStackImmediate();
                           progressDialog.dismiss();
                       } else {
                           progressDialog.dismiss();
                           e.printStackTrace();
                       }
                   }
               });

           }
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }else if(mIsPublic.getId() == v.getId() && mIsPublic.isChecked()){
            Utils.showAlert(getActivity(),"Alert", "All users will see your recipe!");
        }else if(btnPlus.getId() == v.getId()){
            EditText myEditText = new EditText(getActivity()); // Pass it an Activity or Context
            myEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,160)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            recipeLayout.addView(myEditText);
        }
    }

    public boolean getTextFromFields(){
        titleText = title.getText().toString();
        descriptionText = description.getText().toString();
        if(titleText.equals("") && descriptionText.equals("")){
            Utils.showAlert(getActivity(),"Empty field","Please fill title and description fields");
            return false;
        }
       else if(titleText.equals("")){
            Utils.showAlert(getActivity(),"Empty field","Please enter title");
            return false;
        }
       else if(descriptionText.equals("")){
            Utils.showAlert(getActivity(),"Empty field","Please enter description");
            return false;
        }else if(!spinner.isSelected()){
            Utils.showAlert(getActivity(),"Empty field","Please choice category");
            return false;
        }
        if(!titleText.equals("")){
            titleText = titleText.substring(0, 1).toUpperCase()+titleText.substring(1);
        }
        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bitmap photo = null;
            if (requestCode == CAMERA_REQUEST) { //&& resultCode == RESULT_OK
                photo = (Bitmap) data.getExtras().get("data");
                if (Build.MANUFACTURER.equals("LGE")) {  // || (Build.MANUFACTURER.equals("Sony")) || Build.MANUFACTURER.equals("samsung")
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    photo = Bitmap.createBitmap(Bitmap.createScaledBitmap(photo, photo.getWidth(), photo.getHeight(), true), 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
                }
                mMainImage.setImageBitmap(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                photo.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                file  = new ParseFile("picture_1.jpeg", image);

            }else if (requestCode == ACTIVITY_SELECT_IMAGE) {
                Uri bitmapUri = data.getData();

                try {

                   photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), bitmapUri);

                    if (Build.MANUFACTURER.equals("LGE")) {  // || (Build.MANUFACTURER.equals("Sony")) || Build.MANUFACTURER.equals("samsung")
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        photo = Bitmap.createBitmap(Bitmap.createScaledBitmap(photo, photo.getWidth(), photo.getHeight(), true), 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
                    }
                    if(photo.getHeight() > 4095) {
                        photo = getResizedBitmap(photo, 2303, 4095);
                    }
                    mMainImage.setImageBitmap(photo);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Compress image to lower quality scale 1 - 100
                    photo.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                    byte[] image = stream.toByteArray();

                    // Create the ParseFile
                   file  = new ParseFile("picture_1.jpeg", image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=0) {
            spinner.setSelected(true);
            category = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
