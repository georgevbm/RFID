package br.com.george.apprfid.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import br.com.george.apprfid.Database.PatrimonioDAO;
import br.com.george.apprfid.Model.Patrimonio;
import br.com.george.apprfid.R;

public class CadPatrimonioActivity extends AppCompatActivity {
    private EditText txtDescricao;
    private EditText txtIdentificacao;
    private EditText txtNome;
    private CheckBox chkAtivo;
    private Patrimonio patrimonio;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_patrimonio);

        txtNome = (EditText) findViewById(R.id.txtNomePatrimonio);
        txtDescricao = (EditText) findViewById(R.id.txtDescricaoPatrimonio);
        txtIdentificacao = (EditText) findViewById(R.id.txtIdentificacaoPatrimonio);

        if (getIntent().hasExtra("patrimonio")) {
            patrimonio = (Patrimonio) getIntent().getExtras().getSerializable("patrimonio");
        }

        if (getIntent().hasExtra("tag")) {
            txtIdentificacao.setText(getIntent().getExtras().getString("tag").toString());
        }

        final Button btnGravar = (Button) findViewById(R.id.btnGravarPatrimonio);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                patrimonio = new Patrimonio();

                patrimonio.setNome(txtNome.getText().toString());
                patrimonio.setIdentificacao(txtIdentificacao.getText().toString());
                patrimonio.setDescricao(txtDescricao.getText().toString());

                new PatrimonioDAO(CadPatrimonioActivity.this).Salvar(patrimonio);

                AlertDialog.Builder builder = new AlertDialog.Builder(CadPatrimonioActivity.this);
                builder.setMessage("Registro salvo com sucesso!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
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
