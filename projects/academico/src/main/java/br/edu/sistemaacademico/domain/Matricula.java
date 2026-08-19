package br.edu.sistemaacademico.domain;

public class Matricula {
    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código da matrícula é obrigatório.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma é obrigatória.");
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
        return "Matrícula [Código=" + codigo + ", Aluno=" + aluno.getNome() + ", Turma=" + turma.getCodigo() + "]";
    }
}