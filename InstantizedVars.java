package com.example.hunter.alliancepicker;

import android.app.Application;

public class InstantizedVars extends Application {
    public static Boolean firstLoad = true;

    private Question q1 = new Question("Team Name",0 ,"Text"); //Text
    private Question q2 = new Question("Team Number",0, "Number"); //Number
    private Question q3 = new Question("Autonomous",5, "RatingBar");
    private Question q4 = new Question("Rescue Beacon",5, "RatingBar");
    private Question q5 = new Question("Mountain Park",5, "RatingBar");
    private Question q6 = new Question("Beacon Zone Park",5, "RatingBar");
    private Question q7 = new Question("Climbers in Bin",5, "RatingBar");
    private Question q8 = new Question("Teleop",5 ,"RatingBar");
    private Question q9 = new Question("Park on Mountain",5 ,"RatingBar");
    private Question q10 = new Question("Claiming Signal",5 ,"RatingBar");
    private Question q11 = new Question("Quality of Hang",5 ,"RatingBar");
}
