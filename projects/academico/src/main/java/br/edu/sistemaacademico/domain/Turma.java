package br.edu.sistemaacademico.domain;

public class Turma {

    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;

    public Turma(
            String codigo,
            Disciplina disciplina,
            PeriodoLetivo periodoLetivo
    ) {
        validarTexto(codigo, "Código");

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula."
            );
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "O período letivo não pode ser nulo."
            );
        }

        this.codigo = codigo;
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
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
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", disciplina=" + disciplina +
                ", periodoLetivo=" + periodoLetivo +
                '}';
    }
}
