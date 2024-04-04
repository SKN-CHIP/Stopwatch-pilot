package com.example.stoperchip;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class BluetoothActivity extends AppCompatActivity implements Serializable {
    Button return_button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);
        return_button = findViewById(R.id.button_blt_return);
        return_button.setOnClickListener(v -> finish());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DevicesFragment(), "devices").commit();
    }


}
