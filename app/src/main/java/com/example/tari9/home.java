package com.example.tari9;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class home extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LatLng latLng;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker marker;
    private FirebaseFirestore db;
    private EditText userlocation;
    private Button btnnext, btnhome, btnlist, btnprofil;
    private String clientid, requestid, requesttype, requestservice, address;
    private Map<String, Object> addy;
    private static int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        userlocation = findViewById(R.id.userlocation);

        btnnext = findViewById(R.id.next);
        btnhome = findViewById(R.id.home);
        btnlist = findViewById(R.id.list_requests);
        btnprofil = findViewById(R.id.profile);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        clientid = user.getUid();
        Toast.makeText(this, "Wait, till your address shows", Toast.LENGTH_SHORT).show();

        if (getIntent().getStringExtra("request_mechanic_id")!=null){
            requestid = getIntent().getStringExtra("request_mechanic_id");
        }
        if (getIntent().getStringExtra("request_tow_id")!=null){
            requestid = getIntent().getStringExtra("request_tow_id");
        }
        if (getIntent().getStringExtra("request_station_id")!=null){
            requestid = getIntent().getStringExtra("request_station_id");
        }
        if (getIntent().getStringExtra("request_team_id")!=null){
            requestid = getIntent().getStringExtra("request_team_id");
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        startLocationListener();

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(home.this, R.anim.button_vibrate);
                btnnext.startAnimation(shake);
                if (getIntent().getStringExtra("request_mechanic_id")!=null){
                    Intent activityChangeIntent = new Intent(home.this, list_mechanics.class);
                    activityChangeIntent.putExtra("request_mechanic_id", getIntent().getStringExtra("request_mechanic_id"));
                    home.this.startActivity(activityChangeIntent);
                }
                if (getIntent().getStringExtra("request_tow_id")!=null){
                    Intent activityChangeIntent = new Intent(home.this, list_tows.class);
                    activityChangeIntent.putExtra("request_tow_id", getIntent().getStringExtra("request_tow_id"));
                    if (getIntent().getStringExtra("request_taxi_id")!=null){
                        activityChangeIntent.putExtra("request_taxi_id", getIntent().getStringExtra("request_taxi_id"));
                    }
                    if (getIntent().getStringExtra("request_ambulance_id")!=null){
                        activityChangeIntent.putExtra("request_ambulance_id", getIntent().getStringExtra("request_ambulance_id"));
                    }
                    home.this.startActivity(activityChangeIntent);
                }
                if (getIntent().getStringExtra("request_station_id")!=null){
                    Intent activityChangeIntent = new Intent(home.this, list_stations.class);
                    activityChangeIntent.putExtra("request_station_id", getIntent().getStringExtra("request_station_id"));
                    home.this.startActivity(activityChangeIntent);
                }
                if (getIntent().getStringExtra("request_team_id")!=null){
                    Intent activityChangeIntent = new Intent(home.this, list_garage.class);
                    activityChangeIntent.putExtra("request_team_id", getIntent().getStringExtra("request_team_id"));
                    home.this.startActivity(activityChangeIntent);
                }
            }
        });
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(home.this, menu.class);
                home.this.startActivity(activityChangeIntent);
            }
        });
        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(home.this, list_requests.class);
                home.this.startActivity(activityChangeIntent);
            }
        });
        btnprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(home.this, profile.class);
                home.this.startActivity(activityChangeIntent);
            }
        });
    }
    private void startLocationListener() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                        View customLayout = getLayoutInflater().inflate(R.layout.location_alert_diag, null);
                        ImageView icon = customLayout.findViewById(R.id.alert_icon);
                        builder.setView(customLayout)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                        startLocationListener();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }}, 2500);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Locale desiredLocale = Locale.ENGLISH;
                    Geocoder geocoder = new Geocoder(getApplicationContext(), desiredLocale);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        address = addresses.get(0).getCountryName() + " ";
                        address += addresses.get(0).getSubAdminArea();

                        addy = new HashMap<>();
                        addy.put("country", addresses.get(0).getCountryName());
                        addy.put("city", addresses.get(0).getSubAdminArea());

                        userlocation.setText(address);
                        btnnext.setEnabled(true);

                        latLng = new LatLng(latitude, longitude);
                        updateclientlocation();
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("You"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }}

    private void updateclientlocation() {
        Map<String, Object> request = new HashMap<>();

        GeoPoint location = new GeoPoint(latLng.latitude, latLng.longitude);
        request.put("location", location);
        request.put("address", addy);

        Map<String, Object> client = new HashMap<>();
        client.put("location", location);
        client.put("location_address", addy);
        db.collection("client").document(clientid).update(client);
        db.collection("request").document(requestid).update(request);
        if (getIntent().getStringExtra("request_taxi_id")!=null){
            db.collection("request").document(getIntent().getStringExtra("request_taxi_id")).update(request);
        }
        if (getIntent().getStringExtra("request_ambulance_id")!=null){
            db.collection("request").document(getIntent().getStringExtra("request_ambulance_id")).update(request);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                startActivityForResult(intent, LOCATION_PERMISSION_REQUEST_CODE);
                startLocationListener();
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Locale desiredLocale = Locale.ENGLISH;
                        Geocoder geocoder = new Geocoder(getApplicationContext(), desiredLocale);
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            address = addresses.get(0).getCountryName() + " ";
                            address += addresses.get(0).getSubAdminArea();

                            addy = new HashMap<>();
                            addy.put("country", addresses.get(0).getCountryName());
                            addy.put("city", addresses.get(0).getSubAdminArea());

                            userlocation.setText(address);
                            btnnext.setEnabled(true);

                            latLng = new LatLng(latitude, longitude);
                            updateclientlocation();

                            if (marker != null) {
                                marker.remove();
                            }

                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("You"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                };
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (requestid!=null){
            db.collection("request").document(requestid).delete();
            finish();
        } else {
            finish();
        }
    }
}