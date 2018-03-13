package br.com.george.apprfid.Database.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.george.apprfid.Model.Departamento;


public class DepartamentoDao {
    private SQLiteDatabase db;

    public DepartamentoDao(SQLiteDatabase db) {
        this.db = db;
    }

    public void inserir(Departamento departamento) {
        ContentValues val = new ContentValues();

        val.put("_id", departamento.getId());
        val.put("nome", departamento.getNome());
        val.put("sigla", departamento.getSigla());

        db.insert("Departamento", null, val);
    }

    public void atualizar(Departamento departamento) {
        ContentValues val = new ContentValues();

        val.put("_id", departamento.getId());
        val.put("nome", departamento.getNome());
        val.put("sigla", departamento.getSigla());

        db.update("Departamento", val, "_id=" + departamento.getId(), null);
    }

    public void deletar(Departamento departamento) {
        db.delete("Departamento", "_id=" + departamento.getId(), null);
    }

    public List<Departamento> buscarTodos() {
        List<Departamento> departamentos = new ArrayList<>();
        String[] colunas = new String[]{"_id", "nome", "sigla"};

        Cursor cursor = db.query("Departamento", colunas, null, null, null, null, "nome ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Departamento dep = new Departamento();

                dep.setId(cursor.getInt(0));
                dep.setNome(cursor.getString(1));
                dep.setSigla(cursor.getString(2));

                departamentos.add(dep);
            } while (cursor.moveToNext());
        }

        return departamentos;
    }

    public Departamento buscarPorId(int id) {
        Departamento departamento = new Departamento();
        String[] colunas = new String[]{"_id", "nome", "sigla"};

        Cursor cursor = db.query("Departamento", colunas, "_id =" + id, null, null, null, null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            departamento.setId(cursor.getInt(0));
            departamento.setNome(cursor.getString((1)));
            departamento.setSigla(cursor.getString(2));
        } else {
            return null;
        }

        return departamento;
    }

    public void deletarTodos() {
        db.delete("departamento", "", null);
    }
}
