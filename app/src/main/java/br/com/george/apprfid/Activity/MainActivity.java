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

import br.com.george.apprfid.Database.TagDAO;
import br.com.george.apprfid.Model.Tag;
import br.com.george.apprfid.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recebendo o botão da tela inicial (activity_main.xml)
        Button btnIniciarRFID = (Button) findViewById(R.id.btnIniciarRFID);

        // Evento de clique do botão da tela inicial
        btnIniciarRFID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
                startActivity(intent);
            }
        });

        List<Tag> tags = new ArrayList<>();
        tags = new TagDAO(MainActivity.this).SelecionarTodos();

//        new TagDAO(MainActivity.this).Excluir(2);

        for(Tag tag: tags){
            Log.i("DADOS BANCO", "Cod: " + tag.getCod());
            Log.i("DADOS BANCO", "Nome: " + tag.getNome());
            Log.i("DADOS BANCO", "Descricao: " + tag.getDescricao());
            Log.i("DADOS BANCO", "Identificacao: " + tag.getIdentificacao());
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

