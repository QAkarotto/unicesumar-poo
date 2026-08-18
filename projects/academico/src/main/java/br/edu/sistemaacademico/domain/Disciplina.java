package br.edu.sistemaacademico.domain;

public class Disciplina {

    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da disciplina não pode ser nulo ou vazio.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da disciplina não pode ser nulo ou vazio.");
        }
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser estritamente positiva.");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public String toString() {
        return nome + " [" + codigo + "] - " + cargaHoraria + "h";
    }
}