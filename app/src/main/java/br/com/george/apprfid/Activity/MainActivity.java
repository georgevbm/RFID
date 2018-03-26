package br.com.george.apprfid.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import br.com.george.apprfid.Database.PatrimonioDAO;
import br.com.george.apprfid.Model.Patrimonio;
import br.com.george.apprfid.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Botão RFID
        Button btnIniciarRFID = (Button) findViewById(R.id.btnIniciarRFID);
        btnIniciarRFID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ListarBluetoothActivity.class);
                startActivity(intent);
            }
        });

        List<Patrimonio> patrimonios = new ArrayList<>();
        patrimonios = new PatrimonioDAO(MainActivity.this).SelecionarTodos();

        new PatrimonioDAO(MainActivity.this).Excluir(1);

        for(Patrimonio p: patrimonios){
            Log.i("DADOS BANCO", "Cod: " + p.getCod());
            Log.i("DADOS BANCO", "Nome: " + p.getNome());
            Log.i("DADOS BANCO", "Descricao: " + p.getDescricao());
            Log.i("DADOS BANCO", "Identificacao: " + p.getIdentificacao());
        }
    }

    //region MÉTODOS NÃO UTILIZADOS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

