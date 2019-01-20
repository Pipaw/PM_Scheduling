package com.example.paolo.pm_scheduling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    Button btnScan;
    Button ViewBtn;
    Button mButton;
    TextView textView;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_CODE = 200;

    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new java.util.Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.scanBtn);
        ViewBtn = findViewById(R.id.btnView);
        mButton = findViewById(R.id.btn);
        textView = findViewById(R.id.textView);

        //ito ay yung pagka load nag tatanong ng permission sa camera
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CODE);
        }

        //ito yung sa pag click ng Scan Button
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ito yung pag pasa ng value sa ibang activity
                Intent intent = new Intent(MainActivity.this, QrScanner.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        ViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Studname = "AGUSTIN, ROLLAN VICTOR PERILLO";
                String Studno = "201410057";
                String dates = date.toString();
                String remarks = "Present";
                insertData(Studno,Studname,dates,remarks);
            }
        });
    }

    @Override
    //ito yung sa result ng pag scan kung nabasa nya sa QR code
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("Barcode");
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(barcode.displayValue);
                        Toast.makeText(getApplicationContext(), "Code Scanned",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    /*

    For the time being and have many projects to finish this is a Hard Coded info of your student
    you can do what ever you want in here but in the future I will add some features to remove this
    hard coding


     */
    public void compareStudent(){
        //Compare your student info in here
    }

    public void insertData(String studNumber, String studName, String Date, String Remarks) {
        boolean insertData = mDatabaseHelper.insertData(studNumber,studName,Date, Remarks);
        if (insertData == true){
            Toast.makeText(MainActivity.this, "Data added", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this, "Data not added", Toast.LENGTH_SHORT).show();
        }
    }

}