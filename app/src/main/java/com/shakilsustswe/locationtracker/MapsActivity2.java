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
import com.shakilsustswe.locationtracker.databinding.ActivityMaps2Binding;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;

    private Intent i;
    private String user_name;
    private double latitude;
    private double longitude;

    double a = 10, b = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        i = new Intent();
        try{
            user_name = i.getExtras().getString("USER_NAME");
            latitude = i.getExtras().getDouble("Latitude");
            latitude = i.getExtras().getDouble("longitude");
            a = latitude;
            b = longitude;

            MarkerOptions userMarkerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(a, b);
            userMarkerOptions.position(latLng);
            userMarkerOptions.title("YOU");
            userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            mMap.clear();
            mMap.addMarker(userMarkerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


            Toast.makeText(this, "" + latitude+longitude, Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){

        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions userMarkerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(a, b);
        userMarkerOptions.position(latLng);
        userMarkerOptions.title("YOU");
        userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        mMap.clear();
        mMap.addMarker(userMarkerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}