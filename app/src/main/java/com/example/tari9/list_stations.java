package com.example.tari9;


import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.client;
import com.example.tari9.data.station;
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
import java.util.Map;

public class list_stations extends AppCompatActivity implements listener_station {

    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String clientid, requestid, service;
    private GeoPoint client_location, station_location;
    private Map<String, Object> client_address;
    private station stat;
    private RecyclerView recyclerView;
    private ArrayList<com.example.tari9.data.station> station;
    private adapter_station adapter_stations;
    private Button btnhome, btnlist, btnprofile, btngoback, btnhelpcenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_stations);

        btnhome = findViewById(R.id.home);
        btnlist = findViewById(R.id.list_requests);
        btnprofile = findViewById(R.id.profile);
        btngoback = findViewById(R.id.go_back);
        btnhelpcenter = findViewById(R.id.help_center);
        recyclerView = findViewById(R.id.recycler_station);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        clientid = user.getUid();

        requestid = getIntent().getStringExtra("request_station_id");

        station = new ArrayList<station>();
        adapter_stations = new adapter_station(this, station, new listener_station() {
            @Override
            public void onItemClicked(String doc_id, station station, int position) {
                sendrequest(doc_id);
                    Intent intent = new Intent(list_stations.this, request.class);
                    intent.putExtra("request_station_id", requestid);
                    list_stations.this.startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter_stations);

        get_list_stations();

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_stations.this, menu.class);
                list_stations.this.startActivity(activityChangeIntent);
            }
        });
        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_stations.this, list_requests.class);
                list_stations.this.startActivity(activityChangeIntent);
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_stations.this, profile.class);
                list_stations.this.startActivity(activityChangeIntent);
            }
        });
        btngoback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.collection("request").document(requestid).delete();
                finish();
            }
        });
        btnhelpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_stations.this, help_center.class);
                list_stations.this.startActivity(activityChangeIntent);
            }
        });
    }

    private void get_list_stations() {
        db.collection("client").document(clientid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                client client = documentSnapshot.toObject(client.class);
                //.whereEqualTo("address", client.getLocation_address())
                db.collection("station")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    return;
                                }
                                for (DocumentChange dc : value.getDocumentChanges()) {
                                    if (dc.getType() == DocumentChange.Type.ADDED) {
                                        station.add(dc.getDocument().toObject(station.class));
                                    }
                                    adapter_stations.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
    }

    private void sendrequest(String station_id) {
        db.collection("client").document(clientid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                client client = documentSnapshot.toObject(client.class);
                db.collection("station").whereEqualTo("id", station_id)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    return;
                                }
                                for (DocumentChange dc : value.getDocumentChanges() ) {
                                    if (dc.getType() == DocumentChange.Type.ADDED) {
                                        stat = dc.getDocument().toObject(station.class);
                                        stat.setDistance(get_distance(client.getLocation(), stat.getLocation()));
                                        stat.setDunit(get_unit(client.getLocation(), stat.getLocation()));

                                        HashMap<String, Object> m = new HashMap<String, Object>();

                                        DocumentReference ref = db.collection("request").document(requestid);
                                        m.put("worker_id", stat.getId());
                                        m.put("worker_location", stat.getLocation());
                                        m.put("worker_phone", stat.getPhone());
                                        m.put("distance", stat.getDistance());
                                        m.put("dunit", stat.getDunit());
                                        m.put("state", "waiting");
                                        ref.update(m);
                                    }

                                    adapter_stations.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
    }

    private float get_distance(GeoPoint client_location, GeoPoint station_location) {

        Location client_loc = new Location("");
        client_loc.setLatitude(client_location.getLatitude() / 1E6);
        client_loc.setLongitude(client_location.getLongitude() / 1E6);
        Location station_loc = new Location("");
        station_loc.setLatitude(station_location.getLatitude() / 1E6);
        station_loc.setLongitude(station_location.getLongitude() / 1E6);
        DecimalFormat df = new DecimalFormat("#.##");
        float distanceInMeters = Float.parseFloat(df.format(client_loc.distanceTo(station_loc)));
        float distanceInKm = distanceInMeters / 1000;

        if (distanceInMeters>1000){
            return distanceInKm;
        }
        else return distanceInMeters;
    }
    private String get_unit(GeoPoint client_location, GeoPoint mechanic_location) {

        Location client_loc = new Location("");
        client_loc.setLatitude(client_location.getLatitude() / 1E6);
        client_loc.setLongitude(client_location.getLongitude() / 1E6);
        Location mechanic_loc = new Location("");
        mechanic_loc.setLatitude(mechanic_location.getLatitude() / 1E6);
        mechanic_loc.setLongitude(mechanic_location.getLongitude() / 1E6);
        DecimalFormat df = new DecimalFormat("#.##");
        float distanceInMeters = Float.parseFloat(df.format(client_loc.distanceTo(mechanic_loc)));

        if (distanceInMeters>1000){
            return "Km";
        }
        else return "M";
    }
    @Override
    public void onItemClicked(String doc_id, station station, int position) {
        Intent activityChangeIntent = new Intent(list_stations.this, menu.class);
        list_stations.this.startActivity(activityChangeIntent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (requestid!=null){
            db.collection("request").document(requestid).delete();
            finish();
        } else {
            finish();
        }
    }
}
