package com.example.mandar_app.production;

import java.util.List;

public class ModelResponse {
    int kode;
    String pesan;
    List<ModelProduction> data;

    public int getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelProduction> getData() {
        return data;
    }
}
