package com.nehad.assettrackingapp.UI.ExportFilesActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;

import com.nehad.assettrackingapp.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class ExportSheetsActivity extends AppCompatActivity {


//    https://www.youtube.com/watch?v=MSgJJzhSI-A
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_sheets);
/////////////////////////////////////////////////////
        String path = Environment.getExternalStorageDirectory() + "/" + "Movies" + "/";
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(uri, "*/*");
        startActivity(intent);

    }




}

