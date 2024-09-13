package com.example.mandar_app.perahu;

import com.example.mandar_app.kelompok.ModelKelompok;

import java.util.List;

public class ModelResponse {
    int kode;
    String pesan;
    List<ModelPerahu> data;

    public int getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelPerahu> getData() {
        return data;
    }
}
