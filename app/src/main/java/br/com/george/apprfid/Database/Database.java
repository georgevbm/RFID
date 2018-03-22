package br.com.george.apprfid.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.george.apprfid.Database.DAO.PatrimonioDao;
import br.com.george.apprfid.Model.Patrimonio;

public class Database {
    private SQLiteDatabase db;

    public Database(Context ctx) {
        DatabaseHelper cDb = new DatabaseHelper(ctx);
        db = cDb.getWritableDatabase();
    }

    //region Patrimonio
    public void inserirPatrimonio(Patrimonio patrimonio) {
        new PatrimonioDao(db).inserir(patrimonio);
    }

    public void atualizarPatrimonio(Patrimonio patrimonio) {
        new PatrimonioDao(db).atualizar(patrimonio);
    }

    public void deletarPatrimonio(Patrimonio patrimonio) {
        new PatrimonioDao(db).deletar(patrimonio);
    }

    public List<Patrimonio> buscarPatrimonios() {
        return new PatrimonioDao(db).buscarTodos();
    }

    public List<Patrimonio> buscarPatrimoniosParaInserir() {
        return new PatrimonioDao(db).buscarTodosParaInserir();
    }

    public List<Patrimonio> buscarPatrimoniosParaAlterar() {
        return new PatrimonioDao(db).buscarTodosParaAlterar();
    }

    public Patrimonio buscarPatrimonio(int id) {
        return new PatrimonioDao(db).buscarPorId(id);
    }

    public Patrimonio buscarPatrimonioTag(String tag) {
        return new PatrimonioDao(db).buscarPorTag(tag);
    }

    public void deletarTodosPatrimonios() {
        new PatrimonioDao(db).deletarTodos();
    }

    public void deletarTodosPatrimoniosNovos() {
        new PatrimonioDao(db).deletarTodosNovos();
    }

    public void registroEnviado(List<Patrimonio> patrimonios) { new PatrimonioDao(db).registroEnviado(patrimonios);}
    //endregion
}
