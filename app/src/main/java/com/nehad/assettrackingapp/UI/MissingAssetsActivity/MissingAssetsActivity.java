package com.nehad.assettrackingapp.UI.MissingAssetsActivity;

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
import com.nehad.assettrackingapp.UI.AllAssetsActivity.AllAssetsAdapter;
import com.nehad.assettrackingapp.UI.HomeActivity;
import com.nehad.assettrackingapp.databinding.ActivityHomeBinding;
import com.nehad.assettrackingapp.databinding.ActivityMissingAssetsBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MissingAssetsActivity extends AppCompatActivity {
    private ActivityMissingAssetsBinding binding;
    public static final String TAG = MissingAssetsActivity.class.getSimpleName();

    private List<AssetModel> assetModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_missing_assets);
        binding = ActivityMissingAssetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String location = intent.getStringExtra("loc_key");
        Log.i(TAG, "Location in scan" + location);



        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            assetModelList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetsStatus(false,location);
            Log.i("DatabaseSize",
                    assetModelList.get(0).getLocation()
                            + "");




            //Background work here
            handler.post(() -> {
                //UI Thread work here

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);

                binding.missingAssetsRV.setLayoutManager(layoutManager );
                MissingAssetsAdapter adapter = new MissingAssetsAdapter(assetModelList);
                binding.missingAssetsRV.setAdapter(adapter);


            });
        });


    }
}