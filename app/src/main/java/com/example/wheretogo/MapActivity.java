package com.example.wheretogo;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_CODE = 1234;
    private static final float ZOOM = 50;
    private CustomInfoAdapter customInfoAdapter;

    //Widgets
    private EditText SearchText;
    private ImageView Gps;
    private ImageView Info;

    //Variables
    private Boolean LocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //Initialize widgets
        SearchText = findViewById(R.id.search_input);
        Gps = findViewById(R.id.ic_gps);
        Info = findViewById(R.id.ic_info);
        // Object from class that custom the card that pop up on marker
        customInfoAdapter = new CustomInfoAdapter(this);

        //check for permission for location
        getLocationPermission();


    }

    private void init(){

        SearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });

        Gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });

        Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(marker.isInfoWindowShown()){
                        marker.hideInfoWindow();
                    }else{
                        marker.showInfoWindow();
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, "onClickL NullPointerException: " + e.getMessage());
                }
            }
        });

        HideSoftKeyboard();
    }
    private void getweather(double lat,double lon){
        newweather obj=new newweather(this,customInfoAdapter.getmWeather(),String.valueOf(lat),String.valueOf(lon));


    }
    private void geoLocate(){
        //This function return the location from the search text
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = SearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), ZOOM, address);
        }
    }

    // this function get my device location
    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (LocationPermissionGranted) {
                // if location is granted
                // then create a task to get last location of my device
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        if (task.isSuccessful()) {
                            // on complete and successful
                            // get current location
                            Location currentLoaction = (Location) task.getResult();

                            try{
                                // create a geocoder object
                                Geocoder geocoder = new Geocoder(MapActivity.this);
                                // get address of my location using GeoCoder
                                Address address = geocoder.getFromLocation(currentLoaction.getLatitude()
                                    , currentLoaction.getLongitude()
                                    ,1).get(0);
                                // then move the camera to this location
                                 moveCamera(new LatLng(currentLoaction.getLatitude(), currentLoaction.getLongitude()), ZOOM, address);
                            }catch (IOException e){
                                Log.e(TAG, "IOException: " + e.getMessage());
                            }
                        } else {
                            Toast.makeText(MapActivity.this, "unable to find current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security Execption: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, Address address)
    {
        //this function move the camera to the location passed

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        // remove all dots from map
        mMap.clear();
        //setting info adapter to my custom adapter
        mMap.setInfoWindowAdapter(customInfoAdapter);

        if(address != null){
            try {
                String snippet = "Address: " + address.getAddressLine(0) + "\n";

               // getNews(address.getCountryCode());
    getweather(address.getLatitude(),address.getLongitude());
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(address.getAddressLine(0))
                        .snippet(snippet);

                marker = mMap.addMarker(options);
            }catch (NullPointerException e){
                Log.e(TAG, "MarkerInfo: NullPointerException: " + e.getMessage());
            }
        }
        else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        HideSoftKeyboard();
    }

   /* private void getNews(String article){
        NewsMap newsMap = new NewsMap(this, customInfoAdapter.getmNews(),customInfoAdapter.getmNewsTitle(),article);
    }*/

    /*private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
        HideSoftKeyboard();
    }*/

    private void initMap() {
        //initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // After finishing will call on map ready
        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission() {
        // Array of permissions
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationPermissionGranted = true;
                // if all permissions is granted then initialize map
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            LocationPermissionGranted = false;
                            return;
                        }
                    }
                    LocationPermissionGranted = true;
                    //initialize or map
                    initMap();
                }
                break;
        }
    }


    //This function is called after init->getMapAsnyc
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (LocationPermissionGranted) {
            // if location permission is granted
            //get device location
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //if no permission return
                return;
            }
            //put little dot on my location
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            // initialize controls
            init();
        }
    }

    private void HideSoftKeyboard(){
        // hide keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
