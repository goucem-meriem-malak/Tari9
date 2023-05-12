package com.example.tari9;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tari9.data.get_requests;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class request extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private TextView type, service, nbr_passenger, date, address, vehicle, price;
    private LinearLayout ll_type, ll_service, ll_nbr_passenger, ll_date, ll_address, ll_vehicle, ll_price;
    private get_requests request, request1, request2;
    private int passenger_nbr;
    private String client_id, request_id, request_taxi_id, request_ambulance_id, worker_id, worker_phone;
    private Button btnconfirm, btncancel, btnmenu, btnlistrequests, btnprofile, btngoback, btnhelpcenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);

        ll_service = findViewById(R.id.ll_service);
        ll_type = findViewById(R.id.ll_type);
        ll_nbr_passenger = findViewById(R.id.ll_nbr_passenger);
        ll_nbr_passenger.setVisibility(View.GONE);
        ll_date = findViewById(R.id.ll_date);
        ll_address = findViewById(R.id.ll_address);
        ll_vehicle = findViewById(R.id.ll_vehicle);
        ll_price = findViewById(R.id.ll_price);
        service = findViewById(R.id.service);
        type = findViewById(R.id.type);
        nbr_passenger = findViewById(R.id.nbr_passenger);
        date = findViewById(R.id.date);
        address = findViewById(R.id.address);
        vehicle = findViewById(R.id.vehicle);
        price = findViewById(R.id.price);
        btnmenu = findViewById(R.id.menu);
        btnlistrequests = findViewById(R.id.list_requests);
        btnprofile = findViewById(R.id.profile);
        btngoback = findViewById(R.id.go_back);
        btnhelpcenter = findViewById(R.id.help_center);

        btnconfirm = findViewById(R.id.confirm);
        btncancel = findViewById(R.id.cancel);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        client_id = user.getUid();

        if (getIntent().getStringExtra("request_mechanic_id")!=null){
            request_id = getIntent().getStringExtra("request_mechanic_id");
        }
        if (getIntent().getStringExtra("request_tow_id")!=null){
            request_id = getIntent().getStringExtra("request_tow_id");
            if (getIntent().getStringExtra("request_taxi_id")!=null){
                get_taxi_request(getIntent().getStringExtra("request_taxi_id"));
            }
            if (getIntent().getStringExtra("request_ambulance")!=null){
                get_ambulance_request(getIntent().getStringExtra("request_ambulance_id"));
            }
        }
        if (getIntent().getStringExtra("request_station_id")!=null){
            request_id = getIntent().getStringExtra("request_station_id");
        }
        if (getIntent().getStringExtra("request_team_id")!=null){
            request_id = getIntent().getStringExtra("request_team_id");
        }
        get_request_info(request_id);

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(request.this);
                builder.setTitle("Phone Number");
                builder.setMessage(R.string.confirm_request);
                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onCallButtonClicked(worker_phone);
                    }
                });
                builder.setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Phone Number", worker_phone);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(), "Number copied", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(request.this, list_requests.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(request.this);
                View customLayout = getLayoutInflater().inflate(R.layout.alert_cancel_request, null);
                builder.setView(customLayout)
                        .setCancelable(true).
                        setIcon(R.drawable.cancel).
                        setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.collection("request").document(request_id).delete();
                                Intent activityChangeIntent = new Intent(request.this, menu.class);
                                startActivity(activityChangeIntent);
                                Toast.makeText(request.this, "Your Request Has Been Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        btnmenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btnlistrequests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(request.this, list_requests.class);
                request.this.startActivity(activityChangeIntent);
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(request.this, profile.class);
                request.this.startActivity(activityChangeIntent);
            }
        });
        btnhelpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(request.this, help_center.class);
                request.this.startActivity(activityChangeIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Intent intent = new Intent(this, list_requests.class);
            startActivity(intent);
        }
    }
    private void get_request_info(String request_id) {
        db.collection("request").document(request_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        request = document.toObject(get_requests.class);
                        if(request.getType()==null){
                            ll_type.setVisibility(View.GONE);
                        } else {
                            type.setText(request.getType());
                            if (request.getType().equals("tow")) {
                                if (getIntent().getStringExtra("request_taxi_id") != null && getIntent().getStringExtra("request_ambulance_id") != null) {
                                    service.setText("Taxi && Ambulance");
                                } else if (getIntent().getStringExtra("request_taxi_id") == null && getIntent().getStringExtra("request_ambulance_id") != null) {
                                    service.setText("Ambulance");
                                } else if (getIntent().getStringExtra("request_taxi_id") != null && getIntent().getStringExtra("request_ambulance_id") == null) {
                                    service.setText("Taxi");
                                } else {
                                    ll_service.setVisibility(View.GONE);
                                }
                            } else if (request.getType().equals("station")){
                                if (request.isFuel() && request.isOil()){
                                    ll_service.setVisibility(View.VISIBLE);
                                    service.setText("Fuel: " + request.getFuel_type() + " " + request.getFuel_quantity() + " " + request.getFuel_unit()
                                            +" & Oil: " + request.getOil_type() + " " + request.getOil_quantity() + " " + request.getOil_unit());
                                } else if (request.isFuel() && !request.isOil()){
                                    service.setText("Fuel: " + request.getFuel_type() + " " + request.getFuel_quantity() + " " + request.getFuel_unit());
                                } else if (!request.isFuel() && request.isOil()){
                                    service.setText("Oil: " + request.getOil_type() + " " + request.getOil_quantity() + " " + request.getOil_unit());
                                } else {
                                    ll_service.setVisibility(View.GONE);
                                }
                            }  else if (request.getType().equals("team")) {
                                service.setText(request.getService());
                            } else {
                                ll_service.setVisibility(View.GONE);
                            }
                        }
                        if (request.getDate() == null){
                            ll_date.setVisibility(View.GONE);
                        } else {
                            Date datee = null;
                            try {
                                datee = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(String.valueOf(request.getDate().toDate()));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            String formattedDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US).format(datee);
                            date.setText(formattedDate);
                        }
                        if (request.getAddress()==null){
                            ll_address.setVisibility(View.GONE);
                        } else {
                            if (request.getAddress().get("state")==null){
                                address.setText(request.getAddress().get("country").toString() + " "
                                        + request.getAddress().get("city").toString());
                            }
                        }
                        if (request.getVehicle()==null){
                            ll_vehicle.setVisibility(View.GONE);
                        } else {
                            vehicle.setText(request.getVehicle().getType() + " " + request.getVehicle().getMark());
                        }
                        if(request.getPrice()==0){
                            ll_price.setVisibility(View.GONE);
                        } else {
                            price.setText(String.valueOf(request.getPrice()) + "DA");
                        }
                        if (request.getWorker_phone()!=null){
                            worker_id = request.getWorker_phone().trim();
                        }
                        if (request.getVehicle()!=null){
                            vehicle.setText(request.getVehicle().getType()+" "+request.getVehicle().getMark());
                        }
                        worker_phone = request.getWorker_phone();
                    } else {
                        Toast.makeText(request.this, "Something Went Wrong With The Request, Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(request.this, "Something Went Wrong With The Request, Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void get_taxi_request(String request_idd){
        db.collection("request").document(request_idd).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        request1 = document.toObject(get_requests.class);
                        nbr_passenger.setVisibility(View.VISIBLE);
                        ll_nbr_passenger.setVisibility(View.VISIBLE);
                        if (String.valueOf(request1.getPassenger_number())!=null){
                            nbr_passenger.setText(String.valueOf(request1.getPassenger_number()));
                        }
                    }
                }
            }
        });
    }
    private void get_ambulance_request(String request_id){
        db.collection("request").document(request_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        request2 = document.toObject(get_requests.class);
                    }
                }
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
                    makePhoneCall(worker_phone);
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
