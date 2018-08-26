package com.example.ricardo.hack_2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* getActionBar().setTitle("Settings");
        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        Spinner spinner3= (Spinner) findViewById(R.id.spinner3);
        spinner3.setAdapter(adapter);
        Spinner spinner4 = (Spinner)findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.color_array2, android.R.layout.simple_spinner_item);
        spinner4.setAdapter(adapter2);*/
        // #4B120E, #E6D199, #DFD88A, #C7E4E7
    }
}
