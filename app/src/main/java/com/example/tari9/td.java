package com.example.tari9;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class td extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.td);

        // Get data from intent
        String name = getIntent().getStringExtra("KEY_NAME");
        String email = getIntent().getStringExtra("KEY_EMAIL");
        String type = getIntent().getStringExtra("KEY_TYPE");

        // Display data in text view
        TextView textView = findViewById(R.id.text_view);
        textView.setText("Name: " + name + "\nEmail: " + email + "\nType: " + type);
    }
}
