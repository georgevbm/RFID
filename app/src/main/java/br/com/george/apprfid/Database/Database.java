package br.com.george.apprfid.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{

    //NOME DA BASE DE DADOS
    private static final String NOME_BANCO  = "rfid.db";
    //VERSÃO DO BANCO DE DADOS
    private static final int VERSAO_BANCO = 1;

    public Database(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder stringBuilderCreateTable = new StringBuilder();

        stringBuilderCreateTable.append(" CREATE TABLE tags (");
        stringBuilderCreateTable.append("        id             INTEGER PRIMARY KEY AUTOINCREMENT, ");
        stringBuilderCreateTable.append("        nome           TEXT    NOT NULL,            ");
        stringBuilderCreateTable.append("        descricao      TEXT    NOT NULL,            ");
        stringBuilderCreateTable.append("        identificacao  TEXT    NOT NULL)            ");

        db.execSQL(stringBuilderCreateTable.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS tb_pessoa");
        onCreate(db);
    }

    public SQLiteDatabase GetConexaoDataBase(){
        return this.getWritableDatabase();
    }
}
