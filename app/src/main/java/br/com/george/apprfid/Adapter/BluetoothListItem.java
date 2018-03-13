package br.com.george.apprfid.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.george.apprfid.R;

public class BluetoothListItem extends BaseAdapter {
    private List<BluetoothDevice> dispostivos;
    private LayoutInflater inflater;
    private Context ctx;
    private ImageView imgBluetooth;

    public BluetoothListItem(Context ctx, List<BluetoothDevice> dispositivos) {
        this.dispostivos = dispositivos;
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return dispostivos.size();
    }

    @Override
    public Object getItem(int position) {
        return dispostivos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BluetoothDevice dispositivo = (BluetoothDevice) getItem(position);

        View view = inflater.inflate(R.layout.adapter_bluetooth, null);

        TextView txtNomeDispositivo = (TextView) view.findViewById(R.id.txtNome_Dispositivo);
        txtNomeDispositivo.setText(dispositivo.getName());

        TextView txtCodigoDispositivo = (TextView) view.findViewById(R.id.txtCodigo_Dispositivo);
        txtCodigoDispositivo.setText(dispositivo.getAddress());

        imgBluetooth = (ImageView) view.findViewById(R.id.imgBluetooth);

        return view;
    }
}