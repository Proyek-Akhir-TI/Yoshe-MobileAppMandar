package com.example.mandar_app.nelayan;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelNelayan implements Parcelable {
    int id_fisherman;
    String fisherman_name, npwp, kusuka, nib, group_name, boat_name;

    public ModelNelayan() {
        // Default constructor
    }

    protected ModelNelayan(Parcel in) {
        id_fisherman = in.readInt();
        fisherman_name = in.readString();
        npwp = in.readString();
        kusuka = in.readString();
        nib = in.readString();
        group_name = in.readString();
        boat_name = in.readString();
    }

    public static final Creator<ModelNelayan> CREATOR = new Creator<ModelNelayan>() {
        @Override
        public ModelNelayan createFromParcel(Parcel in) {
            return new ModelNelayan(in);
        }

        @Override
        public ModelNelayan[] newArray(int size) {
            return new ModelNelayan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_fisherman);
        dest.writeString(fisherman_name);
        dest.writeString(npwp);
        dest.writeString(kusuka);
        dest.writeString(nib);
        dest.writeString(group_name);
        dest.writeString(boat_name);
    }

    public int getId_fisherman() {
        return id_fisherman;
    }

    public String getFisherman_name() {
        return fisherman_name;
    }

    public String getNpwp() {
        return npwp;
    }

    public String getKusuka() {
        return kusuka;
    }

    public String getNib() {
        return nib;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getBoat_name() {
        return boat_name;
    }
}
