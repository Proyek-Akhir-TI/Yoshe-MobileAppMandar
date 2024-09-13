package com.example.mandar_app;

import java.util.List;

public class ModelBannerResponse {
    private int kode;
    private String pesan;
    private List<ModelBanner> data;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<ModelBanner> getData() {
        return data;
    }

    public void setData(List<ModelBanner> data) {
        this.data = data;
    }
}
