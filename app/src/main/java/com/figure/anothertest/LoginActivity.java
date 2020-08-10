package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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


public class LoginActivity extends AppCompatActivity {

    private RelativeLayout loginBtn;
    private CardView login_button_card_view;
    private TextView button_title;
    private ProgressBar button_progress;
    private ImageView button_error_icon;


    private TextView forgot_password_text_view;
    private TextView login_to_register;

    private EditText lUsername;
    private EditText lPassword;

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
        setContentView(R.layout.activity_login);
        internet_connection();

        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        mAuth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.login);
        login_button_card_view = findViewById(R.id.login_button_card_view);

        forgot_password_text_view = findViewById(R.id.forgot_password_text_view);
        login_to_register = findViewById(R.id.lRegister_text_view);

        button_title = findViewById(R.id.button_title);
        button_progress = findViewById(R.id.button_progress);
        button_error_icon = findViewById(R.id.button_error_icon);

        lUsername = findViewById(R.id.lUsername);
        lPassword = findViewById(R.id.lPasword);

        forgotPasswordOnClick();
        inputChange();
        registerOnClick();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = lUsername.getText().toString().trim();
                String password = lPassword.getText().toString();

                if(!internet_connection()){
                    Snackbar snackbar = Snackbar.make(view,R.string.no_internet,3000);
                    snackbar.show();

                }
                else{
                    if(permissionGranted){
                        if(!email.isEmpty() && !password.isEmpty()){
                            loginUser(email,password);
                        }

                    }else{
                        Toast.makeText(LoginActivity.this,"We need you location to proceed",Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }

                }



            }
        });







    }



    @SuppressLint("ResourceType")
    private void loginButtonStyle() {

        setButtonStyle(BUTTON_TYPES.RESET);


        if (lPassword.getText().length() > 0 && lUsername.getText().length() > 0) {
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
    private void setButtonStyle(BUTTON_TYPES buttonType) {
        if (buttonType == BUTTON_TYPES.RESET) {
            buttonErrorIconIsVisible(false);
            buttonProgressIsVisible(false);
            buttonTitleIsVisible(true);
            //login_button_card_view.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
            button_title.setText(getString(R.string.login));
            button_title.setTextColor(Color.parseColor(getString(R.color.colorWhite)));
        } else if (buttonType == BUTTON_TYPES.LOADING) {
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

    private void forgotPasswordOnClick() {
        forgot_password_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, getString(R.string.forgot_password), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerOnClick() {
        login_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,Login_Signup_Activity.class));
            }
        });
    }

    private void inputChange() {
        lUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                loginButtonStyle();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                loginButtonStyle();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loginUser(String email, String password) {
        setButtonStyle(BUTTON_TYPES.LOADING);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    SharedPrefs.setDefaults(getApplication());

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout),"Login Successful ",2000);
                    snackbar.show();

                    Intent intent = new Intent(LoginActivity.this,ErrandMapActivity.class);
                    startActivity(intent);
                    //CustomIntent.customType(LoginActivity.this,"left-to-right");
                    finish();
                }
                else{
                    setButtonStyle(BUTTON_TYPES.ERROR);
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout),"Failed: Email / Password combination is invalid",3000);
                    snackbar.show();




                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //make a boolean here where intent checks before proceeding
                permissionGranted = true;
            } else {
                Toast.makeText(LoginActivity.this, "We need you location to proceed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean internet_connection(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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
