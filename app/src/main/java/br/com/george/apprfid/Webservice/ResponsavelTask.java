package br.com.george.apprfid.Webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.george.apprfid.Database.Database;
import br.com.george.apprfid.Model.Responsavel;


public class ResponsavelTask extends AsyncTask<Void, Void, String> {
    private Context ctx;
    private String retorno = null;
    private ProgressDialog progressDialog;

    public ResponsavelTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(Void... params) {
        Webservice webservice = new Webservice(ctx, "Responsavel");
        String retorno = webservice.getJSON();
        return retorno;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Recebendo tabela: Responsavel");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String json) {
        Database db = new Database(ctx);
        Gson gson = new Gson();

        TypeToken<List<Responsavel>> token = new TypeToken<List<Responsavel>>() {
        };
        List<Responsavel> listaResponsavel = gson.fromJson(json, token.getType());

        if (listaResponsavel.size() > 0)
            db.deletarTodosResponsaveis();

        for (Responsavel responsavel : listaResponsavel) {
            db.inserirResponsavel(responsavel);
        }
        progressDialog.dismiss();
    }
}