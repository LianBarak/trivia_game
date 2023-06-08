package com.example.trivia_game;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {
    DatabaseReference userRef;
    CountDownTimer timer;
    TextView tvCountdown, tvQuestion, tvNumbering;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;
    private int questionNumber = 0, wrongAnswers = 0, rightAnswers = 0;
    Question currentQuestion;
    User user;
    Ranking ranking;
    private ArrayList<Question> questionList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tvCountdown = findViewById(R.id.tvCountdown);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvNumbering = findViewById(R.id.tvNumbering);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);

        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Get questions list from intent
        Intent i = getIntent();
        questionList = (ArrayList<Question>) i.getSerializableExtra("questions");

        // Get user information from intent
        user = (User) i.getSerializableExtra("user");
        ranking = (Ranking) i.getSerializableExtra("ranking") ;

        // Set on click listeners to buttons
        btnAnswer1.setOnClickListener(this::onClick);
        btnAnswer2.setOnClickListener(this::onClick);
        btnAnswer3.setOnClickListener(this::onClick);
        btnAnswer4.setOnClickListener(this::onClick);

        // Set timer
        timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update a TextView to display the time
                tvCountdown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Handle timer finish event
                // Automatically count as a wrong answer and move to the next question
                wrongAnswers++;
                newQuestion();
            }
        };

        startGame();
    }

    private void onClick(View v) {
        timer.cancel();
        currentQuestion.getRight_answer();
        Button selectedButton = (Button) v;
        if (selectedButton.getText().toString().equals(currentQuestion.getRight_answer())) {
            rightAnswers++;
            Toast.makeText(this, "תשובה נכונה! כל הכבוד", Toast.LENGTH_SHORT).show();

        } else {
            wrongAnswers++;
            Toast.makeText(this, "תשובה לא נכונה", Toast.LENGTH_SHORT).show();
        }

        // Disable buttons to prevent further interaction
        disableButtons();

        // Move to the next question
        newQuestion();
    }

    private void startGame() {
        // Start the game by displaying the first question
        newQuestion();
    }

    private void endGame() {
        boolean isWin = wrongAnswers < rightAnswers;
        boolean isGold = rightAnswers == 5;

        // Update win or loss status in the database
        long score = calculateScore(isWin, isGold);
        long gamesPlayed = ranking.getGames() + 1;
        long wins = ranking.getWin() + (isWin ? 1 : 0);
        long losses = ranking.getLost() + (isWin ? 0 : 1);
        updateUserData(score, gamesPlayed, wins, losses);

        showGameResultAlertDialog(isWin, isGold);
    }

    private void newQuestion() {
        if (questionNumber < questionList.size()) {
            currentQuestion = questionList.get(questionNumber);
            questionNumber++;

            // Update the question numbering text
            String numberingText = String.format("%d/%d", questionNumber, questionList.size());
            tvNumbering.setText(numberingText);

            // Display the current question
            displayQuestion();

            // Start the timer for the next question
            timer.start();
        } else {
            // If all questions have been answered, end the game
            endGame();
        }
    }

    private void displayQuestion() {
        // Shuffle answers
        ArrayList<String> answers = currentQuestion.getAllAnswers();
        Collections.shuffle(answers);

        // Set question on the layout
        tvQuestion.setText(currentQuestion.getQuestion());
        btnAnswer1.setText(answers.get(0));
        btnAnswer2.setText(answers.get(1));
        btnAnswer3.setText(answers.get(2));
        btnAnswer4.setText(answers.get(3));

        // Enable buttons
        enableButtons();
    }

    private void disableButtons() {
        btnAnswer1.setEnabled(false);
        btnAnswer2.setEnabled(false);
        btnAnswer3.setEnabled(false);
        btnAnswer4.setEnabled(false);
    }

    private void enableButtons() {
        btnAnswer1.setEnabled(true);
        btnAnswer2.setEnabled(true);
        btnAnswer3.setEnabled(true);
        btnAnswer4.setEnabled(true);
    }

    private long calculateScore(boolean isWin, boolean isGold) {
        long score = ranking.getScore();
        if (isWin) {
            score += isGold ? 150 : 100;
        } else {
            score = Math.max(score - 50, 0);
        }
        return score;
    }

    private void showGameResultAlertDialog(boolean isWin, boolean isGold) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Result");

        if (isWin) {
            if (isGold) {
                builder.setMessage("כל הכבוד! ניצחת ניצחון זהב\n ענית על כל התשובות נכון וקיבלת 150 נקודות");
            } else {
                builder.setMessage("כל הכבוד! ניצחת והרווחת 100 נקודות");
            }
        } else {
            builder.setMessage("אופס! הפסדת ואיבדת 50 נקודות");
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog and navigate back to NewGameActivity
                Intent intent = new Intent(GameActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void updateUserData(long score, long gamesPlayed, long wins, long losses) {
        ranking.setScore(score);
        ranking.setGames(gamesPlayed);
        ranking.setWin(wins);
        ranking.setLost(losses);

        userRef.child(user.getUserId()).child("userData").setValue(user);
        userRef.child(user.getUserId()).child("ranking").setValue(new Ranking(user.getUsername(), score, gamesPlayed, wins, losses));
    }
}
