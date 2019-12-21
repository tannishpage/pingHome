package com.example.pinghome;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryStatus {

    IntentFilter ifliter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent batteryStatus;
    public BatteryStatus(Context context){
        batteryStatus = context.registerReceiver(null, ifliter);
    }

    public double getBatteryPercentage(){
        int batteryLevel = this.batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = this.batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
        return batteryLevel * 100 / (double) scale;
    }

    public boolean isCharging(){
        int status = this.batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING;
    }

    public String methodOfCharge(){
        int chargeMethod = this.batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        switch (chargeMethod){
            case BatteryManager.BATTERY_PLUGGED_AC:
                return "AC";

            case BatteryManager.BATTERY_PLUGGED_USB:
                return "USB";

            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                return "WIRELESS";

            default:
                return "UNKNOWN";
        }
    }
}
