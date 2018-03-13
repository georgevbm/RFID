package br.com.george.apprfid.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import br.com.george.apprfid.Database.Database;
import br.com.george.apprfid.Model.Local;
import br.com.george.apprfid.Model.Patrimonio;
import br.com.george.apprfid.Model.Responsavel;
import br.com.george.apprfid.R;

public class CadPatrimonioActivity extends AppCompatActivity {
    private EditText txtDescricao;
    private EditText txtIdentificacao;
    private EditText txtEstado;
    private EditText txtNome;
    private Spinner cboLocal;
    private Spinner cboResponsavel;
    private CheckBox chkAtivo;
    private Database db;
    private Patrimonio patrimonio;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new Database(CadPatrimonioActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_patrimonio);

        List<Local> listaLocal;
        List<Responsavel> listaResponsavel;

        listaLocal = db.buscarLocais();
        listaResponsavel = db.buscarResponsaveis();

        txtNome = (EditText) findViewById(R.id.txtNomePatrimonio);
        txtDescricao = (EditText) findViewById(R.id.txtDescricaoPatrimonio);
        txtIdentificacao = (EditText) findViewById(R.id.txtIdentificacaoPatrimonio);
        txtEstado = (EditText) findViewById(R.id.txtEstadoPatrimonio);
        cboLocal = (Spinner) findViewById(R.id.cboLocalPatrimonio);
        cboResponsavel = (Spinner) findViewById(R.id.cboResponsavel);
        chkAtivo = (CheckBox) findViewById(R.id.chkAtivoPatrimonio);
        chkAtivo.setChecked(true);

        ArrayAdapter<Local> adapterLocal = new ArrayAdapter<Local>(this, android.R.layout.simple_dropdown_item_1line, listaLocal);
        cboLocal.setAdapter(adapterLocal);

        ArrayAdapter<Responsavel> adapterResponsavel = new ArrayAdapter<Responsavel>(this,
                android.R.layout.simple_dropdown_item_1line, listaResponsavel);

        cboResponsavel.setAdapter(adapterResponsavel);

        if (getIntent().hasExtra("patrimonio")) {
            patrimonio = (Patrimonio) getIntent().getExtras().getSerializable("patrimonio");

            txtNome.setText(patrimonio.getNome());
            txtDescricao.setText(patrimonio.getDescricao());
            txtIdentificacao.setText(patrimonio.getIdentificacao());
            txtEstado.setText(patrimonio.getEstado());
            if (patrimonio.getStatusRegistro() == true)
                chkAtivo.setChecked(true);
            else
                chkAtivo.setChecked(false);

            cboLocal.setId(patrimonio.getLocal().getId());
            cboResponsavel.setId(patrimonio.getResponsavel().getId());
        }

        if (getIntent().hasExtra("tag")) {
            txtIdentificacao.setText(getIntent().getExtras().getString("tag").toString());
        }


        final Button btnGravar = (Button) findViewById(R.id.btnGravarPatrimonio);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (patrimonio == null) {
                    patrimonio = new Patrimonio();
                    patrimonio.setEnviarBancoOnline(true);
                    patrimonio.setAtualizarBancoOnline(false);
                }
                else {
                    if (patrimonio.isEnviarBancoOnline()) {
                        patrimonio.setAtualizarBancoOnline(false);
                        patrimonio.setEnviarBancoOnline(true);
                    }
                    else {
                        patrimonio.setAtualizarBancoOnline(true);
                        patrimonio.setEnviarBancoOnline(false);
                    }
                }
/*
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
*/
                patrimonio.setNome(txtNome.getText().toString());
                patrimonio.setIdentificacao(txtIdentificacao.getText().toString());
                patrimonio.setDescricao(txtDescricao.getText().toString());
                patrimonio.setEstado(txtEstado.getText().toString());

//                patrimonio.setDataEntrada(date);
                if (chkAtivo.isChecked())
                    patrimonio.setStatusRegistro(true);
                else
                    patrimonio.setStatusRegistro(false);

                Local local = (Local) cboLocal.getSelectedItem();

                patrimonio.setLocal(local);

                Responsavel responsavel = (Responsavel) cboResponsavel.getSelectedItem();

                patrimonio.setResponsavel(responsavel);

                if (patrimonio.getCod() == 0) {
                    patrimonio.setEnviarBancoOnline(true);
                    db.inserirPatrimonio(patrimonio);
                }
                else
                    db.atualizarPatrimonio(patrimonio);

                AlertDialog.Builder builder = new AlertDialog.Builder(CadPatrimonioActivity.this);
                builder.setMessage("Registro salvo com sucesso!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //limparCampos();
                                finish();

                                //txtNome.requestFocus();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

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

    public void limparCampos() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtIdentificacao.setText("");
        txtEstado.setText("");
        chkAtivo.setChecked(true);
    }

    //region MÉTODOS NÃO UTILIZADOS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_cad_patrimonio, menu);
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
