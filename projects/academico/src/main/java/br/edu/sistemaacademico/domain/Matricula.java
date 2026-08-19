package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        validarTexto(codigo, "Código");

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno não pode ser nulo."
            );
        }

        if (turma == null) {
            throw new IllegalArgumentException(
                    "A turma não pode ser nula."
            );
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

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser nulo ou vazio."
            );
        }
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
