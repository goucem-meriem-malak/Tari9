package com.example.tari9;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class todelete1 extends AppCompatActivity {

    private static final String CHANNEL_ID = "MyChannel";
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todelete);

        createNotificationChannel();

     /*   Button btn = findViewById(R.id.btn_send_notification);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
            }
        });*/

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference requestsRef = db.collection("request");

        requestsRef.whereEqualTo("worker_id", user.getUid()).whereEqualTo("state", "waiting")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "Error getting request snapshot: ", error);
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                if (documentSnapshot.exists()) {
                                    request request = documentSnapshot.toObject(request.class);
                                    showNotification(request);
                                } else {
                                    Log.d(TAG, "No matching request found");
                                }
                            }
                    }
                });
 /*       requestsRef.whereEqualTo("client_id", user.getUid()).whereEqualTo("state", "waiting")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Error getting request snapshot: ", error);
                            return;
                        }

                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            if (documentSnapshot.exists()) {
                                request request = documentSnapshot.toObject(request.class);
                                showNotification(request);
                            } else {
                                Log.d(TAG, "No matching request found");
                            }
                        }
                    }
                });*/
        }

    private void sendNotification(String title, String message) {
        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
