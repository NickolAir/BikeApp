package com.example.bluetoothconnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SensorSelectionActivity extends AppCompatActivity {

    private CheckBox speedometerCheckbox, pressureMeterCheckbox, heartRateMonitorCheckbox;
    private EditText speedometerEditText, pressureMeterEditText, heartRateMonitorEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sensor_selection);

        speedometerCheckbox = findViewById(R.id.speedometerCheckbox);
        pressureMeterCheckbox = findViewById(R.id.pressureMeterCheckbox);
        heartRateMonitorCheckbox = findViewById(R.id.heartRateMonitorCheckbox);

        speedometerEditText = findViewById(R.id.speedometerEditText);
        pressureMeterEditText = findViewById(R.id.pressureMeterEditText);
        heartRateMonitorEditText = findViewById(R.id.heartRateMonitorEditText);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Получаем данные о выбранных датчиках
                boolean hasSpeedometer = speedometerCheckbox.isChecked();
                boolean hasPressureMeter = pressureMeterCheckbox.isChecked();
                boolean hasHeartRateMonitor = heartRateMonitorCheckbox.isChecked();

                // Получаем данные из полей ввода
                String speedometerText = speedometerEditText.getText().toString();
                String pressureMeterText = pressureMeterEditText.getText().toString();
                String heartRateMonitorText = heartRateMonitorEditText.getText().toString();

                // Создаем объект BikeData с данными о выбранных датчиках
                BikeData bikeData = new BikeData();
                bikeData.setSpeedometer(hasSpeedometer, speedometerText);
                bikeData.setPressureMeter(hasPressureMeter, pressureMeterText);
                bikeData.setHeartRateMonitor(hasHeartRateMonitor, heartRateMonitorText);

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