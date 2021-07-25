package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EmergencyHelp extends Fragment {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<LocationHelper> usersArrayList;
    AlertDialog.Builder builder;
    RecyclerView recyclerView;
    EmergencyHelpAdapter userAdapter;
    Task<Void> reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_emergency_help, container, false);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        usersArrayList = new ArrayList<>();

        userAdapter = new EmergencyHelpAdapter(getActivity(), usersArrayList);

        databaseReference = firebaseDatabase.getReference().child("ShareLocation").child(auth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LocationHelper users = dataSnapshot.getValue(LocationHelper.class);
                    usersArrayList.add(users);
                }
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        recyclerView = v.findViewById(R.id.activity_emergency_help_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(userAdapter);

        userAdapter.setOnAdapterInteractionListener(new EmergencyHelpAdapter.onAdapterInteractionListener() {
            @Override
            public void onItemClick(LocationHelper helper) {

                Intent intent = new Intent(getActivity().getBaseContext(),
                        AboutThis.class);
                intent.putExtra("message", helper.getUid());
                getActivity().startActivity(intent);

            }
        });

        return v;
    }




}