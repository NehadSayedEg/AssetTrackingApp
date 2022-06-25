package com.nehad.assettrackingapp.Util;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.Database.entities.locationChange;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



@RequiresApi(api = Build.VERSION_CODES.N)
public class ChangeLocationExcelUtils {
    // https://programmerworld.co/android/how-to-share-file-or-text-on-gmail-whatsapp-sms-bluetooth-from-your-android-app-share-button/
    public static final String TAG = "ExcelUtil";
    private static Cell cell;
    private static Sheet sheet;
    private static Workbook workbook;
    private static Row rowData ;
    private static CellStyle headerCellStyle;


    public static boolean exportDataIntoWorkbook(Context context, String fileName,
                                                 List<AssetModel> dataList) {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();

        // Creating a New Sheet and Setting width for each column
        sheet = workbook.createSheet("Change Location");
        sheet.setColumnWidth(0, (15 * 400));
        sheet.setColumnWidth(1, (15 * 400));
        sheet.setColumnWidth(2, (15 * 400));
        sheet.setColumnWidth(3, (15 * 400));
        sheet.setColumnWidth(4, (15 * 400));

        setHeaderRow();
        fillDataIntoExcel( context , dataList );



        // isWorkbookWrittenIntoStorage = storeExcelInStorage(context, fileName);
        return true;
    }

    private static boolean isExternalStorageReadOnly() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState);
    }

    /**Checks if Storage is Available*/
    private static boolean isExternalStorageAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }

    /**Setup header cell style */
    private static void setHeaderCellStyle() {
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER); }

    /** Setup Header Row */
    private static void setHeaderRow() {
        Row headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellValue("Barcode");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue("Description");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(2);
        cell.setCellValue("Last Location   ");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(3);

        cell.setCellValue(" First Location   ");
        cell.setCellStyle(headerCellStyle);
    }

    /**Fills Data into Excel Sheet*/
    private static void fillDataIntoExcel ( Context context ,  List<AssetModel> dataList) {


        new Thread(() -> {
            Log.i(TAG, "doInBackground:  grt data base size  database Table ...");
            List<locationChange> assetModelList = new ArrayList<>();

            assetModelList = AssetsDatabase.getAssetsDatabase(context.getApplicationContext()).assetsDao().getAllLocationChangedList();

            for (int i = 0; i < assetModelList.size(); i++) {
                Log.i(TAG, " index " + i);

                rowData = sheet.createRow(i + 1);
                // Create Cells for each row
                cell = rowData.createCell(0);
                cell.setCellValue(assetModelList.get(i).getBarcode());
                Log.i(TAG, " barcode cell " + assetModelList.get(i).getBarcode());

                cell = rowData.createCell(1);
                cell.setCellValue(assetModelList.get(i).getDescription());
                Log.i(TAG, " des cell " + assetModelList.get(i).getDescription());


                cell = rowData.createCell(2);
                cell.setCellValue(assetModelList.get(i).getFirstLocation());
                Log.i(TAG, " is found cell " + assetModelList.get(i).getFirstLocation());

                cell = rowData.createCell(3);
                cell.setCellValue(assetModelList.get(i).getLastLocation());
                Log.i(TAG, " is found cell " + assetModelList.get(i).getLastLocation());

                Log.i(TAG, " in  LOOP ");
            }
            Log.i(TAG, " OUT LOOP ");

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => "+c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
            String formattedDate = df.format(c.getTime());
            String FileName = "/"+ formattedDate +"LocationChange.xls";

            //   File file = new File(Environment.getExternalStorageDirectory() + "/AssetTracking" + "/changeLocation.xlsx");
            File file = new File(Environment.getExternalStorageDirectory() + "/AssetTracking" + FileName);


            FileOutputStream fileOutputStream = null;
            boolean success = false;
            try {
                fileOutputStream = new FileOutputStream(file);
                workbook.write(fileOutputStream);
                Log.w("FileUtils", "Writing file" + file);
                success = true;
            } catch (IOException e) {
                Log.w("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                Log.w("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != fileOutputStream)
                        fileOutputStream.close();
                } catch (Exception ex) {
                }
            }

            Log.w("FileUtils", "success " + success);



        }).start();

    }

    /**Store Excel Workbook in external storage*/
    private static boolean storeExcelInStorage(Context context, String fileName) {
        boolean isSuccess;
        //File file = new File(context.getExternalFilesDir(null), fileName);
        File file = new File(Environment.getExternalStorageDirectory() + "/AssetTracking"+"/changeLocation.xls");

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Log.e(TAG, "Writing file" + file);
            isSuccess = true;
        } catch (IOException e) {
            Log.e(TAG, "Error writing Exception: ", e);
            isSuccess = false;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save file due to Exception: ", e);

            isSuccess = false;
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return isSuccess;
    }




}

