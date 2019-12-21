package com.example.pinghome;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BatteryStatus bs = new BatteryStatus(getApplicationContext());
        TextView x = (TextView) findViewById(R.id.tv);
        x.setText(Double.toString(bs.getBatteryPercentage()));
    }
}
