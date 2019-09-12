package com.example.pinghomeapp;

import androidx.appcompat.app.AppCompatActivity;
import java.net.*;
import java.io.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button conBtn = (Button) findViewById(R.id.conBtn);
        conBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView connStat = (TextView) findViewById(R.id.connStatText);
                boolean connection = connect("192.168.43.81", 5000);
                if (connection)
                {
                    connStat.setText("Connected");
                }else
                    {
                        connStat.setText("Disconnected");
                    }
            }
        });

    }

    private boolean connect(String ipaddress, int port)
    {
        try
        {
            Socket client = new Socket(ipaddress, port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            out.writeUTF("PING");
            if (in.readUTF().compareTo("PONG") == 0)
            {
                return true;
            } else
                {
                    return false;
                }
        } catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

    }
}
