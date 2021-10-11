package com.nehad.assettrackingapp.UI.ScanLocation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

import static java.lang.String.valueOf;

public class ScanLocationActivity extends AppCompatActivity {
    ScanLocationViewModel scanLocationViewModel;

    private ActivityScanLocationBinding binding;
    public static final String TAG = ScanLocationActivity.class.getSimpleName();

    List<AssetModel> assetslList  = new ArrayList<>();

    List<AssetModel> scannedAssetList  = new ArrayList<>();

    private ScanAdapter  scanAdapter ;
    Activity activity ;
    Context context ;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    private final Handler handlerTextWacher = new Handler();

    String barcode ;
    String location;
    String scannedLocation ;
    String Des ;
    String secondLocation ;
    List<String> assetsList = new LinkedList<String>();
    private List<AssetModel> assetsModelList = new ArrayList<>();
    List<AssetModel> assetLoc ;
    String newloc ;
    String scannedBeforeLoc ;
    boolean isScannedBefore ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScanLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        scanLocationViewModel = new ViewModelProvider(this).get(ScanLocationViewModel.class);

        //  get passed the location key
        Intent intent = getIntent();
         location = intent.getStringExtra("loc_key");
        Log.i(TAG, "Location in scan" + location);


        // Recyclerview & Adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        binding.scanRecyclerView.setHasFixedSize(true);
        binding.scanRecyclerView.setLayoutManager(layoutManager);
        binding.scanRecyclerView.scrollToPosition(0);
//        scanAdapter = new ScanAdapter(assetslList ,activity  ,context);
//        binding.scanRecyclerView.setAdapter(scanAdapter);


        setAssetsAdapter();


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
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
               // if( s != null  ) {
                    if(! s.toString().isEmpty()){

                        handlerTextWacher.removeCallbacksAndMessages(null);
                    handlerTextWacher.postDelayed(() -> {
                        Log.e("" , s.toString());
                         barcode = s.toString();
                         Log.e("barcode" ,  barcode);

                        CheckScannedBefore();
                        AssetScannedBefore();
                    }, 1000);

                    //barcode =   binding.barcodeEt.getText().toString();

                }


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

    private void CheckScannedBefore() {
        Log.e(" ENTER cHECK", "");
    }


    private void setAssetsAdapter(){

        scannedAssetList.clear();
            scanLocationViewModel.getAllAssetLiveData().observe(this, new Observer<List<AssetModel>>() {
                @Override
                public void onChanged(List<AssetModel> assetModels) {
                    Log.i("DatabaseSize locations ", assetModels.size() + " live Data");
                    scannedAssetList.clear();

                    for (int i = 0; i < assetModels.size(); i++) {
                        String loc = assetModels.get(i).getLocation();
                        Boolean isFound = assetModels.get(i).isFound();
                        if (location.equals(loc) && isFound == true){
                            Log.i("the same location "  + loc , "");


                                scannedAssetList.add(assetModels.get(i));
                                Log.i("scanned List" + scannedAssetList.size() , "");

                                scanAdapter = new ScanAdapter(scannedAssetList ,activity  ,context);
                                binding.scanRecyclerView.setAdapter(scanAdapter);

                            Log.i("location" + loc , "");
                            Log.i("found" + isFound , "");

                        }





                    }
                }
            });


    }


