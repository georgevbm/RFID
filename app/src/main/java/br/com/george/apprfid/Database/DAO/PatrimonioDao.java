package br.com.george.apprfid.Database.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.george.apprfid.Model.Patrimonio;


public class PatrimonioDao {
    private SQLiteDatabase db;
    String[] colunas = new String[]{"_cod", "nome", "descricao", "identificacao", "estado",
            "statusRegistro", "idGrails"};

    public PatrimonioDao(SQLiteDatabase db) {
        this.db = db;
    }

    public void inserir(Patrimonio patrimonio) {
        ContentValues val = new ContentValues();

        try {
            val.put("nome", patrimonio.getNome());
            val.put("descricao", patrimonio.getDescricao());
            val.put("identificacao", patrimonio.getIdentificacao());

            if (patrimonio.getStatusRegistro() == true)
                val.put("statusRegistro", "t");
            else
                val.put("statusRegistro", "f");

            db.insert("Patrimonio", null, val);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void atualizar(Patrimonio patrimonio) {
        String statusRegistro;
        ContentValues val = new ContentValues();

        val.put("_cod", patrimonio.getCod());
        val.put("nome", patrimonio.getNome());
        val.put("descricao", patrimonio.getDescricao());
        val.put("identificacao", patrimonio.getIdentificacao());

        if (patrimonio.getStatusRegistro() == true)
            val.put("statusRegistro", "t");
        else
            val.put("statusRegistro", "f");

        try {
            db.update("Patrimonio", val, "_cod=" + patrimonio.getCod(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deletar(Patrimonio patrimonio) {
        db.delete("Patrimonio", "_cod=" + patrimonio.getCod(), null);
    }

    public List<Patrimonio> buscarTodos() {
        List<Patrimonio> patrimonios = new ArrayList<>();

        Cursor cursor = db.query("Patrimonio", colunas, null, null, null, null, "_cod");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                patrimonios.add(iniciarPatrimonio(cursor, true));
            } while (cursor.moveToNext());
        }

        return patrimonios;
    }

    public Patrimonio buscarPorId(int id) {
        Cursor cursor = db.query("Patrimonio", colunas, "_cod =" + id, null, null, null, null);

        return iniciarPatrimonio(cursor, true);
    }

    public List<Patrimonio> buscarTodosParaAlterar() {
        List<Patrimonio> patrimonios = new ArrayList<>();

        Cursor cursor = db.query("Patrimonio", colunas, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                //patrimonios.add(iniciarPatrimonio(cursor, false));
                patrimonios.add(iniciarPatrimonio(cursor, true));
            } while (cursor.moveToNext());
        }

        return patrimonios;
    }

    public Patrimonio buscarPorTag(String tag) {
        Cursor cursor = db.query("Patrimonio", colunas, "identificacao ='" + tag + "'", null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            return iniciarPatrimonio(cursor, true);
        }
        else {
            return null;
        }
    }

    //region iniciarPatrimonio: DEFINE O CURSOR AO OBJETO PATRIMONIO
    private Patrimonio iniciarPatrimonio(Cursor cursor, boolean adicionarId) {
        Patrimonio patrimonio = new Patrimonio();;

        if (adicionarId)
            patrimonio.setCod(cursor.getInt(0));

        patrimonio.setNome(cursor.getString(1));
        patrimonio.setDescricao(cursor.getString(2));
        patrimonio.setIdentificacao(cursor.getString(4));

        if (cursor.getString(8).equals("t"))
            patrimonio.setStatusRegistro(true);
        else
            patrimonio.setStatusRegistro(false);

        return patrimonio;
    }
    //endregion
}
