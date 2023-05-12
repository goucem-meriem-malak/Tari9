package com.example.tari9;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


public class messaging extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String requestid, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mess);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        requestid = getIntent().getStringExtra("requestid");

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                token = task.getResult();
                Toast.makeText(messaging.this, token, Toast.LENGTH_SHORT).show();
                System.out.println(token);
                Map m = new HashMap();
                m.put("token", token);
                db.collection("mechanic").document(user.getUid()).update(m);
            }
        });
/*
        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
        String workerToken = "worker_device_token";

        Map<String, String> data = new HashMap<>();
        data.put("requestId", requestid);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setTitleText("New Request")
                .setBody("You have a new request to review")
                .build();

        RemoteMessage message = new RemoteMessage.Builder(workerToken)
                .setData(data)
                .setNotification(notification)
                .build();

        messaging.send(message)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String messageId) {
                        Log.d(TAG, "Message sent successfully: " + messageId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error sending message", e);
                    }
                });

 */
    }
    }