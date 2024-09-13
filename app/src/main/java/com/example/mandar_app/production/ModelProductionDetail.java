package com.example.mandar_app.production;

public class ModelProductionDetail {
    private int id_fish_species;
    private String weight;
    private String fish_species;

    public ModelProductionDetail(int id_fish_species, String weight) {
        this.id_fish_species = id_fish_species;
        this.weight = weight;
    }

    public int getId_fish_species() {
        return id_fish_species;
    }

    public String getWeight() {
        return weight;
    }

    public String getFish_species() {
        return fish_species;
    }

    public void setFish_species(String fish_species) {
        this.fish_species = fish_species;
    }
}
