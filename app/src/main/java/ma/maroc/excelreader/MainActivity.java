package ma.maroc.excelreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
    final int CHOOSE_XLS_FROM_DEVICE = 1001;
    final String TAG = "MainActivity";
    Button btn_import;
    Button btn_valid;
    String path;
    TextView textView;
    Uri lien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.mytext);
        btn_valid = (Button) findViewById(R.id.btnValider);
        btn_valid.setOnClickListener(v -> {
            if(lien!=null){
                Intent i = new Intent(MainActivity.this, Home.class);
                i.putExtra("path", lien);
                startActivity(i);
                finish();
            }else
            {
                Toast.makeText(MainActivity.this,"Veuillez sélectionner un fichier excel ! \uD83D\uDE42",Toast.LENGTH_LONG).show();
            }

        });
        btn_import = (Button) findViewById(R.id.btnImporter);
        btn_import.setOnClickListener(v -> {
            openFile(lien);
        });
    }
    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //.xlsx     application/vnd.openxmlformats-officedocument.spreadsheetml.sheet       ->FOR EXCEL FILE
        //.xls      application/vnd.ms-excel                                                ->FOR EXCEL FILE
        String[] extraMimeTypes ={"application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
        startActivityForResult(intent, CHOOSE_XLS_FROM_DEVICE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent resultData){
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CHOOSE_XLS_FROM_DEVICE && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Toast.makeText(MainActivity.this,"Fichier accepté \uD83D\uDC4D",Toast.LENGTH_SHORT).show();
            if (resultData != null) {
                lien = resultData.getData();
                Log.d(TAG,"onActivityResult: Uri = " +lien.getPath());
                path=lien.getPath();
                // Perform operations on the document using its URI.
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"fadein-to-fadeout");
    }
}