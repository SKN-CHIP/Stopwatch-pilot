package com.example.stoperchip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * show list of BLE devices
 */
public class DevicesFragment extends ListFragment {

    private enum ScanState { NONE, LE_SCAN, DISCOVERY, DISCOVERY_FINISHED }
    private ScanState scanState = ScanState.NONE;
    private static final long LE_SCAN_PERIOD = 10000; // similar to bluetoothAdapter.startDiscovery
    private final Handler leScanStopHandler = new Handler();
    private final BluetoothAdapter.LeScanCallback leScanCallback;
    private final Runnable leScanStopCallback;
    private final BroadcastReceiver discoveryBroadcastReceiver;
    private final IntentFilter discoveryIntentFilter;
    private BluetoothAdapter bluetoothAdapter;
    private final ArrayList<BluetoothUtil.Device> listItems = new ArrayList<>();
    private ArrayAdapter<BluetoothUtil.Device> listAdapter;
    ActivityResultLauncher<String[]> requestBluetoothPermissionLauncherForStartScan;
    ActivityResultLauncher<String> requestLocationPermissionLauncherForStartScan;
    final String TAG = "DeviceFragmentTAG";

    public DevicesFragment() {
        leScanCallback = (device, rssi, scanRecord) -> {
            if(device != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> updateScan(device));
            }
        };
        discoveryBroadcastReceiver = new BroadcastReceiver() {
            @SuppressLint("MissingPermission")
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: test2");
                if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(Objects.requireNonNull(device).getType() != BluetoothDevice.DEVICE_TYPE_CLASSIC && getActivity() != null) {
                        getActivity().runOnUiThread(() -> updateScan(device));
                    }
                }
                if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                    scanState = ScanState.DISCOVERY_FINISHED; // don't cancel again
                    stopScan();
                }
            }
        };
        discoveryIntentFilter = new IntentFilter();
        discoveryIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        discoveryIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        leScanStopCallback = this::stopScan; // w/o explicit Runnable, a new lambda would be created on each postDelayed, which would not be found again by removeCallbacks
        requestBluetoothPermissionLauncherForStartScan = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                granted -> BluetoothUtil.onPermissionsResult(this, granted, this::startScan));
        requestLocationPermissionLauncherForStartScan = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        new Handler(Looper.getMainLooper()).postDelayed(this::startScan, 1); // run after onResume to avoid wrong empty-text
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getText(R.string.location_permission_title));
                        builder.setMessage(getText(R.string.location_permission_denied));
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.show();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(requireActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listAdapter = new ArrayAdapter<BluetoothUtil.Device>(requireActivity(), 0, listItems) {
            @NonNull
            @Override
            public View getView(int position, View view, @NonNull ViewGroup parent) {
                BluetoothUtil.Device device = listItems.get(position);
                if (view == null){
                    view = requireActivity().getLayoutInflater().inflate(R.layout.device_list, parent, false);
                }
                TextView text1 = view.findViewById(R.id.blt_name);
                TextView text2 = view.findViewById(R.id.blt_address);
                String deviceName = device.getName();
                text1.setText(deviceName);
                text2.setText(device.getDevice().getAddress());
                return view;
            }
        };
        startScan();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(listAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(discoveryBroadcastReceiver, discoveryIntentFilter);
        if(bluetoothAdapter == null) {
            Log.d(TAG, "onResume: <bluetooth LE not supported>");
        } else if(!bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "onResume: <bluetooth is disabled>");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScan();
        requireActivity().unregisterReceiver(discoveryBroadcastReceiver);
    }

    private void startScan() {
        if(scanState != ScanState.NONE)
            return;
        ScanState nextScanState = ScanState.LE_SCAN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!BluetoothUtil.hasPermissions(this, requestBluetoothPermissionLauncherForStartScan))
                return;
        } else {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                scanState = ScanState.NONE;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.location_permission_title);
                builder.setMessage(R.string.location_permission_grant);
                builder.setPositiveButton(android.R.string.ok,
                        (dialog, which) -> requestLocationPermissionLauncherForStartScan.launch(Manifest.permission.ACCESS_FINE_LOCATION));
                builder.show();
                return;
            }
            // Starting with Android 6.0 a bluetooth scan requires ACCESS_COARSE_LOCATION permission, but that's not all!
            // LESCAN also needs enabled 'location services', whereas DISCOVERY works without.
            // Most users think of GPS as 'location service', but it includes more, as we see here.
            // Instead of asking the user to enable something they consider unrelated,
            // we fall back to the older API that scans for bluetooth classic _and_ LE
            // sometimes the older API returns less results or slower
        }
        scanState = nextScanState;
        listItems.clear();
        listAdapter.notifyDataSetChanged();
        if(scanState == ScanState.LE_SCAN) {
            leScanStopHandler.postDelayed(leScanStopCallback, LE_SCAN_PERIOD);
            new Thread(() -> bluetoothAdapter.startLeScan(null, leScanCallback), "startLeScan")
                    .start(); // start async to prevent blocking UI, because startLeScan sometimes take some seconds
        } else {
            bluetoothAdapter.startDiscovery();
        }
    }

    @SuppressLint("MissingPermission")
    private void updateScan(BluetoothDevice device) {
        if(scanState == ScanState.NONE)
            return;
        BluetoothUtil.Device device2 = new BluetoothUtil.Device(device); // slow getName() only once
        if(device.getName() == null || device.getName().isEmpty())
            return;
        int pos = Collections.binarySearch(listItems, device2);
        if (pos < 0) {
            listItems.add(-pos - 1, device2);
            listAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("MissingPermission")
    private void stopScan() {
        if(scanState == ScanState.NONE)
            return;
        switch(scanState) {
            case LE_SCAN:
                leScanStopHandler.removeCallbacks(leScanStopCallback);
                bluetoothAdapter.stopLeScan(leScanCallback);
                break;
            case DISCOVERY:
                bluetoothAdapter.cancelDiscovery();
                break;
            default:
                // already canceled
        }
        scanState = ScanState.NONE;

    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        stopScan();
        BluetoothUtil.Device device = listItems.get(position);
        Intent resultBluetooth = new Intent();
        resultBluetooth.putExtra("blt_device", device.getDevice().getAddress());
        requireActivity().setResult(100, resultBluetooth);
        requireActivity().finish();
    }
}