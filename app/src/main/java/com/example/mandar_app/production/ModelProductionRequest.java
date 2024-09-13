package com.example.mandar_app.production;

import java.util.List;

public class ModelProductionRequest {
    private String date;
    private int id_boat;
    private List<ModelProductionDetail> productionDetails;

    public ModelProductionRequest(String date, int id_boat, List<ModelProductionDetail> productionDetails, String boatName) {
        this.date = date;
        this.id_boat = id_boat;
        this.productionDetails = productionDetails;
    }

    public String getDate() {
        return date;
    }

    public int getId_boat() {
        return id_boat;
    }

    public List<ModelProductionDetail> getProductionDetails() {
        return productionDetails;
    }
}
