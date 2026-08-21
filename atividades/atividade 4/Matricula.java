package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        this.codigo = validarObrigatorio(codigo, "Código");

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

    private static String validarObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser nulo ou vazio."
            );
        }

        return valor;
    }

    @Override
    public String toString() {
        // Até uma fusão precisa começar com objetos em estados válidos.
        return "Matricula{" +
                "codigo='" + codigo + '\'' +
                ", aluno=" + aluno +
                ", turma=" + turma +
                '}';
    }
}
