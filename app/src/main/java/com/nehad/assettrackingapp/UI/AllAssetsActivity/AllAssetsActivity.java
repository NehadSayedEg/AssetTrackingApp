package com.nehad.assettrackingapp.UI.AllAssetsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.R;
import com.nehad.assettrackingapp.Util.ExcelUtil;
import com.nehad.assettrackingapp.databinding.ActivityAllAssetsBinding;
import com.nehad.assettrackingapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllAssetsActivity extends AppCompatActivity {
    private ActivityAllAssetsBinding binding;
    public static final String TAG = AllAssetsActivity.class.getSimpleName();
    private RecyclerView allAssetsRV ;
    private List<AssetModel> assetModelList;
    private  AllAssetsAdapter adapter ;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_assets);


        binding = ActivityAllAssetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Thread(() -> {
            Log.i(TAG, "doInBackground: Importing...");

            assetModelList =
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();

        }).start();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        binding.allAssetsRV.setLayoutManager(layoutManager );
        assetModelList = new ArrayList<>();
        adapter = new AllAssetsAdapter(assetModelList);
        binding.allAssetsRV.setAdapter(adapter);



        getAllAssets();
    }


    private void getAllAssets() {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: get All assets from Assets Table ...");

            AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();

            Log.i("DatabaseSize",
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size()
                            + "");
          assetModelList =
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();







            Log.i("Database string",
                    assetModelList.get(0).getDescription()+ "");
//            Log.i("Database string",
//                    assetModelList.get(1).getBarcode()+ "");

        }).start();

    }


}