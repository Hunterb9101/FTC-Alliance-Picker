package com.example.hunter.alliancepicker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    int teamArrayNum = -1; // Team that has been selected for this menu
    int rowNum = 0;
    int expandableState = 0;
    double scoreThresh = .25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        teamArrayNum = Integer.parseInt(intent.getStringExtra("loadType"));
        Info_CreateMain();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.action_settings) { return true; }
        return super.onOptionsItemSelected(item);
    }

    public void Info_CreateMain(){
        boolean hasStrength = false;
        boolean hasWeakness = false;

        // Find Strengths //
        Info_CreateRow("Team " + Team.allTeams.get(teamArrayNum).Name + "'s Strengths", true);

        for(int cntr = 0;cntr< Question.allQuestions.toArray().length;cntr++){
            int avg = findAverageScore(cntr);
            if((int)(avg*(1+scoreThresh)) < Team.allTeams.get(teamArrayNum).Scores[cntr]){
                Info_CreateRow("    - " + Question.allQuestions.get(cntr).question,false);
                hasStrength = true;
            }
        }

        if(Team.allTeams.toArray().length < 2){ Info_CreateRow("There aren't enough teams to find a strength.",false); }
        else if(!hasStrength){ Info_CreateRow("This team has no strengths",false); }

        // Find Weaknesses //
        Info_CreateRow("Team " + Team.allTeams.get(teamArrayNum).Name+ "'s Weaknesses", true);

        for(int cntr = 0;cntr<Question.allQuestions.toArray().length;cntr++){ // Why was this 2?
            int avg = findAverageScore(cntr);
            if((int)(avg*(1-scoreThresh)) > Team.allTeams.get(teamArrayNum).Scores[cntr]){
                Info_CreateRow("    - " + Question.allQuestions.get(cntr).question,false);
                hasWeakness = true;
            }
        }

        if(Team.allTeams.toArray().length < 2){ Info_CreateRow("There aren't enough teams to find a weakness.", false); }
        else if(!hasWeakness){ Info_CreateRow("This team has no weaknesses",false);}

        Info_CreateMenu();

    }

    public void Info_CreateRow(String dispText, Boolean header) {
        TableLayout tl = (TableLayout)findViewById(R.id.infoTable);

        ScrollView sv = (ScrollView)findViewById(R.id.top_info);
        sv.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.EdgeBgColor));

        TableRow tr1 = new TableRow(this);
        tr1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView textview;

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 3;
        tr1.setLayoutParams(params);

        if(header){ textview = (TextView) getLayoutInflater().inflate(R.layout.info_header, null); }
        else{ textview = (TextView) getLayoutInflater().inflate(R.layout.info_row,null);}

        /* Team Text */
        textview.setId(5300 + rowNum);
        textview.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));
        textview.setText(dispText);

        if (rowNum % 2 == 0) {tr1.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor));}
        else {tr1.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor2));}

        tr1.addView(textview);

        tl.addView(tr1);
        rowNum++;
    }

    public void Info_CreateMenu() {
        TableLayout tl = (TableLayout) findViewById(R.id.btnTable);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final Button submit = (Button) getLayoutInflater().inflate(R.layout.generic_button, null);

        rowNum += 1;
        if (rowNum % 2 == 0) {tr.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor));}
        else {tr.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor2));}
        submit.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (expandableState == 0) {
                    Info_CreateSpinnerTable();
                    expandableState = 1;
                    submit.setText("See Less");
                } else {
                    finish();
                    startActivity(getIntent());
                    expandableState = 0;
                    submit.setText("See More");
                }
            }
        });
        tr.addView(submit);
        tl.addView(tr);
        tr.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor));
    }

    public void Info_CreateSpinnerTable(){
        TableLayout tl = (TableLayout) findViewById(R.id.spinnerTable);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        Spinner teamSelect = (Spinner) getLayoutInflater().inflate(R.layout.info_spinner, null);//Main Spinner
        TextView tv = (TextView) getLayoutInflater().inflate(R.layout.info_header,null);
        tv.setText("Compare To:");

        rowNum += 1;
        if (rowNum % 2 == 0) {tr.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor));}
        else {tr.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor2));}
        tv.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));

        List<String> items = new ArrayList<String>();

        items.add("Average");
        for(int cntr=0; cntr<Team.allTeams.toArray().length; cntr++){
            if(teamArrayNum != cntr){ //Make sure not to add the team that is being compared!!!
                items.add(Team.allTeams.get(cntr).Name);
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.info_spinner_text, items);

        dataAdapter.setDropDownViewResource(R.layout.info_spinner_item);
        teamSelect.setAdapter(dataAdapter);

        teamSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ((TableLayout) findViewById(R.id.scoreTable)).removeAllViews();
                Info_CreateScoreTable(parent.getItemAtPosition(pos).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        tr.addView(tv);
        tr.addView(teamSelect);
        tl.addView(tr);
    }

    public void Info_CreateScoreTable(String teamComparedName){
        Team team1 = Team.allTeams.get(teamArrayNum);
        Info_CreateScoreTableRow("----", teamComparedName, "Teams: ", true); // Shows team names

        if(teamComparedName.equalsIgnoreCase("Average")){
            int[] scores = new int[Question.allQuestions.toArray().length];
            int totalScore = 0;

            for(int cntr=0; cntr<(Question.allQuestions.toArray().length) ;cntr++){
                scores[cntr] = findAverageScore(cntr);
                for(int cntr2=2;cntr<Question.allQuestions.toArray().length;cntr++){
                    Info_CreateScoreTableRow(Integer.toString(team1.Scores[cntr]),Integer.toString(scores[cntr2]), Question.allQuestions.get(cntr).question,false);
                    totalScore+=scores[cntr2];
                }

                Info_CreateScoreTableRow(Integer.toString(team1.totalScore()), Integer.toString(totalScore), "Total Score", false);
            }

        }
        else{
            for(int cntr=2;cntr<Question.allQuestions.toArray().length;cntr++){
                Info_CreateScoreTableRow(Integer.toString(team1.Scores[cntr]),Integer.toString(Team.allTeams.get(Team.findByName(teamComparedName)).Scores[cntr]), Question.allQuestions.get(cntr).question,false);
            }

            Info_CreateScoreTableRow(Integer.toString(team1.totalScore()), Integer.toString(Team.allTeams.get(Team.findByName(teamComparedName)).totalScore()), "Total Score", false);
        }

    }

    public void Info_CreateScoreTableRow(String c1, String c2, String title, Boolean header) {
        TableLayout tl = (TableLayout) findViewById(R.id.scoreTable);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView rowTitle;
        if (header) { rowTitle = (TextView) getLayoutInflater().inflate(R.layout.info_header, null);}
        else { rowTitle = (TextView) getLayoutInflater().inflate(R.layout.info_row, null);}

        rowTitle.setText(title);

        TextView column1;
        if (header) { column1 = (TextView) getLayoutInflater().inflate(R.layout.info_header, null);}
        else { column1 = (TextView) getLayoutInflater().inflate(R.layout.info_row, null);}

        column1.setText(c1);

        TextView column2;
        if (header) { column2 = (TextView) getLayoutInflater().inflate(R.layout.info_header, null);}
        else { column2 = (TextView) getLayoutInflater().inflate(R.layout.info_row, null);}
        column2.setText(c2);

        if (rowNum % 2 == 0) {tr.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor));}
        else {tr.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor2));}

        rowTitle.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));
        column1.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));
        column2.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));

        tr.addView(rowTitle);
        tr.addView(column1);
        tr.addView(column2);
        tl.addView(tr);

        rowNum++;
    }

    public int findAverageScore(int qIdx){
        int length = Team.allTeams.toArray().length;
        int sumTeamScores = 0;

        for(int cntr = 0; cntr < Team.allTeams.toArray().length; cntr++){ // Adds in all team scores for that question
            sumTeamScores = sumTeamScores + Team.allTeams.get(cntr).Scores[qIdx];
        }


        return sumTeamScores/length;
    }
}
