package br.edu.sistemaacademico.domain;

public class Matricula {

    private String codigo;
    private Aluno aluno;
    private Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "Código da matrícula não pode ser nulo ou vazio."
            );
        }

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno não pode ser nulo."
            );
        }

        if (turma == null) {
            throw new IllegalArgumentException(
                    "Turma não pode ser nula."
            );
        }

        this.codigo = codigo;
        this.aluno = aluno;
        this.turma = turma;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "codigo='" + codigo + '\'' +
                ", aluno=" + aluno +
                ", turma=" + turma +
                '}';
    }
}