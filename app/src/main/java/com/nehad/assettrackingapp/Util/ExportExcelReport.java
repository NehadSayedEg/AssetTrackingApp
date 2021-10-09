package com.nehad.assettrackingapp.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.Model.TableHeader;
import com.nehad.assettrackingapp.databinding.ActivityExportFilesBinding;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportExcelReport {
    private static final String TAG = ExportExcelReport.class.getSimpleName();




    // create a list
    public static List<Map<Integer, Object>> getAllAssetsData(Context context) {

        List<Map<Integer, Object>> list = null;


        // create a list
        List<TableHeader> tableHeaderList  = new ArrayList<TableHeader>();

        list = new ArrayList<>();


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


    public static void writeAllAssetsReport(Context context, List<Map<Integer, Object>> ExcelList, Uri uri) {



        // create map with the help of Object (stu) method create object of Map class
        Map<Integer, String> mapHeader = new HashMap<>();


        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("Sheet1"));

            int colums = mapHeader.size();
            for (int i = 0; i < colums; i++) {
                //set the cell default width to 15 characters
                sheet.setColumnWidth(i, 15 * 256);
            }

            for (int i = 0; i < ExcelList.size(); i++) {
                Row row = sheet.createRow(i);
                Map<Integer, Object> integerObjectMap = ExcelList.get(i);
                for (int j = 0; j < colums; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(String.valueOf(integerObjectMap.get(j)));
                }
            }

            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            Log.i(TAG, "writeExcel: export successful");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "writeExcel: error" + e);
        }
    }


    public static void writeExcelNew(Context context, List<Map<Integer, Object>> exportExcel, Uri uri) {
        getAllAssetsData(context);
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("Sheet1"));

            int colums = exportExcel.get(0).size();
            for (int i = 0; i < colums; i++) {
                //set the cell default width to 15 characters
                sheet.setColumnWidth(i, 15 * 256);
            }

            for (int i = 0; i < exportExcel.size(); i++) {
                Row row = sheet.createRow(i);
                Map<Integer, Object> integerObjectMap = exportExcel.get(i);
                for (int j = 0; j < colums; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(String.valueOf(integerObjectMap.get(j)));
                }
            }
            FileOutputStream fileOut = new FileOutputStream("excelFileName");

            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            workbook.write(outputStream);
            // workbook.write(fileOut);

            outputStream.flush();
            outputStream.close();
            Log.i(TAG, "writeExcel: export successful");
            Toast.makeText(context, "export successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "writeExcel: error" + e);
            Toast.makeText(context, "export error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * get single cell data
     *
     * @param cell </>
     * @return cell
     */
    private static Object getCellFormatValue(Cell cell) {
        Object cellValue;
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    cellValue = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    // determine if the cell is in date format
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // Convert to date format YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        // Numeric
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }
}
