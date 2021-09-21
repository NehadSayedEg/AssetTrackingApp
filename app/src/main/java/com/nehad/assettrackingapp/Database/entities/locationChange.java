package com.nehad.assettrackingapp.Database.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "asset_location_change")
public class locationChange {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "barcode")
    private String barcode;


    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "first_location")
    private String firstLocation;

    @ColumnInfo(name = "last_location")
    private String lastLocation;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(String firstLocation) {
        this.firstLocation = firstLocation;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }
}
