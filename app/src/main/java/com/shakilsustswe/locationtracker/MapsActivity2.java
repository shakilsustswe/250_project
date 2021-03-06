package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.shakilsustswe.locationtracker.databinding.ActivityMaps2Binding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.http.Url;


public class MapsActivity2 extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{


    private RelativeLayout rootLayout;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private String provider;
    private FirebaseUser firebaseUser;
    private Task<Void> firebaseDatabase;
    private FirebaseAuth mAuth;
    private Task<Void> databaseReference;

    private Intent i;
    private String user_name;
    String path;

    private Button secDirection;

    private TextView textViewYou, textViewOthers;

    double a = 10, b = 10;
    private Spinner map_type;
    private boolean haveAccess = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }


        map_type = findViewById(R.id.activity_maps_map_type);
        rootLayout = findViewById(R.id.activity_maps2_root_layout);
        textViewOthers = findViewById(R.id.activity_map_2_text_others);
        textViewYou = findViewById(R.id.activity_map_2_text_you);
        secDirection = findViewById(R.id.activity_map_2_see_direction);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        Intent intent = getIntent();
        String message = intent.getStringExtra("uid");


        if(message != null)
            run(message);
        else{
            Snackbar snackbar = Snackbar
                    .make(rootLayout, "Having same networking problem", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        mapType();

        secDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayDirection();
            }
        });

    }

    private void run(String uid){
        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                checkHavePermission(uid);
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 5000);
    }

    private void showLocation(String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Location").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String name = snapshot.child("name").getValue().toString();
                    String  lat = snapshot.child("latitude").getValue().toString();
                    String log = snapshot.child("longitude").getValue().toString();

                    double a= Double.parseDouble(lat);
                    double b= Double.parseDouble(log);
                    textViewOthers.setText(name+ " : " +lat +" , " +log);
                    try{
                        path = lat+","+log;
                    }
                    catch (Exception e){

                    }


                    mMap.clear();
                    MarkerOptions userMarkerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(a,b);
                    userMarkerOptions.position(latLng);
                    userMarkerOptions.title(name);
                    userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    mMap.addMarker(userMarkerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                }
                catch (Exception e){

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }


    private void mapType() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.may_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        map_type.setAdapter(adapter);

        map_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = parent.getItemAtPosition(position).toString();
                if(type.equals("NORMAL")){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                else if(type.equals("HYBRID")){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
                else if(type.equals("NONE")){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                }
                else if(type.equals("SALELITIE")){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                else if(type.equals("TERRAIN")){
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {
        try{
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                        locationRequest, this);
            }
        }
        catch (Exception e){

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        textViewYou.setText("You :" + location.getLatitude() + " , " + location.getLongitude());
        if(currentUserLocationMarker != null){
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        try{
            locationManager = (LocationManager)
                    this.getSystemService(Context.LOCATION_SERVICE);
            provider = locationManager.getBestProvider(new Criteria(), true);
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location locations = locationManager.getLastKnownLocation(provider);
            List<String> providerList = locationManager.getAllProviders();
            if (null != locations && null != providerList && providerList.size() > 0) {
                double longitude = locations.getLongitude();
                double latitude = locations.getLatitude();
                Geocoder geocoder = new Geocoder(this,
                        Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(latitude,
                            longitude, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        String state = listAddresses.get(0).getAdminArea();
                        String country = listAddresses.get(0).getCountryName();
                        String subLocality = listAddresses.get(0).getSubLocality();
                        markerOptions.title("You" + latLng + "," + subLocality + "," + state
                                + "," + country);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            currentUserLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            if (googleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,
                        this);
            }
        }
        catch (Exception e){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void DisplayDirection(){
        try{
            Intent i = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("google.navigation:q="+path);
            i.setData(uri);
            startActivity(i);
        }
        catch (Exception e){

        }
    }

    private void checkHavePermission(String uid){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("ShareLocation").child(FirebaseAuth.getInstance().getUid());
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uid)){
                    showLocation(uid);
//                    Snackbar snackbar = Snackbar
//                            .make(rootLayout, "ON", Snackbar.LENGTH_LONG);
//                    snackbar.show();
                }
                else{
                    Snackbar snackbar = Snackbar
                            .make(rootLayout, "Sorry, User Turn off her live location to you", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

}