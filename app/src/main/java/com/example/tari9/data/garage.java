package com.example.tari9.data;

import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class garage {
    private String id, name, dunit, phone;
    private GeoPoint location;
    private Map < String, Object> address;
    private float distance;

    public garage() {
    }

    public garage(String id, String name, String dunit, String phone, GeoPoint location, Map<String, Object> address, float distance) {
        this.id = id;
        this.name = name;
        this.dunit = dunit;
        this.phone = phone;
        this.location = location;
        this.address = address;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDunit() {
        return dunit;
    }

    public void setDunit(String dunit) {
        this.dunit = dunit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
