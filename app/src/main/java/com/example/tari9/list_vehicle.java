package com.example.tari9;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.client;
import com.example.tari9.data.veh;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class list_vehicle extends AppCompatActivity implements listener_vehicle {

    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String clientid, vehicleid;
    private RecyclerView recyclerView;
    private veh vehicle;
    private ArrayList<veh> vehicles;
    private adapter_vehicle adapter_vehicle;
    private Button btnhome, btnlist, btnprofile, btngoback, btnhelpcenter, btn_next_vehicle;
    private int currentItemPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_vehicle);

        btnhome = findViewById(R.id.home);
        btnlist = findViewById(R.id.list_requests);
        btnprofile = findViewById(R.id.profile);
        btngoback = findViewById(R.id.goback);
        btnhelpcenter = findViewById(R.id.settings);
        btn_next_vehicle = findViewById(R.id.next_veh);
        recyclerView = findViewById(R.id.recycler_vehicle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        clientid = user.getUid();

        vehicles = new ArrayList<veh>();
        adapter_vehicle = new adapter_vehicle(this, vehicles, new listener_vehicle() {
            @Override
            public void onItemClicked(String doc_id, veh items, int position) {
                sendrequest(doc_id);
                Intent activityChangeIntent = new Intent(list_vehicle.this, profile.class);
                list_vehicle.this.startActivity(activityChangeIntent);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter_vehicle);

        get_list_vehicles();
        btn_next_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItemPosition++;

                recyclerView.scrollToPosition(currentItemPosition);
            }
        });

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_vehicle.this, menu.class);
                list_vehicle.this.startActivity(activityChangeIntent);
            }
        });
        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_vehicle.this, list_requests.class);
                list_vehicle.this.startActivity(activityChangeIntent);
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(list_vehicle.this, profile.class);
                list_vehicle.this.startActivity(activityChangeIntent);
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
                Intent activityChangeIntent = new Intent(list_vehicle.this, help_center.class);
                list_vehicle.this.startActivity(activityChangeIntent);
            }
        });
        btn_next_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void get_list_vehicles() {
        Query query = db.collection("client").document(clientid).collection("vehicle").orderBy("type", Query.Direction.ASCENDING);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<veh> vehicles = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                veh vehicle = document.toObject(veh.class);
                vehicles.add(vehicle);
            }
            adapter_vehicle.setVehicles(vehicles);
        });

    }

    private void sendrequest(String doc_id) {
        db.collection("client").document(clientid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshott) {
                client client = documentSnapshott.toObject(client.class);
                db.collection("vehicle").whereEqualTo("id", vehicleid)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Toast.makeText(list_vehicle.this, "There's Vehicle", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                for (DocumentChange dc : value.getDocumentChanges() ) {
                                    if (dc.getType() == DocumentChange.Type.ADDED) {

                                    }

                                    adapter_vehicle.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
    }
    @Override
    public void onItemClicked(String doc_id, veh items, int position) {
        Intent activityChangeIntent = new Intent(list_vehicle.this, menu.class);
        list_vehicle.this.startActivity(activityChangeIntent);
    }
}