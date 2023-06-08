package com.example.trivia_game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
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
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingActivity extends AppCompatActivity {

    private ListView listViewRanking;
    private RankingAdapter rankingAdapter;
    private User currUser;
    private Ranking currRanking;
    DatabaseReference usersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        currUser = (User) getIntent().getSerializableExtra("user");
        currRanking = (Ranking) getIntent().getSerializableExtra("ranking");

        listViewRanking = findViewById(R.id.ranking_list);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        String username = SaveSharedPreference.getUserName(getApplicationContext());

        getUserByUsername(this, username);

    }

    private void getUserByUsername(Context mContext, String username) {
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        ArrayList<Ranking> rankings = new ArrayList<>();
                        ArrayList<User> users= new ArrayList<>();
                        DataSnapshot dataSnapshot = task.getResult();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            User cuser = userSnapshot.child("userData").getValue(User.class);
                            Ranking crank = userSnapshot.child("ranking").getValue(Ranking.class);
                            rankings.add(crank);
                            users.add(cuser);

                        }
                        updateRankings(rankings,users);
                    }
                }else{
                    Toast.makeText(mContext, "Error not successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void updateRankings(ArrayList<Ranking> rankings, ArrayList<User> users){
        Collections.sort(rankings, new Comparator<Ranking>() {
            @Override
            public int compare(Ranking r1, Ranking r2) {
                return Long.compare(r2.getScore(), r1.getScore());
            }
        });


        // Set up the adapter for the ranking list view
        rankingAdapter = new RankingAdapter(this, rankings, users);
        listViewRanking.setAdapter(rankingAdapter);
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
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_new_game:
                Intent i = new Intent(this, NewGameActivity.class);
                i.putExtra("user", currUser);
                i.putExtra("ranking", currRanking);
                startActivity(i);
                return true;
            case R.id.action_ranking:
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
