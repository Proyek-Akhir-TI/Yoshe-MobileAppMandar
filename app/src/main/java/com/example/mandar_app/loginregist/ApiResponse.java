package com.example.mandar_app.loginregist;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ApiResponse implements Serializable {
    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private ModelUser user;

    @SerializedName("role")
    private String role;  // Tambahkan ini untuk menangani peran

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ModelUser getUser() {
        return user;
    }

    public void setUser(ModelUser user) {
        this.user = user;
    }

    public String getRole() {  // Tambahkan getter untuk role
        return role;
    }

    public void setRole(String role) {  // Tambahkan setter untuk role
        this.role = role;
    }
}
