package com.example.bluetoothconnectivity;

public class BikeData {
    private int wheelRadius;
    private boolean hasSpeedometer;
    private boolean hasPressureMeter;
    private boolean hasHeartRateMonitor;

    public void setHasSpeedometer(boolean hasSpeedometer) {
        this.hasSpeedometer = hasSpeedometer;
    }

    public void setHasPressureMeter(boolean hasPressureMeter) {
        this.hasPressureMeter = hasPressureMeter;
    }


    public void setHasHeartRateMonitor(boolean hasHeartRateMonitor) {
        this.hasHeartRateMonitor = hasHeartRateMonitor;
    }

    public void setWheelRadius(int selectedRadius) {
        this.wheelRadius = selectedRadius;
    }
}
