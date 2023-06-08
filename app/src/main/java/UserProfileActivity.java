package com.example.trivia_game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    CircleImageView profileImage;
    TextView usernameText;
    TextView totalScoreText;
    TextView totalGamesPlayedText;
    TextView totalWinText;
    TextView totalLostText;
    DatabaseReference usersRef;
    User currUser;
    Ranking currRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImage = findViewById(R.id.profile_image);
        usernameText = findViewById(R.id.username_textview);
        totalScoreText = findViewById(R.id.total_score_textview);
        totalGamesPlayedText = findViewById(R.id.total_games_played_textview);
        totalWinText = findViewById(R.id.total_wins_textview);
        totalLostText = findViewById(R.id.total_losses_textview);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        String username = SaveSharedPreference.getUserName(getApplicationContext());

        getUserByUsername(this,username);
    }

    private void getUserByUsername(Context mContext, String username) {
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            User cuser = userSnapshot.child("userData").getValue(User.class);
                            Ranking crank = userSnapshot.child("ranking").getValue(Ranking.class);
                            if (cuser.getUsername().equals(username)) {
                                updateUI(cuser, crank);
                                break;
                            }
                        }
                    }
                }else{
                    Toast.makeText(mContext, "Error not successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void updateUI(User user, Ranking ranking) {
        currUser = user;
        currRank = ranking;
        Glide.with(this)
                .load(user.getProfileImage())
                .circleCrop()
                .placeholder(R.drawable.blank)
                .into(profileImage);

        usernameText.setText(user.getUsername());
        totalScoreText.setText(ranking.getScore() + " ניקוד ");
        totalGamesPlayedText.setText(ranking.getGames() + " מספר משחקים ");
        totalWinText.setText(ranking.getWin() + " מספר נצחונות");
        totalLostText.setText(ranking.getLost() + " מספר הפסדים ");
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
                return true;
            case R.id.action_new_game:
                Intent i = new Intent(this, NewGameActivity.class);
                i.putExtra("user", currUser);
                i.putExtra("ranking", currRank);

                startActivity(i);
                return true;
            case R.id.action_ranking:
                Intent intent=new Intent(this,RankingActivity.class);
                intent.putExtra("user", currUser);
                intent.putExtra("ranking", currRank);
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
                intent2.putExtra("ranking", currRank);

                startActivity(intent2);
                return true;
        }
        return true;
    }
}
