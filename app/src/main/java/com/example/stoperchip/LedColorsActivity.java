package com.example.stoperchip;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;


public class LedColorsActivity extends AppCompatActivity implements Serializable {

    Button return_button, led_red, led_blue, led_orange, led_white, led_purple, led_off, led_green;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.led_colors_activity);

        return_button = findViewById(R.id.button_return);
        led_red = findViewById(R.id.button_led_red);
        led_blue = findViewById(R.id.button_led_blue);
        led_orange = findViewById(R.id.button_led_orange);
        led_purple = findViewById(R.id.button_led_purple);
        led_white = findViewById(R.id.button_led_white);
        led_off = findViewById(R.id.button_led_off);
        led_green = findViewById(R.id.button_led_green);

        SendDataClass SendData = (SendDataClass) getIntent().getSerializableExtra("SendObjectToLed");
        if(SendData == null)
            return;

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        led_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setData2(2);
                //blt send
                String color = "red";
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", color);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        led_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setData2(0);
                //blt send
                String color = "off";
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", color);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        led_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setData2(1);
                //blt send
                String color = "white";
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", color);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        led_purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setData2(6);
                //blt send
                String color = "purple";
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", color);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        led_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setData2(5);
                //blt send
                String color = "blue";
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", color);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        led_orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setData2(3);
                //blt send
                String color = "orange";
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", color);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        led_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setData2(4);
                //blt send
                String color = "green";
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", color);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }

}
