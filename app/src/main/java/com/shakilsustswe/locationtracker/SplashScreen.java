package com.shakilsustswe.locationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button buttonRequest = findViewById(R.id.activity_splash_screen_location_permission);

        statusCheck();
        fetchLocation();
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this,Register.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}