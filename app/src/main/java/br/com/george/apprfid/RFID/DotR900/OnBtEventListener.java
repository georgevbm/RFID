package br.com.george.apprfid.RFID.DotR900;

/***********************************************************************************
* OnBtnEventListener revision history                                              *
*************+*************+********+***********************************************
* 2012.12.12	ver 1.0.0  	  eric     1. Generated(First release)                 *
************************************************************************************/

import android.bluetooth.BluetoothDevice;

public interface OnBtEventListener
{
    void onNotifyBtDataRecv();
    void onBtConnected(BluetoothDevice device);
}


