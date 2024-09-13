package com.example.mandar_app.production;

import java.util.List;

public class ModelProduction {
    int id_production;
    String date;
    String boat_name;
    String total_weight;
    List<ModelProductionDetail> fish_details;

    public int getId_production() {
        return id_production;
    }

    public String getDate() {
        return date;
    }

    public String getBoat_name() {
        return boat_name;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public List<ModelProductionDetail> getFish_details() {
        return fish_details;
    }
}
