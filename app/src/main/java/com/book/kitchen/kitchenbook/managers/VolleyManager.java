package com.book.kitchen.kitchenbook.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by use on 19/11/2015.
 */
public class VolleyManager {
    private static VolleyManager ourInstance = new VolleyManager();

    private ImageLoader imageLoader;

    public static VolleyManager getInstance() {
        return ourInstance;
    }

    private RequestQueue mRequestQueue;

    private VolleyManager() {

    }

    public void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(400);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void addToRequestQueue(ImageRequest request) {
        mRequestQueue.add(request);
    }


    public ImageRequest createImageRequest(String url,Response.Listener<Bitmap> listener,Response.ErrorListener errorListener) {
        return new ImageRequest(url,listener,0,0,null,errorListener);
    }

}
