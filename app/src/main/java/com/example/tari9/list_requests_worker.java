package com.example.tari9;


import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.get_requests;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.UUID;

public class list_requests_worker extends AppCompatActivity{
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String clientid;
    private Button btnhome, btngoback, btnhelpcenter, btnlist, btnprofile;
    private RecyclerView recyclerView;
    private ArrayList<get_requests> get_requests;
    private com.example.tari9.adapter_requests_worker adapter_requests_worker;
    private ProgressDialog pd;
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_requests_worker);

        pd = new ProgressDialog(this);
        //   pd.setCancelable(false);
        // pd.setMessage("Fetchingdata");
        //  pd.show();

        btnlist = findViewById(R.id.list_requests);
        btnprofile = findViewById(R.id.profile);
        recyclerView = findViewById(R.id.recycler_request_worker);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        clientid = user.getUid();

        get_requests = new ArrayList<get_requests>();
        adapter_requests_worker = new adapter_requests_worker(this, get_requests, new listener_request_worker() {
            @Override
            public void onItemClicked(String document_id, get_requests request, int position) {
                notif();
                Intent intent = new Intent(list_requests_worker.this, request_worker.class);
                list_requests_worker.this.startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter_requests_worker);

        get_list_requests();
        btnprofile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //  FirebaseAuth.getInstance().signOut();
                Intent activityChangeIntent = new Intent(list_requests_worker.this, profile_worker.class);
                list_requests_worker.this.startActivity(activityChangeIntent);
                finish();
            }
        });
     //   notification();
    }

    private void notif() {
        db.collection("request").whereEqualTo("worker_id", user.getUid()).whereEqualTo("state", "waiting").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                    if (change.getType() == DocumentChange.Type.ADDED) {
                        DocumentSnapshot addedRequest = change.getDocument();
                        String clientid = addedRequest.getString("client_id");
                        sendNotificationToUser(clientid, "New request created");
                    }
                }
            }
        });
    }

   /* private void notification() {
        db.collection("request").whereEqualTo("mechanicid",user.getUid()).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        DocumentSnapshot documentSnapshot = dc.getDocument();
                        String documentId = documentSnapshot.getId();
                        String documentName = documentSnapshot.getString("name");

                        // Send notification to user
                        sendNotification(documentName + " document created with ID: " + documentId);
                    }
                }
            }
        });
    }*/

    private void sendNotificationToUser(String clientid, String message) {
            // Get the user's device token from Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("client").document(clientid);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot userSnapshot = task.getResult();
                        if (userSnapshot.exists()) {
                            String deviceToken = userSnapshot.getString("token");
                            if (deviceToken != null && !deviceToken.isEmpty()) {
                                // Send the notification using FCM
                                FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(deviceToken)
                                        .setMessageId(UUID.randomUUID().toString())
                                        .addData("message", message)
                                        .build());
                            }
                        }
                    }
                }
            });
    }

    private void sendNotification(String message) {
     /*   Intent intent = new Intent(this, list_requests_worker.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("New Document Created")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());*/
    }

    private void get_list_requests() {
        db.collection("request").whereEqualTo("client_id", clientid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                get_requests request = dc.getDocument().toObject(get_requests.class);
                                get_requests.add(request);
                            }
                            adapter_requests_worker.notifyDataSetChanged();
                        }
                    }
                });
    }
    public void onMessageReceived(RemoteMessage remoteMessage) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logonaked)
                .setContentTitle("New Document Created")
                .setContentText("A new document has been created")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create an intent to open the activity when the user taps the notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


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
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}