package br.edu.sistemaacademico.domain;

public class Matricula {
    private String codigo;
    private Aluno aluno;
    private Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da matrícula é obrigatório.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma é obrigatório.");
        }

        this.codigo = codigo;
        this.aluno = aluno;
        this.turma = turma;
    }

    @Override
    public String toString() {
    return codigo + " - " + aluno + " - " + turma;
    }
}