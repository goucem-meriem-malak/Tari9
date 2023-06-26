package com.example.tari9;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class menu extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String mech_veh_typee, mech_veh_markk, tow_veh_typee, tow_veh_markk,
            oil_typee, fuel_typee, team_service_typee;
    private ArrayAdapter<String> mech_veh_types, mech_veh_marks, tow_veh_types, tow_veh_marks,
            oil_types, fuel_types, team_service_types;
    private List<String> team_service = new ArrayList<>(), fueltype = new ArrayList<>(), oiltype = new ArrayList<>(),
    mech_vehtype = new ArrayList<>(), mech_vechmark = new ArrayList<>(), tow_vehtype = new ArrayList<>(),
    tow_vehmark = new ArrayList<>();
    private Button btnmenu, btnlistrequests, btnprofile, btnhelpcenter,
            btnrequestmechanic, btnrequesttowtruck, btnrequeststation, btnrequestteam,
    btnnextmechanic, btnnexttow, btnnextstation, btnnextteam;
    private LinearLayout mechanic, mech_veh_form, towtruck, tow_veh_form, tow_taxi_form, station,
            station_oil_form, station_fuel_form, team, team_service_form, towcheckbox,
            stationcheckbox;
    private CheckBox fuel, oil, taxi, ambulance;
    private Spinner mech_veh_type, mech_veh_mark, tow_veh_type, tow_veh_mark,
            oil_type, fuel_type, team_service_type;
    private TextView qoil, qfuel, addoil, addfuel, daddfuel, daddoil;
    private TextView fprice, oprice;
    private TextView taxi_number_passenger, add, dadd;
    private EditText desc_mech, desc_tow, desc_station, desc_team;
    private String clientid, request_mechanic_id, request_tow_id,  request_taxi_id, request_ambulance_id,
            request_station_id, request_team_id;
    private String[] units = new String[]{"mL", "L"};
    private int fuelquantity, oilquantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        sendNotification();

        taxi_number_passenger=findViewById(R.id.taxi_number_passenger);
        add = findViewById(R.id.add);
        dadd = findViewById(R.id.dadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taxi_number_passenger.setText(String.valueOf(Integer.parseInt(taxi_number_passenger.getText().toString())+1));
            }
        });
        dadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(taxi_number_passenger.getText().toString())!=0){
                    taxi_number_passenger.setText(String.valueOf(Integer.parseInt(taxi_number_passenger.getText().toString())-1));
                }
            }
        });
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        clientid = user.getUid();

        btnprofile = findViewById(R.id.profile);
        btnmenu = findViewById(R.id.home);
        btnlistrequests = findViewById(R.id.list_requests);
        btnhelpcenter = findViewById(R.id.help_center);

        btnrequestmechanic = findViewById(R.id.request_mechanic);
        btnrequesttowtruck = findViewById(R.id.request_towtruck);
        btnrequeststation = findViewById(R.id.request_station);
        btnrequestteam = findViewById(R.id.request_team);

        btnnextmechanic = findViewById(R.id.next_mechanic);
        btnnexttow = findViewById(R.id.next_tow);
        btnnextstation = findViewById(R.id.next_station);
        btnnextteam = findViewById(R.id.next_team);

        mechanic = findViewById(R.id.mechanic);
        towtruck = findViewById(R.id.towtruck);
        station = findViewById(R.id.station);
        team = findViewById(R.id.team);

        desc_mech = findViewById(R.id.description_mechanic);
        desc_tow = findViewById(R.id.description_tow);
        desc_station = findViewById(R.id.description_station);
        desc_team = findViewById(R.id.description_team);

        mech_veh_form = findViewById(R.id.mechanic_veh_form);
        tow_veh_form = findViewById(R.id.tow_veh_form);
        tow_taxi_form = findViewById(R.id.tow_taxi_form);
        station_oil_form = findViewById(R.id.station_oil_form);
        station_fuel_form = findViewById(R.id.station_fuel_form);
        team_service_form = findViewById(R.id.team_service_form);
        towcheckbox = findViewById(R.id.tow_checkbox);
        stationcheckbox = findViewById(R.id.station_checkbox);

        desc_mech = findViewById(R.id.description_mechanic);
        desc_tow = findViewById(R.id.description_tow);
        desc_station = findViewById(R.id.description_station);
        desc_team = findViewById(R.id.description_team);

        fuel = findViewById(R.id.fuel);
        oil = findViewById(R.id.oil);
        taxi = findViewById(R.id.taxi);
        ambulance = findViewById(R.id.ambulance);

        oil_type = findViewById(R.id.oil_type);
        oil_type.setOnItemSelectedListener(this);
        fuel_type = findViewById(R.id.fuel_type);
        fuel_type.setOnItemSelectedListener(this);
        addoil = findViewById(R.id.addoil);
        daddoil = findViewById(R.id.daddoil);
        addfuel = findViewById(R.id.addfuel);
        daddfuel = findViewById(R.id.daddfuel);
        qoil = findViewById(R.id.qoil);
        qfuel = findViewById(R.id.qfuel);
        addfuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qfuel.setText(String.valueOf(Integer.parseInt(qfuel.getText().toString())+1));
                if (qfuel!=null){
                    fprice.setText(Integer.valueOf(qfuel.getText().toString())*45 + "DA");
                }
            }
        });
        daddfuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(qfuel.getText().toString())!=0){
                    qfuel.setText(String.valueOf(Integer.parseInt(qfuel.getText().toString())-1));
                    if (Integer.parseInt(qfuel.getText().toString())==0){
                        fuel.setChecked(false);
                    } else {
                        fprice.setText(Integer.valueOf(qfuel.getText().toString())*45 + "DA");
                    }
                }
            }
        });
        addoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qoil.setText(String.valueOf(Integer.parseInt(qoil.getText().toString())+1));
                if (qoil!=null){
                    oprice.setText(Integer.parseInt(String.valueOf(qoil.getText()))*420 + "DA");
                }
            }
        });
        daddoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(qoil.getText().toString())!=0){
                    qoil.setText(String.valueOf(Integer.parseInt(qoil.getText().toString())-1));
                    if (Integer.parseInt(qoil.getText().toString())==0){
                        oil.setChecked(false);
                    } else {
                        oprice.setText(Integer.parseInt(String.valueOf(qoil.getText()))*420 + "DA");
                    }
                }
            }
        });
        fprice = findViewById(R.id.fuel_price);
        oprice = findViewById(R.id.oil_price);
        mech_veh_type = findViewById(R.id.mech_veh_type);
        mech_veh_type.setOnItemSelectedListener(this);
        mech_veh_mark = findViewById(R.id.mech_veh_mark);
        mech_veh_mark.setOnItemSelectedListener(this);
        tow_veh_type = findViewById(R.id.tow_veh_type);
        tow_veh_type.setOnItemSelectedListener(this);
        tow_veh_mark = findViewById(R.id.tow_veh_mark);
        tow_veh_mark.setOnItemSelectedListener(this);
        oil_type = findViewById(R.id.oil_type);
        oil_type.setOnItemSelectedListener(this);
        fuel_type = findViewById(R.id.fuel_type);
        fuel_type.setOnItemSelectedListener(this);
        team_service_type = findViewById(R.id.team_veh_type);
        team_service_type.setOnItemSelectedListener(this);

        mech_veh_types = new ArrayAdapter<String>(menu.this, android.R.layout.simple_spinner_item, mech_vehtype);
        mech_veh_types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mech_veh_type.setAdapter(mech_veh_types);
        mech_veh_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mech_veh_typee = mech_veh_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mech_veh_typee = mech_veh_type.getSelectedItem().toString();
            }
        });

        mech_veh_marks = new ArrayAdapter<String>(menu.this, android.R.layout.simple_spinner_item, mech_vechmark);
        mech_veh_marks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mech_veh_mark.setAdapter(mech_veh_marks);
        mech_veh_mark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mech_veh_markk = mech_veh_mark.getSelectedItem().toString();
                get_mech_veh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        get_mech_veh();


        tow_veh_types = new ArrayAdapter<String>(menu.this, android.R.layout.simple_spinner_item, tow_vehtype);
        tow_veh_types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tow_veh_type.setAdapter(tow_veh_types);
        tow_veh_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tow_veh_typee = tow_veh_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tow_veh_marks = new ArrayAdapter<String>(menu.this, android.R.layout.simple_spinner_item, tow_vehmark);
        tow_veh_marks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tow_veh_mark.setAdapter(tow_veh_marks);
        tow_veh_mark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tow_veh_markk = tow_veh_mark.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        get_tow_veh();

        oil_types = new ArrayAdapter<String>(menu.this, android.R.layout.simple_spinner_item, oiltype);
        oil_types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oil_type.setAdapter(oil_types);
        get_oil();
         oil_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                oil_typee = oil_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fuel_types = new ArrayAdapter<String>(menu.this, android.R.layout.simple_spinner_item, fueltype);
        fuel_types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuel_type.setAdapter(fuel_types);
        get_fuel();
        fuel_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fuel_typee = fuel_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fuel_typee = fuel_type.getSelectedItem().toString();
            }
        });

        team_service_types = new ArrayAdapter<String>(menu.this, android.R.layout.simple_spinner_dropdown_item, team_service);
        team_service_types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        team_service_type.setAdapter(team_service_types);
        get_team_services();
        team_service_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                team_service_typee = team_service_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                team_service_typee = team_service_type.getSelectedItem().toString();
            }
        });
        btnnextmechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_mechanic();
                AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);
                View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dia, null);
                builder.setView(customLayout)
                        .setCancelable(true).
                        setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DocumentReference ref = db.collection("client").document(clientid).collection("vehicle").document();
                                String veh_id = ref.getId();
                                Map<String, Object> veh = new HashMap<String, Object>();
                                veh.put("id", veh_id);
                                veh.put("type", mech_veh_typee);
                                veh.put("mark", mech_veh_markk);
                                ref.set(veh);

                                Intent activityChangeIntent = new Intent(menu.this, home.class);
                                activityChangeIntent.putExtra("request_mechanic_id", request_mechanic_id);
                                startActivity(activityChangeIntent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                Intent activityChangeIntent = new Intent(menu.this, home.class);
                                activityChangeIntent.putExtra("request_mechanic_id", request_mechanic_id);
                                startActivity(activityChangeIntent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        btnnexttow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_tow();
                if (taxi.isChecked() && ambulance.isChecked()){
                    if (!taxi_number_passenger.getText().toString().isEmpty()){
                        request_ambulance();
                        request_taxi();
                        Intent activityChangeIntent = new Intent(menu.this, home.class);
                        activityChangeIntent.putExtra("request_tow_id", request_tow_id);
                        activityChangeIntent.putExtra("request_taxi_id", request_taxi_id);
                        activityChangeIntent.putExtra("request_ambulance_id", request_ambulance_id);
                        startActivity(activityChangeIntent);
                    } else {
                        Toast.makeText(menu.this, "Fill Passenger Number", Toast.LENGTH_SHORT).show();
                    }
                } else if (!taxi.isChecked() && ambulance.isChecked()){
                    request_ambulance();
                    Intent activityChangeIntent = new Intent(menu.this, home.class);
                    activityChangeIntent.putExtra("request_tow_id", request_tow_id);
                    activityChangeIntent.putExtra("request_ambulance_id", request_ambulance_id);
                    startActivity(activityChangeIntent);
                } else if (taxi.isChecked() && !ambulance.isChecked()){
                    if (!taxi_number_passenger.getText().toString().isEmpty()){
                        request_taxi();
                        Intent activityChangeIntent = new Intent(menu.this, home.class);
                        activityChangeIntent.putExtra("request_tow_id", request_tow_id);
                        activityChangeIntent.putExtra("request_taxi_id", request_taxi_id);
                        startActivity(activityChangeIntent);
                    }
                } else if (!taxi.isChecked() && !ambulance.isChecked()){
                    Intent activityChangeIntent = new Intent(menu.this, home.class);
                    activityChangeIntent.putExtra("request_tow_id", request_tow_id);
                    startActivity(activityChangeIntent);
                }
            }
        });
        btnnextstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oil.isChecked() || fuel.isChecked()){
                    request_station();
                    Intent activityChangeIntent = new Intent(menu.this, home.class);
                    activityChangeIntent.putExtra("request_station_id", request_station_id);
                    menu.this.startActivity(activityChangeIntent);
                } else {
                    Toast.makeText(menu.this, "Choose A Service! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnnextteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_team();
                Intent activityChangeIntent = new Intent(menu.this, home.class);
                activityChangeIntent.putExtra("request_team_id", request_team_id);
                activityChangeIntent.putExtra("type", "team");
                menu.this.startActivity(activityChangeIntent);
            }
        });
        btnlistrequests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(menu.this, list_requests.class);
                menu.this.startActivity(activityChangeIntent);
            }
        });
        btnprofile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(menu.this, profile.class);
                menu.this.startActivity(activityChangeIntent);
            }
        });
        btnhelpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(menu.this, help_center.class);
                menu.this.startActivity(activityChangeIntent);
            }
        });
        btnrequestmechanic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(menu.this, R.anim.button_vibrate);
                btnrequestmechanic.startAnimation(shake);
                        mech_veh_form.setVisibility(View.VISIBLE);
                        btnrequestmechanic.setVisibility(View.GONE);
                        btnnextmechanic.setVisibility(View.VISIBLE);
            }
        });
        btnrequesttowtruck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(menu.this, R.anim.button_vibrate);
                btnrequesttowtruck.startAnimation(shake);
                tow_veh_form.setVisibility(View.VISIBLE);
                btnrequesttowtruck.setVisibility(View.GONE);
                btnnexttow.setVisibility(View.VISIBLE);
            }
        });
        btnrequeststation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(menu.this, R.anim.button_vibrate);
                btnrequeststation.startAnimation(shake);
                    stationcheckbox.setVisibility(View.VISIBLE);
                    btnrequeststation.setVisibility(View.GONE);
                    btnnextstation.setVisibility(View.VISIBLE);
            }
        });
        btnrequestteam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(menu.this, R.anim.button_vibrate);
                btnrequestteam.startAnimation(shake);
                    team_service_form.setVisibility(View.VISIBLE);
                    btnrequestteam.setVisibility(View.GONE);
                    btnnextteam.setVisibility(View.VISIBLE);
            }
        });
        mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mech_veh_form.isShown()) {
                    mech_veh_form.setVisibility(View.GONE);
                    btnrequestmechanic.setVisibility(View.VISIBLE);
                    btnnextmechanic.setVisibility(View.GONE);
                } else {
                    mech_veh_form.setVisibility(View.VISIBLE);
                    btnrequestmechanic.setVisibility(View.GONE);
                    btnnextmechanic.setVisibility(View.VISIBLE);
                }
            }
        });
        towtruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tow_veh_form.isShown() || tow_taxi_form.isShown()) {
                    tow_veh_form.setVisibility(View.GONE);
                    tow_taxi_form.setVisibility(View.GONE);
                    btnrequesttowtruck.setVisibility(View.VISIBLE);
                    btnnexttow.setVisibility(View.GONE);
                } else {
                    if (taxi.isChecked()) {
                        tow_veh_form.setVisibility(View.VISIBLE);
                        tow_taxi_form.setVisibility(View.VISIBLE);
                    } else {
                        tow_veh_form.setVisibility(View.VISIBLE);
                        btnrequesttowtruck.setVisibility(View.GONE);
                        btnnexttow.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stationcheckbox.isShown()){
                    stationcheckbox.setVisibility(View.GONE);
                    btnrequeststation.setVisibility(View.VISIBLE);
                    btnnextstation.setVisibility(View.GONE);
                } else {
                    stationcheckbox.setVisibility(View.VISIBLE);
                    btnrequeststation.setVisibility(View.GONE);
                    btnnextstation.setVisibility(View.VISIBLE);
                }
            }
        });
        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team_service_form.isShown()) {
                    team_service_form.setVisibility(View.GONE);
                    btnrequestteam.setVisibility(View.VISIBLE);
                    btnnextteam.setVisibility(View.GONE);btnrequestmechanic.setFocusable(true);
                } else {
                    team_service_form.setVisibility(View.VISIBLE);
                    btnrequestteam.setVisibility(View.GONE);
                    btnnextteam.setVisibility(View.VISIBLE);
                }
            }
        });
        fuel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (fuel.isChecked()) {
                    station_fuel_form.setVisibility(View.VISIBLE);
                } else {
                    station_fuel_form.setVisibility(View.GONE);
                }
            }
        });
        oil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (oil.isChecked()) {
                    station_oil_form.setVisibility(View.VISIBLE);
                } else {
                    station_oil_form.setVisibility(View.GONE);
                }
            }
        });
        taxi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (taxi.isChecked()) {
                    tow_veh_form.setVisibility(View.VISIBLE);
                    tow_taxi_form.setVisibility(View.VISIBLE);
                } else {
                    tow_taxi_form.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void request_mechanic() {
        HashMap<String, Object> m = new HashMap<String, Object>();
        Map<String, Object> vehicle = new HashMap<String, Object>();
        vehicle.put("type", mech_veh_typee);
        vehicle.put("mark", mech_veh_markk);

        request_mechanic_id = db.collection("request").document().getId();
        m.put("id", request_mechanic_id);
        m.put("client_id", clientid);
        m.put("date", Calendar.getInstance().getTime());
        m.put("type", "mechanic");
        m.put("vehicle", vehicle);
        if (!desc_mech.getText().toString().isEmpty()){
            m.put("description", desc_mech.getText().toString());
        }
        db.collection("request").document(request_mechanic_id).set(m);
    }
    private void request_tow(){
        request_tow_id = db.collection("request").document().getId();
        HashMap<String, Object> m = new HashMap<String, Object>();
        Map<String, Object> veh = new HashMap<String, Object>();
        veh.put("type", tow_veh_typee);
        veh.put("mark", tow_veh_markk);

        m.put("id", request_tow_id);
        m.put("client_id", clientid);
        m.put("date", Calendar.getInstance().getTime());
        m.put("type", "tow");
        m.put("vehicle", veh);
        if (!desc_tow.getText().toString().isEmpty()){
            m.put("description", desc_tow.getText().toString());
        }
        db.collection("request").document(request_tow_id).set(m);
    }
    private void request_ambulance() {
        request_ambulance_id = db.collection("request").document().getId();
        HashMap<String, Object> n = new HashMap<String, Object>();
        n.put("id", request_ambulance_id);
        n.put("client_id", clientid);
        n.put("date", Calendar.getInstance().getTime());
        n.put("type", "ambulance");
        if (!desc_tow.getText().toString().isEmpty()){
            n.put("description", desc_tow.getText().toString());
        }
        db.collection("request").document(request_ambulance_id).set(n);
    }
    private void request_taxi() {
        request_taxi_id = db.collection("request").document().getId();
        HashMap<String, Object> n = new HashMap<String, Object>();

        n.put("id", request_taxi_id);
        n.put("client_id", clientid);
        n.put("date", Calendar.getInstance().getTime());
        n.put("type", "taxi");
        if (taxi_number_passenger.getText()!=null){
            n.put("passenger_number", Integer.parseInt(taxi_number_passenger.getText().toString()));
        }

        if (!desc_tow.getText().toString().isEmpty()){
            n.put("description", desc_tow.getText().toString());
        }
        db.collection("request").document(request_taxi_id).set(n);
    }
    private void request_station() {
        request_station_id = db.collection("request").document().getId();
        if (oil.isChecked() && fuel.isChecked()) {
            HashMap<String, Object> m = new HashMap<String, Object>();
            m.put("id", request_station_id);
            m.put("client_id", clientid);
            m.put("date", Calendar.getInstance().getTime());
            m.put("type", "station");
            m.put("fuel", true);
            m.put("fuel_type", fuel_typee);
            m.put("fuel_quantity", Integer.parseInt(qfuel.getText().toString()));
            m.put("oil", true);
            m.put("oil_type", oil_typee);
            m.put("oil_quantity", Integer.parseInt(qoil.getText().toString()));
            m.put("price", Integer.parseInt(qoil.getText().toString())*420 + Integer.parseInt(qfuel.getText().toString())*45);
            if (qoil!=null){
                oprice.setText(String.valueOf(oilquantity*420) + "DA");
            } if (qfuel!=null){
                fprice.setText(String.valueOf(fuelquantity*45) + "DA");
            }
            if (!desc_station.getText().toString().isEmpty()){
                m.put("description", desc_station.getText().toString());
            }
            db.collection("request").document(request_station_id).set(m);
        } else if (oil.isChecked()&&!fuel.isChecked()) {
            HashMap<String, Object> m = new HashMap<String, Object>();

            m.put("id", request_station_id);
            m.put("client_id", clientid);
            m.put("date", Calendar.getInstance().getTime());
            m.put("type", "station");
            m.put("fuel", false);
            m.put("oil", true);
            m.put("oil_type", oil_typee);
            m.put("oil_quantity", Integer.parseInt(qoil.getText().toString()));
            m.put("price", Integer.parseInt(qoil.getText().toString())*420);
            if (qoil!=null){
                oprice.setText(Integer.parseInt(qoil.getText().toString())*420 + "DA");
            }
            if (!desc_station.getText().toString().isEmpty()){
                m.put("description", desc_station.getText().toString());
            }
            db.collection("request").document(request_station_id).set(m);
        } else if (fuel.isChecked()&& !oil.isChecked()) {
            HashMap<String, Object> m = new HashMap<String, Object>();
            m.put("id", request_station_id);
            m.put("client_id", clientid);
            m.put("date", Calendar.getInstance().getTime());
            m.put("type", "station");
            m.put("fuel", true);
            m.put("oil", false);
            m.put("fuel_type", fuel_typee);
            m.put("fuel_quantity", Integer.parseInt(qfuel.getText().toString()));
            m.put("price", Integer.parseInt(qfuel.getText().toString())*45);
            if (qfuel!=null){
                fprice.setText(Integer.valueOf(qfuel.getText().toString())*45 + "DA");
            }
            if (!desc_station.getText().toString().isEmpty()) {
                m.put("description", desc_station.getText().toString());
            }
            db.collection("request").document(request_station_id).set(m);
        }
    }
    private void request_team() {
        HashMap<String, Object> m = new HashMap<String, Object>();
        request_team_id = db.collection("request").document().getId();

        m.put("id", request_team_id);
        m.put("client_id", clientid);
        m.put("date", Calendar.getInstance().getTime());
        m.put("type", "cleaner");
        m.put("service", team_service_typee);
        if (!desc_team.getText().toString().isEmpty()){
            m.put("description", desc_team.getText().toString());
        }
        db.collection("request").document(request_team_id).set(m);
    }
    public void get_mech_veh() {
        db.collection("types").document("vehicle").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> types = (List<String>) document.get("type");
                        mech_vehtype.clear();
                        if (types != null) {
                            for (String type : types) {
                                mech_vehtype.add(type);
                            }
                            mech_veh_types.notifyDataSetChanged();
                        }
                        List<String> marks = (List<String>) document.get("mark");
                        mech_vechmark.clear();
                        if (marks != null) {
                            for (String mark : marks) {
                                mech_vechmark.add(mark);
                            }
                            mech_veh_marks.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(menu.this, "Check Your Connection Please", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void get_tow_veh() {
        db.collection("types").document("vehicle").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> types = (List<String>) document.get("type");
                        tow_vehtype.clear();
                        if (types != null) {
                            for (String type : types) {
                                tow_vehtype.add(type);
                            }
                            tow_veh_types.notifyDataSetChanged();
                        }
                        List<String> marks = (List<String>) document.get("mark");
                        tow_vehmark.clear();
                        if (marks != null) {
                            for (String mark : marks) {
                                tow_vehmark.add(mark);
                            }
                            tow_veh_marks.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(menu.this, "Check Your Connection Please", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void get_oil () {
        db.collection("types").document("oil").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        List<String> oilList = (List<String>) document.get("oil");
                        if (oilList != null) {
                            for (String oil : oilList) {
                                oiltype.add(oil);
                            }
                            oil_types.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(menu.this, "Check Your Connection Please", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void get_fuel () {
        db.collection("types").document("fuel").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> fuelList = (List<String>) document.get("fuel");
                        if (fuelList != null) {
                            for (String fuel : fuelList) {
                                fueltype.add(fuel);
                            }
                            fuel_types.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(menu.this, "Check Your Connection Please", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void get_team_services () {
        db.collection("types").document("vehicle").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> serviceList = (List<String>) document.get("type");
                        if (serviceList != null) {
                            for (String service : serviceList) {
                                team_service.add(service);
                            }
                            team_service_types.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(menu.this, "Check Your Connection Please", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void sendNotification() {
        // Create a new message
        RemoteMessage notificationMessage = new RemoteMessage.Builder("dL5z_kw-SRq80KJZrd-bVd:APA91bE0-NWm42ZIBCMSuq20XRF4U-ail0ogWvmhg3c7JZ2MyjqfCLVPMirQsRbpgej_yXTcApgs1gfMtkhQLMhpkALDCSnKyfqkMrg8LDaZlH66raRJ8KMEEvhG35Rlg8-kX3MNjjra")
                .setMessageId("00")
                .addData("title", "You have a new request")
                .addData("message", "Click here to check it out")
                .build();

        // Send the message
        try {
            FirebaseMessaging.getInstance().send(notificationMessage);
            System.out.println("Notification sent successfully.");
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
