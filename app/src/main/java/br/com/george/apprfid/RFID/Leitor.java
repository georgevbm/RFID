package br.com.george.apprfid.RFID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.com.george.apprfid.RFID.DotR900.OnBtEventListener;
import br.com.george.apprfid.RFID.DotR900.R900RecvPacketParser;


public class Leitor {
    public enum BluetoothEstado {LIGADO, DESLIGADO, NAO_COMPATIVEL;}

    public static final String ACTION_REQUEST_ENABLE = BluetoothAdapter.ACTION_REQUEST_ENABLE;
    public static final int REQUEST_ENABLE_BT = 0;
    private List<BluetoothDevice> listaDispositivo = new ArrayList<>();
    protected BluetoothAdapter mBluetoothAdapter;
    private Activity activity;
    private boolean mReceiverStarter;
    private UUID mUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    protected R900RecvPacketParser mPacketParser = new R900RecvPacketParser();
    private ArrayList<HashMap<String, String>> mArrTag;
    private String mSelTag;
    private BluetoothDevice dispositivo;
    protected boolean mConnected;
    protected Handler mHandler;
    public static final int MSG_BT_DATA_RECV = 10;
    private static final int MSG_ENCONTROU_BLUETOOTH = 1;
    private FileOutputStream mDbgOutStream;
    protected ConnectedThread mConnectedThread;
    protected ConnectThread mConnectThread;
    private OnBtEventListener mBtEventListener;
    protected BluetoothSocket mBluetoothSocket;

    public List<String> getListaTags() {
        return listaTags;
    }

    private List<String> listaTags = new ArrayList<>();

    public void setSelTag(String mSelTag) {
        this.mSelTag = mSelTag;
    }

    public BluetoothDevice getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(BluetoothDevice dispositivo) {
        this.dispositivo = dispositivo;
    }

    public List<BluetoothDevice> getListaDispositivo() {
        return listaDispositivo;
    }

