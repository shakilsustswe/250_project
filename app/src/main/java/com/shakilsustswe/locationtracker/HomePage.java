package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private AlertDialog.Builder builder;
    private AlertDialog.Builder popupwindow;
    private AlertDialog shearloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNav = findViewById(R.id.home_page_navigationbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, Login.class));
            finish();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.home_page_frame, new HomeScreen()).commit();

    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected( MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    selectedFragment = new HomeScreen();
                    break;
                case R.id.navigation_emergency:
                    selectedFragment = new EmergencyHelp();
                    break;
                case R.id.navigation_mylocation:
                    selectedFragment = new MyLocation();
                    break;
                case R.id.navigation_notification:
                    selectedFragment = new Notification();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.home_page_frame, selectedFragment).commit();
            return true;
        }
    };

//    @Override
//    protected void onStart() {
//        if(mAuth.getCurrentUser() == null){
//            startActivity(new Intent(this, Login.class));
//            finish();
//        }
//        super.onStart();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_page_menu_messege:
                Message();
                Toast.makeText(getApplicationContext(), "Adding chatting feature", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_page_menu_friendrequest:
                Toast.makeText(getApplicationContext(), "Adding friend request feature", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_page_menu_fiendfriends:
                findFriend();
                Toast.makeText(getApplicationContext(), "Adding chatting friend list", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_page_menu_profilesetting:
                settingProfiles();
                Toast.makeText(getApplicationContext(), "Adding profiles setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_page_menu_logout:
                // tmr code copy koresiii.. hahahahahaha(from main activity theke)
                logout();
                break;
            case R.id.home_page_menu_myfriends:
                Toast.makeText(getApplicationContext(), "Adding profiles setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_page_menu_about:
                Toast.makeText(getApplicationContext(), "Adding profiles setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findFriend() {
        Intent intent = new Intent(getApplicationContext(), Alluserslist.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
    private  void Message()
    {
        startActivity(new Intent(getApplicationContext(),Alluserslist.class));
    }

    private void settingProfiles() {
        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
    }


    private void logout() {
        builder = new AlertDialog.Builder(this);

        builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to log out??")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            finish();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), Login.class));
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
                alert.setTitle("Log Out?");
                alert.show();
            }



}
