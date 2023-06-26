package com.example.tari9;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class splash_screen extends AppCompatActivity {
    private TextView textView, full, ineed;
    private String[] textArray = {" Oil", " Fuel", " A Tow", " A Taxi", " A Mechanic",
            " An Ambulance", " Tari9"};
    private int currentIndex = 0, usertype = 0;
    private LinearLayout logo;
    private ProgressBar progressBar;
    private LinearLayout text;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            userid = auth.getCurrentUser().getUid();}

        logo = findViewById(R.id.logo);
        textView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);
        text = findViewById(R.id.text);
        ineed = findViewById(R.id.ineed);
        animateLogo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setVisibility(View.GONE);

                float fromX = 0;
                float toX = 0;
                long duration = 4000;

                Animation animation = new TranslateAnimation(fromX, toX, 0, 0);
                animation.setDuration(duration);
                text.startAnimation(animation);
                text.setVisibility(View.VISIBLE);
            }
        }, 8000);
    }
    private void animateLogo() {
        logo.setScaleX(0.05f);
        logo.setScaleY(0.05f);

        logo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateLogoZoomOut();
                        animateText();
                        simulateBackgroundTask();
                    }
                })
                .start();
    }

    private void animateLogoZoomOut() {
        logo.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateLogoZoomIn();
                    }
                })
                .start();
    }

    private void animateLogoZoomIn() {
        logo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateLogoZoomOut();
                    }
                })
                .start();
    }


    private void animateText() {
        if (currentIndex >= textArray.length) {
            return;
        }

        final String currentText = textArray[currentIndex];
        final int duration = getDurationByIndex(currentIndex);
        final boolean isLastText = currentIndex == textArray.length - 1;

        textView.setText(currentText);
        textView.setAlpha(1f);
        textView.setTranslationY(0f);

        if (isLastText) {
            currentIndex++;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;

            float slideDistance = (text.getX() + (text.getWidth() / 2)) - (screenWidth / 2);
            text.animate()
                    .translationXBy(-slideDistance)
                    .setDuration(duration)
                    .start();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        textView.animate()
                .alpha(0f)
                .setDuration(duration)
                .setStartDelay(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        textView.setTranslationY(-getPixelsFromDp(20));
                        currentIndex++;
                        animateText();
                    }
                })
                .start();
    }


    private int getDurationByIndex(int index) {
        switch (index) {
            case 0:
                return 2000;
            case 1:
                return 1000;
            case 2:
                return 500;
            default:
                return 0;
        }
    }


    private int getPixelsFromDp(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    private void simulateBackgroundTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int progress = 0; progress <= 100; progress += 10) {
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final int currentProgress = progress;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(currentProgress);

                            if (currentProgress == 90) {
                                checkUserExistence();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private void checkUserExistence() {
        db.collection("client").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    progressBar.setProgress(100);
                    if (document.exists()) {
                        Intent intent = new Intent(getApplicationContext(), menu.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), insign.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(splash_screen.this, "Please check your connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}