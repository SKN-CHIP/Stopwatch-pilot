package com.example.stoperchip;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

import java.io.Serializable;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Serializable {
    final String TAG = "MainTAG";
    private ImageButton led1, led2, led3, led4, led5, led6, led7, led8;
    private final SendDataClass SendData = new SendDataClass();
    private final BluetoothAdapter blt_adapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice blt_device;
    private boolean connection = false;
    SerialSocket socket;
    Handler handler = new Handler();
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            //Connection established
            if (status == BluetoothGatt.GATT_SUCCESS
                    && newState == BluetoothProfile.STATE_CONNECTED) {
                MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, "Connecting...", Toast.LENGTH_SHORT).show());
                gatt.discoverServices();

            } else if (status == BluetoothGatt.GATT_SUCCESS
                    && newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "onConnectionStateChange: Disconnected");
                Toast.makeText(getApplicationContext(), "Bluetooth disconnected", Toast.LENGTH_LONG).show();
                //Handle a disconnect event
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered status = " + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                socket = new SerialSocket(getApplicationContext(), blt_device);
                socket.connectCharacteristics1(gatt);
                socket.setGatt(gatt);
                handler.postDelayed(() -> {
                    MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show());
                    connection = true;
                }, 1000);

            }
            else {
                Log.d(TAG, "onServicesDiscovered received: " + status);
            }

        }
    };

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o == null || o.getResultCode() != RESULT_OK)
                return;

            if(!connection){
                MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, "Bluetooth not connected!", Toast.LENGTH_SHORT).show());
                return;
            }

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

            switch (Objects.requireNonNull(o.getData().getStringExtra("result"))){
                case "red":
                    led_color_set.setBackgroundResource(R.drawable.ledred);
                    SendData.setData2(2);
                    break;

                case "blue":
                    led_color_set.setBackgroundResource(R.drawable.ledblue);
                    SendData.setData2(5);
                    break;

                case "purple":
                    led_color_set.setBackgroundResource(R.drawable.ledpurple);
                    SendData.setData2(6);
                    break;

                case "orange":
                    led_color_set.setBackgroundResource(R.drawable.ledorange);
                    SendData.setData2(3);
                    break;

                case "white":
                    led_color_set.setBackgroundResource(R.drawable.ledwhite);
                    SendData.setData2(1);
                    break;

                case "green":
                    led_color_set.setBackgroundResource(R.drawable.ledgreen);
                    SendData.setData2(4);
                    break;

                case "off":
                    led_color_set.setBackgroundResource(R.drawable.led);
                    SendData.setData2(0);
                    break;

                default:
                    break;
            }
            socket.send_data(SendData.create_sending_object());
        }
    });
    ActivityResultLauncher<Intent> bluetoothResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @SuppressLint("MissingPermission")
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o == null || o.getResultCode() != 100)
                return;
            if(o.getData() == null)
                return;
            String device_address = o.getData().getStringExtra("blt_device");
            blt_device = blt_adapter.getRemoteDevice(device_address);
            blt_device.connectGatt(getApplicationContext(), true, mGattCallback);
        }
    });

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button blt_connect = findViewById(R.id.bluetooth_button);
        Button reset_counter = findViewById(R.id.reset_button);
        Button send_time = findViewById(R.id.send_time_button);
        led1 = findViewById(R.id.led1);
        led2 = findViewById(R.id.led2);
        led3 = findViewById(R.id.led3);
        led4 = findViewById(R.id.led4);
        led5 = findViewById(R.id.led5);
        led6 = findViewById(R.id.led6);
        led7 = findViewById(R.id.led7);
        led8 = findViewById(R.id.led8);
        SwitchCompat auto_flag = findViewById(R.id.switch_auto_flag);
        EditText minutes = findViewById(R.id.minutes);
        EditText seconds = findViewById(R.id.seconds);
        minutes.setFilters(new InputFilter[]{ new MinMaxFilter("00", "99")});
        seconds.setFilters(new InputFilter[]{ new MinMaxFilter("00", "59")});

        blt_connect.setOnClickListener(v -> {
            if(connection){
                socket.disconnect();
                connection = false;
                Toast.makeText(getApplicationContext(), "Disconnecting", Toast.LENGTH_LONG).show();
            }
            if(blt_adapter == null){
                Toast.makeText(this, "Bluetooth not available on this device", Toast.LENGTH_LONG).show();
                return;
            }
            if(!blt_adapter.isEnabled()){
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
                return;
            }
            Intent Blt = new Intent(MainActivity.this, BluetoothActivity.class);
            bluetoothResult.launch(Blt);
        });

        reset_counter.setOnClickListener(v -> {
            if(connection){
                SendData.setAddress(1);
                SendData.setData1(0);
                SendData.setData2(0);
                socket.send_data(SendData.create_sending_object());
            }
            else
                Toast.makeText(this, "Bluetooth not connected!", Toast.LENGTH_LONG).show();
        });

        send_time.setOnClickListener(v -> {
            if(!connection){
                Toast.makeText(this, "Bluetooth not connected!", Toast.LENGTH_LONG).show();
                return;
            }
            String minutes_string = minutes.getText().toString();
            String seconds_string = seconds.getText().toString();
            if(minutes_string.isEmpty())
                minutes_string = "0";
            if(seconds_string.isEmpty())
                seconds_string = "0";
            int minutes_value = Integer.parseInt(minutes_string);
            int seconds_value = Integer.parseInt(seconds_string);
            if(minutes_value == 0 && seconds_value < 5){
                Toast.makeText(this, "Set longer time!", Toast.LENGTH_LONG).show();
                return;
            }
            SendData.setAddress(1);
            SendData.setData1(minutes_value);
            SendData.setData2(seconds_value);
            socket.send_data(SendData.create_sending_object());
        });

        led1.setOnClickListener(v -> led_activity(1));

        led2.setOnClickListener(v -> led_activity(2));

        led3.setOnClickListener(v -> led_activity(3));

        led4.setOnClickListener(v -> led_activity(4));

        led5.setOnClickListener(v -> led_activity(5));

        led6.setOnClickListener(v -> led_activity(6));

        led7.setOnClickListener(v -> led_activity(7));

        led8.setOnClickListener(v -> led_activity(8));

        auto_flag.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(!connection){
                Toast.makeText(this, "Bluetooth not connected!", Toast.LENGTH_LONG).show();
                return;
            }
            if (isChecked) {
                SendData.setAddress(3);
                SendData.setData1(1);
                SendData.setData2(1);
                socket.send_data(SendData.create_sending_object());
            }
            else {
                SendData.setAddress(3);
                SendData.setData1(1);
                SendData.setData2(0);
                socket.send_data(SendData.create_sending_object());
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void led_activity(int led_address){
        SendData.setDiodeAddress(led_address);
        Intent Led = new Intent(MainActivity.this, LedColorsActivity.class);
        Led.putExtra("SendObjectToLed", SendData);
        startForResult.launch(Led);
    }

}