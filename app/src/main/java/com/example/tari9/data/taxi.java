package com.example.tari9.data;

import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class taxi {
    private String id, firstname, lastname, phone, dunit;
    private Map<String, Object> address;
    private GeoPoint location;
    private float distance;
    private veh vehicle;

    public taxi() {
    }

    public taxi(String id, String firstname, String lastname, String phone, String dunit, Map<String, Object> address, GeoPoint location, float distance, veh vehicle) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.dunit = dunit;
        this.address = address;
        this.location = location;
        this.distance = distance;
        this.vehicle = vehicle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDunit() {
        return dunit;
    }

    public void setDunit(String dunit) {
        this.dunit = dunit;
    }

    public Map<String, Object> getAddress() {
        return address;
    }

    public void setAddress(Map<String, Object> address) {
        this.address = address;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public veh getVehicle() {
        return vehicle;
    }

    public void setVehicle(veh vehicle) {
        this.vehicle = vehicle;
    }
}
