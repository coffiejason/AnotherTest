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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Login_Signup_Activity extends AppCompatActivity {

    private EditText sPhone,sEmail,sPassword,sConfirmPassword;
    private TextView phone_error_text,email_error_text, password_error_text,registerbtn_tv;
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

    private void registerUser(String phone,String email, String password) {
        String confirm = sConfirmPassword.getText().toString();
        if(!confirm.equals(password)){
            Toast.makeText(Login_Signup_Activity.this,sConfirmPassword.getText().toString()+" "+password, Toast.LENGTH_SHORT).show();
            //registerbtn_tv.setText("REGISTER");
        }
        else{
            //Add a load screen of some sort

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        HashMap<String,Object> h = new HashMap<>();
                        h.put("momo",phone);
                        h.put("email",email);
                        h.put("password",password);
                        h.put("name","static_name");

                        Toast.makeText(Login_Signup_Activity.this,"Signup Successful ", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference().child("Customers available").child(phone).updateChildren(h).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(Login_Signup_Activity.this,LoginActivity.class);
                                startActivity(intent);
                                //CustomIntent.customType(Login_Signup_Activity.this,"left-to-right");
                            }
                        });


                    }
                    else{
                        FirebaseAuthException e = (FirebaseAuthException)task.getException();
                        assert e != null;
                        Toast.makeText(Login_Signup_Activity.this,"Failed:"+e.getMessage(), Toast.LENGTH_LONG).show();

                        isRegistrationClickable = true;
                        registration_button_parent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
                        registerbtn_tv.setText("REGISTER");

                    }
                }
            });
        }


    }

    boolean internet_connection(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void setType() {
        sPhone = findViewById(R.id.phone_input_field);
        sEmail = findViewById(R.id.email_input_field);
        email_error_text = findViewById(R.id.email_error_text);
        phone_error_text = findViewById(R.id.phone_error_text);
        password_error_text = findViewById(R.id.password_error_text);
        sPassword = findViewById(R.id.password_input_field);
        sConfirmPassword = findViewById(R.id.confirm_password_input_field);
        isAtLeast8Parent = findViewById(R.id.p_item_1_icon_parent);
        hasUppercaseParent = findViewById(R.id.p_item_2_icon_parent);
        hasNumberParent = findViewById(R.id.p_item_3_icon_parent);
        registration_button = findViewById(R.id.registration_button);
        registration_button_parent = findViewById(R.id.registration_button_parent);
        registerbtn_tv = findViewById(R.id.registerbtn_tv);
    }

    private void checkEmpty(String phone,String email, String password) {
        if (password.length() > 0 && password_error_text.getVisibility() == View.VISIBLE) {
            password_error_text.setVisibility(View.GONE);
        }
        if (email.length() > 0 && email_error_text.getVisibility() == View.VISIBLE) {
            email_error_text.setVisibility(View.GONE);
        }
        if (phone.length() > 0 && phone_error_text.getVisibility() == View.VISIBLE) {
            phone_error_text.setVisibility(View.GONE);
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
        String password = sPassword.getText().toString(), email = sEmail.getText().toString(), phone = sPhone.getText().toString();

        checkEmpty(phone,email, password);
        registerbtn_tv.setText("REGISTER");
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
        sPhone.addTextChangedListener(new TextWatcher() {
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
                String email = sEmail.getText().toString().trim(), password = sPassword.getText().toString(), phone = sPhone.getText().toString();

                if (email.length() > 0 && password.length() > 0 && phone.length() > 0) {
                    if (isRegistrationClickable) {

                        if(!internet_connection()){
                            Snackbar snackbar = Snackbar.make(view,R.string.no_internet,3000);
                            snackbar.show();

                        }
                        else{
                            registerUser(phone,email,password);
                            isRegistrationClickable = false;
                            registration_button_parent.setCardBackgroundColor(Color.parseColor(getString(R.color.colorCardViewBackground)));




                        }                    }
                } else {
                    if (phone.length() == 0) {
                        phone_error_text.setVisibility(View.VISIBLE);
                    }
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
