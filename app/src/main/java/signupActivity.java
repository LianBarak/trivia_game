package com.example.trivia_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateIntervalInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.validation.Validator;

public class signupActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    EditText EDTusername;
    EditText EDTpassword;
    EditText EDTemail;
    Button submit;
    TextView invalidPass;
    Button btnLogin;
    signupValidator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DatabaseHelper(this);

        EDTusername = findViewById(R.id.username);
        EDTpassword = findViewById(R.id.pass);
        EDTemail = findViewById(R.id.email);
        submit = findViewById(R.id.newSubmit);
        invalidPass = findViewById(R.id.invalidPass);
        btnLogin = findViewById(R.id.btnLogin);
        validator = new signupValidator();




        submit.setOnClickListener(this::onClick);
        btnLogin.setOnClickListener(this::onClickLogin);
    }

    private void onClickLogin(View v) {
        Intent i = new Intent(this, loginActivity.class);
        startActivity(i);
    }

    public void onClick(View v) {
        String userName = EDTusername.getText().toString();
        String userPass = EDTpassword.getText().toString();
        String userEmail = EDTemail.getText().toString();
        String userProfileImage = "android.resource://" + getPackageName() + "/" + R.drawable.blank;

        boolean isUserExists = dbHelper.isUserExists(userName);
        boolean isEmailExists = dbHelper.isEmailExists(userEmail);


        if (userName.isEmpty() || userEmail.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(signupActivity.this, "מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        } else if (!validator.emailValidator(userEmail)) {
            Toast.makeText(this, "אימייל לא תקני!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!validator.passwordValidator(userPass)) {
            Toast.makeText(this, "סיסמא לא תקנית", Toast.LENGTH_SHORT).show();
            invalidPass.setText("סיסמא תקנית כוללת 8-24 תווים באנגלית ומספרים, לפחות אות גדולה אחת ואות קטנה אחת");
            return;
        } else if (isUserExists) {
            Toast.makeText(signupActivity.this, "שם משתמש כבר קיים", Toast.LENGTH_SHORT).show();
            return;
        } else if (isEmailExists) {
            Toast.makeText(this, "אימייל קיים במערכת", Toast.LENGTH_SHORT).show();
            return;
        }

        User newuser = new User(userName, userPass, userEmail, userProfileImage);
        dbHelper.insertNewUser(newuser);


            Toast.makeText(signupActivity.this, "user " + userName + " inserted", Toast.LENGTH_SHORT).show();
            SaveSharedPreference.setUserName(getApplicationContext(), userName);

            // Move to profile activity
            Intent i = new Intent(this, UserProfileActivity.class);
            startActivity(i);
    }
}


