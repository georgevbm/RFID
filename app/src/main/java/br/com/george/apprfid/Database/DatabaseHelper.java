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
        //region TABELA: Departamento
        db.execSQL("CREATE TABLE Departamento" +
                "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Sigla TEXT, " +
                "Nome TEXT);");
        //endregion

        //region TABELA: Local
        db.execSQL("CREATE TABLE Local " +
                "(" +
                "_id INTEGER, " +
                "Departamento INTEGER," +
                "Nome TEXT," +
                "Descricao TEXT, " +
                "FOREIGN KEY (departamento) REFERENCES Departamento(_id)" +
                ");");
        //endregion

        //region TABELA: Responsável
        db.execSQL("CREATE TABLE Responsavel (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Siape TEXT, " +
                "Nome TEXT, " +
                "Telefone TEXT, " +
                "Funcao TEXT);");
        //endregion

        //region TABELA: Patrimonio
        db.execSQL("CREATE TABLE Patrimonio" +
                "(" +
                "_cod INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nome TEXT," +
                "Descricao TEXT," +
                "Identificacao TEXT," +
                "Estado TEXT, DataEntrada TEXT," +
                "Local INTEGER," +
                "Responsavel INTEGER," +
                "statusRegistro TEXT," +
                "enviarBancoOnline TEXT," +
                "atualizarBancoOnline TEXT," +
                "idGrails INT," +
                "FOREIGN KEY (Local) REFERENCES Local(_id)," +
                "FOREIGN KEY (Responsavel) REFERENCES Responsavel(_id)" +
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
