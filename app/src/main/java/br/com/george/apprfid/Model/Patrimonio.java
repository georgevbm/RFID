package br.com.george.apprfid.Model;

import java.io.Serializable;

public class Patrimonio implements Serializable {

    //IDENTITY DA TABELA
    private int cod;
    private String nome;
    private String descricao;
    private String identificacao;
    private Boolean statusRegistro;

    public int getCod() {
        return cod;
    }
    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

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

    public Boolean getStatusRegistro() {
        return statusRegistro;
    }
    public void setStatusRegistro(Boolean statusRegistro) {
        this.statusRegistro = statusRegistro;
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




}
