package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;



public class Login_Signup_Activity extends AppCompatActivity {

    private EditText sEmail,sPassword;
    private TextView email_error_text, password_error_text;
    private CardView isAtLeast8Parent, hasUppercaseParent, hasNumberParent;
    private RelativeLayout registration_button;
    private CardView registration_button_parent;
    private boolean isAtLeast8 = false, hasUppercase = false, hasNumber = false, isRegistrationClickable = false;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__signup_);

        mAuth = FirebaseAuth.getInstance();

        internet_connection();

        setType();
        inputChange();
        setOnClickRegistration();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //CustomIntent.customType(this,"right-to-left");

    }

    private void registerUser(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(Login_Signup_Activity.this," We need your email ", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(Login_Signup_Activity.this," choose a password ", Toast.LENGTH_SHORT).show();
        }

        else{
            //Add a load screen of some sort
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login_Signup_Activity.this,"Signup Successful ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Login_Signup_Activity.this,LoginActivity.class);
                        startActivity(intent);
                        //CustomIntent.customType(Login_Signup_Activity.this,"left-to-right");
                    }
                    else{
                        FirebaseAuthException e = (FirebaseAuthException)task.getException();
                        Toast.makeText(Login_Signup_Activity.this,"Failed:"+e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }

    boolean internet_connection(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void setType() {
        sEmail = findViewById(R.id.email_input_field);
        email_error_text = findViewById(R.id.email_error_text);
        password_error_text = findViewById(R.id.password_error_text);
        sPassword = findViewById(R.id.password_input_field);
        isAtLeast8Parent = findViewById(R.id.p_item_1_icon_parent);
        hasUppercaseParent = findViewById(R.id.p_item_2_icon_parent);
        hasNumberParent = findViewById(R.id.p_item_3_icon_parent);
        registration_button = findViewById(R.id.registration_button);
        registration_button_parent = findViewById(R.id.registration_button_parent);
    }

    private void checkEmpty(String email, String password) {
        if (password.length() > 0 && password_error_text.getVisibility() == View.VISIBLE) {
            password_error_text.setVisibility(View.GONE);
        }
        if (email.length() > 0 && email_error_text.getVisibility() == View.VISIBLE) {
            email_error_text.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ResourceType")
    private void checkAllData(String email) {
        if (isAtLeast8 && hasUppercase && hasNumber && email.length() > 0) {
            isRegistrationClickable = true;
            registration_button_parent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            isRegistrationClickable = false;
            registration_button_parent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }
    }

    @SuppressLint("ResourceType")
    private void registrationDataCheck() {
        String password = sPassword.getText().toString(), email = sEmail.getText().toString();

        checkEmpty(email, password);

        if (password.length() >= 8) {
            isAtLeast8 = true;
            isAtLeast8Parent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            isAtLeast8 = false;
            isAtLeast8Parent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }
        if (password.matches("(.*[A-Z].*)")) {
            hasUppercase = true;
            hasUppercaseParent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            hasUppercase = false;
            hasUppercaseParent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }
        if (password.matches("(.*[0-9].*)")) {
            hasNumber = true;
            hasNumberParent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckOk)));
        } else {
            hasNumber = false;
            hasNumberParent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCheckNo)));
        }

        checkAllData(email);
    }

    private void inputChange() {
        sEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setOnClickRegistration() {
        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = sEmail.getText().toString().trim(), password = sPassword.getText().toString();

                if (email.length() > 0 && password.length() > 0) {
                    if (isRegistrationClickable) {

                        if(!internet_connection()){
                            Snackbar snackbar = Snackbar.make(view,R.string.no_internet,3000);
                            snackbar.show();

                        }
                        else{
                            registerUser(email,password);

                        }                    }
                } else {
                    if (email.length() == 0) {
                        email_error_text.setVisibility(View.VISIBLE);
                    }
                    if (password.length() == 0) {
                        password_error_text.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

}
