package com.nehad.assettrackingapp.UI.ExportFilesActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class ExportFilesActivity extends AppCompatActivity {

    private ActivityExportFilesBinding binding;
    public static final String TAG = ExportFilesActivity.class.getSimpleName();

    private Context mContext;
    private int FILE_SELECTOR_CODE = 10000;
    private int DIR_SELECTOR_CODE = 20000;
    List<Map<Integer, Object>> writeExcelList = new ArrayList<>();
    List<Map<Integer, Object>> list = null;



    String location = null;
    // create a list
    List<TableHeader> tableHeaderList  = new ArrayList<TableHeader>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_export_files);

        binding = ActivityExportFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String location = intent.getStringExtra("loc_key");
        Log.i(TAG, "Location in scan" + location);



        // add the member of list
        tableHeaderList.add(new TableHeader(1, "barcode"));
        tableHeaderList.add(new TableHeader(2, "description"));
        tableHeaderList.add(new TableHeader(3, "location"));
        tableHeaderList.add(new TableHeader(4, "Found"));


        // create map with the help of Object (stu) method create object of Map class
        Map<Integer, String> map = new HashMap<>();

        // put every value list to Map
        for (TableHeader th : tableHeaderList) {
            map.put(th.getId() , th.getHeaderName());
        }


        Log.i(TAG  , map +"");


        Map<Integer, Object> list = new HashMap<>();


            new Thread(() -> {
                Log.i(TAG, "doInBackground: hash  Table ...");


                List<AssetModel> assetsList = new ArrayList<>();
                assetsList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();
                //   Log.i("DatabaseSize", assetModelList.get(0).getLocation() + "");


                // put every value list to Map
                for (int i = 0; i < assetsList.size(); i++) {
                    list.put(i, assetsList.get(i));

                    Log.i(TAG, list.get(i) + "");

                }


                Log.i("Hiiiiiii Hash ", list + "");

                for (Object  i : list.values()) {
                    System.out.println(i);
                    Log.i( "values" , i + "");

                }



//                for (int j = 0 ; j< list.size(); j++){
//
//                    Log.i("From Hash ", list.get(j).toString() + "");
//                    Log.i("From Hash ", list.get(j) + "");
//
//
//
//                }


            }).start();


                    binding.locReportBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


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

