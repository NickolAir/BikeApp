package com.example.bluetoothconnectivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BluetoothConnection extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;

    TextView deviceList;
    Button pairBtn;
    BluetoothAdapter bluetoothAdapter;

    Intent btEnablingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);

        pairBtn = findViewById(R.id.pairedDevicesButton);
        deviceList = findViewById(R.id.pairedDevices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        if (bluetoothAdapter == null) {
            Toast.makeText(BluetoothConnection.this, "Bluetooth doesn't support", Toast.LENGTH_SHORT).show();
        }

        pairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bluetoothAdapter.isEnabled()) {
                    deviceList.setText("Paired Devices");
                    if (ActivityCompat.checkSelfPermission(BluetoothConnection.this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    for (BluetoothDevice device: devices){
                        deviceList.append("\nDevice: " + device.getName() + "," + device);
                    }
                }else{
                    showToast("Turn On Bluetooth to get paired devices");
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode==RESULT_OK){
                    showToast("Bluetooth is ON");
                }else{
                    showToast("Bluetooth is OFF");
                }
                break;
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}