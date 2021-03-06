package com.nehad.assettrackingapp.UI.AssetsLocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.nehad.assettrackingapp.UI.ExportFilesActivity.ExportFilesActivity;
import com.nehad.assettrackingapp.UI.ExportFilesActivity.ExportSheetsActivity;
import com.nehad.assettrackingapp.UI.FoundAssetsActivity.FoundAssetsActivity;
import com.nehad.assettrackingapp.UI.HomeActivity;
import com.nehad.assettrackingapp.UI.MissingAssetsActivity.MissingAssetsActivity;
import com.nehad.assettrackingapp.UI.ScanLocation.ScanLocationActivity;
import com.nehad.assettrackingapp.databinding.ActivityAssetsLocationBinding;

import java.io.File;

public class AssetsLocationActivity extends AppCompatActivity {
    private ActivityAssetsLocationBinding binding;
    public static final String TAG = AssetsLocationActivity.class.getSimpleName();


    String location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_assets_location);
        binding = ActivityAssetsLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String location = intent.getStringExtra("loc_key");
        Log.i(TAG, "Location in scan" + location);


        binding.scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AssetsLocationActivity.this , ScanLocationActivity.class);
                intent.putExtra("loc_key",location);
                startActivity(intent);
            }
        });
        binding.foundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(AssetsLocationActivity.this , FoundAssetsActivity.class);
                    intent.putExtra("loc_key",location);
                    startActivity(intent);
            }
        });

        binding.missingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AssetsLocationActivity.this , MissingAssetsActivity.class);
                intent.putExtra("loc_key",location);
                startActivity(intent);
            }
        });

        binding.exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssetsLocationActivity.this , ExportSheetsActivity.class);
                intent.putExtra("loc_key",location);
                startActivity(intent);

            }
        });


        binding.locTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssetsLocationActivity.this , HomeActivity.class);
                startActivity(intent);
                finish();



            }
        });


        binding.exportTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path = Environment.getExternalStorageDirectory() + "/" + "Downloads" + "/";

//                String path = Environment.getExternalStorageDirectory() + "/" + "Asset Tracking" + "/";
                Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(uri, "*/*");
                startActivity(intent);

            }
        });
    }

}