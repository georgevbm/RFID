package br.com.george.apprfid.Model;

import br.com.george.apprfid.Model.Local;
import java.io.Serializable;
import java.util.Date;

public class Patrimonio implements Serializable {

    //IDENTITY DA TABELA
    private int cod;
    private String nome;
    private String descricao;
    private String identificacao;
    private String estado;
    private Date dataEntrada;
    private Local local;
    private Responsavel responsavel;
    private Boolean statusRegistro;
    private boolean enviarBancoOnline;
    private boolean atualizarBancoOnline;
    private int id;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Responsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public boolean isEnviarBancoOnline() {
        return enviarBancoOnline;
    }

    public void setEnviarBancoOnline(boolean enviarBancoOnline) {
        this.enviarBancoOnline = enviarBancoOnline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patrimonio that = (Patrimonio) o;

        return !(identificacao != null ? !identificacao.equals(that.identificacao) : that.identificacao != null);

    }

    @Override
    public int hashCode() {
        return identificacao != null ? identificacao.hashCode() : 0;
    }

    public Boolean getStatusRegistro() {
        return statusRegistro;
    }

    public void setStatusRegistro(Boolean statusRegistro) {
        this.statusRegistro = statusRegistro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtualizarBancoOnline() {
        return atualizarBancoOnline;
    }

    public void setAtualizarBancoOnline(boolean atualizarBancoOnline) {
        this.atualizarBancoOnline = atualizarBancoOnline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
