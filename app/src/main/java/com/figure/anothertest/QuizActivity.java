package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private RadioGroup q7,q6,q5,q4,q3,q2,q1;
    private RadioButton radioButton;
    private RelativeLayout submit;

    private Toolbar closeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        init();


    }

    public void init(){
        closeBtn = findViewById(R.id.closebtn);

        q1 = findViewById(R.id.radiogroup1);
        q2 = findViewById(R.id.radiogroup2);
        q3 = findViewById(R.id.radiogroup3);
        q4 = findViewById(R.id.radiogroup4);
        q5 = findViewById(R.id.radiogroup5);
        q6 = findViewById(R.id.radiogroup6);
        q7 = findViewById(R.id.radiogroup7);

        closeBtn.setOnClickListener(v -> onBackPressed());


        submit = findViewById(R.id.registration_button);

        submit.setOnClickListener(v -> {
            //int selectedId=q7.getCheckedRadioButtonId();
            //radioButton= findViewById(selectedId);
            //Toast.makeText(QuizActivity.this,""+radioButton.getText(),Toast.LENGTH_SHORT).show();
            int score  = checkAnswers();
            if(checkAnswers() < 6){
                Toast.makeText(getApplicationContext(),"Your score is "+score+", you need 6 or higher to proceed ",Toast.LENGTH_SHORT).show();
            }
            else if(checkAnswers() >= 6){
                startActivity(new Intent(QuizActivity.this,ErrandMapActivity.class));
            }


        });

    }

    public int checkAnswers(){
        int score = 0;

        if(getAns(q1).equals(""+getResources().getString(R.string.Q1r))){
            score++;
        }

        if(getAns(q2).equals(""+getResources().getString(R.string.Q2r))){
            score++;
        }

        if(getAns(q3).equals(""+getResources().getString(R.string.Q3r))){
            score++;
        }

        if(getAns(q4).equals(""+getResources().getString(R.string.Q4r))){
            score++;
        }

        if(getAns(q5).equals(""+getResources().getString(R.string.Q5r))){
            score++;
        }

        if(getAns(q6).equals(""+getResources().getString(R.string.Q6r))){
            score++;
        }

        if(getAns(q7).equals(""+getResources().getString(R.string.Q7r))){
            score++;
            Toast.makeText(getApplicationContext(),"Correct, Score:"+score,Toast.LENGTH_SHORT).show();
        }

        return score;
    }

    public String getAns(RadioGroup qn){
        int selectedId=qn.getCheckedRadioButtonId();
        String value = "";

        radioButton= findViewById(selectedId);
        if(radioButton != null) {
            value = ""+radioButton.getText();
        }

        return value;
    }
}
