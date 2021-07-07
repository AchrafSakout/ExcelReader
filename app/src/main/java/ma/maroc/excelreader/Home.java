package ma.maroc.excelreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import maes.tech.intentanim.CustomIntent;

public class Home extends AppCompatActivity {
    final String TAG = "Home";
    //tableau des valeurs structurer sous la forme val,val,val...,
    ArrayList<String> tabValues = new ArrayList<>();
    //declaration des elements view
    EditText ET_val;
    EditText result1;
    EditText result2;
    Button find;
    Button autre;
    //declaration des variables
    String PATH;
    Uri lien;

    ArrayList<String> uploadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ET_val = findViewById(R.id.value);
        result1 = findViewById(R.id.et_X);
        result2 = findViewById(R.id.et_Y);
        find =(Button) findViewById(R.id.btn_chercher);
        autre =(Button) findViewById(R.id.autre);
        uploadData = new ArrayList<>();

        Intent myIntent = getIntent();
        Bundle b = myIntent.getExtras();
        if (b != null) {
            //PATH = (String) b.get("path");
            lien = (Uri) b.get("path");
            PATH = lien.getPath();
            //readExcelData(PATH);
            readExcelFileFromAssets(lien);
            Log.d(TAG, "onCreate: path : " + PATH);
        }
        find.setOnClickListener(v -> {
            if(!ET_val.getText().toString().isEmpty()){
                findPosValue(ET_val.getText().toString());
            }else
            {
                Toast.makeText(Home.this,"Insérer la valeur à chercher ! \uD83E\uDD14",Toast.LENGTH_LONG).show();
                result1.setText("");
                result2.setText("");
            }

        });
        autre.setOnClickListener(v -> {
            startActivity(new Intent(Home.this,MainActivity.class));
            finish();
        });
    }

    private void readExcelFileFromAssets(final Uri filePath) {
        try {
            //  open excel sheet
            InputStream myInput = getContentResolver().openInputStream(filePath);
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                String value = "";
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();

                    value = value.concat(myCell.toString() + ",");
                    Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                }
                tabValues.add(value.replace("\n",""));
            }
            Log.e(TAG, " final results :");
            for (String str : tabValues) {
                Log.e(TAG, str);
            }
        } catch (Exception e) {
            Log.e(TAG, "error " + e.toString());
        }
    }
    private void findPosValue(String val){
        Boolean find = false;
        for(String str:tabValues){
            String[] tabCol = str.split(",");
            for(int i=0;i<tabCol.length;i++){
                if(tabCol[i].toLowerCase().contains(val.toLowerCase())){
                    result1.setText(tabCol[0].toString());
                    result2.setText(String.valueOf(i));
                    find=true;
                }
            }
        }
        if(!find){
            Toast.makeText(Home.this,"Cette valeur n'existe pas ! \uD83D\uDE10",Toast.LENGTH_LONG).show();
            result1.setText("");
            result2.setText("");
        }
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"fadein-to-fadeout");
    }
}