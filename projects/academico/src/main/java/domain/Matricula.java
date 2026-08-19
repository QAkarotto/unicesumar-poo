package br.edu.sistemaacademico.domain;

public class Matricula {
    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código da matrícula não pode ser nulo ou vazio.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma não pode ser nula.");
        }

        this.codigo = codigo;
        this.aluno = aluno;
        this.turma = turma;
    }

    public String getCodigo() {
        return codigo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    @Override
    public String toString() {
        return "Matrícula: " + codigo + " - " + aluno.getNome() + " → " + turma.getCodigo();
    }
}