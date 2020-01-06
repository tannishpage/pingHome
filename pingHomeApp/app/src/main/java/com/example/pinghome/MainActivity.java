package com.example.pinghome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    TextView tv;
    TextView tv2;
    GPSStatus gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        Button click = findViewById(R.id.click);
        this.tv = (TextView) findViewById(R.id.tv);
        this.tv2 = (TextView) findViewById(R.id.tv2);
        gps = new GPSStatus(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                (Context) MainActivity.this,
                (Activity) MainActivity.this);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Double> coordinates = gps.getCurrentCoordinates();
                tv.setText(Double.toString(coordinates.get(0)));
                tv2.setText(Double.toString(coordinates.get(1)));

            }
        });
    }
}
