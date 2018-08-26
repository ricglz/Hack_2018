package com.example.ricardo.hack_2018;

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

public class ClockActivity extends AppCompatActivity {
    private EditText mEditTextInputMin;
    private EditText mEditTextInputSec;
    //private TextView mTextViewCoundown;
    private Button mButtonSet;
    //private Button mButtonStartPause;
   // private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long startTimeTemp;
    private long mTimeLeftInMillis;
    private long timeLeftTemp;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        mEditTextInputMin = findViewById(R.id.editTextInputMin);
        mEditTextInputSec = findViewById(R.id.editTextInputSec);
        //mTextViewCoundown = findViewById(R.id.textViewCountdown);

        mButtonSet = findViewById(R.id.buttonSet);
       // mButtonStartPause = findViewById(R.id.buttonStartPause);
       // mButtonReset = findViewById(R.id.buttonReset);

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMin = mEditTextInputMin.getText().toString();
                String inputSec = mEditTextInputSec.getText().toString();
                if (inputMin.length() == 0 || inputSec.length() == 0) {
                    Toast.makeText(ClockActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                mTimerRunning = true;
                long millisInput = Long.parseLong(inputMin) * 60000 + Long.parseLong(inputSec) * 1000;
                if (millisInput == 0) {
                    Toast.makeText(ClockActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                startTimeTemp = getIntent().getLongExtra("mStartTimeInMills", 0);
                //timeLeftTemp = getIntent().getLongExtra("mTimeLeftInMills", 0);

                setTime(millisInput);
                mEditTextInputMin.setText("");
                mEditTextInputSec.setText("");
            }
        });


        /*mButtonStartPause.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }

    private void setTime(long milliseconds) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        //Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
        //if (!mTimerRunning)
            i.putExtra("milliseconds", milliseconds);
        //else {
        //    i.putExtra("startTimeTemp", startTimeTemp);
            //i2.putExtra("timeLeftTemp", timeLeftTemp);
            //startActivity(i2);
        //}
        startActivity(i);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

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

        /*SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", mStartTimeInMillis);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mTimeLeftInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", mTimerRunning);*/

    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*private void startTimer() {
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

                startActivity(new Intent(ClockActivity.this, MainActivity.class));
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
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int minutes = (int) ((mTimeLeftInMillis / 1000) / 60);
        int seconds = (int) ((mTimeLeftInMillis / 1000) % 60);

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        mTextViewCoundown.setText(timeLeftFormatted);
    }
    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInputMin.setVisibility(View.INVISIBLE);
            mEditTextInputSec.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mEditTextInputMin.setVisibility(View.VISIBLE);
            mEditTextInputSec.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
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
    }
        */

/*
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

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", mStartTimeInMillis);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mTimeLeftInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", mTimerRunning);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", mEndTime);
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
*/

}


