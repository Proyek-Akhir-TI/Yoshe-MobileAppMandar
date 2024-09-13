package com.example.mandar_app.nelayan;

import java.util.List;

public class ModelResponseGroups {
    private int kode;
    private String pesan;
    private List<ModelGroup> data;

    // Getter and Setter methods
    public int getKode() { return kode; }
    public void setKode(int kode) { this.kode = kode; }
    public String getPesan() { return pesan; }
    public void setPesan(String pesan) { this.pesan = pesan; }
    public List<ModelGroup> getData() { return data; }
    public void setData(List<ModelGroup> data) { this.data = data; }
}

