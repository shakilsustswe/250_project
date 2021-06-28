package com.shakilsustswe.locationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText useremail;
    EditText userpassword;
    Button loginBtn;
    TextView forgetpasswordBtn;
    ImageButton googlesignBtn;
    TextView signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // initialization
        useremail = findViewById(R.id.activity_login_email);
        userpassword = findViewById(R.id.activity_login_password);
        loginBtn = findViewById(R.id.activity_login_login);
        forgetpasswordBtn = findViewById(R.id.activity_login_forgetpassword);
        googlesignBtn = findViewById(R.id.activity_login_googlesignup);
        signupBtn = findViewById(R.id.activity_login_signup);


        loginBtn.setOnClickListener(this::onClick);
        forgetpasswordBtn.setOnClickListener(this::onClick);
        googlesignBtn.setOnClickListener(this::onClick);
        signupBtn.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_login:
                login();
                break;
            case R.id.activity_login_forgetpassword:
                forgetPassword();
                break;
            case R.id.activity_login_googlesignup:
                googlesignin();
                break;
            case R.id.activity_login_signup:
                signup();
                break;

        }
    }



    private void login() {
        String email, password;
        email = useremail.getText().toString();
        password = userpassword.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            useremail.setError("Enter an email");
            useremail.requestFocus();
            return;
        }

        if(password.length() <= 6){
            userpassword.setError("Minimum 6 character");
            userpassword.requestFocus();
            return;
        }

        // mAuth email authentication
    }

    private void forgetPassword() {

    }

    private void googlesignin() {

    }

    private void signup() {
        startActivity(new Intent(this, Register.class));
    }

}