package com.example.mandar_app.kelompok;

public class ModelKelompok {
    int id_fisherman_group;
    String chair_name, group_name, office_photo;
    int jumlah_anggota; // new field

    public int getId_fisherman_group() {
        return id_fisherman_group;
    }

    public String getChair_name() {
        return chair_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getOffice_photo() {
        return office_photo;
    }

    public int getJumlah_anggota() {
        return jumlah_anggota; // new getter
    }
}
