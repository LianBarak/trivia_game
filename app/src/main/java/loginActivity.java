package com.example.trivia_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    EditText EDTusername;
    EditText EDTpassword;
    Button submit;
    Button btnRegister;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DatabaseHelper(this);
        EDTusername = findViewById(R.id.username);
        EDTpassword = findViewById(R.id.pass);
        submit = findViewById(R.id.loginSubmit);
        btnRegister = findViewById(R.id.btnRegister);

        submit.setOnClickListener(this::onClick);
        btnRegister.setOnClickListener(this::onClickRegister);

    }

    private void onClickRegister(View view) {
        Intent i=new Intent(this,signupActivity.class);
        startActivity(i);
    }

    public void onClick(View v) {
        String userName = EDTusername.getText().toString();
        String userPass = EDTpassword.getText().toString();

        if (userName.isEmpty() ||  userPass.isEmpty()) {
            Toast.makeText(loginActivity.this, "מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }


        if(dbHelper.loginValidator(userName, userPass)){
            SaveSharedPreference.setUserName(getApplicationContext(), userName);
            Toast.makeText(loginActivity.this, "user " + userName + " logged in", Toast.LENGTH_SHORT).show();
            SaveSharedPreference.setUserName(loginActivity.this, userName);
            //move activity
            Intent i=new Intent(this,UserProfileActivity.class);

            startActivity(i);
        }
        else{
            Toast.makeText(loginActivity.this, "שם משתמש או סיסמה לא נכונים", Toast.LENGTH_SHORT).show();
        }

    }

}
