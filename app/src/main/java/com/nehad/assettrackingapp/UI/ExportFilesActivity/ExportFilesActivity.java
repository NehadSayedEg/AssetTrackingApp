package com.nehad.assettrackingapp.UI.ExportFilesActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.Model.TableHeader;
import com.nehad.assettrackingapp.R;
import com.nehad.assettrackingapp.UI.AssetsLocation.AssetsLocationActivity;
import com.nehad.assettrackingapp.UI.MainActivity;
import com.nehad.assettrackingapp.UI.ScanLocation.ScanLocationActivity;
import com.nehad.assettrackingapp.Util.ExcelUtil;
import com.nehad.assettrackingapp.Util.ExportExcelReport;
import com.nehad.assettrackingapp.databinding.ActivityAssetsLocationBinding;
import com.nehad.assettrackingapp.databinding.ActivityExportFilesBinding;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class ExportFilesActivity extends AppCompatActivity  {

    private ActivityExportFilesBinding binding;
    public static final String TAG = ExportFilesActivity.class.getSimpleName();

    private Context mContext;
    private int FILE_SELECTOR_CODE = 10000;
    private int DIR_SELECTOR_CODE = 20000;
    List<Map<Integer, Object>> list = null;
    private List<Map<Integer, Object>> readExcelList = new ArrayList<>();
    List<Map<Integer, Object>> ExcelList = new ArrayList<>();

    // Request code for creating a PDF document.
    private static final int CREATE_FILE = 1;
    String location = null;
    // create a list
    List<TableHeader> tableHeaderList  = new ArrayList<TableHeader>();
    private Activity activity;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_export_files);

        binding = ActivityExportFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String location = intent.getStringExtra("loc_key");
        Log.i(TAG, "Location in scan" + location);


        ActivityCompat.requestPermissions(this , new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

//
//        // add the member of list
//        tableHeaderList.add(new TableHeader(0, "barcode"));
//        tableHeaderList.add(new TableHeader(1, "description"));
//        tableHeaderList.add(new TableHeader(2, "location"));
//        tableHeaderList.add(new TableHeader(3, "Found"));
//
//
//        // create map with the help of Object (stu) method create object of Map class
//        Map<Integer, Object> headerMap = new HashMap<>();
//
//        // put every value list to Map
//        for (TableHeader th : tableHeaderList) {
//            headerMap.put(th.getId() , th.getHeaderName());
//        }
//
//
//        Log.i(TAG +" Map  header " , headerMap +"");
//
//        List<Map<Integer, Object>> ExcelList = new ArrayList<>();
//
//
//        ExcelList.add(headerMap);
//
//
//            new Thread(() -> {
//                Log.i(TAG, "doInBackground: hash  Table ...");
//
//               // Map<String, Object> itemMap = new HashMap<>();
//                Map<Integer, Object> itemMap = new HashMap<>();
//
//
//                List<AssetModel> assetsList = new ArrayList<>();
//                assetsList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();
//                //   Log.i("DatabaseSize", assetModelList.get(0).getLocation() + "");
//
//
//                // put every value list to Map
//                for (int i = 0; i < assetsList.size(); i++) {
//
//                       int  key = 0;
//                       //String  key = "barcode";
//                       String value =   assetsList.get(i).getBarcode();
//                       itemMap.put(key, value);
//
//                       int  keyDes = 1;
//                       // String  keyDes = "description";
//                        String valueDes =   assetsList.get(i).getDescription();
//                        itemMap.put(keyDes, valueDes);
//
//                         int  keyLoc = 2;
//                        //String  keyLoc = "location";
//                        String valueLoc =   assetsList.get(i).getLocation();
//                        itemMap.put(keyLoc, valueLoc);
//
//                       int  keyFound = 3;
//                       //String  keyFound = "Found";
//                       String valueFound = String.valueOf(assetsList.get(i).isFound());
//                       itemMap.put(keyFound, valueFound);
//
//                    Log.i(TAG, "item Map Size: " + itemMap.size());
//                    Log.i(TAG, "item Map  " + itemMap);
//
//                    ExcelList.add(itemMap);
//                    Log.i( "Hash List values" , ExcelList.get(i)+ "");
//                    Log.i( "Hash List size " , ExcelList.size()+ "");
//
//
//                }
//
//
//
//            }).start();


                    binding.locReportBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            openFolderSelector();

                        }
                    });

                    binding.allAssetsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });

                    binding.exportFoundBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });


                    binding.missingBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //onWriteClick();


                        }
                    });

                }

