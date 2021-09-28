package com.nehad.assettrackingapp.UI.FoundAssetsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.R;
import com.nehad.assettrackingapp.UI.MissingAssetsActivity.MissingAssetsActivity;
import com.nehad.assettrackingapp.UI.MissingAssetsActivity.MissingAssetsAdapter;
import com.nehad.assettrackingapp.databinding.ActivityFoundAssetsBinding;
import com.nehad.assettrackingapp.databinding.ActivityMissingAssetsBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoundAssetsActivity extends AppCompatActivity {
    private ActivityFoundAssetsBinding binding;
    public static final String TAG = FoundAssetsActivity.class.getSimpleName();
    private List<AssetModel> assetModelList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_found_assets);

        binding = ActivityFoundAssetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String location = intent.getStringExtra("loc_key");
        Log.i(TAG, "Location in scan" + location);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            assetModelList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetsStatus(true,location);
//            Log.i("DatabaseSize",
//                    assetModelList.get(0).getLocation()
//                            + "");




            //Background work here
            handler.post(() -> {
                //UI Thread work here

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);

                binding.foundAssetsRV.setLayoutManager(layoutManager );
                MissingAssetsAdapter adapter = new MissingAssetsAdapter(assetModelList);
                binding.foundAssetsRV.setAdapter(adapter);


            });
        });


    }
}