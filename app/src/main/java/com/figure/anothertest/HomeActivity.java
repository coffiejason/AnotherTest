package com.figure.anothertest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {
    RelativeLayout login_button;
    TextView register_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setType();
        loginOnClick();
        registerOnClick();

        if(SharedPrefs.getDefaults("Loggedin",getApplication()) != null){
            Intent intent = new Intent(HomeActivity.this, ErrandMapActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setType() {
        login_button = findViewById(R.id.login_button);
        register_text_view = findViewById(R.id.register_text_view);
    }

    private void registerOnClick() {
        register_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(HomeActivity.this,"Register User",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(HomeActivity.this, Login_Signup_Activity.class));
                //CustomIntent.customType(HomeActivity.this,"left-to-right");
            }
        });
    }

    private void loginOnClick() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                //CustomIntent.customType(HomeActivity.this,"left-to-right");

            }
        });
    }
}

