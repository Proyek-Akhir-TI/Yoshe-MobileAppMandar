package com.example.mandar_app.kelompok;

import com.example.mandar_app.perahu.ModelPerahu;

import java.util.List;

public class ModelResponse {
    int kode;
    String pesan;
    List<ModelKelompok> data;

    public int getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelKelompok> getData() {
        return data;
    }
}
