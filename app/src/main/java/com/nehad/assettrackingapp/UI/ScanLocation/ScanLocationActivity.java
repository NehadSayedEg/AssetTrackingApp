package com.nehad.assettrackingapp.UI.ScanLocation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.entities.AssetModel;

import com.nehad.assettrackingapp.Database.entities.locationChange;
import com.nehad.assettrackingapp.R;
import com.nehad.assettrackingapp.UI.HomeActivity;
import com.nehad.assettrackingapp.UI.MainActivity;
import com.nehad.assettrackingapp.databinding.ActivityScanLocationBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanLocationActivity extends AppCompatActivity {
    private ActivityScanLocationBinding binding;
    public static final String TAG = ScanLocationActivity.class.getSimpleName();

    EditText barcodeEt;
    RecyclerView scanRecyclerView ;
    List<AssetModel> assetslList  = new ArrayList<>();

    private ScanAdapter  scanAdapter ;
    Activity activity ;
    Context context ;
    boolean found = false;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String barcode ;
    String location;
    String scannedLocation ;
    String Des ;
     String secondLocation ;
    List<String> assetsList = new LinkedList<String>();

   // private List<String> assetsList ;
    private List<AssetModel> assetsModelList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_location);
        binding = ActivityScanLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
         location = intent.getStringExtra("loc_key");


        Log.i(TAG, "Location in scan" + location);


        // binding.scanRecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        binding.scanRecyclerView.setHasFixedSize(true);
        binding.scanRecyclerView.setLayoutManager(layoutManager);
        binding.scanRecyclerView.scrollToPosition(0);
        scanAdapter = new ScanAdapter(assetslList ,activity  ,context);
        binding.scanRecyclerView.setAdapter(scanAdapter);



        //set the edit text focus
        binding.barcodeEt.requestFocus();
        binding.barcodeEt.setFocusable(true);
        binding.barcodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(! s.toString().isEmpty()){
                    Log.v("on text change  Text  ", s.toString());

                     barcode =   binding.barcodeEt.getText().toString();
                    InPlaceAssets();
                    AssetsScan();


                }else{
                    //Toast.makeText(getApplicationContext() , "Empty " , Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        binding.finishScanBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                getUnFoundItems();

            }
        });



    }

    private void AssetsScan() {
        executor.execute(() -> {
       AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().setAssetFound(barcode,true, location );
      AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().setScannedFound(barcode ,true, location );

            assetslList.clear();
            assetslList.addAll(AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetsFound(true, location ));

            for (int i = 0 ; i< assetslList.size() ; i++ ) {
                scannedLocation = assetslList.get(i).getLocation();
                Des = assetslList.get(i).getDescription();
                String barcode = assetslList.get(i).getBarcode();


                Log.i("loc ", scannedLocation + "");
                Log.i("Des ", Des + "");
                Log.i("barcode ", barcode + "");

                Log.i("isScannedBefore ", assetslList.get(i).isScannedBefore() + "");

                Log.i("isFound ", assetslList.get(i).isFound() + "");


            }
            //Background work here
            handler.post(() -> {
                //UI Thread work here
                binding.barcodeEt.setText("");
                scanAdapter.notifyDataSetChanged();

            });
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getUnFoundItems(){
        scanAdapter.notifyDataSetChanged();

        executor.execute(() -> {
            assetsModelList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetsFound( false ,location);

            for (int i = 0 ; i< assetsModelList.size() ; i++ ) {
                String Des = assetsModelList.get(i).getDescription();
                assetsList.add(Des);
                Log.i("Item Des ", Des + "");
            }


            String[] arr = new String[assetsList.size()];

            // ArrayList to Array Conversion
            for (int i =0; i < assetsList.size(); i++) {
                arr[i] = assetsList.get(i);
            }


            //Background work here
            handler.post(() -> {
                //UI Thread work here

                // setup the alert builder
                androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(ScanLocationActivity.this, R.style.AlertDialogTheme)
                        .setTitle("Wait Items no Scanned Yet")
                       .setItems(arr, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("Continue Scan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
                        Intent intent = new Intent(ScanLocationActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).create();
                dialog.show();
                scanAdapter.notifyDataSetChanged();






            });
        });

    }

    private void InPlaceAssets(){
        executor.execute(() -> {
           List<AssetModel> assetLoc = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetByBarcode(barcode );

            for (int i = 0 ; i< assetLoc.size() ; i++ ) {
                 scannedLocation = assetLoc.get(i).getLocation();
                 Des = assetLoc.get(i).getDescription();
                String barcode = assetLoc.get(i).getBarcode();


                Log.i("loc ", scannedLocation + "");
                Log.i("Des ", Des + "");
                Log.i("barcode ", barcode + "");

                Log.i("isScannedBefore ", assetLoc.get(i).isScannedBefore() + "");

                Log.i("isFound ", assetLoc.get(i).isFound() + "");


            }


            //Background work here
            handler.post(() -> {
                //UI Thread work here
                binding.barcodeEt.setText("");
                if (location.equals(scannedLocation)){
                    Toast.makeText(getApplicationContext() , "scannedLocation :" + scannedLocation , Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getApplicationContext() , "scannedLocation  not in place:" + scannedLocation , Toast.LENGTH_LONG).show();



                    AlertDialog dialog = new MaterialAlertDialogBuilder(ScanLocationActivity.this, R.style.AlertDialogTheme)
                            .setTitle("this asset must be in " + scannedLocation)
                            .setMessage("Do you want to change its location? ")
                            .setPositiveButton("change location",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            executor.execute(() -> {

                                                final locationChange newLocation = new locationChange();
                                                newLocation.setFirstLocation(location);
                                                newLocation.setLastLocation(scannedLocation);
                                                newLocation.setBarcode(barcode);
                                                newLocation.setDescription(Des);
                                                  AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().insertAssetNewLocation(newLocation);


                                                  //update location in assets Model table
                                                AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().setAssetLocChanged(barcode , true ,location);



                                          List <locationChange>  locationChangeList =
                                                  AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllLocationChangedList();


//                                                for (int i = 0 ; i< locationChangeList.size() ; i++ ) {
//                                                    String firstLocation = locationChangeList.get(i).getFirstLocation();
//                                                     secondLocation = locationChangeList.get(i).getLastLocation();
//
//                                                    String des = locationChangeList.get(i).getDescription();
//                                                    String barcodeLC = locationChangeList.get(i).getBarcode();
//
//
//                                                    Log.i("loc  1st", firstLocation + "");
//                                                    Log.i("loc  1st", secondLocation + "");
//
//                                                    Log.i("Des ", des + "");
//                                                    Log.i("barcode ", barcodeLC + "");
//
//                                                }

                                                //Background work here
                                                handler.post(() -> {
                                                    //UI Thread work here

                                                    Toast.makeText(getApplicationContext() , "new Location is set" + secondLocation , Toast.LENGTH_LONG).show();

                                                });
                                            });



                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();

                }


            });
        });

    }


}