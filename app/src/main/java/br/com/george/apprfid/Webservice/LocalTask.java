package br.com.george.apprfid.Webservice;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.george.apprfid.Database.Database;
import br.com.george.apprfid.Model.Local;

public class LocalTask extends AsyncTask<Void, Void, String> {
    private Context ctx;
    private String retorno = null;

    public LocalTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(Void... params) {
        Webservice webservice = new Webservice(ctx, "Local");
        String retorno = webservice.getJSON();
        return retorno;
    }


    @Override
    protected void onPostExecute(String json) {
        Database db = new Database(ctx);
        Gson gson = new Gson();

        TypeToken<List<Local>> token = new TypeToken<List<Local>>() {
        };
        List<Local> listaLocal = gson.fromJson(json, token.getType());

        if (listaLocal.size() > 0 )
            db.deletarTodosLocais();

        for (Local local : listaLocal) {
            db.inserirLocal(local);
        }
    }


}
