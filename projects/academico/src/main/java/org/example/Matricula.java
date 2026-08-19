package org.example;

    public class Matricula {
    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("O código da matrícula é obrigatório.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("A turma é obrigatória.");
        }

        this.codigo = codigo;
        this.aluno = aluno;
        this.turma = turma;
    }

    public String getCodigo() { return codigo; }
    public Aluno getAluno() { return aluno; }
    public Turma getTurma() { return turma; }

    @Override
    public String toString() {
        return "Matrícula [Código: " + codigo + "\n  " + aluno + "\n  " + turma + "]";
    }
}

