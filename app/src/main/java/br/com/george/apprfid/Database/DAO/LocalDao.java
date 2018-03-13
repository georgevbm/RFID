package br.com.george.apprfid.Database.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.george.apprfid.Model.Departamento;
import br.com.george.apprfid.Model.Local;


public class LocalDao {
    private SQLiteDatabase db;

    public LocalDao(SQLiteDatabase db) {
        this.db = db;
    }

    public void inserir(Local local) {
        ContentValues val = new ContentValues();
        val.put("_id", local.getId());
        val.put("nome", local.getNome());
        val.put("descricao", local.getDescricao());
        val.put("departamento", local.getDepartamento().getId());

        db.insert("Local", null, val);
    }

    public void atualizar(Local local) {
        ContentValues val = new ContentValues();
        val.put("nome", local.getNome());
        val.put("descricao", local.getDescricao());
        val.put("departamento", local.getDepartamento().getId());

        db.update("Local", val, "_id=" + local.getId(), null);
    }

    public void deletar(Local local) {
        db.delete("Local", "_id=" + local.getId(), null);
    }

    public List<Local> buscarTodos() {
        List<Local> locais = new ArrayList<>();
        String[] colunas = new String[]{"_id", "nome","descricao", "departamento"};
        DepartamentoDao depDao = new DepartamentoDao(db);

        Cursor cursor = db.query("Local", colunas, null, null, null, null, "nome ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Local local = new Local();

                local.setId(cursor.getInt(0));
                local.setNome(cursor.getString(1));
                local.setDescricao(cursor.getString(2));

                //Buscar os dados referente ao departamento
                local.setDepartamento(depDao.buscarPorId(cursor.getInt(3)));

                locais.add(local);
            }while (cursor.moveToNext());
        }

        return locais;
    }

    public Local buscarPorId(int id) {
        DepartamentoDao departamentoDao = new DepartamentoDao(db);
        Departamento departamento;

        Local local = new Local();
        String[] colunas = new String[]{"_id", "nome","descricao", "departamento"};

        Cursor cursor = db.query("Local",colunas,"_id ="+id, null,null,null,null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            local.setId(cursor.getInt(0));
            local.setNome(cursor.getString(1));
            local.setDescricao(cursor.getString(2));
            departamento = departamentoDao.buscarPorId(cursor.getInt(3));
            local.setDepartamento(departamento);
        }
        else {
            return null;
        }

        return local;
    }

    public void deletarTodos() {
        db.delete("local", "", null);
    }
}
