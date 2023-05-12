package com.example.tari9.data;

public class oil_fuel {
    private String oil, fuel, fuel_unit, oil_unit;
    private float fuel_quantity, oil_quantity;

    public oil_fuel() {
    }

    public oil_fuel(String oil, String fuel, String fuel_unit, String oil_unit, float fuel_quantity, float oil_quantity) {
        this.oil = oil;
        this.fuel = fuel;
        this.fuel_unit = fuel_unit;
        this.oil_unit = oil_unit;
        this.fuel_quantity = fuel_quantity;
        this.oil_quantity = oil_quantity;
    }

    public String getOil() {
        return oil;
    }

    public void setOil(String oil) {
        this.oil = oil;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getFuel_unit() {
        return fuel_unit;
    }

    public void setFuel_unit(String fuel_unit) {
        this.fuel_unit = fuel_unit;
    }

    public String getOil_unit() {
        return oil_unit;
    }

    public void setOil_unit(String oil_unit) {
        this.oil_unit = oil_unit;
    }

    public float getFuel_quantity() {
        return fuel_quantity;
    }

    public void setFuel_quantity(float fuel_quantity) {
        this.fuel_quantity = fuel_quantity;
    }

    public float getOil_quantity() {
        return oil_quantity;
    }

    public void setOil_quantity(float oil_quantity) {
        this.oil_quantity = oil_quantity;
    }
}
