package com.example.mandar_app.bbm;

import java.util.List;

public class ModelBoatResponse {
    int kode;
    String pesan;
    List<ModelBoat> data;

    public int getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelBoat> getData() {
        return data;
    }
}
