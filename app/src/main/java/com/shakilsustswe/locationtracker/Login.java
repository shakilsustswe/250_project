package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    EditText useremail;
    EditText userpassword;
    Button loginBtn;
    TextView forgetpasswordBtn;
    ImageButton googlesignBtn;
    TextView signupBtn;
    ////firebaseActivity
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //////progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login...");
        progressDialog.setMessage("Please wait😴😴");
        progressDialog.setCancelable(false);


        // initialization
        useremail = findViewById(R.id.activity_login_email);
        userpassword = findViewById(R.id.activity_login_password);
        loginBtn = findViewById(R.id.activity_login_login);
        forgetpasswordBtn = findViewById(R.id.activity_login_forgetpassword);
        googlesignBtn = findViewById(R.id.activity_login_googlesignup);
        signupBtn = findViewById(R.id.activity_login_signup);


        /*loginBtn.setOnClickListener(this::onClick);
        forgetpasswordBtn.setOnClickListener(this::onClick);
        googlesignBtn.setOnClickListener(this::onClick);
        signupBtn.setOnClickListener(this::onClick);*/

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        /////////check if user is null
        if (firebaseUser != null) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                String person_email = useremail.getText().toString();
                String person_password = userpassword.getText().toString();

                if (TextUtils.isEmpty(person_email) || TextUtils.isEmpty(person_password)) {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "All fill doesn't fill up", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(person_email, person_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });


                }
            }
        });


    }




}