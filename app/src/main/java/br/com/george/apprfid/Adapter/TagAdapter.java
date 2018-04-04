package br.com.george.apprfid.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.com.george.apprfid.R;

public class TagAdapter extends BaseAdapter {
    Context ctx;
    List<String> listaTags;

    public TagAdapter(Context ctx, List<String> listaTags) {
        this.listaTags = listaTags;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return listaTags.size();
    }

    @Override
    public Object getItem(int position) {
        return listaTags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            final int auxPosition = position;

            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.adapter_etiqueta, null);

            String tag = listaTags.get(position);

            TextView lblPatrimonio = (TextView) layout.findViewById(R.id.lblPatrimonio);
            TextView lblLocalizacao = (TextView) layout.findViewById(R.id.lblLocalizacao);
            TextView lblDescPatrimonio = (TextView) layout.findViewById(R.id.lblDescPatrimonio2);

            lblPatrimonio.setText(tag);
            lblDescPatrimonio.setText(tag);
            lblDescPatrimonio.setVisibility(View.VISIBLE);
            lblLocalizacao.setVisibility(View.VISIBLE);

            return layout;
        } catch (Exception ex) {
            Log.d("ERRO", ex.getMessage());
            return null;
        }
    }
}