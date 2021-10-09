package com.nehad.assettrackingapp.Database.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "asset_table")
public class AssetModel implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "barcode")
    private String barcode ;


    @ColumnInfo(name = "location")
    private String location ;

    @ColumnInfo(name = "description")
    private String description ;

    @ColumnInfo(name = "status")
    private boolean status ;


    @ColumnInfo(name = "found")
    private boolean found ;

    @ColumnInfo(name = "scannedBefore")
    private boolean scannedBefore ;
    @ColumnInfo(name = "missing")
    private boolean missing ;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean isScannedBefore() {
        return scannedBefore;
    }

    public void setScannedBefore(boolean scannedBefore) {
        this.scannedBefore = scannedBefore;
    }

    public boolean isMissing() {
        return missing;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }


    @Nullable
    @Override
    public String toString() {
        return "AssetModel{" +
                "barcode='" + barcode + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", found=" + found +
                ", scannedBefore=" + scannedBefore +
                ", missing=" + missing +
                '}';
    }
}
