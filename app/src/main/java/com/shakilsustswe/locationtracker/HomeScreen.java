package com.shakilsustswe.locationtracker;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class HomeScreen extends Fragment implements View.OnClickListener {


    private RelativeLayout rootLayout;
    private FloatingActionButton addmeeting;
    private FloatingActionButton shearlocation;
    private AlertDialog builder;
    private AlertDialog.Builder dialogbuilder;
    private User_location user_location;

    private Switch aSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home_screen,
                container,
                false);
        addmeeting = v.findViewById(R.id.fragment_home_addmeeting);
        shearlocation = v.findViewById(R.id.fragment_home_shearlocation);
        aSwitch = v.findViewById(R.id.user_location_shearing_permission);
        rootLayout = v.findViewById(R.id.fragment_home_screen_root_layout);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aSwitch.setText("Stop Shearing");
                    Snackbar snackbar = Snackbar
                            .make(rootLayout, "Location Shearing Services Start, Successfully", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else{
                    aSwitch.setText("Start Shearing");
                    Snackbar snackbar = Snackbar
                            .make(rootLayout, "Location Shearing Services Stop, Successfully", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });



        addmeeting.setOnClickListener((View.OnClickListener) this);
        shearlocation.setOnClickListener((View.OnClickListener) this);


        user_location = new User_location();
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