package br.com.george.apprfid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.george.apprfid.Model.Patrimonio;
import br.com.george.apprfid.R;

public class PatrimonioAdapter extends BaseAdapter {
    private List<Patrimonio> listaPatrimonio;
    private Context ctx;

    public PatrimonioAdapter(Context ctx, List<Patrimonio> listaPatrimonio) {
        this.ctx = ctx;
        this.listaPatrimonio = listaPatrimonio;
    }

    @Override
    public int getCount() {
        return listaPatrimonio.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPatrimonio.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int auxPosition = position;

        try {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.adapter_patrimonio, null);

            Patrimonio patrimonio = listaPatrimonio.get(position);

            TextView lblPatrimonio = (TextView) layout.findViewById(R.id.lblDescricaoPatrimonio);
            TextView lblLocal = (TextView) layout.findViewById(R.id.lblLocalPatrimonio);
            TextView lblResponsael = (TextView) layout.findViewById(R.id.lblResponsavelPatrimonio);
            TextView lblDescPatrimonio = (TextView) layout.findViewById(R.id.lblDescPatrimonio);

            lblPatrimonio.setText(patrimonio.getNome());
            lblLocal.setText(patrimonio.getLocal().getDescricao());
            lblDescPatrimonio.setText(patrimonio.getDescricao());
            lblResponsael.setText(patrimonio.getResponsavel().getNome());

            return layout;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
