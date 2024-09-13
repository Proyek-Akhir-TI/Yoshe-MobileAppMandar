package com.example.mandar_app.nelayan;

import java.util.List;

public class ModelResponse {
    int kode;
    String pesan;
    List<ModelNelayan> data;

    public int getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelNelayan> getData() {
        return data;
    }
}
