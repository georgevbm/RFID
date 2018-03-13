package br.com.george.apprfid.Database.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.george.apprfid.Model.Local;
import br.com.george.apprfid.Model.Patrimonio;
import br.com.george.apprfid.Model.Responsavel;


public class PatrimonioDao {
    private SQLiteDatabase db;
    String[] colunas = new String[]{"_cod", "nome", "descricao", "dataentrada", "identificacao", "estado",
            "local", "responsavel", "statusRegistro", "enviarbancoonline", "atualizarBancoOnline",
            "idGrails"};

    public PatrimonioDao(SQLiteDatabase db) {
        this.db = db;
    }

    public void inserir(Patrimonio patrimonio) {
        String statusRegistro;
        ContentValues val = new ContentValues();

        try {
            val.put("nome", patrimonio.getNome());
            val.put("dataEntrada", String.valueOf(patrimonio.getDataEntrada()));
            val.put("descricao", patrimonio.getDescricao());
            val.put("estado", patrimonio.getEstado());
            val.put("identificacao", patrimonio.getIdentificacao());
            val.put("local", patrimonio.getLocal().getId());
            val.put("responsavel", patrimonio.getResponsavel().getId());

            if (patrimonio.getStatusRegistro() == true)
                val.put("statusRegistro", "t");
            else
                val.put("statusRegistro", "f");

            val.put("enviarBancoOnline", patrimonio.isEnviarBancoOnline() ? 1 : 0);
            val.put("atualizarBancoOnline", patrimonio.isAtualizarBancoOnline() ? 1 : 0);
            val.put("idGrails", patrimonio.getId());

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
        val.put("estado", patrimonio.getEstado());
        val.put("identificacao", patrimonio.getIdentificacao());
        val.put("local", patrimonio.getLocal().getId());
        val.put("responsavel", patrimonio.getResponsavel().getId());

        if (patrimonio.getStatusRegistro() == true)
            val.put("statusRegistro", "t");
        else
            val.put("statusRegistro", "f");

        val.put("enviarBancoOnline", patrimonio.isEnviarBancoOnline() ? 1 : 0);
        val.put("atualizarBancoOnline", patrimonio.isAtualizarBancoOnline() ? 1 : 0);

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
        LocalDao localDao = new LocalDao(db);
        ResponsavelDao responsavelDao = new ResponsavelDao(db);
        List<Patrimonio> patrimonios = new ArrayList<>();

        Cursor cursor = db.query("Patrimonio", colunas, null, null, null, null, "dataEntrada ASC");

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

    public List<Patrimonio> buscarTodosParaInserir() {
        LocalDao localDao = new LocalDao(db);
        ResponsavelDao responsavelDao = new ResponsavelDao(db);
        List<Patrimonio> patrimonios = new ArrayList<>();

        Cursor cursor = db.query("Patrimonio", colunas, "enviarBancoOnline = 1", null, null, null, "dataEntrada ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                //patrimonios.add(iniciarPatrimonio(cursor, false));
                patrimonios.add(iniciarPatrimonio(cursor, true));
            } while (cursor.moveToNext());
        }

        return patrimonios;
    }

    public List<Patrimonio> buscarTodosParaAlterar() {
        LocalDao localDao = new LocalDao(db);
        ResponsavelDao responsavelDao = new ResponsavelDao(db);
        List<Patrimonio> patrimonios = new ArrayList<>();

        Cursor cursor = db.query("Patrimonio", colunas, "atualizarBancoOnline = 1", null, null, null, "dataEntrada ASC");

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
        Patrimonio patrimonio = new Patrimonio();
        Local local;
        Responsavel responsavel;
        LocalDao localDao = new LocalDao(db);
        ResponsavelDao responsavelDao = new ResponsavelDao(db);

        if (adicionarId)
            patrimonio.setCod(cursor.getInt(0));

        patrimonio.setNome(cursor.getString(1));
        patrimonio.setDescricao(cursor.getString(2));
        patrimonio.setIdentificacao(cursor.getString(4));
        patrimonio.setEstado(cursor.getString(5));

        local = localDao.buscarPorId(cursor.getInt(6));

        patrimonio.setLocal(local);

        responsavel = responsavelDao.buscarPorId(cursor.getInt(7));
        patrimonio.setResponsavel(responsavel);

        if (cursor.getString(8).equals("t"))
            patrimonio.setStatusRegistro(true);
        else
            patrimonio.setStatusRegistro(false);

        if (cursor.getString(9).equals("1"))
            patrimonio.setEnviarBancoOnline(true);
        else
            patrimonio.setEnviarBancoOnline(false);

        if (cursor.getString(10).equals("1"))
            patrimonio.setAtualizarBancoOnline(true);
        else
            patrimonio.setAtualizarBancoOnline(false);

        patrimonio.setId(cursor.getInt(11));

        return patrimonio;
    }
    //endregion

    //DELETA TODOS QUE JÁ ESTÃO NA BASE ONLINE
    public void deletarTodos() {
        db.delete("patrimonio", "enviarBancoOnline=0", null);
    }

    public void deletarTodosNovos() {
        db.delete("patrimonio", "enviarBancoOnline=1", null);
    }

    //INFORMA QUE O REGISTRO NÃO DEVE SER ENVIADO NOVAMENTE
    public void registroEnviado(List<Patrimonio> patrimonios) {
        ContentValues val = new ContentValues();

        for (Patrimonio patrimonio: patrimonios) {
            val.put("enviarBancoOnline", "0");
            val.put("atualizarBancoOnline", "1");

            try {
                db.update("Patrimonio", val, "_cod=" + patrimonio.getCod(), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
