package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shakilsustswe.locationtracker.FindFriendAdapter;
import com.shakilsustswe.locationtracker.R;
import com.shakilsustswe.locationtracker.Users;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyfriendList extends AppCompatActivity {

    RecyclerView recview;
    FindFriendAdapter adapter;
    ArrayList<String> arrayList;
    AlertDialog.Builder builder;

    private RelativeLayout rootLayout;
    private Task<Void> databaseReference;
    private FirebaseAuth mAuth;

    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        setTitle("My friend");

        recview=(RecyclerView)findViewById(R.id.recycularViewId);
        rootLayout = findViewById(R.id.activity_find_friend_rootLayout);
        recview.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


        mAuth = FirebaseAuth.getInstance();
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("FriendsList").child(firebaseUser.getUid()), Users.class)
                        .build();

        adapter=new FindFriendAdapter(options);
        recview.setAdapter(adapter);
        arrayList = new ArrayList<>();

        adapter.setOnAdapterInteractionListener(new FindFriendAdapter.onAdapterInteractionListener() {
            @Override
            public void onItemClick(String uid, String name, String imageUri, String status, String email) {
                addFriend(uid, name, imageUri, status, email);

            }
        });

    }

    private void addFriend(String uid, String name, String imageUri, String status, String email) {
        builder = new AlertDialog.Builder(this);


        builder.setMessage("Are you sure to unfriend this contract ??")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //addFriendList(uid, name, imageUri, status, email);
                        databaseReference = FirebaseDatabase.getInstance().getReference("FriendsList").child(firebaseUser.getUid()).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Snackbar snackbar = Snackbar
                                            .make(rootLayout, "Unfriend successfully", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Add " + name + "?");
        alert.show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_bar,menu);

        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User").orderByChild("name").startAt(s).endAt(s+"\uf8ff"), Users.class)
                        .build();

        adapter=new FindFriendAdapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }
}