    public BluetoothEstado getEstado() {
        if (mBluetoothAdapter == null) {
            return BluetoothEstado.NAO_COMPATIVEL;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                return BluetoothEstado.DESLIGADO;
            }
            return BluetoothEstado.LIGADO;
        }
    }

    public Leitor(Activity activity, Handler handler, OnBtEventListener listener) {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.activity = activity;
        this.mHandler = handler;
        this.mBtEventListener = listener;

        registrarBroadcast();
    }

    //region buscarBluetooth: RETORNA TODOS DISPOSITIVOS BLUETOOTH
    public void buscarBluetooth() {
        listaDispositivo.clear();

        if (getEstado() == BluetoothEstado.LIGADO) {
            dispositivosPareados();
            dispositivosConectados();
        }
    }
    ///endregion

    //region dispositivosPareados: RETORNA OS DISPOSITIVOS PAREADOS COM O APARELHO
    private void dispositivosPareados() {
        Set<BluetoothDevice> pairedDevicesList = mBluetoothAdapter.getBondedDevices();

        if (pairedDevicesList.size() > 0) {
            for (BluetoothDevice device : pairedDevicesList) {
                listaDispositivo.add(device);
            }
        }
    }
    //endregion

    //region dispositivosConectados: RETORNA TODOS DISPOSITIVOS BLUETOOTH CONECTADOS POR PERTO
    private void dispositivosConectados() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }
    //endregion

    //region pararBusca: PARA DE PROCURAR POR DISPOSITIVOS BLUETOOTH
    public void pararBusca() {
        if (mReceiverStarter) {
            activity.unregisterReceiver(mReceiver);
            mReceiverStarter = false;
        }

        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }
    //endregion

    //region Conectar
    public void conectar(String address) {
        conectar(mBluetoothAdapter.getRemoteDevice(address));
    }

    public void conectar(BluetoothDevice dispositivo) {
        try {
            this.dispositivo = dispositivo;
            disconnect();
            mConnectThread = new ConnectThread();
            mConnectThread.start();
            mConnected = true;
        } catch (Exception ex) {
            mConnected = false;
        }

    }
    //endregion

    //region disconnect: RESPONSÁVEL POR FINALIZAR A CONEXÃO
    public void disconnect() {
        try {
            if (mConnectThread != null) {
                mConnectThread.stop();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mConnectThread = null;

        try {
            if (mConnectedThread != null) {
                mConnectedThread.cancel();
                mConnectedThread.stop();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mConnectedThread = null;
    }
    //endregion

    //region finalize: RESPONSÁVEL POR FINALIZAR A DESCOBERTA DE DISPOSITIVOS
    protected void finalize() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();

        disconnect();

        if (mDbgOutStream != null) {
            try {
                mDbgOutStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (mReceiverStarter)
            activity.unregisterReceiver(mReceiver);
    }
    //endregion

    //region BroadcastReceiver
    private void registrarBroadcast() {
        // Registro os Broadcast necessarios para a busca de dispositivos
        IntentFilter filter = new IntentFilter(ILeitor.ACTION_FOUND);
        IntentFilter filter2 = new IntentFilter(ILeitor.ACTION_DISCOVERY_FINISHED);
        IntentFilter filter3 = new IntentFilter(ILeitor.ACTION_DISCOVERY_STARTED);
        activity.registerReceiver(mReceiver, filter);
        activity.registerReceiver(mReceiver, filter2);
        activity.registerReceiver(mReceiver, filter3);
        mReceiverStarter = true;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.compareTo(BluetoothDevice.ACTION_FOUND) == 0) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //se a lista já tiver esse dispositivo eu retorno para o proximo
                //isso permite que sejá mostrado somente uma vez meu dispositivo
                //problema muito comum em exemplos
                if (listaDispositivo.contains(device)) {
                    return;
                }

                mHandler.sendEmptyMessage(MSG_ENCONTROU_BLUETOOTH);
                listaDispositivo.add(device);
            }
        }
    };
    //endregion

    //region Thread de Conexão
    // ------------- For Manage bluetooth
    protected class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private boolean mInitOk;

        public ConnectedThread() {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            mPacketParser.reset();

            try {
                tmpIn = mBluetoothSocket.getInputStream();
                tmpOut = mBluetoothSocket.getOutputStream();
                mInitOk = true;
            } catch (IOException e) {
                mInitOk = false;
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public boolean isInitOk() {
            return mInitOk;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (mInitOk) {
                try {
                    bytes = mmInStream.read(buffer);

                    if (mDbgOutStream != null)
                        mDbgOutStream.write(buffer, 0, bytes);

                    mPacketParser.pushPacket(buffer, bytes);

                    mHandler.sendEmptyMessage(MSG_BT_DATA_RECV);
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {

            }
        }

    }

    protected class ConnectThread extends Thread {
        public ConnectThread() {
            if (dispositivo != null) {
                try {
                    mBluetoothSocket = dispositivo.createRfcommSocketToServiceRecord(mUuid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void run() {
            if (mBluetoothAdapter != null)
                mBluetoothAdapter.cancelDiscovery();

            if (mBluetoothAdapter != null) {
                try {
                    mBluetoothSocket.connect();
                } catch (IOException connectException) {
                    try {
                        mBluetoothSocket.close();
                    } catch (IOException closeException) {
                    }

                    mConnectThread = null;
                    return;
                }

                // manageConnectedSocket(mmSocket);
                mConnectedThread = new ConnectedThread();
                if (mConnectedThread.isInitOk()) {
                    mConnectedThread.start();

                    if (mBtEventListener != null)
                        mBtEventListener.onBtConnected(dispositivo);
                }
            } else {

            }

            mConnectThread = null;
        }
    }
    //endregion

    protected void atualizarListaTag(final String param) {

        if (param == null || param.length() <= 4)
            return;

        final String tagRFId = param.substring(0, param.length() - 4);

        if (!listaTags.contains(tagRFId))
            listaTags.add(tagRFId);
    }

}
