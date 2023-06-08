package com.example.trivia_game;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class NewGameActivity extends AppCompatActivity {
    private User currUser;
    private Ranking currRanking;
    private ArrayList<Question> questionsArrayList;
    private DatabaseReference questionsRef;

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        currUser = (User) getIntent().getSerializableExtra("user");
        currRanking = (Ranking) getIntent().getSerializableExtra("ranking");

        questionsArrayList = new ArrayList<>();
        questionsRef = FirebaseDatabase.getInstance().getReference("questions");

        startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveQuestions();
            }
        });
    }

    private void retrieveQuestions() {
        questionsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        for (DataSnapshot questionSnapshot : task.getResult().getChildren()) {
                            Question question = questionSnapshot.getValue(Question.class);
                            questionsArrayList.add(question);
                        }

                        // Proceed to start the game once questions are retrieved
                        startGame();
                    } else {
                        Toast.makeText(NewGameActivity.this, "No questions available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewGameActivity.this, "Failed to retrieve questions", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startGame() {
        if (questionsArrayList.size() < 5) {
            Toast.makeText(this, "Not enough questions available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Shuffle the questions
        Collections.shuffle(questionsArrayList);

        // Get the first 5 questions
        ArrayList<Question> selectedQuestions = new ArrayList<>(questionsArrayList.subList(0, 5));

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("questions", selectedQuestions);
        intent.putExtra("user", currUser);
        intent.putExtra("ranking", currRanking);
        startActivityForResult(intent, 1);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_profile:
                Intent i = new Intent(this, UserProfileActivity.class);
                startActivity(i);
                return true;
            case R.id.action_new_game:
                return true;
            case R.id.action_ranking:
                Intent intent=new Intent(this,RankingActivity.class);
                intent.putExtra("user", currUser);
                intent.putExtra("ranking", currRanking);
                startActivity(intent);
                return true;
            case R.id.logout:
                SaveSharedPreference.clearUserName(getApplicationContext());
                Intent intent1 = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                return true;
            case R.id.editProfile:
                Intent intent2=new Intent(this,EditProfileActivity.class);
                intent2.putExtra("user", currUser);
                intent2.putExtra("ranking", currRanking);
                startActivity(intent2);
                return true;
        }
        return true;
    }
}
