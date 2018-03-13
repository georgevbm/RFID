package br.com.george.apprfid.Database.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.george.apprfid.Model.Responsavel;


public class ResponsavelDao {
    private SQLiteDatabase db;

    public ResponsavelDao(SQLiteDatabase db) {
        this.db = db;
    }

    public void inserir(Responsavel responsavel) {
        ContentValues val = new ContentValues();
        val.put("_id", responsavel.getId());
        val.put("nome", responsavel.getNome());
        val.put("funcao", responsavel.getFuncao());
        val.put("siape", responsavel.getSiape());
        val.put("telefone", responsavel.getTelefone());

        db.insert("Responsavel", null, val);
    }

    public void atualizar(Responsavel responsavel) {
        ContentValues val = new ContentValues();
        val.put("_id", responsavel.getId());
        val.put("nome", responsavel.getNome());
        val.put("funcao", responsavel.getFuncao());
        val.put("siape", responsavel.getSiape());
        val.put("telefone", responsavel.getTelefone());

        db.update("Responsavel", val, "_id=" + responsavel.getId(), null);
    }

    public void deletar(Responsavel responsavel) {
        db.delete("Responsavel", "_id=" + responsavel.getId(), null);
    }

    public List<Responsavel> buscarTodos() {
        List<Responsavel> responsaveis = new ArrayList<>();
        String[] colunas = new String[]{"_id", "nome","funcao", "siape", "telefone"};

        Cursor cursor = db.query("Responsavel", colunas, null, null, null, null, "nome ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Responsavel responsavel = new Responsavel();

                responsavel.setId(cursor.getInt(0));
                responsavel.setNome(cursor.getString(1));
                responsavel.setFuncao(cursor.getString(2));
                responsavel.setSiape(cursor.getString(3));
                responsavel.setTelefone(cursor.getString(4));

                responsaveis.add(responsavel);
            }while (cursor.moveToNext());
        }

        return responsaveis;
    }

    public Responsavel buscarPorId(int id) {
        Responsavel responsavel = new Responsavel();
        String[] colunas = new String[]{"_id", "nome","funcao", "siape", "telefone"};

        Cursor cursor = db.query("Responsavel",colunas,"_id ="+id, null,null,null,null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            responsavel.setId(cursor.getInt(0));
            responsavel.setNome(cursor.getString(1));
            responsavel.setFuncao(cursor.getString(2));
            responsavel.setSiape(cursor.getString(3));
            responsavel.setTelefone(cursor.getString(4));
        }
        else {
            return null;
        }

        return responsavel;
    }

    public void deletarTodos() {
        db.delete("responsavel", "", null);
    }
}
