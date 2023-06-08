package com.example.trivia_game;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private String question;
    private String right_answer;
    private String wrong_answer1;
    private String wrong_answer2;
    private String wrong_answer3;
    private String category;

    public Question(){
        this.question = " ";
        this.category = " ";
        this.right_answer = " ";
        this.wrong_answer1 = " ";
        this.wrong_answer2 = " ";
        this.wrong_answer3 = " ";
    }
    public Question(String question,
                    String right_answer,
                    String wrong_answer1,
                    String wrong_answer2,
                    String wrong_answer3,
                    String category
                    ) {
        this.question = question;
        this.category = category;
        this.right_answer = right_answer;
        this.wrong_answer1 = wrong_answer1;
        this.wrong_answer2 = wrong_answer2;
        this.wrong_answer3 = wrong_answer3;
    }

    public String getQuestion() {
        return question;
    }

    public String getRight_answer() {
        return right_answer;
    }

    public String getWrong_answer1() {
        return wrong_answer1;
    }

    public String getWrong_answer2() {
        return wrong_answer2;
    }

    public String getWrong_answer3() {
        return wrong_answer3;
    }

    public String getCategory() {
        return category;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setRight_answer(String right_answer) {
        this.right_answer = right_answer;
    }

    public void setWrong_answer1(String wrong_answer1) {
        this.wrong_answer1 = wrong_answer1;
    }

    public void setWrong_answer2(String wrong_answer2) {
        this.wrong_answer2 = wrong_answer2;
    }

    public void setWrong_answer3(String wrong_answer3) {
        this.wrong_answer3 = wrong_answer3;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public ArrayList<String> getAllAnswers()
    {
        ArrayList<String> answers = new ArrayList<>();
        answers.add(getRight_answer());
        answers.add(getWrong_answer1());
        answers.add(getWrong_answer2());
        answers.add(getWrong_answer3());
        return answers;
    }
}
