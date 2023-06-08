package com.example.trivia_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button enter;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.welcome);
        enter = findViewById(R.id.enter);
        enter.setOnClickListener(this);


        if (SaveSharedPreference.getUserName(MainActivity.this).length() != 0) {
            tv.setText(" ברוכים הבאים " + SaveSharedPreference.getUserName(getApplicationContext()));
        }
    }

    public void onClick(View v) {
        if (v == enter) {
            if (SaveSharedPreference.getUserName(getApplicationContext()).length() == 0) {
                Intent i = new Intent(MainActivity.this, newUserActivity.class);
                startActivity(i);
                // new user activity
            } else {
                Intent i = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(i);
                // Go to profile
            }
        }
    }
}
