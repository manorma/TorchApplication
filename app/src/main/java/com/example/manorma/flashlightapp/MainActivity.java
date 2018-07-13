package com.example.manorma.flashlightapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 0;
    private ImageView ivFlashImage;
    private Button toggleBtn;
    private boolean flashLightStatus = false;
    boolean hasCameraFlash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivFlashImage = (ImageView) findViewById(R.id.ivFlashImage);
        toggleBtn =(Button) findViewById(R.id.btnFlashStatus);
        toggleBtn.setOnClickListener(clickListener);
        hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isEnabled = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;
            if(isEnabled){
                turnOnOffTorch();

            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

            }
        }
    };

    private void turnOnOffTorch() {
        if(hasCameraFlash){
            if(flashLightStatus){
                flashLightOff();
            }
            else {
                flashLightOn();
            }
        }
        else {
            Toast.makeText(MainActivity.this, "No flash available on your device",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
            ivFlashImage.setImageResource(R.drawable.ic_highlight_green);
            toggleBtn.setBackgroundColor(getResources().getColor(R.color.green));
        } catch (CameraAccessException e) {
            Log.e("MainActivity","Exception: "+e);

        }
    }


    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
            ivFlashImage.setImageResource(R.drawable.ic_highlight_red);
            toggleBtn.setBackgroundColor(getResources().getColor(R.color.red));
        } catch (CameraAccessException e) {
            Log.e("MainActivity","Exception: "+e);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    turnOnOffTorch();
                }
                else {
                    Toast.makeText(MainActivity.this, "Permission Denied for the Camera",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
