package com.example.stoperchip;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Serializable, SerialListener, ServiceConnection {
    final String TAG = "MainTAG";

    private enum Connected { False, Pending, True }
    private Connected connected = Connected.False;
    private Button blt_connect, reset_counter, send_time;
    private ImageButton led1, led2, led3, led4, led5, led6, led7, led8;
    private SwitchCompat auto_flag;
    private EditText minutes, seconds;
    private SendDataClass SendData = new SendDataClass();
    private BluetoothAdapter blt_adapter = BluetoothAdapter.getDefaultAdapter();
    private String device_address;
    private boolean initialStart = true;
    private SerialService service;

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

            switch (Objects.requireNonNull(o.getData().getStringExtra("result"))){
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
    ActivityResultLauncher<Intent> bluetoothResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o == null || o.getResultCode() != 100)
                return;
            if(o.getData() == null)
                return;
            device_address = o.getData().getStringExtra("blt_device");
            set_bluetooth_connection();
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

        blt_connect.setOnClickListener(v -> {
            if(blt_adapter == null){
                Log.d(TAG, "onCreate: Bluetooth not available");
                //show message "Bluetooth not available"
                return;
            }
            if(!blt_adapter.isEnabled()){
                Log.d(TAG, "onCreate: Bluetooth not enabled");
                //show message "Bluetooth not enabled"
                return;
            }
            Intent Blt = new Intent(MainActivity.this, BluetoothActivity.class);
            bluetoothResult.launch(Blt);
        });

        reset_counter.setOnClickListener(v -> {

        });

        send_time.setOnClickListener(v -> {

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

            if (isChecked) {
                SendData.setAddress(3);
                SendData.setData1(1);
                SendData.setData2(1);
                //bltsend
                led1.setBackgroundResource(R.drawable.ledgreen);
            }
            else {
                SendData.setAddress(3);
                SendData.setData1(1);
                SendData.setData2(0);
                led1.setBackgroundResource(R.drawable.ledpurple);
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

    @Override
    public void onSerialConnect() {
        connected = Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        Log.d(TAG, "onSerialConnectError: Disconnected");
        service.disconnect();
    }

    @Override
    public void onSerialIoError(Exception e) {
        Log.d(TAG, "onSerialIoError: Disconnected");
        service.disconnect();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        Log.d(TAG, "onServiceConnected: wchodzitu?");
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected: Disconnected");
    }

    @Override
    public void onBindingDied(ComponentName name) {
        ServiceConnection.super.onBindingDied(name);
    }

    @Override
    public void onNullBinding(ComponentName name) {
        ServiceConnection.super.onNullBinding(name);
    }

    private void set_bluetooth_connection(){
        this.bindService(new Intent(this, SerialService.class), this, Context.BIND_AUTO_CREATE);

        if(service != null){
            service.attach(this);
            Log.d(TAG, "set_bluetooth_connection: nienull");
        }
        else
            this.startService(new Intent(this, SerialService.class));

        try {
            BluetoothDevice device = blt_adapter.getRemoteDevice(device_address);
            SerialSocket socket = new SerialSocket(getApplicationContext(), device);
            service.connect(socket);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

}