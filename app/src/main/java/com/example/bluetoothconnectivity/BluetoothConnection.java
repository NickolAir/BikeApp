package com.example.bluetoothconnectivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnection extends AppCompatActivity {
    private static final int REQUEST_BLUETOOTH_CONNECT_PERMISSION = 1;

    RecyclerView deviceRecyclerView;
    Button showDevicesBtn, pairBtn;
    TextView status;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] deviceArray;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    private static final String APP_NAME = "BikeApp";
    private static final UUID MY_UUID = UUID.fromString("a57710a8-2b9b-43e7-b938-ee753fa162c0");

    private List<BluetoothDeviceModel> deviceList = new ArrayList<>();
    private DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bluetooth_connection);

        pairBtn = findViewById(R.id.pairButton);
        showDevicesBtn = findViewById(R.id.pairedDevicesButton);
        deviceRecyclerView = findViewById(R.id.deviceRecyclerView);
        status = findViewById(R.id.status);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(BluetoothConnection.this, "Bluetooth doesn't support", Toast.LENGTH_SHORT).show();
        }

        deviceAdapter = new DeviceAdapter(deviceList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        deviceRecyclerView.setLayoutManager(layoutManager);
        deviceRecyclerView.setAdapter(deviceAdapter);

        pairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerClass serverClass = new ServerClass();
                serverClass.start();
            }
        });

        showDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isEnabled()) {
                    if (ActivityCompat.checkSelfPermission(BluetoothConnection.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(BluetoothConnection.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_PERMISSION);
                        return;
                    } else {
                        getBluetoothPermission();
                    }

                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    deviceArray = new BluetoothDevice[devices.size()];
                    int index = 0;
                    for (BluetoothDevice device : devices) {
                        deviceArray[index] = device;
                        index++;
                        BluetoothDeviceModel deviceModel = new BluetoothDeviceModel(device.getName(), device.getAddress());
                        deviceList.add(deviceModel);
                    }
                    deviceAdapter.notifyDataSetChanged();
                } else {
                    showToast("Turn On Bluetooth to get paired devices");
                }
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case STATE_LISTENING:
                    status.setText("listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("failed");
                    break;
                case STATE_MESSAGE_RECEIVED:

                    //write later

                    break;
            }
            return true;
        }
    });

    private class ServerClass extends Thread {
        private BluetoothServerSocket serverSocket;

        public ServerClass() {
            try {
                if (ActivityCompat.checkSelfPermission(BluetoothConnection.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BluetoothConnection.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_PERMISSION);
                    return;
                } else {
                    getBluetoothPermission();
                }
                serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            BluetoothSocket socket = null;
            while (socket == null) {
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if (socket != null) {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);

                    //code for send/receive

                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1) {
            device = device1;
            try {
                if (ActivityCompat.checkSelfPermission(BluetoothConnection.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BluetoothConnection.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_PERMISSION);
                    return;
                } else {
                    getBluetoothPermission();
                }
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                if (ActivityCompat.checkSelfPermission(BluetoothConnection.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BluetoothConnection.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_PERMISSION);
                    return;
                } else {
                    getBluetoothPermission();
                }
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);

//                sendReceive=new SendReceive(socket);
//                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

/*    private class DeviceAdapter extends RecyclerView.Adapter<com.example.bluetoothconnectivity.DeviceAdapter.DeviceViewHolder> {

        private List<BluetoothDeviceModel> devices;

        public DeviceAdapter(List<BluetoothDeviceModel> devices) {
            this.devices = devices;
        }

        @NonNull
        @Override
        public com.example.bluetoothconnectivity.DeviceAdapter.DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_device, parent, false);
            return new com.example.bluetoothconnectivity.DeviceAdapter.DeviceViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.bluetoothconnectivity.DeviceAdapter.DeviceViewHolder holder, int position) {
            BluetoothDeviceModel deviceModel = devices.get(position);
            holder.deviceNameTextView.setText(deviceModel.getDeviceName() + "\n" + deviceModel.getDeviceAddress());
        }

        @Override
        public int getItemCount() {
            return devices.size();
        }

        public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView deviceNameTextView;

            public DeviceViewHolder(View itemView) {
                super(itemView);
                deviceNameTextView = itemView.findViewById(R.id.deviceName);

                // Установка обработчика клика для элемента списка
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BluetoothDeviceModel clickedDevice = devices.get(position);

                    // Здесь можно создать и запустить новую активность,
                    // передавая информацию о выбранном устройстве (например, имя и MAC-адрес)
                    Intent intent = new Intent(itemView.getContext(), DeviceExtra.class);
                    intent.putExtra("deviceName", clickedDevice.getDeviceName());
                    itemView.getContext().startActivity(intent);

                    ClientClass clientClass = new ClientClass(deviceArray[position]);
                    clientClass.start();
                    status.setText("Connecting");
                }
            }
        }
    }*/

    private void getBluetoothPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_CONNECT)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to show devices")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(BluetoothConnection.this,
                                    new String[] {Manifest.permission.BLUETOOTH_CONNECT},
                                    REQUEST_BLUETOOTH_CONNECT_PERMISSION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_CONNECT_PERMISSION);
        }
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}