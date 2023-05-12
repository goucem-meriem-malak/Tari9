package com.example.tari9.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class get_requests {
    private String id, client_id, worker_id, worker_phone, type, state, oil_type, fuel_type,
            oil_unit, fuel_unit, service;
    boolean oil, fuel;
    private GeoPoint client_location, worker_location;
    private Map<String, Object> address;
    private Timestamp date;
    private float distance, price, passenger_number;
    private int team_nbr, fuel_quantity, oil_quantity;
    private veh vehicle;

    public get_requests() {
    }

    public get_requests(String id, String client_id, String worker_id, String worker_phone, String type, String state, String oil_type, String fuel_type, String oil_unit, String fuel_unit, String service, boolean oil, boolean fuel, GeoPoint client_location, GeoPoint worker_location, Map<String, Object> address, Timestamp date, float distance, float price, int passenger_number, int team_nbr, int fuel_quantity, int oil_quantity, veh vehicle) {
        this.id = id;
        this.client_id = client_id;
        this.worker_id = worker_id;
        this.worker_phone = worker_phone;
        this.type = type;
        this.state = state;
        this.oil_type = oil_type;
        this.fuel_type = fuel_type;
        this.oil_unit = oil_unit;
        this.fuel_unit = fuel_unit;
        this.service = service;
        this.oil = oil;
        this.fuel = fuel;
        this.client_location = client_location;
        this.worker_location = worker_location;
        this.address = address;
        this.date = date;
        this.distance = distance;
        this.price = price;
        this.passenger_number = passenger_number;
        this.team_nbr = team_nbr;
        this.fuel_quantity = fuel_quantity;
        this.oil_quantity = oil_quantity;
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

    public String getWorker_phone() {
        return worker_phone;
    }

    public void setWorker_phone(String worker_phone) {
        this.worker_phone = worker_phone;
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

    public String getOil_unit() {
        return oil_unit;
    }

    public void setOil_unit(String oil_unit) {
        this.oil_unit = oil_unit;
    }

    public String getFuel_unit() {
        return fuel_unit;
    }

    public void setFuel_unit(String fuel_unit) {
        this.fuel_unit = fuel_unit;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isOil() {
        return oil;
    }

    public void setOil(boolean oil) {
        this.oil = oil;
    }

    public boolean isFuel() {
        return fuel;
    }

    public void setFuel(boolean fuel) {
        this.fuel = fuel;
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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPassenger_number() {
        return passenger_number;
    }

    public void setPassenger_number(float passenger_number) {
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

    public veh getVehicle() {
        return vehicle;
    }

    public void setVehicle(veh vehicle) {
        this.vehicle = vehicle;
    }
}
