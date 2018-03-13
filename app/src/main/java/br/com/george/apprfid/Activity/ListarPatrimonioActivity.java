package br.com.george.apprfid.Activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import br.com.george.apprfid.Adapter.PatrimonioAdapter;
import br.com.george.apprfid.Database.Database;
import br.com.george.apprfid.Model.Patrimonio;
import br.com.george.apprfid.R;


public class ListarPatrimonioActivity extends AppCompatActivity {
    private ListView lstPatrimonios;
    private BaseAdapter adapterPatrimonio;
    private Database db;
    private List<Patrimonio> listaPatrimonio;
    private Patrimonio patrimonio;
    private Toolbar mToolbar;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_patrimonio);

        db = new Database(ListarPatrimonioActivity.this);

        if (getIntent().hasExtra("tag")) {
            this.tag = (String) getIntent().getExtras().getString("tag");
        }

        lstPatrimonios = (ListView) findViewById(R.id.lstPatrimonio);
        listaPatrimonio = db.buscarPatrimonios();
        adapterPatrimonio = new PatrimonioAdapter(ListarPatrimonioActivity.this, listaPatrimonio);

        lstPatrimonios.setAdapter(adapterPatrimonio);

        lstPatrimonios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                patrimonio = listaPatrimonio.get(position);
                abrirMenu();
                return false;
            }
        });

        lstPatrimonios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tag != null) {
                    Intent intent = new Intent(ListarPatrimonioActivity.this, CadPatrimonioActivity.class);
                    intent.putExtra("tag", tag);

                    patrimonio = listaPatrimonio.get(position);
                    intent.putExtra("patrimonio", patrimonio);
                    startActivity(intent);
                }
            }
        });

        //region Toolbar
        mToolbar = (Toolbar) findViewById(R.id.ToolbarTop);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbar.findViewById(R.id.btnVoltar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //endregion

    }

    public void abrirMenu() {
        final Dialog dialog = new Dialog(ListarPatrimonioActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_patrimonio);

        Button btnAlterar = (Button) dialog.findViewById(R.id.btnAlterarPatrimonio);
        Button btnExcluir = (Button) dialog.findViewById(R.id.btnExcluirPatrimonio);
        Button btnCancelar = (Button) dialog.findViewById(R.id.btnCancelar);

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListarPatrimonioActivity.this, CadPatrimonioActivity.class);
                intent.putExtra("patrimonio", patrimonio);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deletarPatrimonio(patrimonio);
                listaPatrimonio = db.buscarPatrimonios();

                PatrimonioAdapter patrimonioAdapter = new PatrimonioAdapter(ListarPatrimonioActivity.this, listaPatrimonio);
                lstPatrimonios.setAdapter(patrimonioAdapter);

                dialog.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    //region MÉTODOS NÃO UTILIZADOS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_listar_patrimonio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}

