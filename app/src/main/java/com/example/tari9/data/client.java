package com.example.tari9.data;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Map;

public class client {
    private String id, phone, firstname, lastname, email, token;
    private GeoPoint location;
    private Map<String, Object> address, location_address;
    public client() {
    }

    public client(String id, String phone, String firstname, String lastname, String email, String token, GeoPoint location, Map<String, Object> address, Map<String, Object> location_address, List<veh> vehicle) {
        this.id = id;
        this.phone = phone;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.token = token;
        this.location = location;
        this.address = address;
        this.location_address = location_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Map<String, Object> getAddress() {
        return address;
    }

    public void setAddress(Map<String, Object> address) {
        this.address = address;
    }

    public Map<String, Object> getLocation_address() {
        return location_address;
    }

    public void setLocationaddress(Map<String, Object> location_address) {
        this.location_address = location_address;
    }
}
