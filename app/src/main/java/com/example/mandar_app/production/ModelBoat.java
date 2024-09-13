package com.example.mandar_app.production;

public class ModelBoat {
    int id_boat;
    String boat_name;
    String fisherman_name;  // Tambahkan ini

    // Getter dan Setter
    public int getId_boat() {
        return id_boat;
    }

    public String getBoat_name() {
        return boat_name;
    }

    public String getFishermanName() {
        return fisherman_name;
    }

    public void setFishermanName(String fisherman_name) {
        this.fisherman_name = fisherman_name;
    }
}
