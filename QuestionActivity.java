package com.example.hunter.alliancepicker;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {
    int qRowNum = 0;

    String compileSave = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRequestedOrientation();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        final int teamArrayNum = Integer.parseInt(intent.getStringExtra("loadType"));
        final int runType = Integer.parseInt(intent.getStringExtra("runType"));

        if(runType == 1){
            this.setTitle("Change Preferences");
        }

        setContentView(R.layout.activity_question);

        for (int cntr = 0; cntr < Question.allQuestions.toArray().length; cntr++) {
            if (teamArrayNum == -1 && runType != 1) {
                if(!(runType == 1 && (Question.allQuestions.get(cntr).inputType.equalsIgnoreCase("Number") || Question.allQuestions.get(cntr).inputType.equalsIgnoreCase("Text")))){
                    Questions_CreateRow(Question.allQuestions.get(cntr).question, Question.allQuestions.get(cntr).inputType, 0.0f, "");
                }
                else {
                    qRowNum++;
                }
            }

            else if(teamArrayNum == -1 && runType == 1){
                float ratingBarValue = (float) Question.allQuestions.get(cntr).multiplier / 2;

                if(!(runType == 1 && (Question.allQuestions.get(cntr).inputType.equalsIgnoreCase("Number") || Question.allQuestions.get(cntr).inputType.equalsIgnoreCase("Text")))){
                    Questions_CreateRow(Question.allQuestions.get(cntr).question, Question.allQuestions.get(cntr).inputType, ratingBarValue, "");
                }
                else {
                    qRowNum++;
                }
            }

            else {
                float ratingBarValue = (float) Team.allTeams.get(teamArrayNum).Scores[cntr] / Question.allQuestions.get(cntr).multiplier / 2;
                String editTextValue = "";

                if (Question.allQuestions.get(cntr).inputType.equalsIgnoreCase("Number")) {
                    editTextValue = Team.allTeams.get(teamArrayNum).Number;
                }

                if (Question.allQuestions.get(cntr).inputType.equalsIgnoreCase("Text")) {
                    editTextValue = Team.allTeams.get(teamArrayNum).Name;
                }
                Questions_CreateRow(Question.allQuestions.get(cntr).question, Question.allQuestions.get(cntr).inputType, ratingBarValue, editTextValue);
            }
        }

        Questions_CreateMenu();

        final EditText teamName = ((EditText) findViewById(1100));
        final EditText teamNumber = ((EditText) findViewById(1101));


        Button button9 = (Button) findViewById(2001);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qRowNum = 0;
                int[] CompileQs = new int[Question.allQuestions.toArray().length];
                String scoreToString = "";

                for (int cntr = 0; cntr < Question.allQuestions.toArray().length; cntr++) {
                    if (Question.allQuestions.get(cntr).inputType.equalsIgnoreCase("RatingBar")) {
                        RatingBar rb = (RatingBar) findViewById(1100 + cntr);

                        scoreToString = "" + CompileQs[cntr - 1];
                        compileSave = compileSave + "%" + scoreToString;

                        try {
                            if(runType == 0){
                                CompileQs[cntr] = ((int) (rb.getRating() * 2)) * Question.allQuestions.get(cntr).multiplier;
                            }
                            else{
                                CompileQs[cntr] = Question.allQuestions.get(cntr).multiplier;
                                Question.allQuestions.get(cntr).multiplier = ((int) (rb.getRating() * 2));
                            }
                        } catch (NullPointerException e) {
                            CompileQs[cntr] = 0;
                        }
                    }
                }

                if (runType == 0) {
                    compileSave = compileSave + "@" + teamName.getText().toString() + "%" + teamNumber.getText().toString();

                    Team newTeam = new Team(teamName.getText().toString(), teamNumber.getText().toString(), CompileQs);

                    if (teamArrayNum != -1) {
                        newTeam.allTeams.remove(teamArrayNum);
                    }
                    Team.sortTeams(newTeam.allTeams);
                }

                else{
                    for(int cntr=0;cntr< Team.allTeams.toArray().length; cntr++){
                        for(int cntr2=0; cntr2 < Question.allQuestions.toArray().length; cntr2++){
                            try {
                                Team.allTeams.get(cntr).Scores[cntr2] = Team.allTeams.get(cntr).Scores[cntr2] / CompileQs[cntr2] * Question.allQuestions.get(cntr2).multiplier;
                            }
                            catch(ArithmeticException e){
                                Team.allTeams.get(cntr).Scores[cntr2] = 0;
                            }
                        }
                    }
                }

                //Question.allQuestions.clear();

                Intent myIntent = new Intent(QuestionActivity.this, MainActivity.class);
                QuestionActivity.this.startActivity(myIntent);
                QuestionActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Questions_CreateRow(String questionText, String questionType, Float rbValue, String etValue) {
        TableLayout tl = (TableLayout) findViewById(R.id.QuestionsTable);

        ScrollView sv = (ScrollView) findViewById(R.id.top_questions);
        sv.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.EdgeBgColor));

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TableRow tr2 = new TableRow(this);
        tr2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView question = (TextView) getLayoutInflater().inflate(R.layout.generic_row, null);
        question.setId(1000 + qRowNum);
        question.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));
        question.setText(questionText);

        RatingBar rb = (RatingBar) getLayoutInflater().inflate(R.layout.question_ratingbar, null);
        EditText et = (EditText) getLayoutInflater().inflate(R.layout.question_textedit, null);

        et.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));

        tr.addView(question);
        if (questionType.equalsIgnoreCase("RatingBar")) {
            rb.setRating(rbValue);
            rb.setId(1100 + qRowNum);

            LayerDrawable stars = (LayerDrawable) rb.getProgressDrawable();

            if(qRowNum % 2 == 0){
                stars.getDrawable(0).setColorFilter(Color.parseColor(ColorScheme.selectedColor.BgColor), PorterDuff.Mode.SRC_ATOP);
            }
            
            for(int i = 1; i<3; i++){
                stars.getDrawable(i).setColorFilter(Color.parseColor(ColorScheme.selectedColor.TxtColor), PorterDuff.Mode.SRC_ATOP);
            }

            tr2.addView(rb);

        }
        else if (questionType.equalsIgnoreCase("Text")) {
            et.setId(1100 + qRowNum);
            et.setText(etValue);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            tr2.addView(et);
        }
        else if (questionType.equalsIgnoreCase("Number")) {
            et.setId(1100 + qRowNum);
            et.setText(etValue);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr2.addView(et);
        }
        else {
            System.out.println("ERROR: TYPE ISN'T KNOWN!");
        }

        if (qRowNum % 2 == 0) {
            tr.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor2));
            tr2.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor2));
        }
        else {
            tr.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor));
            tr2.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor));
        }

        tl.addView(tr);
        tl.addView(tr2);
        qRowNum++;
    }

    public void Questions_CreateMenu() {
        TableLayout tl = (TableLayout) findViewById(R.id.QuestionsTable);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        Button submit = (Button) getLayoutInflater().inflate(R.layout.generic_button, null);
        submit.setText("Submit");
        submit.setId(2001);

        qRowNum += 1;
        if (qRowNum % 2 == 0) {
            submit.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor));}
        else {submit.setBackgroundColor(Color.parseColor(ColorScheme.selectedColor.BgColor2));}

        submit.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));

        tl.addView(submit);
    }
}
