package com.example.trivia_game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Ranking> mRankedUsers;
    private ArrayList<User> mUsers;

    public RankingAdapter(Context context, ArrayList<Ranking> rankedUsers, ArrayList<User> users) {
        mContext = context;
        mRankedUsers = rankedUsers;
        mUsers = users;
    }

    @Override
    public int getCount() {
        return mRankedUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mRankedUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ranking_item, parent, false);
        }


        Ranking rankingUser = mRankedUsers.get(position);
        User currUser = new User();
        for(User user: mUsers){
            if (user.getUsername().equals(rankingUser.getUsername())){
                currUser = user;
            }
        }

        TextView tvRanking = convertView.findViewById(R.id.tv_rank);
        TextView tvUsername = convertView.findViewById(R.id.tv_username);
        TextView tvTotalScore = convertView.findViewById(R.id.tv_score);
        CircleImageView profileImage = convertView.findViewById(R.id.iv_profile_image);

        tvRanking.setText(String.valueOf(position + 1)); // add 1 to position to start ranking from 1 instead of 0
        tvUsername.setText(rankingUser.getUsername());
        tvTotalScore.setText(String.valueOf(rankingUser.getScore()));
        Glide.with(mContext)
                .load(currUser.getProfileImage())
                .circleCrop()
                .placeholder(R.drawable.blank)
                .into(profileImage);
        return convertView;
    }
}

