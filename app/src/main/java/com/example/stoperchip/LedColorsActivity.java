package com.example.stoperchip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

        return_button.setOnClickListener(v -> finish());

        led_red.setOnClickListener(v -> {
            SendData.setData2(2);
            //blt send
            finish_led_activity("red");
        });

        led_off.setOnClickListener(v -> {
            SendData.setData2(0);
            //blt send
            finish_led_activity("off");
        });

        led_white.setOnClickListener(v -> {
            SendData.setData2(1);
            //blt send
            finish_led_activity("white");
        });

        led_purple.setOnClickListener(v -> {
            SendData.setData2(6);
            //blt send
            finish_led_activity("purple");
        });

        led_blue.setOnClickListener(v -> {
            SendData.setData2(5);
            //blt send
            finish_led_activity("blue");
        });

        led_orange.setOnClickListener(v -> {
            SendData.setData2(3);
            //blt send
            finish_led_activity("orange");
        });

        led_green.setOnClickListener(v -> {
            SendData.setData2(4);
            //blt send
            finish_led_activity("green");
        });

    }

    private void finish_led_activity(String color){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", color);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
