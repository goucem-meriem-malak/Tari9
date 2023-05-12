package com.example.tari9;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tari9.data.veh;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class form_vehicule extends AppCompatActivity {//implements AdapterView.OnItemSelectedListener

        private FirebaseFirestore db;
        private FirebaseUser user;
        private FirebaseAuth auth;
        private String clientid;
        private ArrayList<String> type, mark, model;
        private ArrayAdapter typee, markk, modell;
        private com.example.tari9.data.vehicule vehicule;
        private Button btnhome, btnlist, btnprofile, btnnext;
        private Spinner types, marks, models;
        private veh veh = new veh();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.form_vehicule);
/*
            btnhome = findViewById(R.id.home);
            btnlist = findViewById(R.id.list);
            btnprofile = findViewById(R.id.profile);
            btnnext = findViewById(R.id.next);

            types = findViewById(R.id.type);
            types.setOnItemSelectedListener(this);
            marks = findViewById(R.id.mark);
            marks.setOnItemSelectedListener(this);
            models = findViewById(R.id.model);
            models.setOnItemSelectedListener(this);

            db = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            clientid = user.getUid();

            typee = new ArrayAdapter<String>(form_vehicule.this, android.R.layout.simple_spinner_item, veh.getType());
            typee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            types.setAdapter(typee);

            markk = new ArrayAdapter<String>(form_vehicule.this, android.R.layout.simple_spinner_item, veh.getPassenger_vehicule());
            markk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            marks.setAdapter(markk);

            btnhome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent activityChangeIntent = new Intent(form_vehicule.this, home.class);
                    form_vehicule.this.startActivity(activityChangeIntent);
                }
            });
            btnlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent activityChangeIntent = new Intent(form_vehicule.this, list_requests.class);
                    form_vehicule.this.startActivity(activityChangeIntent);
                }
            });
            btnprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent activityChangeIntent = new Intent(form_vehicule.this, profile.class);
                    form_vehicule.this.startActivity(activityChangeIntent);
                }
            });
            btnnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent activityChangeIntent = new Intent(form_vehicule.this, list_mechanics.class);
                    form_vehicule.this.startActivity(activityChangeIntent);
                }
            });
        }

    private void get_list_types() {
  /*      db.collection("vehicule").whereEqualTo("id","vehicule").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                String name = null;

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        name = dc.getDocument().get("category").toString();
                        Toast.makeText(form_vehicule.this, type.toString(), Toast.LENGTH_LONG).show();
                        type.add(name);
                    }
                }
            }
        });*/
    /*    db.collection("vehicule").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("category");
                        type.add(subject);
                    }
                    typee.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}}
