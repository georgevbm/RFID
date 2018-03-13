package br.com.george.apprfid.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.george.apprfid.Database.DAO.DepartamentoDao;
import br.com.george.apprfid.Database.DAO.LocalDao;
import br.com.george.apprfid.Database.DAO.PatrimonioDao;
import br.com.george.apprfid.Database.DAO.ResponsavelDao;
import br.com.george.apprfid.Model.Departamento;
import br.com.george.apprfid.Model.Local;
import br.com.george.apprfid.Model.Patrimonio;
import br.com.george.apprfid.Model.Responsavel;


public class Database {
    private SQLiteDatabase db;

    public Database(Context ctx) {
        DatabaseHelper cDb = new DatabaseHelper(ctx);
        db = cDb.getWritableDatabase();
    }

    //region Departamento
    public void inserirDepartamento(Departamento departamento) {
        new DepartamentoDao(db).inserir(departamento);
    }

    public void atualizarDepartamento(Departamento departamento) {
        new DepartamentoDao(db).atualizar(departamento);
    }

    public void deletarDepartamento(Departamento departamento) {
        new DepartamentoDao(db).deletar(departamento);
    }

    public List<Departamento> buscarDepartamentos() {
        return new DepartamentoDao(db).buscarTodos();
    }

    public Departamento buscarDepartamento(int id) {
        return new DepartamentoDao(db).buscarPorId(id);
    }

    public void deletarTodosDepartamentos() {
        new DepartamentoDao(db).deletarTodos();
    }
    //endregion

    //region Local
    public void inserirLocal(Local local) {
        new LocalDao(db).inserir(local);
    }

    public void atualizarLocal(Local local) {
        new LocalDao(db).atualizar(local);
    }

    public void deletarLocal(Local local) {
        new LocalDao(db).deletar(local);
    }

    public List<Local> buscarLocais() {
        return new LocalDao(db).buscarTodos();
    }

    public Local buscarLocal(int id) {
        return new LocalDao(db).buscarPorId(id);
    }

    public void deletarTodosLocais() {
        new LocalDao(db).deletarTodos();
    }
    //endregion

    //region Responsavel
    public void inserirResponsavel(Responsavel responsavel) {
        new ResponsavelDao(db).inserir(responsavel);
    }

    public void atualizarResponsavel(Responsavel responsavel) {
        new ResponsavelDao(db).atualizar(responsavel);
    }

    public void deletarResponsavel(Responsavel responsavel) {
        new ResponsavelDao(db).deletar(responsavel);
    }

    public List<Responsavel> buscarResponsaveis() {
        return new ResponsavelDao(db).buscarTodos();
    }

    public Responsavel buscarResponsavel(int id) {
        return new ResponsavelDao(db).buscarPorId(id);
    }

    public void deletarTodosResponsaveis() {
        new ResponsavelDao(db).deletarTodos();
    }
    //endregion

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
