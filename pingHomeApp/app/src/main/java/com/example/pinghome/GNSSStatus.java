package com.example.pinghome;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Arrays;

public class GNSSStatus implements LocationListener{

    private LocationManager locationManager;
    private Location gps_loc;
    private Location network_loc;
    private Location final_loc;
    private Context context;
    private Activity activity;
    private double latitude, longitude;
    private boolean send = true;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public GNSSStatus(LocationManager locationManager, Context context, Activity activity) {
        this.locationManager = locationManager;
        this.context = context;
        this.activity = activity;
        try{
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private boolean checkPerm(){
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private Location getGPSLocation() {
        try{
            return this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e){
            e.printStackTrace();
            return null;
        }

    }

    private Location getNetworkLocation(){
        try{
            return this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException e){
            e.printStackTrace();
            return null;
        }
    }

    public void updateLocation(){
        this.gps_loc = this.getGPSLocation();
        this.network_loc = this.getNetworkLocation();

        if (gps_loc != null){
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else if(network_loc != null){
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else{
            latitude = 0.0;
            longitude = 0.0;
        }
    }

    public ArrayList<Double> getCurrentCoordinates(){
        this.updateLocation();
        if (this.checkPerm()){
            Toast.makeText(this.context, "You need to allow location services for this app",
                    Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<Double>(Arrays.asList(latitude,longitude));
    }


    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }


    @Override
    public void onLocationChanged(Location location) {
        this.updateLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this.context, "Location Provider enabled",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this.context, "Location Provider disabled",
                Toast.LENGTH_SHORT).show();
    }
}
