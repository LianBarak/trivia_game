package com.example.trivia_game;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;

    private EditText etUsername, etPassword, etEmail;
    private CircleImageView ivProfileImage;
    private Button btnSaveChanges;
    private String currentPhoto;
    private DatabaseHelper db;
    private User currUser;
    private Ranking currRank;
    private signupValidator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = new DatabaseHelper(this);

        currUser = (User) getIntent().getSerializableExtra("user");
        currRank = (Ranking) getIntent().getSerializableExtra("ranking");
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        etEmail = findViewById(R.id.email);
        ivProfileImage = findViewById(R.id.profile_image);
        btnSaveChanges = findViewById(R.id.save_changes);

        // Set the current user data
         etUsername.setText(currUser.getUsername());
        etPassword.setText(currUser.getPassword());
        etEmail.setText(currUser.getEmail());
        Glide.with(this).load(currUser.getProfileImage()).into(ivProfileImage);
        currentPhoto = currUser.getProfileImage();

        // Open camera when profile image is clicked
        ivProfileImage.setOnClickListener(view -> openCamera());

        // Open gallery when profile image is long clicked
        ivProfileImage.setOnLongClickListener(view -> onClickProfile(view));



        // Save changes to the database
        btnSaveChanges.setOnClickListener(this::onClickSaveChanges);

        validator = new signupValidator();

    }

    private void onClickSaveChanges(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();
        String oldUsername = currUser.getUsername();

        // input validation
        if (!validator.emailValidator(email)) {
            Toast.makeText(this, "אימייל לא תקני!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!validator.passwordValidator(password)) {
            Toast.makeText(this, "סיסמא לא תקנית, סיסמה תקנית מכילה אות גדולה, אות קטנה, מספרים", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!currUser.getUsername().equals(username)) {
            boolean isUsernameExists = db.isUserExists(username);
            if (isUsernameExists){
                Toast.makeText(this, "שם משתמש קיים במערכת", Toast.LENGTH_SHORT).show();
                return;
            }
            currUser.setUsername(username);
        }
        else if (!currUser.getEmail().equals(email)) {
            boolean isEmailExists = db.isEmailExists(email);
            if(isEmailExists){
                Toast.makeText(this, "אימייל קיים במערכת", Toast.LENGTH_SHORT).show();
                return;
            }
            currUser.setEmail(email);
        }


        // Update the current user with the new data
        currUser.setPassword(password);
        currUser.setProfileImage(currentPhoto);
        currUser.setUsername(username);


        // Update the user in the database
        db.setUser(currUser,oldUsername);
        SaveSharedPreference.clearUserName(getApplicationContext());
        SaveSharedPreference.setUserName(getApplicationContext(), currUser.getUsername());

        // get to profile page to see changes
        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);
    }

    private boolean onClickProfile(View view) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_profile, null);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        // Set the click listeners for the buttons
        Button btnGallery = dialogView.findViewById(R.id.btn_gallery);
        Button btnCamera = dialogView.findViewById(R.id.btn_camera);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                dialog.dismiss();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
        return true;
    }




    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.trivia_game.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                savePhotoToGallery(photoURI);
            } else {
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }


    private void openGallery () {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
    }

    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Glide.with(this).load(currentPhoto).into(ivProfileImage);
            } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY && data != null) {
                currentPhoto = data.getData().toString();
                Glide.with(this).load(currentPhoto).into(ivProfileImage);
            }
        }
    }


    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        currentPhoto = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void savePhotoToGallery(Uri photoUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(photoUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "Photo saved to gallery", Toast.LENGTH_SHORT).show();
    }
}