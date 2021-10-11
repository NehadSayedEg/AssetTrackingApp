package com.nehad.assettrackingapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.Database.entities.locationChange;

import java.util.List;

@Dao
public interface AssetsDao {

   @Query("SELECT * FROM  asset_table")
    List<AssetModel> getAllAssets();


 @Query("SELECT * from asset_table WHERE barcode = :barcode  AND  location =:loc  ")
 List<AssetModel> getAssetsLocation(String barcode  , String loc );

    @Query("SELECT * from asset_table WHERE barcode = :barcode   ")
    List<AssetModel> getAssetByBarcode(String barcode  );



    @Query("SELECT * from asset_table WHERE   location =:loc  AND found =:found ")
    List<AssetModel> getAssetsStatus( boolean found  , String loc );


    @Query("SELECT * from asset_table WHERE    location =:loc  AND found =:found ")
    List<AssetModel> getAssetsFound(  boolean found  , String loc );




    @Query("UPDATE asset_table  SET  found = :found  WHERE  barcode  = :barcode AND  location =:loc ")
    void  setAssetFound(String barcode  , Boolean found , String loc);
//
   @Query("UPDATE asset_table  SET  scannedBefore =:scannedBefore WHERE  barcode  = :barcode AND  location =:loc ")
   void  setScannedFound(String barcode  , Boolean scannedBefore , String loc);


//
//    @Query("UPDATE asset_table  SET  scannedBefore =:scannedBefore  AND found = :found  WHERE  barcode  = :barcode AND  location =:loc ")
//    void  setScannedFound(String barcode  , Boolean scannedBefore ,Boolean found , String loc);


   @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAsset(AssetModel assetModel);

    @Query("DELETE  FROM  asset_table")
    public void DeleteAllAssets();


    @Query("DELETE  FROM  asset_location_change")
    public void DeleteAllLocationChange();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssetNewLocation(locationChange locationChange);

    @Query("SELECT * FROM  asset_location_change")
    List<locationChange> getAllLocationChangedList();

//    @Query("UPDATE asset_location_change  SET   first_location = :firstLocation  WHERE  barcode  = :barcode AND  last_location =:secondLocation ")
//    void  setAssetLocChanged(String barcode  , String firstLocation , String secondLocation);


    @Query("UPDATE asset_table  SET  location = :loc  WHERE  barcode  = :barcode  ")
    void  setAssetLocChanged(String barcode   , String loc);

    @Query("UPDATE asset_table  SET  found = :found  WHERE  barcode  = :barcode  ")
    void  setAssetLocFound(String barcode   , Boolean found );
    @Query("UPDATE asset_table  SET  scannedBefore =:scannedBefore WHERE  barcode  = :barcode  ")
    void  setScanned(String barcode  , Boolean scannedBefore );




}
