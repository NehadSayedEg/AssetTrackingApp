package com.nehad.assettrackingapp.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.R;
import com.nehad.assettrackingapp.UI.AllAssetsActivity.AllAssetsActivity;
import com.nehad.assettrackingapp.UI.AssetsLocation.AssetsLocationActivity;
import com.nehad.assettrackingapp.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    public static final String TAG = HomeActivity.class.getSimpleName();
    private List<AssetModel> assetModelList = new ArrayList<>();


    private List<String> LocationlList = new ArrayList<>();
    String location = null;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_home);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            assetModelList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();
         //   Log.i("DatabaseSize", assetModelList.get(0).getLocation() + "");

                    for (int i = 0 ; i< assetModelList.size() ; i++ ) {
                       String loc = assetModelList.get(i).getLocation();

                        if (!LocationlList.contains(loc)) {

                            LocationlList.add(loc);
                        }

                        Log.i("loc ", loc + "");
                    }





            //Background work here
            handler.post(() -> {
                //UI Thread work here

                ArrayAdapter <String> arrayAdapter = new ArrayAdapter( this , R.layout.option_item_location ,LocationlList);

                binding.autoComplete.setAdapter(arrayAdapter);
                binding.autoComplete.setOnItemClickListener( new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent , View view , int position , long id){
                        String item  = parent.getItemAtPosition(position).toString();
                        location = item ;
                        Toast.makeText(getApplicationContext() , "Item :" + location , Toast.LENGTH_LONG).show();

                    }


                });
            });
        });


        binding.AllAssetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this , AllAssetsActivity.class);
                startActivity(intent);


            }
        });

        binding.scanLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location != null){
                    Intent intent = new Intent(HomeActivity.this , AssetsLocationActivity.class);
                    intent.putExtra("loc_key",location);
                    startActivity(intent);
                }else{

                    Toast.makeText(getApplicationContext() , "Please select  location first  :" , Toast.LENGTH_LONG).show();

                }


            }
        });


        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new MaterialAlertDialogBuilder(HomeActivity.this, R.style.AlertDialogTheme).setTitle("Delete Data")
                        .setMessage("Are you Sure you want to delete Data?")


                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearAssetsDB();
                                Toast.makeText(getApplicationContext(), "Bach to Main", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();

            }




        });


    }



    private void clearAssetsDB() {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: Clear database Table ...");

            AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().DeleteAllAssets();

            Log.i("DatabaseSize",
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size()
                            + "");

        }).start();

    }



    private void initDeleteDialog() {




    }




}