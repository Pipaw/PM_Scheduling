package com.example.paolo.pm_scheduling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QrScanner extends AppCompatActivity {

    SurfaceView cameraView;
    BarcodeDetector mBarcodeDetector;
    CameraSource mCameraSource;
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        cameraView = findViewById(R.id.cameraView);
        cameraView.setZOrderMediaOverlay(true);
        mSurfaceHolder = cameraView.getHolder();
        mBarcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        if(!mBarcodeDetector.isOperational()){
            Toast.makeText(getApplicationContext(), "Sorry, couldn't setup detector",Toast.LENGTH_LONG).show();
            this.finish();
        }
        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector).setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920,1024).build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(ContextCompat.checkSelfPermission(QrScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        mCameraSource.start(cameraView.getHolder());
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });


        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size()> 0){
                    Intent intent = new Intent();
                    intent.putExtra("Barcode", barcodes.valueAt(0));
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }
        });

    }
}