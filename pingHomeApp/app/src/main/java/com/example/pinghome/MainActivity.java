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
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public TextView tv;
    public TextView tv2;
    public TextView tv3;
    public Thread t = new Thread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        tv = (TextView) findViewById(R.id.tv);
        tv2 =(TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        Button but = (Button) findViewById(R.id.click);
        t = this.startGNSSProcess(tv, tv2, tv3);
        but.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button but = (Button) view.findViewById(R.id.click);
                if (!(t.isAlive())){
                    but.setText("Disconnect");
                    t = startGNSSProcess(tv, tv2, tv3);
                } else{
                    but.setText("Connect");
                    while (true){
                        t.interrupt();
                        if (!(t.isAlive())){
                            break;
                        }
                    }
                }
            }
        });
    }

    private Thread startGNSSProcess(final TextView lat, final TextView lon, final TextView batStat){
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                final ArrayList<ArrayList<Double>> coordinatesList = new ArrayList<>();
                GNSSStatus gps = new GNSSStatus(
                        (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                        (Context) MainActivity.this,
                        (Activity) MainActivity.this);
                final BatteryStatus bs = new BatteryStatus(MainActivity.this);

                while (true){
                    System.out.println("Working?");
                    ArrayList<Double> c = gps.getCurrentCoordinates();
                    if (!(coordinatesList.contains(c))){
                        coordinatesList.add(c);
                    }
                    System.out.println(coordinatesList.get(coordinatesList.size()-1));
                    System.out.println(coordinatesList);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Double> coordinates = coordinatesList.get(coordinatesList.size()-1);
                            lat.setText(Double.toString(coordinates.get(0)));
                            lon.setText(Double.toString(coordinates.get(1)));
                            batStat.setText(String.format("%.2f, %b, %s", bs.getBatteryPercentage(), bs.isCharging(), bs.getChargeMethod()));
                        }
                    });
                    try{
                        Thread.sleep(1500);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                        break;
                    }
                }

            }
        });
        thread.start();
        return thread;
    }

}
