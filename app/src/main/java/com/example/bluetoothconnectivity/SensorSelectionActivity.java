package com.example.bluetoothconnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SensorSelectionActivity extends AppCompatActivity {

    private CheckBox speedometerCheckbox;
    private CheckBox pressureMeterCheckbox;
    private CheckBox heartRateMonitorCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_selection);

        speedometerCheckbox = findViewById(R.id.speedometerCheckbox);
        pressureMeterCheckbox = findViewById(R.id.pressureMeterCheckbox);
        heartRateMonitorCheckbox = findViewById(R.id.heartRateMonitorCheckbox);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Получаем данные о выбранных датчиках
                boolean hasSpeedometer = speedometerCheckbox.isChecked();
                boolean hasPressureMeter = pressureMeterCheckbox.isChecked();
                boolean hasHeartRateMonitor = heartRateMonitorCheckbox.isChecked();

                // Создаем объект BikeData с данными о выбранных датчиках
                BikeData bikeData = new BikeData();
                bikeData.setHasSpeedometer(hasSpeedometer);
                bikeData.setHasPressureMeter(hasPressureMeter);
                bikeData.setHasHeartRateMonitor(hasHeartRateMonitor);

                // Получаем переданный из предыдущей активности выбранный радиус колеса
                Intent intent = getIntent();
                int selectedRadius = intent.getIntExtra("selectedRadius", 20); // Значение по умолчанию, если данные не были переданы

                bikeData.setWheelRadius(selectedRadius);

                // Преобразуем BikeData в JSON строку с помощью Gson
                Gson gson = new Gson();
                String jsonData = gson.toJson(bikeData);

                // Сохраняем JSON строку в файл
                saveJsonToFile("bike_data.json", jsonData);

                // Завершаем активность и переходим на активность BluetoothConnection
                Intent bluetoothIntent = new Intent(SensorSelectionActivity.this, BluetoothConnection.class);
                startActivity(bluetoothIntent);
                finish();
            }
        });
    }

    private void saveJsonToFile(String fileName, String jsonData) {
        try {
            File file = new File(getFilesDir(), fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(jsonData.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}