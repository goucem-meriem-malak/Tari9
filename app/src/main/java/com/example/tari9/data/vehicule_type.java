package com.example.tari9.data;

public class vehicule_type {
    private String type;
    private String mark;
    private String model;

    public vehicule_type() {
    }

    public vehicule_type(String type, String mark, String model) {
        this.type = type;
        this.mark = mark;
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
