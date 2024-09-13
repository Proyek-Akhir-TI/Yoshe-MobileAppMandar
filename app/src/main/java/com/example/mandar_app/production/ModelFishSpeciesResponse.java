package com.example.mandar_app.production;

import java.util.List;

public class ModelFishSpeciesResponse {
    int kode;
    String pesan;
    List<ModelFishSpecies> data;

    public int getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelFishSpecies> getData() {
        return data;
    }
}
