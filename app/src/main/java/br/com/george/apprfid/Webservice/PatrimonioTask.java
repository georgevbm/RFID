package br.com.george.apprfid.Webservice;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.george.apprfid.Database.Database;
import br.com.george.apprfid.Model.Patrimonio;


public class PatrimonioTask extends AsyncTask<Void, Void, String> {
    private Context ctx;
    private String retorno = null;

    public enum TransacaoJSON {GET, POST, PUT;}

    private TransacaoJSON tipoTransacao;
    private List<Patrimonio> listaPatrimonio;

    public PatrimonioTask(Context ctx) {
        this.ctx = ctx;
        this.tipoTransacao = TransacaoJSON.GET;
    }

    public PatrimonioTask(Context ctx, List<Patrimonio> listaPatrimonio, TransacaoJSON tipoTransacao) {
        this.ctx = ctx;
        this.tipoTransacao = tipoTransacao;
        this.listaPatrimonio = listaPatrimonio;
    }

    @Override
    protected String doInBackground(Void... params) {
        Webservice webservice = new Webservice(ctx, "Patrimonio");
        String json = null;

        if (tipoTransacao.equals(TransacaoJSON.GET)) {
            String retorno = webservice.getJSON();
            return retorno;
        }
        else {
            if (listaPatrimonio != null) {
                for (Patrimonio patrimonio:listaPatrimonio) {
                    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                    json = gson.toJson(patrimonio);

                    switch (tipoTransacao) {
                        case POST:
                            webservice.postJSON("Patrimonio", json);
                            break;
                        case PUT:
                            webservice.putJSON("Patrimonio/" + patrimonio.getId(), json);
                            break;
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String json) {
        if (tipoTransacao == TransacaoJSON.GET) {
            Database db = new Database(ctx);
            Gson gson = new Gson();

            TypeToken<List<Patrimonio>> token = new TypeToken<List<Patrimonio>>() {
            };
            List<Patrimonio> listaPatrimonio = gson.fromJson(json, token.getType());

            if (listaPatrimonio.size() > 0)
                db.deletarTodosPatrimonios();


            for (Patrimonio patrimonio : listaPatrimonio) {
                patrimonio.setEnviarBancoOnline(false);
                db.inserirPatrimonio(patrimonio);
            }
        }
    }

}
