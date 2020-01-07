package com.example.pinghome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryStatus {

    IntentFilter ifliter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent batteryStatus;
    private double batteryLevel = 0;
    private boolean charging;
    private String chargeMethod;
    public BatteryStatus(Context context){
        BroadcastReceiver mBatteryLevelReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
                BatteryStatus.this.batteryLevel = rawLevel * 100 / (double) scale;
                BatteryStatus.this.charging =
                        BatteryManager.BATTERY_STATUS_CHARGING ==
                        intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                BatteryStatus.this.chargeMethod = BatteryStatus.this.methodOfCharge(intent);
            }
        };
        batteryStatus = context.registerReceiver(mBatteryLevelReceiver, ifliter);
    }

    public double getBatteryPercentage(){
        return this.batteryLevel;
    }

    public boolean isCharging(){
        return this.charging;
    }

    public String getChargeMethod(){
        return chargeMethod;
    }

    private String methodOfCharge(Intent intent){
        int chargeMethod = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
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
