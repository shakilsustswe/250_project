package com.shakilsustswe.locationtracker;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeScreen extends Fragment implements View.OnClickListener {


    private FloatingActionButton addmeeting;
    private FloatingActionButton shearlocation;
    private AlertDialog builder;
    private AlertDialog.Builder dialogbuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home_screen,
                container,
                false);
        addmeeting = v.findViewById(R.id.fragment_home_addmeeting);
        shearlocation = v.findViewById(R.id.fragment_home_shearlocation);
        addmeeting.setOnClickListener((View.OnClickListener) this);
        shearlocation.setOnClickListener((View.OnClickListener) this);
        return v;
    }

    /*Enable options menu in this fragment*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public void onClick(View v) {
        if(v.getId()  == R.id.fragment_home_shearlocation){
            shearLocation();
        }
        if(v.getId() == R.id.fragment_home_addmeeting){

        }
    }

    private void shearLocation(){
        dialogbuilder = new AlertDialog.Builder(getContext());
        final View shear = getLayoutInflater().inflate(R.layout.shearlocation, null);
        dialogbuilder.setView(shear);

        builder = dialogbuilder.create();
        builder.show();
    }

}