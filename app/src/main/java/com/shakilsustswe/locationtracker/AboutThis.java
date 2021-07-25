package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutThis extends AppCompatActivity {


    String user_name;
    double latitude;
    double longitude;
    Intent i;

    TextView t1,t2,t3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_this);

        t1 = findViewById(R.id.t);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        t1.setText(message);
        String uid = message;

        try{
//            Intent intent = getIntent();
//            String message = intent.getStringExtra("message");


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Location").child(uid);

            Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue().toString();
                    String lat = snapshot.child("latitude").getValue().toString();
                    String log = snapshot.child("longitude").getValue().toString();
                    String image = snapshot.child("imageUri").getValue().toString();

                    t1.setText(lat+log);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){

        }


    }
}