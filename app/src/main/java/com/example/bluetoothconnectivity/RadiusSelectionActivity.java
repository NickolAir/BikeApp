package com.example.bluetoothconnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class RadiusSelectionActivity extends AppCompatActivity {

    private Spinner radiusSpinner;
    LottieAnimationView lottieWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_radius_selection);

        lottieWheel = findViewById(R.id.lottie_wheel);
        radiusSpinner = findViewById(R.id.radiusSpinner);

        List<String> radiusValues = new ArrayList<>();
        for (int i = 20; i <= 40; i++) {
            radiusValues.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, radiusValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radiusSpinner.setAdapter(adapter);

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadius = Integer.parseInt(radiusSpinner.getSelectedItem().toString());
                // Здесь можно сохранить выбранное значение радиуса, например, в SharedPreferences
                // Или передать дальше в следующую активность через Intent.putExtra()

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RadiusSelectionActivity.this, SensorSelectionActivity.class);
                        intent.putExtra("selectedRadius", selectedRadius);
                        startActivity(intent);
                    }
                }, 1000);

                lottieWheel.animate().translationX(2000).setDuration(2000).setStartDelay(0);
            }
        });
    }
}
