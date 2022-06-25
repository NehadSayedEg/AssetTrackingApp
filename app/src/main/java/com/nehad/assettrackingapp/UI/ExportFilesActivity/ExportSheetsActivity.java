package com.nehad.assettrackingapp.UI.ExportFilesActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.R;
import com.nehad.assettrackingapp.UI.AssetsLocation.AssetsLocationActivity;
import com.nehad.assettrackingapp.Util.ChangeLocationExcelUtils;
import com.nehad.assettrackingapp.Util.ExportExcelUtil;
import com.nehad.assettrackingapp.Util.FoundExcelUtil;
import com.nehad.assettrackingapp.Util.MissingExcelUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class ExportSheetsActivity extends AppCompatActivity {
    Context context ;
    private List<AssetModel> assetModelList = new ArrayList<>();

    String FolderName ="/AssetTracking/";
    private  static final int PERMISSION_REQUEST_CODE = 7;

    private File filePath = new File(Environment.getExternalStorageDirectory() + "/AssetTracking"+"/Demo.xlsx");
    //  String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aSSET/";
    // File file = new File(path);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_sheets);
        context = this;
        Dialog();

        new Thread(() -> {
            assetModelList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();
        }).start();


    }


    private void askPermission() {

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                createDirectory(FolderName);
            }else
            {
                Toast.makeText(ExportSheetsActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createDirectory(String folderName) {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),folderName);
        // String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aSSET/";
        //   File file = new File(path);
        if (!file.exists()){

            file.mkdir();

            Toast.makeText(ExportSheetsActivity.this,getString(R.string.folderCreatedToast),Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(ExportSheetsActivity.this,getString(R.string.folderExistToast),Toast.LENGTH_SHORT).show();
        }

        createExcelSheet();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createExcelSheet() {
        ExportExcelUtil.exportDataIntoWorkbook(context,"AssetTracking.xls" ,assetModelList );
        ChangeLocationExcelUtils.exportDataIntoWorkbook(context,"ChangeLocation .xls" ,assetModelList );
        MissingExcelUtil.exportDataIntoWorkbook(context,"Missing .xls" ,assetModelList );
        FoundExcelUtil.exportDataIntoWorkbook(context,"Found .xls" ,assetModelList );



        String path = Environment.getExternalStorageDirectory().toString()+"/AssetTracking";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
//        for (int i = 0; i < files.length; i++)
//        {
//            Log.d("Files", "FileName:" + files[i].getName());
//            Log.d("Files", "FileName:" + files[i].getPath());
//
//        }
        Intent intent = new Intent(ExportSheetsActivity.this , AssetsLocationActivity.class);
        startActivity(intent);

    }


    private void Dialog() {
        AlertDialog dialog = new MaterialAlertDialogBuilder(ExportSheetsActivity.this, R.style.AlertDialogTheme).setTitle(R.string.dialogExportTitle)
                .setMessage(R.string.dialogExportMessage)
                .setPositiveButton(R.string.alertExportPostiveBtn,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ContextCompat.checkSelfPermission(ExportSheetsActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                                    createDirectory(FolderName);

                                }else
                                {
                                    askPermission();
                                }


                            }
                        }).setNegativeButton(R.string.alertExportCancelBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(ExportSheetsActivity.this, AssetsLocationActivity.class);
                        startActivity(intent);
                        finish();                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

    }



}


