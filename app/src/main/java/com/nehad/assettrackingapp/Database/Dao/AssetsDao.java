package com.nehad.assettrackingapp.Database.Dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nehad.assettrackingapp.Database.entities.AssetModel;

import java.util.List;

@androidx.room.Dao
public interface AssetsDao {

   @Query("SELECT * FROM  asset_table")
    List<AssetModel> getAllAssets();

   @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAsset(AssetModel assetModel);

    @Query("DELETE  FROM  asset_table")
    public void DeleteAllAssets();


    @Query("DELETE  FROM  asset_location_change")
    public void DeleteAllLocationChange();
}
