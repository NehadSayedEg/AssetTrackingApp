package com.nehad.assettrackingapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Util.ExcelUtil;
import com.nehad.assettrackingapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    public static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private int FILE_SELECTOR_CODE = 10000;
    private int DIR_SELECTOR_CODE = 20000;
    private List<Map<Integer, Object>> readExcelList = new ArrayList<>();
    private RecyclerView recyclerView;
   // private ExcelAdapter excelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.importFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileSelector();

            }
        });

        binding.HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetsDBSize();

            }
        });

        binding.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAssetsDB();

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


    /**
     * open local filer to select file
     */
    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        startActivityForResult(intent, FILE_SELECTOR_CODE);
    }

    /**
     * open the local filer and select the folder
     */
    private void openFolderSelector() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_TITLE,
                System.currentTimeMillis() + ".xlsx");
        startActivityForResult(intent, DIR_SELECTOR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECTOR_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) return;
            Log.i(TAG, "onActivityResult: " + "filePath：" + uri.getPath());
            //select file and import
            importExcelDeal(uri);
        } else if (requestCode == DIR_SELECTOR_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) return;
            Log.i(TAG, "onActivityResult: " + "filePath：" + uri.getPath());
            Toast.makeText(mContext, "Exporting...", Toast.LENGTH_SHORT).show();
            //you can modify readExcelList, then write to excel.
            ExcelUtil.writeExcelNew(this, readExcelList, uri);
        }
    }

    private void importExcelDeal(final Uri uri) {
        new Thread(() -> {
            Log.i(TAG, "doInBackground: Importing...");
            runOnUiThread(() -> Toast.makeText(mContext, "Importing...", Toast.LENGTH_SHORT).show());

            List<Map<Integer, Object>> readExcelNew = ExcelUtil.readExcelNew(mContext, uri, uri.getPath());

            Log.i(TAG, "onActivityResult:readExcelNew " + ((readExcelNew != null) ? readExcelNew.size() : ""));

            if (readExcelNew != null && readExcelNew.size() > 0) {
                readExcelList.clear();
                readExcelList.addAll(readExcelNew);
                goToHome();
                assetsDBSize();

                Log.i(TAG, "run: successfully imported");
                runOnUiThread(() -> Toast.makeText(mContext, "successfully imported", Toast.LENGTH_SHORT).show()

                );



            } else {
                runOnUiThread(() -> Toast.makeText(mContext, "no data", Toast.LENGTH_SHORT).show());
            }
        }).start();


    }



    /**
     *   go To Home Activity
     */
    private void goToHome() {
        runOnUiThread(() -> {

            Intent intent = new Intent(this , HomeActivity.class);
            startActivity(intent);

        });
    }

    /**
     * refresh RecyclerView
     */
    private void updateUI() {
        runOnUiThread(() -> {


            Intent intent = new Intent(this , HomeActivity.class);
                startActivity(intent);
//            if (readExcelList != null && readExcelList.size() > 0) {
//               // excelAdapter.notifyDataSetChanged();
//            }
        });
    }


    private void assetsDBSize() {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: check database size...");

         int size =  AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size();

         Log.i("DatabaseSize",
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size()
                            + "");

               if ( size > 0){
                   goToHome();               }{

                runOnUiThread(() -> Toast.makeText(mContext, "Insert Data file First ", Toast.LENGTH_SHORT).show());

            }

        }).start();

    }






    }