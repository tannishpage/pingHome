package com.example.pinghomeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.net.*;
import java.io.*;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public TextView textEdit;
    public Thread connThread = new Thread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button conBtn = (Button) findViewById(R.id.conBtn);
        Button disBtn = (Button) findViewById(R.id.disBtn);
        textEdit = (TextView) findViewById(R.id.connStatText);
        conBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView connStat = (TextView) findViewById(R.id.connStatText);
                if (!(connThread.isAlive())){
                    connThread = testConnect("192.168.0.37", 5000);
                }
            }
        });

        disBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                textEdit.setText("Disconnected");
                connThread.interrupt();
            }
        });

        getSupportActionBar().setTitle("Ping Home");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Thread testConnect(final String ipaddress, final int port)
    {
        final Handler handler = new Handler();
        boolean status;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run(){
                int retryCount = 0;
                while (true){

                    try
                    {
                        Socket client = new Socket(ipaddress, port);
                        OutputStream outToServer = client.getOutputStream();
                        DataOutputStream out = new DataOutputStream(outToServer);
                        BufferedReader input = new BufferedReader(
                                new InputStreamReader(client.getInputStream()));


                        out.writeUTF("PING");
                        if (input.readLine().compareTo("PONG") == 0)
                        {
                            retryCount = 0;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textEdit.setText("Connected");
                                }
                            });
                            System.out.println("Yay, it works");
                        } else
                        {
                            System.out.println("No, it doesn't work");
                        }
                    } catch(IOException e)
                    {
                        retryCount += 1;
                        if (retryCount >= 100){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textEdit.setText("Connection Timed Out");
                                }
                            });
                            break;
                        }
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textEdit.setText("Retrying...");
                            }
                        });
                    }
                    try{
                    Thread.sleep(10000);
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
