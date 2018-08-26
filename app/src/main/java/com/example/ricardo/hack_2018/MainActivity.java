package com.example.ricardo.hack_2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> memesUrls;
    private NetworkImageView memeImage;
    private View view;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = getWindow().getDecorView().getRootView();

        memeImage = view.findViewById(R.id.meme);

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        System.out.println("Ok, let's see how it works");

        parseMemesJson();

    }

    private void parseMemesJson() {
        String url = "https://api.imgflip.com/get_memes";

        memesUrls = new ArrayList<String>();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try {

                            JSONObject jsonObject = response.getJSONObject("data");
                            JSONArray jsonArray = jsonObject.getJSONArray("memes");
                            int amountOfMemes = jsonArray.length();

                            for(int i = 0; i < amountOfMemes; i++){
                                JSONObject meme = jsonArray.getJSONObject(i);

                                String imageUrl = meme.getString("url");

                                memesUrls.add(imageUrl);

                            }

                            String memeUrl = memesUrls.get((int) (amountOfMemes * Math.random()));
                            memeImage.setImageUrl(memeUrl,mImageLoader);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
            }
        });


        mRequestQueue.add(request);

    }


}
