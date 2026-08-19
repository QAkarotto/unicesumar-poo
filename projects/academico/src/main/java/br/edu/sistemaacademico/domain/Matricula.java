package br.edu.sistemaacademico.domain;

public class Matricula {
    private String codigo;
    private Aluno aluno;
    private Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        if (codigo == null || codigo.equals("")) {
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

    public String getCodigo() {
        return this.codigo;
    }
    public Aluno getAluno() {
        return this.aluno;
    }
    public Turma getTurma() {
        return this.turma;
    }

    @Override
    public String toString() {
        return "Matrícula " + this.codigo + " | Aluno: " + this.aluno.getNome() + " | " + this.turma;
    }
}