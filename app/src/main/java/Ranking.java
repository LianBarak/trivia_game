package com.example.trivia_game;

import java.io.Serializable;

public class Ranking implements Serializable {
    private String username;
    private long score;
    private long win, lost, games;

    public Ranking() {
        this.username = "no user";
        this.score = 0;
        this.games = 0;
        this.win = 0;
        this.lost = 0;
    }

    public Ranking(String username, long score, long games,long win, long lost) {
        this.username =username;
        this.score = score;
        this.games = games;
        this.win = win;
        this.lost = lost;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getWin() {
        return win;
    }

    public void setWin(long win) {
        this.win = win;
    }

    public long getLost() {
        return lost;
    }

    public void setLost(long lost) {
        this.lost = lost;
    }

    public long getGames() {
        return games;
    }

    public void setGames(long games) {
        this.games = games;
    }

}
