package com.example.tari9;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class request_worker extends FragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

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
    private Button btnrefuse, btnaccept, btnlist, btnprofile;
    private String userid, address;
    private Map<String, Object> addy;
    private static int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String CHANNEL_ID = "MyChannel";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_worker);

        btnlist = findViewById(R.id.list_requests);
        btnprofile = findViewById(R.id.profile);
        btnaccept = findViewById(R.id.accept);
        btnrefuse = findViewById(R.id.refuse);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        userid = user.getUid();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng location1 = new LatLng(35.0749, 5.4194); // San Francisco, CA
                LatLng location2 = new LatLng(35.7128, 6.0060); // New York, NY

                MarkerOptions markerOptions1 = new MarkerOptions().position(location1).title("San Francisco");
                googleMap.addMarker(markerOptions1);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 15));

                MarkerOptions markerOptions2 = new MarkerOptions().position(location2).title("New York");
                googleMap.addMarker(markerOptions2);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, 9));

                // Create the polyline options
                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(location1)
                        .add(location2)
                        .width(5) // Set the width of the polyline
                        .color(Color.RED); // Set the color of the polyline

                // Add the polyline to the map
                Polyline polyline = googleMap.addPolyline(polylineOptions);
                /*
                // Create a Directions API client
DirectionsApiClient directionsApiClient = new DirectionsApiClient(this);

// Set the origin and destination locations
LatLng origin = new LatLng(35.0749, 5.4194); // San Francisco, CA
LatLng destination = new LatLng(35.7128, 6.0060); // New York, NY

// Create a directions request
DirectionsRequest directionsRequest = new DirectionsRequest.Builder()
        .origin(origin)
        .destination(destination)
        .build();

// Send the directions request and handle the response
directionsApiClient.requestDirections(directionsRequest, new DirectionsApiResponseListener() {
    @Override
    public void onSuccess(DirectionsResponse directionsResponse) {
        // Get the list of points for the polyline
        List<LatLng> points = directionsResponse.getRoutes().get(0).getOverviewPolyline().getPoints();

        // Create the polyline options
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(points)
                .width(5) // Set the width of the polyline
                .color(Color.RED); // Set the color of the polyline

        // Add the polyline to the map
        Polyline polyline = googleMap.addPolyline(polylineOptions);
    }

    @Override
    public void onFailure(Throwable throwable) {
        Log.e(TAG, "Failed to get directions", throwable);
    }
});

                * */
            }});
        btnprofile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  FirebaseAuth.getInstance().signOut();
                sendNotification("Your Request was accepted", "Please, Wait");
                Intent activityChangeIntent = new Intent(request_worker.this, profile_worker.class);
                request_worker.this.startActivity(activityChangeIntent);
                finish();
            }
        });
        btnlist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  FirebaseAuth.getInstance().signOut();
                Intent activityChangeIntent = new Intent(request_worker.this, list_requests_worker.class);
                request_worker.this.startActivity(activityChangeIntent);
                finish();
            }
        });
        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification("Your Request was accepted", "Please, Wait");
            }
        });
        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification("Your Request was refused", "Please, try  again");
            }
        });
    }
    private void sendNotification(String title, String message) {
        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.settings)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Create a unique notification id using the current time
        long notificationId = System.currentTimeMillis();

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify((int) notificationId, builder.build());


    }

    private void showNotification(request request) {
        //unique id to show new notification each time we click notification, if you want to replace, previous use a constant volue as id
        Date date = new Date();
        int notificationId = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(date));

        Intent mainIntent = new Intent(this, list_requests_worker.class);
        //if you want to pass data in notification and get in required activity
        mainIntent.putExtra("KEY_NAME", "ATIF PERVAIZ");
        mainIntent.putExtra("KEY_EMAIL", "mohammadatif50@gmail.com");
        mainIntent.putExtra("KEY_TYPE", "Notification Content Clicked...");
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_ONE_SHOT);

        //handle (call) action button click, start SecondActivity by Topping it. (You may start different activities and pass different data in each click)
        Intent callIntent = new Intent(this, list_requests_worker.class);
        //if you want to pass data in notification and get in required activity
        callIntent.putExtra("KEY_NAME", "ATIF PERVAIZ");
        callIntent.putExtra("KEY_EMAIL", "mohammadatif50@gmail.com");
        callIntent.putExtra("KEY_TYPE", "Call Button Clicked...");
        PendingIntent callPendingIntent = PendingIntent.getActivity(this, 2, callIntent, PendingIntent.FLAG_IMMUTABLE);

        //handle (message) action button click, start SecondActivity by Topping it. (You may start different activities and pass different data in each click)
        Intent messageIntent = new Intent(this, list_requests_worker.class);
        //if you want to pass data in notification and get in required activity
        messageIntent.putExtra("KEY_NAME", "ATIF PERVAIZ");
        messageIntent.putExtra("KEY_EMAIL", "mohammadatif50@gmail.com");
        messageIntent.putExtra("KEY_TYPE", "Message Button Clicked...");
        PendingIntent messagePendingIntent = PendingIntent.getActivity(this, 3, messageIntent, PendingIntent.FLAG_IMMUTABLE);

        //creating notification builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                //notification icon
                .setSmallIcon(R.drawable.logonaked)
                //notification title
                .setContentTitle("New Request")
                //notification description
                .setContentText("You Have A New Request, Click To See More")
                //notification priority
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //cancel notification on click
                .setAutoCancel(true)
                //add click intent
                .setContentIntent(mainPendingIntent)
                //add action button (call)
                .addAction(R.drawable.settings, "Accept", callPendingIntent)
                //add action button (message)
                .addAction(R.drawable.chat, "Refuse", messagePendingIntent);

        //notification manager
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(notificationId, notificationBuilder.build());
    }

    private void createNotificationChannel(){
        /*To show notification on Android Oreo (API 26) and above we have to create notification channel*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "MyNotification";
            String description = "My notification channel description";
            //importance of your notification
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
