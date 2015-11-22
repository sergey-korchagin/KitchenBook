package com.book.kitchen.kitchenbook.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by User on 15/11/2015.
 */
public class AddRecipe extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final int REQUEST_CODE_FROM_GALLERY_IMAGE = 1;
    private final int REQUEST_CODE_HIGH_QUALITY_IMAGE = 2;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    boolean videoSelected =false;
    int orientation;
    private Uri mHighQualityImageUri = null;


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
    List<EditText> allEds = new ArrayList<EditText>();
    List<EditText> allQty = new ArrayList<EditText>();
    String[] qtyStr;
    int count = 0;
    ProgressDialog progressDialog;
    String[] ingredients;
    private static final int CAMERA_REQUEST = 1888;
    public static final int ACTIVITY_SELECT_IMAGE = 1889;
    Bitmap photo = null;
    EditText londDescription;
    boolean isPublic = false;
    LinearLayout photoLayout;
    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;

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

        londDescription = (EditText)root.findViewById(R.id.long_description);



        currentUser = ParseUser.getCurrentUser();

        btnPlus = (ImageView)root.findViewById(R.id.plusButton);
        btnPlus.setOnClickListener(this);

        recipeLayout = (LinearLayout)root.findViewById(R.id.ingredientsLay);
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

        addIngredientField();

        //Создаем для spinner адаптер:
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,elements);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        photoLayout = (LinearLayout)root.findViewById(R.id.photoLine);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        ViewGroup.LayoutParams params = photoLayout.getLayoutParams();

        params.height = (width/4)-26;

        mImageView1 = (ImageView)root.findViewById(R.id.imageView1);
        mImageView2 = (ImageView)root.findViewById(R.id.imageView2);
        mImageView3 = (ImageView)root.findViewById(R.id.imageView3);
        mImageView4 = (ImageView)root.findViewById(R.id.imageView4);

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        return root;

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mMainImage.getId()){
//            if(!imageClicked){
//                mImageFrom.setVisibility(View.VISIBLE);
//                imageClicked = true;
//            }else{
//                mImageFrom.setVisibility(View.GONE);
//                imageClicked = false;
//            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true)
                    .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, REQUEST_CODE_FROM_GALLERY_IMAGE);
                        }
                    })
                    .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onLargeImageCapture(root);
                        }
                    });
            AlertDialog alert = builder.create();
            Window window = alert.getWindow();
            window.setGravity(Gravity.TOP);
            alert.show();

        }else if(v.getId() == buttonCamera.getId()){
             onLargeImageCapture(root);

        }else if(v.getId() == buttonGallery.getId()){
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, REQUEST_CODE_FROM_GALLERY_IMAGE);
        }else if(btnPost.getId() == v.getId()){

           if( getTextFromFields()){
               progressDialog = ProgressDialog.show(getActivity(),"",getActivity().getResources().getString(R.string.loading));
               ParseObject recipe1 = new ParseObject("recipe");
               recipe1.put("userId",currentUser.getObjectId() );
               recipe1.put("title",titleText);
               recipe1.put("description", descriptionText);
               recipe1.put("userName",currentUser.get("username"));
               recipe1.put("category", category);
               recipe1.put("ingredients", Arrays.asList(ingredients));
               recipe1.put("quantity", Arrays.asList(qtyStr));
               recipe1.put("longDescription",londDescription.getText().toString());
               if(file!=null) {
                   recipe1.put("mainImage", file);
                   if(photo != null){
   //                    photo.recycle();
//                       photo = null;
//                       System.gc();
                   }

               }
               if(((CheckBox)mIsPublic).isChecked()){
                   recipe1.put("public","public");
               } else {
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
        }else if(mIsPublic.getId() == v.getId()){
            if( mIsPublic.isChecked()){
                Utils.showAlert(getActivity(),"Alert", "All users will see your recipe! Need to fill all fields");
                isPublic = true;
            }else{
                isPublic = false;
            }

        }else if(btnPlus.getId() == v.getId()){

            addIngredientField();
        }
        //Adding Images
        else if(mImageView1.getId()  == v.getId()){
            takeImageDialog(1);
        }else if(mImageView2.getId()  == v.getId()){
            takeImageDialog(2);
        }else if(mImageView3.getId()  == v.getId()){
            takeImageDialog(3);
        }else if(mImageView4.getId()  == v.getId()){
            takeImageDialog(4);
        }
    }

    public void takeImageDialog(int imageId){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true)
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = builder.create();
        Window window = alert.getWindow();
        window.setGravity(Gravity.CENTER_HORIZONTAL);

        alert.show();
        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button a = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        b.setBackgroundColor((getResources().getColor(R.color.blue)));
        a.setBackgroundColor((getResources().getColor(R.color.blue)));
    }

    public void addIngredientField(){
        LinearLayout ln;
        EditText ed;
        TextView tv;
        EditText qty;

        ln = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.ingredient_row_layout,null);
        ln.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120));

        ed = (EditText)ln.findViewById(R.id.ingrField);
        allEds.add(ed);

        qty = (EditText)ln.findViewById(R.id.qty);
        allQty.add(qty);

        tv = (TextView)ln.findViewById(R.id.counter);
        tv.setText(Integer.toString(count+1));
        count++;

        recipeLayout.addView(ln);
    }

    public boolean getTextFromFields(){
        ingredients = new String[allEds.size()];
        for(int i=0; i < allEds.size(); i++){
            ingredients[i] = allEds.get(i).getText().toString();
        }
        qtyStr = new String [allQty.size()];
        for(int i=0; i < allQty.size(); i++){
            qtyStr[i] = allQty.get(i).getText().toString();
        }

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
//        bm.recycle();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bitmap imageBitmap = null;
            // Bitmap scaledBitmap = null;

            if (resultCode == getActivity().RESULT_OK) {
                if (requestCode == REQUEST_CODE_HIGH_QUALITY_IMAGE) {
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mHighQualityImageUri);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String picturePath = mHighQualityImageUri.getPath(); //imageCursor.getString(fileColumnIndex);


                    try {
                        ExifInterface exifInterface = new ExifInterface(picturePath);
                        orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (Build.MANUFACTURER.equals("LGE") || (Build.MANUFACTURER.equals("Sony")) || Build.MANUFACTURER.equals("samsung")) {
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                        }

                        imageBitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth(), imageBitmap.getHeight(), true), 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);

                    }
                } else {

                    Uri selectedImage = data.getData();
                    if (!ifVideo(selectedImage)) {
                        videoSelected = false;
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getActivity().getContentResolver().query(
                                selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();

                        BitmapFactory.Options bitmapFatoryOptions = new BitmapFactory.Options();
                        bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                        bitmapFatoryOptions.inPurgeable = true;

                        imageBitmap = BitmapFactory.decodeFile(filePath, bitmapFatoryOptions);

                        try {

                            ExifInterface exifInterface = new ExifInterface(filePath);
                            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (Build.MANUFACTURER.equals("LGE") || (Build.MANUFACTURER.equals("Sony")) || Build.MANUFACTURER.equals("samsung")) {
                            Matrix matrix = new Matrix();

                            if (orientation == 6) {
                                matrix.postRotate(90);

                            } else if (orientation == 3) {
                                matrix.postRotate(180);

                            } else if (orientation == 8) {
                                matrix.postRotate(270);
                            }
                            imageBitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth(), imageBitmap.getHeight(), true), 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);

                        }
                    } else {
                        videoSelected = true;
                       Utils.showAlert(getActivity(),"Error!","Cannot load video!");
                    }
                }
                if (!videoSelected) {
                 photo = imageBitmap;
                    if(photo.getHeight() > 4095 || photo.getWidth() > 4095) {
                        double ratio = 1.0;
                        double tmpHeight= photo.getHeight();
                        double tmpWidth = photo.getWidth();
                        if(photo.getHeight()>photo.getWidth()) {
                            ratio = tmpHeight/tmpWidth;
                        }else if(photo.getWidth() >photo.getHeight()){
                            ratio = tmpWidth/tmpHeight;
                        }
                        tmpHeight = photo.getHeight()/ratio;
                        tmpWidth = photo.getWidth()/ratio;
                        photo = getResizedBitmap(photo, (int)tmpWidth, (int)tmpHeight);
                    }
                    mMainImage.setImageBitmap(photo);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    //    Compress image to lower quality scale 1 - 100
                    photo.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                    byte[] image = stream.toByteArray();

                    // Create the ParseFile
                   file  = new ParseFile("picture_1.jpeg", image);


                }
            }
        }catch (OutOfMemoryError e){
            Utils.showAlert(getActivity(),"Error", "No memory!");
        }
    }

    public void onLargeImageCapture(View v) {

        mHighQualityImageUri = generateTimeStampPhotoFileUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
        startActivityForResult(intent, REQUEST_CODE_HIGH_QUALITY_IMAGE);

    }

    private Uri generateTimeStampPhotoFileUri() {

        Uri photoFileUri = null;
        File outputDir = getPhotoDirectory();
        if (outputDir != null) {
            Time t = new Time();
            t.setToNow();
            File photoFile = new File(outputDir, System.currentTimeMillis()
                    + ".jpg");
            photoFileUri = Uri.fromFile(photoFile);
        }
        return photoFileUri;
    }



    public boolean ifVideo(Uri uri){
        if(uri.toString().contains("video")){
            return true;
        }
        return false;
    }

    private File getPhotoDirectory() {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            outputDir = new File(photoDir, getString(R.string.app_name));
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Log.v("Test", "NO Direcrory");
                    outputDir = null;
                }
        }
        return outputDir;
    }

}
