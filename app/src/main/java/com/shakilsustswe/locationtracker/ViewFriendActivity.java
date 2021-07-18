package com.shakilsustswe.locationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ViewFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        String userId = getIntent().getStringExtra("userKey");
        Toast.makeText(this, ""+userId, Toast.LENGTH_SHORT).show();
    }
}