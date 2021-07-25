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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ViewFriendActivity extends AppCompatActivity {

    String user_name;
    double latitude;
    double longitude;
    Intent i;

    TextView t1,t2,t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);


        user_name = i.getExtras().getString("USER_NAME");
        latitude = i.getExtras().getDouble("Latitude");
        longitude = i.getExtras().getDouble("longitude");
        String uid = i.getExtras().getString("UID");

        t1 = findViewById(R.id.test1);
        t2 = findViewById(R.id.test2);
        t3 = findViewById(R.id.test3);

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Location").child(uid);
//
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String name = snapshot.child("name").getValue().toString();
//                String lat = snapshot.child("latitude").getValue().toString();
//                String log = snapshot.child("longitude").getValue().toString();
//                String image = snapshot.child("imageUri").getValue().toString();
//
//                t1.setText(name);
//                t2.setText(lat);
//                t3.setText(log);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}