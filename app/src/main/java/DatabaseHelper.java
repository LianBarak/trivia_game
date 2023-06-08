package com.example.trivia_game;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private Context mContext;
    private FirebaseDatabase database;
    public  ArrayList<User> usersArrayList = new ArrayList<>();
    public ArrayList<Question> questionsArrayList = new ArrayList<>();
    public ArrayList<Ranking> rankingArrayList = new ArrayList<>();

    private static final String TABLE_USERS = "users";
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_CATEGORIES = "categories";

    public DatabaseHelper(Context context) {
        mContext = context;
        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        // Retrieve data
        getAllUsers();
        getAllRanking();
        getAllQuestions();
    }



    public void insertNewUser(User user) {
        DatabaseReference usersRef = database.getReference(TABLE_USERS);
        DatabaseReference newUserRef = usersRef.push();
        user.setUserId(newUserRef.getKey());
        newUserRef.child("userData").setValue(user);
        newUserRef.child("ranking").setValue(new Ranking(user.getUsername(), 0,0,0,0));

    }


    public boolean isUserExists(String username) {
        boolean r = false;
        for(User user : usersArrayList){

            if(user.getUsername().equals(username)){
                r = true;
                break;
            }
        }
        return r;
    }



    public User getUserByUsername(String username) {
        User ruser = new User();
        for(User user : usersArrayList){
            Toast.makeText(mContext,"User list exists",Toast.LENGTH_SHORT).show();

            if(user.getUsername().equals(username)){
                ruser = user;
                break;
        }
        }
        return ruser;
    }



    public void setUser(User user, String oldUsername) {
        // Update user details
        DatabaseReference userRef = database.getReference(TABLE_USERS).child(user.getUserId());
        userRef.child("userData").setValue(user);
        userRef.child("ranking").child("username").setValue(user.getUsername());

        }

    public boolean loginValidator(String username, String password) {
        boolean r = false;
        if (isUserExists(username)) {
            User user = getUserByUsername(username);
            r = user.getPassword().equals(password);
        }

        return r;
    }

    public void getAllRanking() {
        DatabaseReference rankingsRef = database.getReference(TABLE_USERS);
        rankingsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot.exists()) {
                            ArrayList<Ranking> rankings = new ArrayList<>();
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                Ranking crank = userSnapshot.child("ranking").getValue(Ranking.class);
                                rankings.add(crank);
                            }
                            rankingArrayList = rankings;
                        }
                    }
                }
            }});
    }


    public boolean isEmailExists(String email) {
        boolean r = false;
        for(User user: usersArrayList){
            if(user.getEmail().equals(email))
                r = true;
        }
        return r;
    }

    public void getAllQuestions() {
        DatabaseReference questionsRef = database.getReference(TABLE_QUESTIONS);
        questionsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        if(dataSnapshot.exists()){
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                Question question = userSnapshot.getValue(Question.class);
                                if(questionsArrayList.contains(question)){
                                    questionsArrayList.add(question);
            }
        }}}}}});
    }



    public void getAllUsers() {
        DatabaseReference usersRef = database.getReference(TABLE_USERS);
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            User cuser = userSnapshot.child("userData").getValue(User.class);
                            if(!usersArrayList.contains(cuser)) {
                                usersArrayList.add(cuser);
                                }
                        }
                    }else{
                        Toast.makeText(mContext, "data does not exists", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "not succeful", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
