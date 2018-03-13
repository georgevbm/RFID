package br.com.george.apprfid.Webservice;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.george.apprfid.Database.Database;
import br.com.george.apprfid.Model.Departamento;

public class DepartamentoTask extends AsyncTask<Void, Void, String> {
    private Context ctx;
    private String retorno = null;

    public DepartamentoTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(Void... params) {

        Webservice webservice = new Webservice(ctx, "Departamento");
        String retorno = webservice.getJSON();
        return retorno;
    }

    @Override
    protected void onPostExecute(String json) {
        Database db = new Database(ctx);
        Gson gson = new Gson();

        TypeToken<List<Departamento>> token = new TypeToken<List<Departamento>>() {
        };
        List<Departamento> listaDepartamento = gson.fromJson(json, token.getType());

        if (listaDepartamento.size() > 0)
                db.deletarTodosDepartamentos();

        for (Departamento departamento : listaDepartamento) {
            db.inserirDepartamento(departamento);
        }
    }


}
