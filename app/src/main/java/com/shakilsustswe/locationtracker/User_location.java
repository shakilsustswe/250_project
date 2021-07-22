package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class User_location extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private String provider;

    private EditText addressField;
    private ImageButton searchingBtn;
    private ImageButton secDirection;


    Spinner map_type;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getContext(),
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
    };

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_user_location, container, false);
        addressField = v.findViewById(R.id.fragment_user_location_searchingEditTxt);
        searchingBtn = v.findViewById(R.id.fragment_user_location_searchingBtn);
        secDirection = v.findViewById(R.id.fragment_user_locationDirectionBtn);

        map_type = v.findViewById(R.id.fragment_user_location_maptype);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.may_type, android.R.layout.simple_spinner_item);
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


//        Places.initialize();
        searchingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchingLocation();
            }
        });

        secDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });



        return v;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }


        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastLocation = location;

        if(currentUserLocationMarker != null){
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        try{
            locationManager = (LocationManager)
                    getContext().getSystemService(Context.LOCATION_SERVICE);
            provider = locationManager.getBestProvider(new Criteria(), true);
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location locations = locationManager.getLastKnownLocation(provider);
            List<String> providerList = locationManager.getAllProviders();
            if (null != locations && null != providerList && providerList.size() > 0) {
                double longitude = locations.getLongitude();
                double latitude = locations.getLatitude();
                Geocoder geocoder = new Geocoder(getContext(),
                        Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(latitude,
                            longitude, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        String state = listAddresses.get(0).getAdminArea();
                        String country = listAddresses.get(0).getCountryName();
                        String subLocality = listAddresses.get(0).getSubLocality();
                        markerOptions.title("" + latLng + "," + subLocality + "," + state
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
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

        try{
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(getContext(),
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
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getContext(), "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }




    private void showSearchingLocation() {

        String address = addressField.getText().toString();

        List<Address> addressList = null;
        MarkerOptions userMarkerOptions = new MarkerOptions();
        if(!TextUtils.isEmpty(address)){
            secDirection.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Tap to conner see the direction road map", Toast.LENGTH_SHORT).show();
            Geocoder geocoder = new Geocoder(getContext());
            try{
                addressList = geocoder.getFromLocationName(address, 6);
                if(addressList != null){
                    for(int i = 0; i < addressList.size(); i++){
                        Address userAddress = addressList.get(i);

                        LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());
                        userMarkerOptions.position(latLng);
                        userMarkerOptions.title(address);
                        userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        mMap.clear();
                        mMap.addMarker(userMarkerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                    }
                }
                else{
                    Toast.makeText(getContext(),  "Location not found....", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){

            }
        }
        else{
            if(TextUtils.isEmpty(addressField.getText().toString())){
                secDirection.setVisibility(View.INVISIBLE);
            }
            Toast.makeText(getContext(), "Please enter a location name", Toast.LENGTH_SHORT).show();
        }
    }



}


