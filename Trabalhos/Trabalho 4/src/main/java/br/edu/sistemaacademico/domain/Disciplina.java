package br.edu.sistemaacademico.domain;

public class Disciplina {
    private String sigla;
    private String nome;
    private int cargaHoraria;


    public Disciplina(String sigla, String nome, int cargaHoraria) {
        this.sigla = sigla;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public String getCodigo() {
        return sigla;
    }

    public String getSigla() {
        return sigla;
    }

    public String getNome() {
        return nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public String toString() {
        return nome;
    }
}