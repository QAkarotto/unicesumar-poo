package br.edu.sistemaacademico.domain;

public class Disciplina {

    private String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código é obrigatório.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }

        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("Carga horária deve ser positiva.");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public String toString() {
        return codigo + " " + nome + " " + cargaHoraria + "h";
    }
}