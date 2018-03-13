package br.com.george.apprfid.RFID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public interface ILeitor {
    String ACTION_DISCOVERY_STARTED = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
    String ACTION_FOUND = BluetoothDevice.ACTION_FOUND;
    String ACTION_DISCOVERY_FINISHED = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;


    void leitura();
}
