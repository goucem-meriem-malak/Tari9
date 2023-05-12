package com.example.tari9;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tari9.data.client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


public class edit_profile extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String imagePath;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private EditText firstname, lastname, email, country, city, state, phone, fphone;
    private Button btnhome, btnlistrequests, btnprofile, btneditprofile, btngoback, brnsettings;
    private ImageView pfp;
    private TextView username;
    private String userid;
    private Uri imagepath;
    private Uri photoUri;
    private StorageReference profilesRef;
    private Intent profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        btngoback = findViewById(R.id.goback);
        btnhome = findViewById(R.id.home);
        btnprofile = findViewById(R.id.profile);
        btnlistrequests = findViewById(R.id.list_requests);
        btneditprofile = findViewById(R.id.edit);
        brnsettings = findViewById(R.id.settings);
        pfp = findViewById(R.id.pfp);

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        username = findViewById(R.id.username);
        fphone = findViewById(R.id.fphone);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        userid = user.getUid();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        profilesRef = storageRef.child("image/client/");
        db.collection("client").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if (task.isSuccessful()) {
                     DocumentSnapshot document = task.getResult();
                     if (document.exists()) {
                         client client = document.toObject(client.class);
                         if (client.getFirstname() == null) {
                             firstname.setText("");
                         } else {
                             firstname.setText(client.getFirstname());
                         }
                         if (client.getLastname() == null) {
                             lastname.setText("");
                         } else {
                             lastname.setText(client.getLastname());
                         }
                         if (client.getPhone() == null) {
                             fphone.setText("");
                             phone.setText("");
                         } else {
                             fphone.setText(client.getPhone().substring(0, client.getPhone().length() - 9));
                             phone.setText(client.getPhone().substring(client.getPhone().length() - 9, client.getPhone().length()));
                         }
                         if (client.getEmail() == null) {
                             email.setText("");
                         } else {
                             email.setText(client.getEmail());
                         }
                         if (client.getAddress()==null){
                             country.setText("");
                             city.setText("");
                         } else {
                             String[] words = client.getAddress().toString().split(" ");

                             country.setText(words[0]);
                             city.setText(words[1]);
                         }
                     }
                 } else {
                     Log.d(TAG, "Failed to get document.", task.getException());
                 }
             }
         });
        getclientinfo();
        btnhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(edit_profile.this, R.anim.button_vibrate);
                btnhome.startAnimation(shake);
                Intent activityChangeIntent = new Intent(edit_profile.this, menu.class);
                edit_profile.this.startActivity(activityChangeIntent);
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(edit_profile.this, R.anim.button_vibrate);
                btnprofile.startAnimation(shake);
                Intent activityChangeIntent = new Intent(edit_profile.this, profile.class);
                edit_profile.this.startActivity(activityChangeIntent);
                finish();
            }
        });
        brnsettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(edit_profile.this, R.anim.button_vibrate);
                btneditprofile.startAnimation(shake);
                PopupMenu popupMenu = new PopupMenu(edit_profile.this, brnsettings);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
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
                            case R.id.menu_logout: {
                                auth.signOut();
                                Intent activityChangeIntent = new Intent(edit_profile.this, launch_screen.class);
                                edit_profile.this.startActivity(activityChangeIntent);
                                finish();
                                return true;
                            }
                            case R.id.menu_help_center: {
                                Intent activityChangeIntent = new Intent(edit_profile.this, help_center.class);
                                edit_profile.this.startActivity(activityChangeIntent);
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
        btneditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(edit_profile.this, R.anim.button_vibrate);
                btneditprofile.startAnimation(shake);
                if (btneditprofile.getText().equals("Edit")) {
                    firstname.setEnabled(true);
                    lastname.setEnabled(true);
                    country.setEnabled(true);
                    city.setEnabled(true);
                    //    state.setEnabled(true);
                    email.setEnabled(true);
                    btneditprofile.setText("Save");
                    btneditprofile.setBackgroundColor(Color.BLUE);
                    btneditprofile.setTextColor(Color.WHITE);
                } else {
                    Map<String, Object> update = new HashMap<>();
                    Map<String, Object> addresss = new HashMap<>();
                    if (!firstname.getText().toString().isEmpty()) {
                        update.put("firstname", firstname.getText().toString().trim());
                    }
                    if (!lastname.getText().toString().isEmpty()) {
                        update.put("lastname", lastname.getText().toString().trim());
                    }
                    if (!email.getText().toString().isEmpty()) {
                        Pattern rfc2822 = Pattern.compile(
                                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
                        if (!rfc2822.matcher(email.getText().toString()).matches() && !email.getText().toString().isEmpty()) {
                            Toast.makeText(edit_profile.this, "Email invalide", Toast.LENGTH_LONG).show();
                            email.setFocusable(true);
                        } else {
                            update.put("email", email.getText().toString().trim());
                        }
                    }
                    if (!country.getText().toString().isEmpty()) {
                        addresss.put("country", country.getText().toString().trim());
                    }
                    if (!city.getText().toString().isEmpty()) {
                        addresss.put("city", city.getText().toString().trim());
                    }
               /*     if (state.getText() != null){
                        addresss.put("state", state.getText().toString().trim());
                    }*/
                    if (!addresss.isEmpty()) {
                        update.put("address", addresss);
                    }
                    db.collection("client").document(userid).update(update);
                    btneditprofile.setText("Edit");
                    firstname.setEnabled(false);
                    lastname.setEnabled(false);
                    country.setEnabled(false);
                    city.setEnabled(false);
                    //    state.setEnabled(false);
                    email.setEnabled(false);
                }
            }
        });
        btnlistrequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(edit_profile.this, R.anim.button_vibrate);
                btnlistrequests.startAnimation(shake);
                Intent activityChangeIntent = new Intent(edit_profile.this, list_requests.class);
                edit_profile.this.startActivity(activityChangeIntent);
            }
        });
        btngoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(edit_profile.this, R.anim.button_vibrate);
                btngoback.startAnimation(shake);
                finish();
            }
        });
        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btneditprofile.getText().equals("Save")){
                    pfp.setBackgroundColor(Color.TRANSPARENT);
                    Intent profilepic = new Intent(Intent.ACTION_GET_CONTENT);
                    profilepic.setType("image/*");
                    startActivityForResult(Intent.createChooser(profilepic, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data != null){
            imagepath = data.getData();
            try {
                imageinimageview();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected photo
            photoUri = data.getData();

            // Upload the selected photo to Firebase Storage
            StorageReference photoRef = profilesRef.child(userid + ".jpg");
            UploadTask uploadTask = photoRef.putFile(photoUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL of the uploaded photo
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Save the download URL to Firebase Database or use it to display the photo in your app
                            Log.d(TAG, "Photo uploaded: " + downloadUri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors that occur during the upload
                    Log.e(TAG, "Error uploading photo: " + exception.getMessage());
                }
            });
        }
    }

    private void imageinimageview() throws IOException {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pfp.setImageBitmap(bitmap);
    }
    private void getclientinfo(){
        Map address;
        db.collection("client").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        client client = document.toObject(client.class);
                        if (client != null) {
                            if (client.getLastname() != null) {
                                lastname.setText(client.getLastname());
                            }
                            if (client.getFirstname() != null) {
                                firstname.setText(client.getFirstname());
                            }
                            if (client.getLastname()!= null && client.getFirstname()!= null){
                                username.setText(client.getFirstname() + " " + client.getLastname());
                            }
                            if (client.getPhone() != null) {
                                fphone.setText(client.getPhone().substring(0, client.getPhone().length() - 9));
                                phone.setText(client.getPhone().substring(client.getPhone().length() - 9, client.getPhone().length()));
                            }
                            if (client.getEmail() != null) {
                                email.setText(client.getEmail());
                            }
                            if (client.getAddress() != null) {
                                Map<String, Object> address;
                                address = client.getAddress();
                                if (address.get("country") != null) {
                                    country.setText(address.get("country").toString());
                                }
                                if (address.get("city") != null) {
                                    city.setText(address.get("city").toString());
                                }
                                if (address.get("state") != null) {
                                    //    state.setText(address.getState());
                                }
                            }
                        }
                    } else {
                        Toast.makeText(edit_profile.this, "Please Try Later", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(edit_profile.this, "Check Your Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

// Create a reference to the image file
        StorageReference imageRef = storageRef.child("image/client/"+userid+".jpg");

// Download the image and get a byte array of its data
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Convert the byte array to a Bitmap object and set it to an ImageView
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                pfp.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

}
