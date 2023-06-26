package com.example.tari9;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class chat extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter);

        editTextMessage = findViewById(R.id.get);
        buttonSend = findViewById(R.id.gett);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                }
            }
        });
    }

    private void sendMessage(String message) {
        String receiverToken = "dL5z_kw-SRq80KJZrd-bVd:APA91bE0-NWm42ZIBCMSuq20XRF4U-ail0ogWvmhg3c7JZ2MyjqfCLVPMirQsRbpgej_yXTcApgs1gfMtkhQLMhpkALDCSnKyfqkMrg8LDaZlH66raRJ8KMEEvhG35Rlg8-kX3MNjjra"; // Replace with the actual receiver's FCM token

        Map<String, String> data = new HashMap<>();
        data.put("message", message);

        RemoteMessage.Builder messageBuilder = new RemoteMessage.Builder(receiverToken + "@gcm.googleapis.com");
        messageBuilder.setMessageId(Integer.toString(new Random().nextInt(9999)));
        messageBuilder.setData(data);

        FirebaseMessaging.getInstance().send(messageBuilder.build());
    }
}