    private void AssetsScan() {
        executor.execute(() -> {
        //    set scanned asset found &  scannedBefore
       AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().setAssetFound(barcode,true, location );
      AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().setScannedFound(barcode ,true, location );
            //Background work here
            handler.post(() -> {
                //UI Thread work here
                binding.barcodeEt.setText("");
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

             assetLoc = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetByBarcode(barcode );
            for (int i = 0 ; i< assetLoc.size() ; i++ ) {
                 scannedLocation = assetLoc.get(i).getLocation();
                 Des = assetLoc.get(i).getDescription();
                String barcode = assetLoc.get(i).getBarcode();
//                Log.i("loc ", scannedLocation + "");
//                Log.i("Des ", Des + "");
//                Log.i("barcode ", barcode + "");
//
//                Log.i("isScannedBefore ", assetLoc.get(i).isScannedBefore() + "");
//
//                Log.i("isFound ", assetLoc.get(i).isFound() + "");
            }
            assetLoc.clear();

            //Background work here
          handler.post(() -> {
                //UI Thread work here
                binding.barcodeEt.setText("");

              if( scannedLocation == null){
                  Log.e("barcode", " not include database ");

                  AlertDialog dialog = new MaterialAlertDialogBuilder(ScanLocationActivity.this,
                          R.style.AlertDialogTheme).setTitle(R.string.deleteTitle)
                          .setMessage("This asset barcode not include inserted data ")
                          .setPositiveButton("Ok",
                                  new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int which) {
                                          dialog.dismiss();                                      }
                          }).create();
                  dialog.show();


              }else if (location.equals(scannedLocation)){
                    Toast.makeText(getApplicationContext() , getString(R.string.inlocation) + scannedLocation , Toast.LENGTH_LONG).show();

                }

              else {

                    Toast.makeText(getApplicationContext() , getString(R.string.wronglocation) + scannedLocation , Toast.LENGTH_LONG).show();


              AlertDialog dialog = new MaterialAlertDialogBuilder(ScanLocationActivity.this, R.style.AlertDialogTheme)
                            .setTitle(getString(R.string.alertTitle) + scannedLocation)
                            .setMessage(getString(R.string.alertmessage))
                            .setPositiveButton(getString(R.string.alertPostiveBtn),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.e("Postive btn", "is clicked");

                                            executor.execute(() -> {

                                                final locationChange newLocation = new locationChange();
                                                newLocation.setFirstLocation(location);
                                                newLocation.setLastLocation(scannedLocation);
                                                newLocation.setBarcode(barcode);
                                                newLocation.setDescription(Des);
                                                  AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().insertAssetNewLocation(newLocation);


                                                  //update location in assets Model table
                                                AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().setAssetLocChanged(barcode  , location);

                                                AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().setAssetLocFound(barcode  , true);
                                                AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().setScanned(barcode  , true);



                                                List<AssetModel> asset = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetByBarcode(barcode);


                                                for (int i = 0 ; i< asset.size() ; i++ ) {
                                                     newloc = asset.get(i).getLocation();
                                                   Log.i(" new Location Herer " , newloc);
                                                }

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

                                                    Toast.makeText(getApplicationContext() , getString(R.string.newLocToast) + newloc , Toast.LENGTH_LONG).show();

                                                });
                                            });

                                        }
                                    }).setNegativeButton(getString(R.string.alertCancelBtn), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e("before Dismiss", "");
                                   // dialog.dismiss();
                                    dialog.cancel();
                                    Log.e("After Dismiss", "");

                                }
                            }).create();
                    dialog.show();

                }
            });
        });

    }



    private void AssetScannedBefore(){
        Log.e(" enter method ","AssetScannedBefore");

        executor.execute(() -> {


            String scannedBeforeDes ;
            boolean isFound ;

            List <AssetModel>  scannedBeforeList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetByBarcode(barcode );
            for (int i = 0 ; i< scannedBeforeList.size() ; i++ ) {
                scannedBeforeLoc = scannedBeforeList.get(i).getLocation();
                scannedBeforeDes = scannedBeforeList.get(i).getDescription();
                String barcode = scannedBeforeList.get(i).getBarcode();
                isFound = scannedBeforeList.get(i).isFound();
                isScannedBefore = scannedBeforeList.get(i).isScannedBefore();

                Log.i(" scannedBeforeList loc ", scannedLocation + "");
                Log.i(" scannedBeforeList Des ", scannedBeforeDes + "");
                Log.i(" scannedBefore code ", barcode + "");

                Log.i("isScannedBefore ",  isScannedBefore + "");

                Log.i("isFound ", isFound + "");


            }
            scannedBeforeList.clear();

            //Background work here
            handler.post(() -> {
                //UI Thread work here
                binding.barcodeEt.setText("");
                String isScannedString = valueOf(isScannedBefore);
                Log.i("isScannedBeforeString  ",  isScannedBefore + "");

                if (isScannedString.equals("true") ){
                    Toast.makeText(getApplicationContext() , "" + scannedBeforeLoc , Toast.LENGTH_LONG).show();

                    // setup the alert builder
                    AlertDialog.Builder dialog = new MaterialAlertDialogBuilder(ScanLocationActivity.this, R.style.AlertDialogTheme);

                    dialog.setTitle(" ");
                    dialog.setMessage("This asset scanned before in." + scannedBeforeLoc);

                    // add a button
                    dialog.setPositiveButton("OK", null);

                    // create and show the alert dialog
                    dialog.create();
                    dialog.show();

                }else {
                    Log.i("Not Scanned Before   ",   "");
                    InPlaceAssets();
                    AssetsScan();

                }


            });
        });

    }
    private void AssetScannedBefore1(){
        Log.e(" enter method ","AssetScannedBefore");

        executor.execute(() -> {

            String scannedBeforeDes ;
            boolean isFound ;

          List <AssetModel>  scannedBeforeList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetByBarcode(barcode );
            for (int i = 0 ; i< scannedBeforeList.size() ; i++ ) {
                scannedBeforeLoc = scannedBeforeList.get(i).getLocation();
                scannedBeforeDes = scannedBeforeList.get(i).getDescription();
                String barcode = scannedBeforeList.get(i).getBarcode();
                isFound = scannedBeforeList.get(i).isFound();
                isScannedBefore = scannedBeforeList.get(i).isScannedBefore();

                Log.i(" scannedBeforeList loc ", scannedLocation + "");
                Log.i(" scannedBeforeList Des ", scannedBeforeDes + "");
                Log.i(" scannedBefore code ", barcode + "");

                Log.i("isScannedBefore ",  isScannedBefore + "");

                Log.i("isFound ", isFound + "");

                 if( isScannedBefore = true){

                     // setup the alert builder
//                     AlertDialog.Builder dialog = new MaterialAlertDialogBuilder(ScanLocationActivity.this, R.style.AlertDialogTheme);
//
//                     dialog.setTitle(" ");
//                     dialog.setMessage("This asset is scanned Before in"  + scannedLocation );
//
//                     // add a button
//                     dialog.setNegativeButton(" ok ", new DialogInterface.OnClickListener() {
//                         @Override
//                         public void onClick(DialogInterface dialog, int which) {
//                             dialog.cancel();
//                             dialog.dismiss();
//                         }
//                     });
//                     dialog.create();
//                     dialog.show();
                 }


            }
            scannedBeforeList.clear();

            //Background work here

          handler.post(() -> {
                //UI Thread work here
                binding.barcodeEt.setText("");

              String isScannedString = valueOf(isScannedBefore);
                Log.i("isScannedBeforeString  ",  isScannedBefore + "");

                if (isScannedString.equals("true") ){
                    Toast.makeText(getApplicationContext() , "" + scannedBeforeLoc , Toast.LENGTH_LONG).show();

                    // setup the alert builder
                    AlertDialog.Builder dialog = new MaterialAlertDialogBuilder(ScanLocationActivity.this, R.style.AlertDialogTheme);

                    dialog.setTitle(" ");
                    dialog.setMessage("This asset scanned before in." + scannedBeforeLoc);

                    // add a button
                    dialog.setPositiveButton("OK", null);

                    // create and show the alert dialog
                    dialog.create();
                    dialog.show();


                }else {
                    Log.i("Not Scanned Before   ",   "");
                    InPlaceAssets();
                    AssetsScan();

                }


            });
        });

    }




}