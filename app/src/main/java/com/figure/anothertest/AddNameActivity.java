package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AddNameActivity extends AppCompatActivity {

    Toolbar closebtn;

    private RelativeLayout loginBtn;
    private CardView login_button_card_view;
    private TextView button_title;
    private ProgressBar button_progress;
    private ImageView button_error_icon;

    private TextView login_to_register;

    private EditText lUsername;

    private FirebaseAuth mAuth;

    private Boolean permissionGranted = false;

    public enum BUTTON_TYPES {
        RESET,
        LOADING,
        ERROR
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);

        closebtn = findViewById(R.id.closebtn);

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
        inputChange();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefs.setUsername(lUsername.getText().toString());

                Log.d("textsetoh",""+SharedPrefs.getUsername());
                finish();
            }
        });
    }

    void init(){

        loginBtn = findViewById(R.id.login);
        login_button_card_view = findViewById(R.id.login_button_card_view);

        login_to_register = findViewById(R.id.lRegister_text_view);

        button_title = findViewById(R.id.button_title);
        button_progress = findViewById(R.id.button_progress);
        button_error_icon = findViewById(R.id.button_error_icon);

        lUsername = findViewById(R.id.lUsername);

    }

    @SuppressLint("ResourceType")
    private void loginButtonStyle() {

        setButtonStyle(LoginActivity.BUTTON_TYPES.RESET);


        if (lUsername.getText().length() > 0) {
            if (!loginBtn.isFocusable()) {
                loginBtn.setFocusable(true);
                loginBtn.setClickable(true);
                login_button_card_view.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
                TypedValue outValue = new TypedValue();
                getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                loginBtn.setBackgroundResource(outValue.resourceId);
            }
        } else {
            if (loginBtn.isFocusable()) {
                loginBtn.setFocusable(false);
                loginBtn.setClickable(false);
                login_button_card_view.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCardViewBackground)));
                loginBtn.setBackgroundResource(0);
            }
        }
    }

    @SuppressLint("ResourceType")
    private void setButtonStyle(LoginActivity.BUTTON_TYPES buttonType) {
        if (buttonType == LoginActivity.BUTTON_TYPES.RESET) {
            buttonErrorIconIsVisible(false);
            buttonProgressIsVisible(false);
            buttonTitleIsVisible(true);
            //login_button_card_view.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
            button_title.setText("Add");
            button_title.setTextColor(Color.parseColor(getString(R.color.colorWhite)));
        } else if (buttonType == LoginActivity.BUTTON_TYPES.LOADING) {
            buttonErrorIconIsVisible(false);
            buttonProgressIsVisible(true);
            buttonTitleIsVisible(true);
            login_button_card_view.setCardBackgroundColor(Color.parseColor(getString(R.color.colorLoading)));
            button_title.setText(getString(R.string.loading_title));
            button_title.setTextColor(Color.parseColor(getString(R.color.colorLoadingTitle)));
        } else {
            buttonErrorIconIsVisible(true);
            buttonProgressIsVisible(false);
            buttonTitleIsVisible(false);
            login_button_card_view.setCardBackgroundColor(Color.parseColor(getString(R.color.colorUnsuccessful)));

            loginBtn.setFocusable(false);
            loginBtn.setClickable(false);
        }
    }

    private void inputChange() {
        lUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                loginButtonStyle();
                loginButtonStyle();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void buttonErrorIconIsVisible(boolean isVisible) {
        if (isVisible) {
            button_error_icon.setVisibility(View.VISIBLE);
        } else {
            button_error_icon.setVisibility(View.GONE);
        }
    }

    private void buttonProgressIsVisible(boolean isVisible) {
        if (isVisible) {
            button_progress.setVisibility(View.VISIBLE);
        } else {
            button_progress.setVisibility(View.GONE);
        }
    }

    private void buttonTitleIsVisible(boolean isVisible) {
        if (isVisible) {
            button_title.setVisibility(View.VISIBLE);
        } else {
            button_title.setVisibility(View.GONE);
        }
    }
}
