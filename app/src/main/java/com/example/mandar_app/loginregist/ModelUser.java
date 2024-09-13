package com.example.mandar_app.loginregist;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ModelUser implements Serializable {
    @SerializedName("id_account")
    private int idAccount;

    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role;

    @SerializedName("group_name")
    private String groupName;

    @SerializedName("boat_name")
    private String boatName;

    @SerializedName("nib")
    private String nib;

    @SerializedName("kusuka")
    private String kusuka;

    @SerializedName("npwp")
    private String npwp;

    // Getters and Setters

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBoatName() {
        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public String getNib() {
        return nib;
    }

    public void setNib(String nib) {
        this.nib = nib;
    }

    public String getKusuka() {
        return kusuka;
    }

    public void setKusuka(String kusuka) {
        this.kusuka = kusuka;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }
}
