package com.example.tari9;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.client;
import com.example.tari9.data.garage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class list_garage extends AppCompatActivity implements listener_garage {

    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String clientid, requestid;
    private GeoPoint client_location, worker_id;
    private Map<String, Object> client_address;
    private garage grg;
    private RecyclerView recyclerView;
    private ArrayList<garage> garage;
    private adapter_garage adapter_garage;
    private Button btnhome, btnlist, btnprofile, btngoback, btnhelpcenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_garages);

        btnhome = findViewById(R.id.home);
        btnlist = findViewById(R.id.list_requests);
        btnprofile = findViewById(R.id.profile);
        btngoback = findViewById(R.id.go_back);
        btnhelpcenter = findViewById(R.id.help_center);
        recyclerView = findViewById(R.id.recycler_garage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        clientid = user.getUid();
        if (getIntent().getStringExtra("request_team_id")!=null){
            requestid = getIntent().getStringExtra("request_team_id");
        }
        garage = new ArrayList<garage>();
        adapter_garage = new adapter_garage(this, garage, new listener_garage() {
            @Override
            public void onItemClicked(String doc_id, garage garage, int position) {
                sendrequest(client_location, doc_id);
                Intent activityChangeIntent = new Intent(list_garage.this, request.class);
                activityChangeIntent.putExtra("request_team_id", requestid);
                list_garage.this.startActivity(activityChangeIntent);
            }

        });
        recyclerView.setAdapter(adapter_garage);

        get_list_available_garages();

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_garage.this, menu.class);
                list_garage.this.startActivity(activityChangeIntent);
            }
        });
        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_garage.this, list_requests.class);
                list_garage.this.startActivity(activityChangeIntent);
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_garage.this, profile.class);
                list_garage.this.startActivity(activityChangeIntent);
            }
        });
        btngoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnhelpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_garage.this, help_center.class);
                list_garage.this.startActivity(activityChangeIntent);
            }
        });
    }

    private void get_list_available_garages() {
        //.whereEqualTo("address", client.getLocation_address())
        db.collection("client").document(clientid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshott) {
                client client = documentSnapshott.toObject(client.class);
                db.collection("garage")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    return;
                                }
                                for (DocumentChange dc : value.getDocumentChanges()) {
                                    if (dc.getType() == DocumentChange.Type.ADDED) {
                                        grg = dc.getDocument().toObject(garage.class);
                                        grg.setDistance(get_distance(client.getLocation(), grg.getLocation()));
                                        grg.setDunit(get_unit(client.getLocation(), grg.getLocation()));
                                        garage.add(grg);
                                    }

                                    adapter_garage.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
    }

    private void sendrequest(GeoPoint client_location, String garage_id) {
        db.collection("client").document(clientid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshott) {
                client client = documentSnapshott.toObject(client.class);
                db.collection("garage").whereEqualTo("id", garage_id)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    return;
                                }
                                for (DocumentChange dc : value.getDocumentChanges() ) {
                                    if (dc.getType() == DocumentChange.Type.ADDED) {
                                        grg = dc.getDocument().toObject(garage.class);
                                        grg.setDistance(get_distance(client.getLocation(), grg.getLocation()));
                                        grg.setDunit(get_unit(client.getLocation(), grg.getLocation()));

                                        HashMap<String, Object> m = new HashMap<String, Object>();

                                        m.put("worker_id", grg.getId());
                                        m.put("worker_location", grg.getLocation());
                                        m.put("worker_phone", grg.getPhone());
                                        m.put("type", "team");
                                        m.put("distance", grg.getDistance());
                                        m.put("dunit", grg.getDunit());
                                        if (grg.getDunit()=="M"){
                                            m.put("price", (grg.getDistance()*2)+350);
                                        } else {
                                            m.put("price", (grg.getDistance()*2)+500);
                                        }
                                        m.put("state", "waiting");
                                        db.collection("request").document(requestid).update(m);
                                    }

                                    adapter_garage.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
    }

    private float get_distance(GeoPoint client_location, GeoPoint garage_location) {

        Location client_loc = new Location("");
        client_loc.setLatitude(client_location.getLatitude() / 1E6);
        client_loc.setLongitude(client_location.getLongitude() / 1E6);
        Location garage_loc = new Location("");
        garage_loc.setLatitude(garage_location.getLatitude() / 1E6);
        garage_loc.setLongitude(garage_location.getLongitude() / 1E6);
        DecimalFormat df = new DecimalFormat("#.##");
        float distanceInMeters = Float.parseFloat(String.valueOf(df.format(client_loc.distanceTo(garage_loc))));
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
        if (distanceInMeters>1000) {
            return "Km";
        } else return "M";
    }
    @Override
    public void onItemClicked(String doc_id, garage garage, int position) {
        Intent activityChangeIntent = new Intent(list_garage.this, menu.class);
        list_garage.this.startActivity(activityChangeIntent);
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