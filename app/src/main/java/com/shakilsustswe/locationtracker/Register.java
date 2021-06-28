package com.shakilsustswe.locationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText useremail;
    EditText username;
    Button signupBtn;
    TextView signinBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ini
        username = findViewById(R.id.register_activity_username);
        useremail = findViewById(R.id.register_activity_useremail);
        signinBtn = findViewById(R.id.register_activity_signin);
        signupBtn = findViewById(R.id.register_activity_signup);


        signupBtn.setOnClickListener(this::onClick);
        signupBtn.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.register_activity_signup)
            signup();
        if(v.getId() == R.id.register_activity_signin)
            signin();

    }

    private void signup() {
    }

    private void signin() {
        startActivity(new Intent(this, Login.class));
    }
}