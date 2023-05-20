package com.example.tari9.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class request {
    private String id, client_id, worker_id, type, state, dunit, oil_type, fuel_type,
    worker_phone, service;;
    private boolean fuel, oil, taxi, ambulance;
    private int passenger_number, team_nbr, fuel_quantity, oil_quantity;;
    private Timestamp date;
    private float price, distance;
    private GeoPoint client_location, worker_location;
    private Map<String, Object> address;
    private veh vehicle;

    public request() {
    }

    public request(String id, String client_id, String worker_id, String type, String state, String dunit, String oil_type, String fuel_type, String worker_phone, String service, boolean fuel, boolean oil, boolean taxi, boolean ambulance, int passenger_number, int team_nbr, int fuel_quantity, int oil_quantity, Timestamp date, float price, float distance, GeoPoint client_location, GeoPoint worker_location, Map<String, Object> address, veh vehicle) {
        this.id = id;
        this.client_id = client_id;
        this.worker_id = worker_id;
        this.type = type;
        this.state = state;
        this.dunit = dunit;
        this.oil_type = oil_type;
        this.fuel_type = fuel_type;
        this.worker_phone = worker_phone;
        this.service = service;
        this.fuel = fuel;
        this.oil = oil;
        this.taxi = taxi;
        this.ambulance = ambulance;
        this.passenger_number = passenger_number;
        this.team_nbr = team_nbr;
        this.fuel_quantity = fuel_quantity;
        this.oil_quantity = oil_quantity;
        this.date = date;
        this.price = price;
        this.distance = distance;
        this.client_location = client_location;
        this.worker_location = worker_location;
        this.address = address;
        this.vehicle = vehicle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDunit() {
        return dunit;
    }

    public void setDunit(String dunit) {
        this.dunit = dunit;
    }

    public String getOil_type() {
        return oil_type;
    }

    public void setOil_type(String oil_type) {
        this.oil_type = oil_type;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getWorker_phone() {
        return worker_phone;
    }

    public void setWorker_phone(String worker_phone) {
        this.worker_phone = worker_phone;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isFuel() {
        return fuel;
    }

    public void setFuel(boolean fuel) {
        this.fuel = fuel;
    }

    public boolean isOil() {
        return oil;
    }

    public void setOil(boolean oil) {
        this.oil = oil;
    }

    public boolean isTaxi() {
        return taxi;
    }

    public void setTaxi(boolean taxi) {
        this.taxi = taxi;
    }

    public boolean isAmbulance() {
        return ambulance;
    }

    public void setAmbulance(boolean ambulance) {
        this.ambulance = ambulance;
    }

    public int getPassenger_number() {
        return passenger_number;
    }

    public void setPassenger_number(int passenger_number) {
        this.passenger_number = passenger_number;
    }

    public int getTeam_nbr() {
        return team_nbr;
    }

    public void setTeam_nbr(int team_nbr) {
        this.team_nbr = team_nbr;
    }

    public int getFuel_quantity() {
        return fuel_quantity;
    }

    public void setFuel_quantity(int fuel_quantity) {
        this.fuel_quantity = fuel_quantity;
    }

    public int getOil_quantity() {
        return oil_quantity;
    }

    public void setOil_quantity(int oil_quantity) {
        this.oil_quantity = oil_quantity;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public GeoPoint getClient_location() {
        return client_location;
    }

    public void setClient_location(GeoPoint client_location) {
        this.client_location = client_location;
    }

    public GeoPoint getWorker_location() {
        return worker_location;
    }

    public void setWorker_location(GeoPoint worker_location) {
        this.worker_location = worker_location;
    }

    public Map<String, Object> getAddress() {
        return address;
    }

    public void setAddress(Map<String, Object> address) {
        this.address = address;
    }

    public veh getVehicle() {
        return vehicle;
    }

    public void setVehicle(veh vehicle) {
        this.vehicle = vehicle;
    }
}
