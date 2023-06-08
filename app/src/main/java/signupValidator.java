package com.example.trivia_game;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class signupValidator {
    public signupValidator() {
    }
    public boolean emailValidator(String emailToText) {
        // Android offers the inbuilt patterns which the entered
        // data from the EditText field needs to be compared with
        // In this case the entered data needs to compared with
        // the EMAIL_ADDRESS, which is implemented same below
        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            return true;
        } else {
            return false;
        }
    }
    public boolean passwordValidator(String password)
    {
        Pattern passwordPattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@#$%^&+=]{8,24}");
        return !TextUtils.isEmpty(password) && passwordPattern.matcher(password).matches();
    }
}


