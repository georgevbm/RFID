package br.com.george.apprfid.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.george.apprfid.Model.Tag;
import br.com.george.apprfid.R;

public class DatabaseAdapter extends ArrayAdapter {

    private Context context;
    private List<Tag> tags;

    public DatabaseAdapter(@NonNull Context c, @NonNull List<Tag> objects) {
        super(c, 0, objects);
        context = c;
        tags = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (tags != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_database, parent, false);

            TextView idTag = (TextView) view.findViewById(R.id.idTagDatabase);
            TextView nomeTag = (TextView) view.findViewById(R.id.nomeTagDatabase);
            TextView descricaoTag = (TextView) view.findViewById(R.id.descricaoTagDatabase);
            TextView identificadorTag = (TextView) view.findViewById(R.id.tagDatabase);

            Tag tagAtual = tags.get(position);

            idTag.setText(String.valueOf(tagAtual.getCod()));
            nomeTag.setText(tagAtual.getNome());
            descricaoTag.setText(tagAtual.getDescricao());
            identificadorTag.setText(tagAtual.getIdentificacao());
        }

        return view;
    }

}
