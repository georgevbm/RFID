package br.com.george.apprfid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.george.apprfid.Model.Patrimonio;

public class PatrimonioDAO {

    DatabaseUtil databaseUtil;

    public PatrimonioDAO(Context context) {
        databaseUtil = new DatabaseUtil(context);
    }

    public void Salvar(Patrimonio patrimonio) {
        ContentValues contentValues = new ContentValues();

        /*MONTANDO OS PARAMETROS PARA SEREM SALVOS*/
        contentValues.put("nome", patrimonio.getNome());
        contentValues.put("descricao", patrimonio.getDescricao());
        contentValues.put("identificacao", patrimonio.getIdentificacao());

        /*EXECUTANDO INSERT DE UM NOVO REGISTRO*/
        databaseUtil.GetConexaoDataBase().insert("tags", null, contentValues);
    }

    public void Atualizar(Patrimonio patrimonio) {
        ContentValues contentValues = new ContentValues();

        /*MONTA OS PARAMENTROS PARA REALIZAR UPDATE NOS CAMPOS*/
        contentValues.put("nome", patrimonio.getNome());
        contentValues.put("descricao", patrimonio.getDescricao());
        contentValues.put("identificacao", patrimonio.getIdentificacao());

        /*REALIZANDO UPDATE PELA CHAVE DA TABELA*/
        databaseUtil.GetConexaoDataBase().update("tags", contentValues, "id = ?", new String[]{Integer.toString(patrimonio.getCod())});
    }

    public int Excluir(int codigo) {
        //EXCLUINDO  REGISTRO E RETORNANDO O NÚMERO DE LINHAS AFETADAS
        return databaseUtil.GetConexaoDataBase().delete("tags", "id = ?", new String[]{Integer.toString(codigo)});
    }

    public Patrimonio GetPatrimonio(int codigo) {
        Cursor cursor = databaseUtil.GetConexaoDataBase().rawQuery("SELECT * FROM tags WHERE id= " + codigo, null);

        cursor.moveToFirst();

        ///CRIANDO UMA NOVA PESSOAS
        Patrimonio patrimonio = new Patrimonio();

        //ADICIONANDO OS DADOS DA PESSOA
        patrimonio.setCod(cursor.getInt(cursor.getColumnIndex("id")));
        patrimonio.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        patrimonio.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        patrimonio.setIdentificacao(cursor.getString(cursor.getColumnIndex("identificacao")));

        //RETORNANDO A PESSOA
        return patrimonio;
    }

    public List<Patrimonio> SelecionarTodos() {

        List<Patrimonio> patrimonios = new ArrayList<Patrimonio>();

        //MONTA A QUERY A SER EXECUTADA
        StringBuilder stringBuilderQuery = new StringBuilder();
        stringBuilderQuery.append(" SELECT id,            ");
        stringBuilderQuery.append("        nome,          ");
        stringBuilderQuery.append("        descricao,     ");
        stringBuilderQuery.append("        identificacao ");
        stringBuilderQuery.append("  FROM  tags           ");
        stringBuilderQuery.append(" ORDER BY id           ");

        //CONSULTANDO OS REGISTROS CADASTRADOS
        Cursor cursor = databaseUtil.GetConexaoDataBase().rawQuery(stringBuilderQuery.toString(), null);

        /*POSICIONA O CURSOR NO PRIMEIRO REGISTRO*/
        cursor.moveToFirst();

        Patrimonio patrimonio;

        //REALIZA A LEITURA DOS REGISTROS ENQUANTO NÃO FOR O FIM DO CURSOR
        while (!cursor.isAfterLast()) {

            /* CRIANDO UMA NOVA PESSOAS */
            patrimonio = new Patrimonio();

            //ADICIONANDO OS DADOS DA PESSOA
            patrimonio.setCod(cursor.getInt(cursor.getColumnIndex("id")));
            patrimonio.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            patrimonio.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            patrimonio.setIdentificacao(cursor.getString(cursor.getColumnIndex("identificacao")));

            //ADICIONANDO UMA PESSOA NA LISTA
            patrimonios.add(patrimonio);

            //VAI PARA O PRÓXIMO REGISTRO
            cursor.moveToNext();
        }

        //RETORNANDO A LISTA DE PESSOAS
        return patrimonios;
    }
}