//
//    private void createFile(Uri pickerInitialUri) {
//        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/pdf");
//        intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");
//
//        // Optionally, specify a URI for the directory that should be opened in
//        // the system file picker when your app creates the document.
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
//
//        startActivityForResult(intent, CREATE_FILE);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
//        super.onActivityResult(requestCode, resultCode, resultData);
//        if (requestCode == DIR_SELECTOR_CODE && resultCode == Activity.RESULT_OK) {
//            // The result data contains a URI for the document or directory that
//            // the user selected.
//            Uri uri = null;
//            if (resultData != null) {
//                uri = resultData.getData();
//                createFile(uri);
//                // Perform operations on the document using its URI.
//            }
//        }
//    }



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
       if (requestCode == DIR_SELECTOR_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            if (uri == null) return;
            Log.i(TAG, "onActivityResult: " + "filePath：" + uri.getPath());
            Toast.makeText(mContext, "Exporting...", Toast.LENGTH_SHORT).show();
            //you can modify readExcelList, then write to excel.
           ExportExcelReport.getAllAssetsData(getApplicationContext());
           Toast.makeText(mContext, "Exporting22...", Toast.LENGTH_SHORT).show();

           getAllAssetsData(this);
           ExportExcelReport.writeAllAssetsReport(this, ExcelList, uri);
        }
    }
    public static List<Map<Integer, Object>> getAllAssetsData(Context context) {

        // create a list
        List<TableHeader> tableHeaderList  = new ArrayList<TableHeader>();

        // add the member of list
        tableHeaderList.add(new TableHeader(0, "barcode"));
        tableHeaderList.add(new TableHeader(1, "description"));
        tableHeaderList.add(new TableHeader(2, "location"));
        tableHeaderList.add(new TableHeader(3, "Found"));


        // create map with the help of Object (stu) method create object of Map class
        Map<Integer, Object> headerMap = new HashMap<>();

        // put every value list to Map
        for (TableHeader th : tableHeaderList) {
            headerMap.put(th.getId() , th.getHeaderName());
        }
        Log.i(TAG +" Map  header " , headerMap +"");

        List<Map<Integer, Object>> ExcelList = new ArrayList<>();


        ExcelList.add(headerMap);


        new Thread(() -> {
            Log.i(TAG, "doInBackground: hash  Table ...");

            // Map<String, Object> itemMap = new HashMap<>();
            Map<Integer, Object> itemMap = new HashMap<>();


            List<AssetModel> assetsList = new ArrayList<>();
            assetsList = AssetsDatabase.getAssetsDatabase(context.getApplicationContext()).assetsDao().getAllAssets();


            // put every value list to Map
            for (int i = 0; i < assetsList.size(); i++) {

                int  key = 0;
                //String  key = "barcode";
                String value =   assetsList.get(i).getBarcode();
                itemMap.put(key, value);

                int  keyDes = 1;
                // String  keyDes = "description";
                String valueDes =   assetsList.get(i).getDescription();
                itemMap.put(keyDes, valueDes);

                int  keyLoc = 2;
                //String  keyLoc = "location";
                String valueLoc =   assetsList.get(i).getLocation();
                itemMap.put(keyLoc, valueLoc);

                int  keyFound = 3;
                //String  keyFound = "Found";
                String valueFound = String.valueOf(assetsList.get(i).isFound());
                itemMap.put(keyFound, valueFound);

                Log.i(TAG, "item Map Size: " + itemMap.size());
                Log.i(TAG, "item Map  " + itemMap);

                ExcelList.add(itemMap);
                Log.i( "Hash List values" , ExcelList.get(i)+ "");
                Log.i( "Hash List size " , ExcelList.size()+ "");


            }



        }).start();
        return ExcelList;

    }


}



//    public void onWriteClick() {
//        Log.e( TAG , "writing xlsx file");
//
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("mysheet"));
//        for (int i=0;i<10;i++) {
//            Row row = sheet.createRow(i);
//            Cell cell = row.createCell(0);
//            cell.setCellValue(i);
//        }
//        String outFileName = "filetoshare.xlsx";
//        try {
//            Log.e( TAG , "writing file " + outFileName);
//
//            File cacheDir = getCacheDir();
//            File outFile = new File(cacheDir, outFileName);
//            OutputStream outputStream = new FileOutputStream(outFile.getAbsolutePath());
//            workbook.write(outputStream);
//            outputStream.flush();
//            outputStream.close();
//            Log.e( TAG , "sharing file...");
//
//            share(outFileName, getApplicationContext());
//        } catch (Exception e) {
//            /* proper exception handling to be here */
//            Log.e( TAG , e.toString());
//
//        }
//    }

//    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
//        String value = "";
//        try {
//            Cell cell = row.getCell(c);
//            CellValue cellValue = formulaEvaluator.evaluate(cell);
//            switch (cellValue.getCellType()) {
//                case Cell.CELL_TYPE_BOOLEAN:
//                    value = ""+cellValue.getBooleanValue();
//                    break;
//                case Cell.CELL_TYPE_NUMERIC:
//                    double numericValue = cellValue.getNumberValue();
//                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
//                        double date = cellValue.getNumberValue();
//                        SimpleDateFormat formatter =
//                                new SimpleDateFormat("dd/MM/yy");
//                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
//                    } else {
//                        value = ""+numericValue;
//                    }
//                    break;
//                case Cell.CELL_TYPE_STRING:
//                    value = ""+cellValue.getStringValue();
//                    break;
//                default:
//            }
//        } catch (NullPointerException e) {
//            /* proper error handling should be here */
//            Log.e( "Error :" , e.toString());
//        }
//        return value;
//    }
//
//
//
//    public void share(String fileName, Context context) {
//        Uri fileUri = Uri.parse("content://"+getPackageName()+"/"+fileName);
//        Log.e( "sending"  , fileUri.toString()+" ...");
//
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
//        shareIntent.setType("application/octet-stream");
//        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
//    }

