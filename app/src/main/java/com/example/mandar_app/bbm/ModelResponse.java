package com.example.mandar_app.bbm;

import java.util.List;

public class ModelResponse {
    int kode;
    String pesan;
    List<ModelBBM> data;

    public int getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelBBM> getData() {
        return data;
    }
}
