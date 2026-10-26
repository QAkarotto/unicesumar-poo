package br.edu.sistemaacademico.domain;

public class Disciplina {
    private String sigla;
    private String nome;
    private int cargaHoraria;


    public Disciplina(String sigla, String nome, int cargaHoraria) {
        if (sigla == null || sigla.isBlank()) {
            throw new IllegalArgumentException("Código da disciplina é obrigatório");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da disciplina é obrigatório");
        }
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("Carga horária deve ser maior que zero");
        }
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