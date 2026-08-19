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
            throw new IllegalArgumentException("Turma é obrigatória.");
        }

        this.codigo = codigo.trim();
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
        return "Matrícula: " + codigo + " | Aluno: " + aluno + " | Turma: " + turma;
    } // João Pedro Hulchak Kazmierzak RA: 25141620-2 e Hiuri Luciano dos Santos RA: 25208360-2
}