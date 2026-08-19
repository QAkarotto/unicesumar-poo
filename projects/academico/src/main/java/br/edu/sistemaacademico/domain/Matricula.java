package br.edu.sistemaacademico.domain;

public class Matricula {
    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da matrícula é obrigatório.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("Matrícula precisa estar vinculada a um aluno.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Matrícula precisa estar vinculada a uma turma.");
        }

        this.codigo = codigo.trim();
        this.aluno = aluno;
        this.turma = turma;
    }

    @Override
    public String toString() {
        return String.format("%s: %s em %s", codigo, aluno, turma);
    }
}