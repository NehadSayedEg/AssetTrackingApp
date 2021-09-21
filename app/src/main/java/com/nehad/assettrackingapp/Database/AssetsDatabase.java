package com.nehad.assettrackingapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nehad.assettrackingapp.Database.Dao.AssetsDao;
import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.Database.entities.locationChange;

@Database(entities = { locationChange.class , AssetModel.class   }, version = 1 , exportSchema = false)
public  abstract  class AssetsDatabase extends RoomDatabase {
    public static AssetsDatabase assetsDatabase ;
     public static synchronized AssetsDatabase getAssetsDatabase(Context context){
         if(assetsDatabase == null){
             assetsDatabase = Room.databaseBuilder(context , AssetsDatabase.class , "assets_db").build();
         }
         return assetsDatabase;
     }

     public abstract AssetsDao assetsDao();
}
