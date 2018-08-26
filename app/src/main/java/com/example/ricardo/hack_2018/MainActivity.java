package com.example.ricardo.hack_2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Clock;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //private EditText mEditTextInputMin;
    //private EditText mEditTextInputSec;
    private TextView mTextViewCoundown;
    //private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private ArrayList<String> urls;
    private NetworkImageView image;
    private View view;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mEditTextInputMin = findViewById(R.id.editTextInputMin);
        //mEditTextInputSec = findViewById(R.id.editTextInputSec);
        mTextViewCoundown = findViewById(R.id.textViewCountdown);

        //mButtonSet = findViewById(R.id.buttonSet);
        mButtonStartPause = findViewById(R.id.buttonStartPause);
        mButtonReset = findViewById(R.id.buttonReset);

        long millisInput = getIntent().getLongExtra("milliseconds", 0);
        System.out.println("MILLISINPUT: " + millisInput);
        setTime(millisInput);


        /*mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMin = mEditTextInputMin.getText().toString();
                String inputSec = mEditTextInputSec.getText().toString();
                if (inputMin.length() == 0 || inputSec.length() == 0) {
                    Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(inputMin) * 60000 + Long.parseLong(inputSec) * 1000;
                if (millisInput == 0) {
                    Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }


                mEditTextInputMin.setText("");
                mEditTextInputSec.setText("");
            }
        });*/

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning)
                    pauseTimer();
                else
                    startTimer();
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        System.out.println("STARTTIME1: " + mStartTimeInMillis);
        resetTimer();
        //closeKeyboard();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftInMillis = l;
                updateCountDownText();;
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        System.out.println("LEFTTIME: " + mTimeLeftInMillis);
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int minutes = (int) ((mTimeLeftInMillis / 1000) / 60);
        int seconds = (int) ((mTimeLeftInMillis / 1000) % 60);

        System.out.println("MINUTES: " + minutes);
        System.out.println("SECONDS: " + seconds);

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        System.out.println("STRING: " + timeLeftFormatted);

        mTextViewCoundown.setText(timeLeftFormatted);
    }
    private void updateWatchInterface() {
        if (mTimerRunning) {
            //mEditTextInputMin.setVisibility(View.INVISIBLE);
            //mEditTextInputSec.setVisibility(View.INVISIBLE);
            //mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            //mEditTextInputMin.setVisibility(View.VISIBLE);
//            mEditTextInputSec.setVisibility(View.VISIBLE);
//            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000)
                mButtonStartPause.setVisibility(View.INVISIBLE);
            else
                mButtonStartPause.setVisibility(View.VISIBLE);

            if (mTimeLeftInMillis < mStartTimeInMillis)
                mButtonReset.setVisibility(View.VISIBLE);
            else
                mButtonReset.setVisibility(View.INVISIBLE);

        }
        System.out.println("STARTTIME2: " + mStartTimeInMillis);
    }

    /*private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();


        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);



        /*mStartTimeInMillis = prefs.getLong("startTimeInMillis", mStartTimeInMillis);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mTimeLeftInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", mTimerRunning);*/

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {

            //mEndTime = prefs.getLong("endTime", mEndTime);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }

    public void goToTime(View view) {
        Intent intent = new Intent(this, ClockActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.action_bar1, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int o=item.getItemId();
        if(o==R.id.tlbSettings) {
            Intent i= new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void parseMemesJson() {
        String url = "https://api.imgflip.com/get_memes";

        urls = new ArrayList<String>();

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

                                urls.add(imageUrl);

                            }

                            String memeUrl = urls.get((int) (amountOfMemes * Math.random()));
                            image.setImageUrl(memeUrl,mImageLoader);

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

    private void parseCats() {
        String url = "https://api.thecatapi.com/v1/images/search?format=src";
        image.setImageUrl(url, mImageLoader);

    }

    private void parseDogs() {
        String url = "https://dog.ceo/api/breeds/image/random";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String imageUrl = response.getString("message");
                            image.setImageUrl(imageUrl, mImageLoader);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);

      }


}

/*import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}*/
