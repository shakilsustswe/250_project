package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FindFriendActivity extends AppCompatActivity {

    RecyclerView recview;
    FindFriendAdapter adapter;
    ArrayList<String> arrayList;
    AlertDialog.Builder builder;

    private RelativeLayout rootLayout;

    // add friend
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    FirebaseUser firebaseUser;


    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        setTitle("Find Friend");

        recview=(RecyclerView)findViewById(R.id.recycularViewId);
        rootLayout = findViewById(R.id.activity_find_friend_rootLayout);
        recview.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


        mAuth = FirebaseAuth.getInstance();
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User"), Users.class)
                        .build();

        adapter=new FindFriendAdapter(options);
        recview.setAdapter(adapter);
        arrayList = new ArrayList<>();

        adapter.setOnAdapterInteractionListener(new FindFriendAdapter.onAdapterInteractionListener() {
            @Override
            public void onItemClick(String uid, String name) {
                addFriend(uid, name);
            }
        });

    }


    // writer fahim 21, 07, 2021
    // friend request add feature
    private void addFriend(String uid, String name) {
        builder = new AlertDialog.Builder(this);

        // handle if current uid it's your itself, so u can't sent friend request
        if(uid.equals(firebaseUser.getUid().toString())){
            builder.setMessage("Sorry U can't sent request itself");
            builder.setTitle("Hellow, it is your own accound!!" );
            Snackbar snackbar = Snackbar
                    .make(rootLayout, "Sorry, u can't add you", Snackbar.LENGTH_LONG);
            snackbar.show();
            builder.setCancelable(false)
                    .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else{
            builder.setMessage("Are you sure to add this contract on your friends list??")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            addFriendList(uid);
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


    }

    // code write, fahim
    private void addFriendList(String uid) {

        databaseReference = FirebaseDatabase.getInstance().getReference("FriendsList").child(firebaseUser.getUid());


            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                // if already friend
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(uid)){
                        Snackbar snackbar = Snackbar
                                .make(rootLayout, "Already friend", Snackbar.LENGTH_LONG);
                        snackbar.show();
                       // Toast.makeText(FindFriendActivity.this, "Already friend", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        databaseReference.child(uid).setValue("friend").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Snackbar snackbar = Snackbar
                                            .make(rootLayout, "Sucessfully added", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

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