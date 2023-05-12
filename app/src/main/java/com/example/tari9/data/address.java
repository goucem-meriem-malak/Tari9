package com.example.tari9.data;

import java.util.Map;

public class address {
    private String country, state, city;
    private Map<String, Object> address;

    public address(Map<String, Object> address, String country, String state, String city) {
        address.put(this.country, country);
        address.put(this.state, state);
        address.put(this.city, city);
        this.address = address;
    }

    public address(String country, String state, String city, Map address) {
        this.country = country;
        this.state = state;
        this.city = city;
    }

    public address() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Map<String, Object> getAddress() {
        return address;
    }
    public void setAddress(Map<String, Object> address) {
        this.address = address;
    }
}
