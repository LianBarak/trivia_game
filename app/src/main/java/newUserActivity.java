package com.example.trivia_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class newUserActivity extends AppCompatActivity {
    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_users);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(this::onClick);
        register.setOnClickListener(this::onClick);
    }


    public void onClick(View v) {
        if(login == v)
        {
            Intent login_intent=new Intent(this, loginActivity.class);
            startActivity(login_intent);
        }
        else if(register == v)
        {
            Intent reg_intent=new Intent(this,signupActivity.class);
            startActivity(reg_intent);
        }
    }
}
