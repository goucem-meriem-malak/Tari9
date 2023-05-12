package com.example.tari9.data;

public class get_mechanics {

    private String id, name, dunit;
    private float distance;

    public get_mechanics(String id, String name, float distance, String dunit) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.dunit = dunit;
    }

    public get_mechanics() {
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getDunit() {
        return dunit;
    }

    public void setDunit(String dunit) {
        this.dunit = dunit;
    }
}
