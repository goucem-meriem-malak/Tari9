package com.example.tari9;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class help_center extends AppCompatActivity {
    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    private Button btngoback, btnsettings;
    private LinearLayout btnchat, btnhelpline, btnfaq, btnmessenger, btninstagram, btnwhatsapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_center);

        btngoback = findViewById(R.id.goback);
        btnchat = findViewById(R.id.chat);
        btnhelpline = findViewById(R.id.helpline);
        btnmessenger = findViewById(R.id.messenger);
        btninstagram = findViewById(R.id.instagram);
        btnwhatsapp = findViewById(R.id.whatsapp);
        btnsettings = findViewById(R.id.settings);

        btngoback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        btnchat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String phoneNumber = "+213672671666";
                String message = "Hello, this is a message!";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + phoneNumber));
                intent.putExtra("sms_body", message);

                startActivity(intent);
            }
        });
        btnhelpline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCallButtonClicked("+213672671666");
            }
        });
        btnmessenger.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/profile.php?id=100038426890290"));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse("https://www.facebook.com/profile.php?id=100038426890290&mibextid=ZbWKwL"));
                    startActivity(intent);
                }

            }
        });
        btninstagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://instagram.com/merie_m_m_alak?igshid=ZGUzMzM3NWJiOQ=="));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse("https://instagram.com/merie_m_m_alak?igshid=ZGUzMzM3NWJiOQ=="));
                    startActivity(intent);
                }
            }
        });
        btnwhatsapp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/qr/OWRY7B5JSAR3G1"));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse("https://wa.me/qr/OWRY7B5JSAR3G1"));
                    startActivity(intent);
                }
            }
        });
        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(help_center.this, R.anim.button_vibrate);
                btnsettings.startAnimation(shake);
                PopupMenu popupMenu = new PopupMenu(help_center.this, btnsettings);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_help_center, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_language: {
                                String languageCode = "fr";
                                Locale locale = new Locale(languageCode);
                                Configuration config = new Configuration(getResources().getConfiguration());
                                config.setLocale(locale);
                                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                                return true;
                            }
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }
    private boolean isCallPhonePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestCallPhonePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
    }
    private void makePhoneCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
        startActivityForResult(callIntent, 1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CALL_PHONE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall("+213672671666");
                } else {
                    Toast.makeText(this, "Permission denied to make a phone call", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onCallButtonClicked(String Phone) {
        if (isCallPhonePermissionGranted()) {
            makePhoneCall(Phone);
        } else {
            requestCallPhonePermission();
        }
    }
}