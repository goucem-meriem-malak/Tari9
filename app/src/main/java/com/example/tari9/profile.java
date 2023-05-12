package com.example.tari9;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;


public class profile extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private TextView fullname;
    private Button btnhome, btnlistrequests, btnprofile, btngoback, btneditprofile, btnseevehicles, btnlogout, btndelete, btnlanguage;
    private ImageView pfp;
    private String userid, requestid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        requestid = getIntent().getStringExtra("requestid");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        userid = user.getUid();
        StorageReference imageRef = storageRef.child("image/client/"+userid+".jpg");
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                pfp.setBackgroundColor(Color.TRANSPARENT);
                pfp.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(profile.this, "Error, try later", Toast.LENGTH_SHORT).show();
            }
        });

        btngoback = findViewById(R.id.goback);
        btnhome = findViewById(R.id.home);
        btnprofile = findViewById(R.id.profile);
        btnlistrequests = findViewById(R.id.list_requests);
        btneditprofile = findViewById(R.id.edit_profile);
        btnseevehicles = findViewById(R.id.see_vehicles);
        btndelete = findViewById(R.id.delete_account);
        btnlogout = findViewById(R.id.logout);
        btnlanguage = findViewById(R.id.language);
        pfp = findViewById(R.id.pfp);

        fullname =findViewById(R.id.fullname);

        db.collection("client").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fname = documentSnapshot.getString("firstname");
                    String lname = documentSnapshot.getString("lastname");
                    if (fname!=null && lname!=null){
                        fullname.setText(fname + " " + lname);
                    } else {
                        fullname.setText("Add Your Name");
                    }
                } else {
                    Intent intent = new Intent(profile.this, launch_screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        btnhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(profile.this, menu.class);
                profile.this.startActivity(activityChangeIntent);
            }
        });
        btnlistrequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(profile.this, list_requests.class);
                profile.this.startActivity(activityChangeIntent);
            }
        });
        btngoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btneditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(profile.this, edit_profile.class);
                profile.this.startActivity(activityChangeIntent);
            }
        });
        btnseevehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(profile.this, list_vehicle.class);
                profile.this.startActivity(activityChangeIntent);
            }
        });
        btnlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage();
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), launch_screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(profile.this, R.anim.button_vibrate);
                btndelete.startAnimation(shake);
                AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                builder.setMessage("Are You Sure You Want To Delete Your Account? All Your Information Will Be Deleted!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("client").document(userid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        db.collection("client").document(userid).delete();
                                        Intent intent = new Intent(getApplicationContext(), launch_screen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        Toast.makeText(profile.this, "Your Account Has Been Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(profile.this, "An Error Occurred While Deleting Your Account, Try Later", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(profile.this, R.anim.error_animation);
                pfp.startAnimation(shake);
                Toast.makeText(profile.this, "Click On Edit Profile", Toast.LENGTH_SHORT).show();
            }
            });
    }

    private void changeLanguage() {
            Locale currentLocale = getResources().getConfiguration().locale;

            Locale newLocale = currentLocale.equals(Locale.ENGLISH) ? new Locale("fr") : Locale.ENGLISH;

            Configuration configuration = new Configuration();
            configuration.setLocale(newLocale);

            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

            recreate();
    }
}
