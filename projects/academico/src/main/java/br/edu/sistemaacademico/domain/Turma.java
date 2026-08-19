package br.edu.sistemaacademico.domain;

public class Turma {

    private String codigo;
    private Disciplina disciplina;
    private PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "Código da turma não pode ser nulo ou vazio."
            );
        }

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina não pode ser nula."
            );
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "Período letivo não pode ser nulo."
            );
        }

        this.codigo = codigo;
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
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