package com.example.tari9;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class launch_screen extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userid;
    private ImageView myImageView, name;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
        userid = auth.getCurrentUser().getUid();}

        myImageView = findViewById(R.id.main_logo);
        name = findViewById(R.id.app_name);
        pb = findViewById(R.id.pb);

        if (userid != null) {
            db.collection("client").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            clients();
                        } else {
                            news();
                        }
                    }
                }
            });
        } else {
            news();
        }
             new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up);
                myImageView.startAnimation(slideUpAnimation);
                name = findViewById(R.id.app_name);
                name.setVisibility(View.VISIBLE);
                Animation fadeinAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                fadeinAnimation.setFillAfter(true);
                Animation shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.small_shake);
                fadeinAnimation.setFillAfter(true);
                name.startAnimation(shakeAnimation);
            }
        }, 2000);
        name.setVisibility(View.VISIBLE);
    }

    private void clients() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(launch_screen.this, menu.class);
                launch_screen.this.startActivity(intent);
                finish();
            }
        }, 3000);
    }
    private void news() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(launch_screen.this, upsign.class);
                launch_screen.this.startActivity(intent);
                finish();
            }
        }, 3000);
    }
}