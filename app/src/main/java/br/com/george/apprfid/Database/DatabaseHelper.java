package br.com.george.apprfid.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "patrimonio";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
        Por ser poucas tabelas, vou criar aqui mesmo
        Se desejar, modifique a aplicação para ler de um arquivo SQL.
    */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //region TABELA: Patrimonio
        db.execSQL("CREATE TABLE IF NOT EXISTS Patrimonio" +
                "(" +
                "_cod INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nome VARCHAR(30)," +
                "Descricao VARCHAR(50)," +
                "Identificacao VARCHAR(50)," +
                "statusRegistro VARCHAR(10)" +
                ");");
        //endregion
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sql;
        sql = new StringBuilder();

        db.execSQL(sql.toString());
        onCreate(db);
    }

}
