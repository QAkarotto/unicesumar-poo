package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da matrícula não pode ser nulo ou vazio.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno da matrícula não pode ser nulo.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma da matrícula não pode ser nula.");
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
        return "Matricula{codigo='" + codigo + "', aluno=" + aluno.getNome()
                + ", turma=" + turma.getCodigo() + "}";
    }
}