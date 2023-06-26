package com.example.tari9;
import android.app.Activity;
import android.view.View;
import androidx.core.view.ViewCompat;

public class swipeBack {
    private Activity activity;
    private float startX;
    private boolean enabled = true;

    public swipeBack(Activity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        activity.getWindow().setBackgroundDrawable(null);
    }

    public void onPostCreate() {
        View decorView = activity.getWindow().getDecorView();
        decorView.setOnTouchListener((v, event) -> {
            if (enabled) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    startX = event.getX();
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    float endX = event.getX();
                    if (endX - startX > 100 && ViewCompat.canScrollHorizontally(v, -1)) {
                        activity.finish();
                    }
                }
            }
            return false;
        });
    }

    public View findViewById(int id) {
        return activity.findViewById(id);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void onBackPressed() {
        activity.finish();
    }

    public void onDestroy() {
        activity = null;
    }
}
