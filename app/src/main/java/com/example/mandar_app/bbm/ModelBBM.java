package com.example.mandar_app.bbm;

public class ModelBBM {
    int id_fuel;
    String fuel_species, date, quantity_liters, price_of_liter, total_fuel, boat_name;
    int id_boat;

    public int getId_fuel() {
        return id_fuel;
    }

    public String getFuel_species() {
        return fuel_species;
    }

    public String getDate() {
        return date;
    }

    public String getQuantity_liters() {
        return quantity_liters;
    }

    public String getPrice_of_liter() {
        return price_of_liter;
    }

    public String getTotal_fuel() {
        return total_fuel;
    }

    public String getBoat_name() {
        return boat_name;
    }

    public int getId_boat() {
        return id_boat;
    }

    // Setter for total_fuel to ensure calculation is done correctly
    public void setTotal_fuel(String quantity, String price) {
        this.total_fuel = String.valueOf(Double.parseDouble(quantity) * Double.parseDouble(price));
    }
}
