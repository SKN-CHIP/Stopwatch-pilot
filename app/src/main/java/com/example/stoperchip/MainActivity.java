package com.example.stoperchip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.InputFilter;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    Button blt_connect, reset_counter, send_time;
    ImageButton led1, led2, led3, led4, led5, led6, led7, led8;
    SwitchCompat auto_flag;
    EditText minutes, seconds;
    SendDataClass SendData = new SendDataClass();

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o == null || o.getResultCode() != RESULT_OK)
                return;

            if(o.getData() == null || o.getData().getStringExtra("result") == null)
                return;

            ImageButton led_color_set = null;

            switch (SendData.getData1()){
                case 1:
                    led_color_set = led1;
                    break;
                case 2:
                    led_color_set = led2;
                    break;
                case 3:
                    led_color_set = led3;
                    break;
                case 4:
                    led_color_set = led4;
                    break;
                case 5:
                    led_color_set = led5;
                    break;
                case 6:
                    led_color_set = led6;
                    break;
                case 7:
                    led_color_set = led7;
                    break;
                case 8:
                    led_color_set = led8;
                    break;
                default:
                    break;
            }
            if (led_color_set == null)
                return;

            switch (o.getData().getStringExtra("result")){
                case "red":
                    led_color_set.setBackgroundResource(R.drawable.ledred);
                    break;

                case "blue":
                    led_color_set.setBackgroundResource(R.drawable.ledblue);
                    break;

                case "purple":
                    led_color_set.setBackgroundResource(R.drawable.ledpurple);
                    break;

                case "orange":
                    led_color_set.setBackgroundResource(R.drawable.ledorange);
                    break;

                case "white":
                    led_color_set.setBackgroundResource(R.drawable.ledwhite);
                    break;

                case "green":
                    led_color_set.setBackgroundResource(R.drawable.ledgreen);
                    break;

                case "off":
                    led_color_set.setBackgroundResource(R.drawable.led);
                    break;

                default:
                    break;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        blt_connect = findViewById(R.id.bluetooth_button);
        reset_counter = findViewById(R.id.reset_button);
        send_time = findViewById(R.id.send_time_button);
        led1 = findViewById(R.id.led1);
        led2 = findViewById(R.id.led2);
        led3 = findViewById(R.id.led3);
        led4 = findViewById(R.id.led4);
        led5 = findViewById(R.id.led5);
        led6 = findViewById(R.id.led6);
        led7 = findViewById(R.id.led7);
        led8 = findViewById(R.id.led8);
        auto_flag = findViewById(R.id.switch_auto_flag);
        minutes = findViewById(R.id.minutes);
        seconds = findViewById(R.id.seconds);
        minutes.setFilters(new InputFilter[]{ new MinMaxFilter("00", "99")});
        seconds.setFilters(new InputFilter[]{ new MinMaxFilter("00", "59")});

        blt_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        reset_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        send_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        led1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setDiodeAddress(1);
                Intent Led = new Intent(MainActivity.this, LedColorsActivity.class);
                Led.putExtra("SendObjectToLed", SendData);
                startForResult.launch(Led);
            }
        });

        led2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setDiodeAddress(2);
                Intent Led = new Intent(MainActivity.this, LedColorsActivity.class);
                Led.putExtra("SendObjectToLed", SendData);
                startForResult.launch(Led);
            }
        });

        led3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setDiodeAddress(3);
                Intent Led = new Intent(MainActivity.this, LedColorsActivity.class);
                Led.putExtra("SendObjectToLed", SendData);
                startForResult.launch(Led);
            }
        });

        led4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setDiodeAddress(4);
                Intent Led = new Intent(MainActivity.this, LedColorsActivity.class);
                Led.putExtra("SendObjectToLed", SendData);
                startForResult.launch(Led);
            }
        });

        led5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setDiodeAddress(5);
                Intent Led = new Intent(MainActivity.this, LedColorsActivity.class);
                Led.putExtra("SendObjectToLed", SendData);
                startForResult.launch(Led);
            }
        });

        led6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setDiodeAddress(6);
                Intent Led = new Intent(MainActivity.this, LedColorsActivity.class);
                Led.putExtra("SendObjectToLed", SendData);
                startForResult.launch(Led);
            }
        });

        led7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setDiodeAddress(7);
                Intent Led = new Intent(MainActivity.this, LedColorsActivity.class);
                Led.putExtra("SendObjectToLed", SendData);
                startForResult.launch(Led);
            }
        });

        led8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData.setDiodeAddress(8);
                Intent Led = new Intent(MainActivity.this, LedColorsActivity.class);
                Led.putExtra("SendObjectToLed", SendData);
                startForResult.launch(Led);
            }
        });

        auto_flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    led1.setBackgroundResource(R.drawable.ledgreen);
                }
                else {
                    led1.setBackgroundResource(R.drawable.ledpurple);
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}