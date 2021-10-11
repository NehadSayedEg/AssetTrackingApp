package com.nehad.assettrackingapp.UI.ScanLocation;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.Dao.AssetsDao;
import com.nehad.assettrackingapp.Database.entities.AssetModel;

import java.util.List;

public class ScanLocationViewModel extends ViewModel {

    AssetsDao assetsDao ;
    LiveData<List<AssetModel>> getAllAsset;

    public ScanLocationViewModel(){
        assetsDao = AssetsDatabase.assetsDatabase.assetsDao();
        //.getInstance(application).dao();
        getAllAsset = assetsDao.getAllAssetLiveData();
    }

    LiveData<List<AssetModel>> getAllAssetLiveData() {
        // mAllDes = assetsDao.getAlllocAndDesList() ;
        return getAllAsset ; }

//    LiveData<List<AssetModel>> getAllScannedAssetsWithLocation() {
//
//        return AssetsDatabase.getAssetsDatabase(Context).assetsDao().getAllScannedAssetsWithLocation();
//        ; }



}
