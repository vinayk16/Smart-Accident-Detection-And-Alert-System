package com.example.accident_detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_NAME = "example.txt";
    String filepath;
    String firstNum, bloodGrp, secondNum, thirdNum;
    public static String no1, no2, no3, bgrp;
    EditText txt1, txt2, txt3;
    EditText txtO;
    Button btsave, btlod, bton;
    double latitude,longitude;
    LocationFinder finder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt1 = findViewById(R.id.num1);
        txt2 = findViewById(R.id.num2);
        txt3 = findViewById(R.id.num3);
        txtO = findViewById(R.id.bloodgroup);
        btsave = findViewById(R.id.save);
        btlod = findViewById(R.id.load);

        filepath = "MyFileDir";
        


        btsave.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (txt1.getText() != null)
                    firstNum = txt1.getText().toString();
                if (txt2.getText() != null)
                    secondNum = txt2.getText().toString();
                if (txt3.getText() != null)
                    thirdNum = txt3.getText().toString();
                if (txtO.getText() != null)
                    bloodGrp = txtO.getText().toString();
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                    osw.append(firstNum);
                    osw.append("\n");
                    osw.append(secondNum);
                    osw.append("\n");
                    osw.append(thirdNum);
                    osw.append("\n");
                    osw.append(bloodGrp);
                    osw.close();
                    fos.close();
                    txt1.getText().clear();
                    txt2.getText().clear();
                    txt3.getText().clear();
                    txtO.getText().clear();
                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btlod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream fis = null;
                try {
                    fis = openFileInput(FILE_NAME);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    no1 = br.readLine();
                    no2 = br.readLine();
                    no3 = br.readLine();
                    bgrp = br.readLine();

                    br.close();
                    txt1.setText(no1.toString());
                    txt2.setText(no2.toString());
                    txt3.setText(no3.toString());
                    txtO.setText(bgrp.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);


        HamButton.Builder builder = new HamButton.Builder()
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent i = new Intent(getApplicationContext(), accService.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(i);
                        } else {
                            startService(i);
                        }
                    }
                })
                .highlightedTextRes(R.string.seron)
                .normalTextRes(R.string.seron)
                .normalImageRes(R.drawable.on);
        bmb.addBuilder(builder);

        HamButton.Builder builder1 = new HamButton.Builder()
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        finder = new LocationFinder(getApplicationContext());
                        if (finder.canGetLocation()) {
                            latitude = finder.getLatitude();
                            longitude = finder.getLongitude();

                        } else {
                            finder.showSettingsAlert();
                        }
                        SmsManager sm = SmsManager.getDefault();
                        sm.sendTextMessage(MainActivity.no1, null, "Help! My Location is  http://maps.google.com/?q="+String.valueOf(latitude)+","+String.valueOf(longitude)  , null, null);
                        sm.sendTextMessage(MainActivity.no2, null, "Help! My Location is  http://maps.google.com/?q="+String.valueOf(latitude)+","+String.valueOf(longitude)  , null, null);
                        sm.sendTextMessage(MainActivity.no3, null, "Help! My Location is  http://maps.google.com/?q="+String.valueOf(latitude)+","+String.valueOf(longitude)  , null, null);



                        Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();

                    }
                })
                .highlightedTextRes(R.string.womansafety)
                .normalTextRes(R.string.womansafety)
                .normalImageRes(R.drawable.woman);
        bmb.addBuilder(builder1);

        HamButton.Builder builder2 = new HamButton.Builder()
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent io = new Intent(getApplicationContext(), about.class);
                        startActivity(io);

                    }
                })
                .highlightedTextRes(R.string.abt)
                .normalTextRes(R.string.abt)
                .normalImageRes(R.drawable.abt);
        bmb.addBuilder(builder2);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.servicestop:
                Intent i = new Intent(this, accService.class);
                stopService(i);
                Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.exit:

                System.exit(1);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

   /* public void serviceactivate(View view) {
        Intent i = new Intent(this, accService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(i);
        } else {
            startService(i);
        }
    }*/
}