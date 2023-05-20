package com.example.tari9;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.ambulance;
import com.example.tari9.data.client;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class list_ambulance extends AppCompatActivity implements listener_ambulance {

    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String clientid, requestid;
    private ambulance amb;
    private RecyclerView recyclerView;
    private ArrayList<ambulance> ambulance;
    private adapter_ambulance adapter_ambulance;
    private Button btnhome, btnlist, btnprofile, btngoback, btnhelpcenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ambulances);

        btnhome = findViewById(R.id.home);
        btnlist = findViewById(R.id.list_requests);
        btnprofile = findViewById(R.id.profile);
        btngoback = findViewById(R.id.go_back);
        btnhelpcenter = findViewById(R.id.help_center);
        recyclerView = findViewById(R.id.recycler_ambulance);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        clientid = user.getUid();
        if (getIntent().getStringExtra("request_ambulance_id")!=null){
            requestid = getIntent().getStringExtra("request_ambulance_id");
        }

        ambulance = new ArrayList<ambulance>();
        adapter_ambulance = new adapter_ambulance(this, ambulance, new listener_ambulance() {
            @Override
            public void onItemClicked(String doc_id, ambulance ambulance, int position) {
                sendrequest(doc_id);
                Intent activityChangeIntent = new Intent(list_ambulance.this, request.class);
                activityChangeIntent.putExtra("request_tow_id", getIntent().getStringExtra("request_tow_id"));
                if (getIntent().getStringExtra("request_taxi_id")!=null){
                    activityChangeIntent.putExtra("request_taxi_id", getIntent().getStringExtra("request_taxi_id"));
                }
                activityChangeIntent.putExtra("request_ambulance_id", getIntent().getStringExtra("request_ambulance_id"));
                list_ambulance.this.startActivity(activityChangeIntent);
            }
        });
        recyclerView.setAdapter(adapter_ambulance);

        get_list_available_garages();

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_ambulance.this, menu.class);
                list_ambulance.this.startActivity(activityChangeIntent);
            }
        });
        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_ambulance.this, list_requests.class);
                list_ambulance.this.startActivity(activityChangeIntent);
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_ambulance.this, profile.class);
                list_ambulance.this.startActivity(activityChangeIntent);
            }
        });
        btngoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestid!=null){
                    db.collection("request").document(requestid).delete();
                    if (getIntent().getStringExtra("request_taxi_id")!=null){
                        db.collection("request").document(getIntent().getStringExtra("request_taxi_id")).delete();
                    }
                    if (getIntent().getStringExtra("request_tow_id")!=null){
                        db.collection("request").document(getIntent().getStringExtra("request_tow_id")).delete();
                    }
                    Intent intent = new Intent(getApplicationContext(), menu.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), menu.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        btnhelpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_ambulance.this, help_center.class);
                list_ambulance.this.startActivity(activityChangeIntent);
            }
        });
    }

    private void get_list_available_garages() {
        db.collection("client").document(clientid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshott) {
                client client = documentSnapshott.toObject(client.class);

                db.collection("ambulance").whereEqualTo("address", client.getLocation_address())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    return;
                                }
                                for (DocumentChange dc : value.getDocumentChanges()) {
                                    if (dc.getType() == DocumentChange.Type.ADDED) {
                                        amb = dc.getDocument().toObject(ambulance.class);
                                        amb.setDistance(get_distance(client.getLocation(), amb.getLocation()));
                                        amb.setDunit(get_unit(client.getLocation(), amb.getLocation()));
                                        ambulance.add(amb);
                                    }

                                    adapter_ambulance.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
    }

    private void sendrequest(String garage_id) {
        db.collection("client").document(clientid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshott) {
                client client = documentSnapshott.toObject(client.class);
                db.collection("garage").whereEqualTo("id", garage_id)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    return;
                                }
                                for (DocumentChange dc : value.getDocumentChanges() ) {
                                    if (dc.getType() == DocumentChange.Type.ADDED) {
                                        amb = dc.getDocument().toObject(ambulance.class);
                                        amb.setDistance(get_distance(client.getLocation(), amb.getLocation()));
                                        amb.setDunit(get_unit(client.getLocation(), amb.getLocation()));

                                        HashMap<String, Object> m = new HashMap<String, Object>();

                                        DocumentReference ref = db.collection("request").document();
                                        m.put("worker_id", amb.getId());
                                        m.put("worker_location", amb.getLocation());
                                        m.put("worker_phone", amb.getPhone());
                                        m.put("distance", amb.getDistance());
                                        m.put("dunit", amb.getDunit());
                                        if (amb.getDunit()=="M"){
                                            m.put("price", (amb.getDistance() * 2 *20));
                                        } else {
                                            m.put("price", amb.getDistance() * 2 *20);
                                        }
                                        m.put("type", "ambulance");
                                        m.put("state", "waiting");
                                        ref.set(m);
                                    }

                                    adapter_ambulance.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private float get_distance(GeoPoint client_location, GeoPoint ambulance_location) {

        Location client_loc = new Location("");
        client_loc.setLatitude(client_location.getLatitude() / 1E6);
        client_loc.setLongitude(client_location.getLongitude() / 1E6);
        Location ambulance_loc = new Location("");
        ambulance_loc.setLatitude(ambulance_location.getLatitude() / 1E6);
        ambulance_loc.setLongitude(ambulance_location.getLongitude() / 1E6);
        DecimalFormat df = new DecimalFormat("#.##");
        float distanceInMeters = Float.parseFloat(df.format(client_loc.distanceTo(ambulance_loc)));
        float distanceInKm = distanceInMeters / 1000;

        if (distanceInMeters>1000){
            return distanceInKm;
        }
        else return distanceInMeters;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String get_unit(GeoPoint client_location, GeoPoint ambulance_location) {

        Location client_loc = new Location("");
        client_loc.setLatitude(client_location.getLatitude() / 1E6);
        client_loc.setLongitude(client_location.getLongitude() / 1E6);
        Location ambulance_loc = new Location("");
        ambulance_loc.setLatitude(ambulance_location.getLatitude() / 1E6);
        ambulance_loc.setLongitude(ambulance_location.getLongitude() / 1E6);
        DecimalFormat df = new DecimalFormat("#.##");
        float distanceInMeters = Float.parseFloat(df.format(client_loc.distanceTo(ambulance_loc)));

        if (distanceInMeters>1000){
            return "Km";
        }
        else return "M";
    }
    @Override
    public void onItemClicked(String doc_id, ambulance ambulance, int position) {
        Intent activityChangeIntent = new Intent(list_ambulance.this, list_requests.class);
        list_ambulance.this.startActivity(activityChangeIntent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (requestid!=null){
            db.collection("request").document(requestid).delete();
            if (getIntent().getStringExtra("request_taxi_id")!=null){
                db.collection("request").document(getIntent().getStringExtra("request_taxi_id")).delete();
            }
            if (getIntent().getStringExtra("request_tow_id")!=null){
                db.collection("request").document(getIntent().getStringExtra("request_tow_id")).delete();
            }
            Intent intent = new Intent(getApplicationContext(), menu.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), menu.class);
            startActivity(intent);
            finish();
        }
    }
}