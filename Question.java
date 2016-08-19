package com.example.hunter.alliancepicker;

import java.util.ArrayList;

/**
 * Created by Hunter on 8/25/2015.
 */
public class Question {
    static ArrayList<Question> allQuestions = new ArrayList<>();
    String question = "";
    int multiplier = 1;
    String inputType = "";

    public Question(String iQuestion, int iMultiplier, String inpType) {
        multiplier = iMultiplier;
        question = iQuestion;
        inputType = inpType;
        allQuestions.add(this);
    }
}
