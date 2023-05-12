package com.example.tari9.data;

public class vehicule {
    private int id;
    private vehicule type;
    private int registration_num;

    public vehicule() {
    }

    public vehicule(int id, vehicule type, int registration_num) {
        this.id = id;
        this.type = type;
        this.registration_num = registration_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public vehicule getType() {
        return type;
    }

    public void setType(vehicule type) {
        this.type = type;
    }

    public int getRegistration_num() {
        return registration_num;
    }

    public void setRegistration_num(int registration_num) {
        this.registration_num = registration_num;
    }
}
