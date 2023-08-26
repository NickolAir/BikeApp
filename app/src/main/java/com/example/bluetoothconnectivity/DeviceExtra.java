package com.example.bluetoothconnectivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class DeviceExtra extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_device_extra);

        String deviceName = getIntent().getStringExtra("deviceName");
        TextView deviceNameTitle = findViewById(R.id.device);
        deviceNameTitle.setText(deviceName);

        ClientClass
    }


}