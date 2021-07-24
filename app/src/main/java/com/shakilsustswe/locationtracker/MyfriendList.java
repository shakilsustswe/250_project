package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyfriendList extends AppCompatActivity {

    private static final String TAG = "";
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ArrayList<String> allfriend;
    private String string;
    TextView textView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriend_list);
        setTitle("Friend list");


        recyclerView=findViewById(R.id.recycularViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}


/*string = new String();
        allfriend = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        ////textView = findViewById(R.id.activity_my_friend_list_check);
        recyclerView= findViewById(R.id.recycularViewId);
        RetriveallData();


    }

    private void RetriveallData() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("FriendList").child(firebaseUser.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot child:snapshot.getChildren()){
                    Toast.makeText(getApplicationContext(), snapshot.getKey(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });*/
