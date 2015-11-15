package com.book.kitchen.kitchenbook.fragments;

import android.app.Fragment;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.book.kitchen.kitchenbook.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by User on 15/11/2015.
 */
public class AddRecipe extends Fragment implements View.OnClickListener {

    ImageView  mMainImage;
    LinearLayout mImageFrom;
    boolean imageClicked = false;
    TextView buttonCamera;
    TextView buttonGallery;

    private static final int CAMERA_REQUEST = 1888;
    public static final int ACTIVITY_SELECT_IMAGE = 1889;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        mMainImage = (ImageView)root.findViewById(R.id.mainImage);
        mMainImage.setOnClickListener(this);

        mImageFrom = (LinearLayout)root.findViewById(R.id.choiseFrom);

        buttonCamera = (TextView)root.findViewById(R.id.btnCamera);
        buttonCamera.setOnClickListener(this);

        buttonGallery = (TextView)root.findViewById(R.id.btnGallery);
        buttonGallery.setOnClickListener(this);

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
        }
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
}
