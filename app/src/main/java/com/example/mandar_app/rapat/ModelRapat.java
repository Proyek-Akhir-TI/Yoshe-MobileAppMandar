package com.example.mandar_app.rapat;

public class ModelRapat {
    int id_meeting;
    String date, notes, documentation_photo, group_name;

    public int getId_meeting() {
        return id_meeting;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    public String getDocumentation_photo() {
        return documentation_photo;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDocumentation_photo(String documentation_photo) {
        this.documentation_photo = documentation_photo;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
