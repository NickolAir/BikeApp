package com.example.bluetoothconnectivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private static List<BluetoothDeviceModel> devices;

    public DeviceAdapter(List<BluetoothDeviceModel> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        BluetoothDeviceModel deviceModel = devices.get(position);
        holder.deviceNameTextView.setText(deviceModel.getDeviceName() + "\n" + deviceModel.getDeviceAddress());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            }
        }
    }
}