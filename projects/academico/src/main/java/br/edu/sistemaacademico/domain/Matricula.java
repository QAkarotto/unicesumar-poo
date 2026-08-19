package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da matrícula é obrigatório e não pode ser vazio.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno da matrícula é obrigatório.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma da matrícula é obrigatória.");
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
        return "Matrícula [" + codigo + "] " + aluno.getNome() + " em " + turma.getCodigo();
    }
}