package com.shakilsustswe.locationtracker;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shakilsustswe.locationtracker.databinding.ActivityShowLocationBinding;

public class ShowLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityShowLocationBinding binding;


    private Intent i;
    private String user_name;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowLocationBinding.inflate(getLayoutInflater());

        i = getIntent();
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        user_name = i.getExtras().getString("USER_NAME");
        latitude = i.getExtras().getDouble("Latitude");
        latitude = i.getExtras().getDouble("longitude");

        Toast.makeText(ShowLocation.this, user_name, Toast.LENGTH_SHORT).show();
        try{


        }
        catch (Exception e){

        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        MarkerOptions userMarkerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(24, 91);
        userMarkerOptions.position(latLng);
        userMarkerOptions.title("Your");
        userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        mMap.clear();
        mMap.addMarker(userMarkerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}