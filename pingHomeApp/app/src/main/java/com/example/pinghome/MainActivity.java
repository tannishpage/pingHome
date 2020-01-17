package com.example.pinghome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public TextView tv;
    public TextView tv2;
    public TextView tv3;
    public TextView tv4;
    public TextView tv5;
    public Thread t = new Thread();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.CYAN));
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" +
                getString(R.string.app_name) + "</font>"));
        tv = (TextView) findViewById(R.id.tv);
        tv2 =(TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        Button but = (Button) findViewById(R.id.click);
        //t = this.startGNSSProcess(tv, tv2, tv3, tv4, tv5);
        but.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button but = (Button) view.findViewById(R.id.click);
                if (!(t.isAlive())){
                    but.setText("Disconnect");
                    t = startGNSSProcess(tv, tv2, tv3, tv4, tv5);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_setting){
            ListPopupWindow listPopupWindow = new ListPopupWindow(this);
            listPopupWindow.setModal(true);
            listPopupWindow.setAnchorView(toolbar.findViewById(R.id.action_setting));
            listPopupWindow.setWidth(500);
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent settingsPage = new Intent(MainActivity.this,
                            SettingsActivity.class);
                    startActivity(settingsPage);
                }
            });
            listPopupWindow.setAdapter(new MyAdapter(this,
                    com.example.pinghome.Menu.getMenus()));
            listPopupWindow.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Thread startGNSSProcess(final TextView lat, final TextView lon, final TextView batPer,
                                    final TextView batCharge, final TextView batChargeSource){
        final Handler screenHandler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                SharedPreferences sp =
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                final GNSSStatus gps = new GNSSStatus(
                        (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                        (Context) MainActivity.this,
                        (Activity) MainActivity.this);
                final BatteryStatus bs = new BatteryStatus(MainActivity.this);
                final NetworkHandler nh = NetworkHandler.getInstance();
                final StringBuffer data = new StringBuffer();
                int timeElapsed = 0;
                sp.registerOnSharedPreferenceChangeListener(
                        new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
                        //TODO Write some code here to update the settings variables or map
                        switch (key){
                            case "interval":
                                nh.setSendInterval(sharedPreferences.getInt(key, 60));
                                break;
                            case "sendLocation":
                                gps.setSend(sharedPreferences.getBoolean(key, true));
                                break;
                            case "sendBatInfo":
                                bs.setSend(sharedPreferences.getBoolean(key, true));
                                break;
                            case "address":
                                nh.disconnect();
                                nh.connect(sharedPreferences.getString(key, null), 5001);
                        }
                    }
                });

                while (true){
                    System.out.println("Working?");
                    final ArrayList<Double> coordinates = gps.getCurrentCoordinates();
                    final double batteryPercentage = bs.getBatteryPercentage();
                    final boolean isCharging = bs.isCharging();
                    final String chargeMethod = bs.getChargeMethod();

                    data.append(String.format("%s        ", Calendar.getInstance().getTime().toString()));
                    if (gps.isSend()){
                        data.append(String.format("Coordinates: %s, ", String.format("%s, %s",
                                coordinates.get(0).toString(),
                                coordinates.get(1).toString())));
                    }
                    if(bs.isSend()){
                        data.append(String.format("Battery Percentage: %s, ",
                                Double.toString(batteryPercentage)));
                        data.append(String.format("Is Charging: %s, ", Boolean.toString(isCharging)));
                        data.append(String.format("Method Of Charge: %s\n", chargeMethod));
                    }
                    screenHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            lat.setText(String.format("%.5f", coordinates.get(0)));
                            lon.setText(String.format("%.5f", coordinates.get(1)));
                            batPer.setText(String.format("%.0f%s", batteryPercentage, "%"));
                            batCharge.setText(String.format("%b", isCharging).toUpperCase());
                            if (isCharging){
                                batCharge.setTextColor(Color.GREEN);
                            } else{
                                batCharge.setTextColor(Color.RED);
                            }
                            batChargeSource.setText(chargeMethod);
                        }
                    });
                    if (timeElapsed >= nh.getSendInterval()){
                        timeElapsed = 0;
                        nh.send(data);
                        data.delete(0, data.length()-1);
                    }
                    try{
                        Thread.sleep(1000);
                        timeElapsed += 1;

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
