package br.edu.sistemaacademico.domain;

/**
 * Representa uma turma: a oferta de uma disciplina em um período letivo.
 *
 * Código, disciplina e período letivo são definidos na criação e
 * permanecem imutáveis: mudar a disciplina ou o período de uma turma
 * já criada não faz sentido de negócio, seria outra turma.
 */
public class Turma {

    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da turma é obrigatório.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Período letivo é obrigatório.");
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

    @Override
    public String toString() {
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", disciplina=" + disciplina.getNome() +
                ", periodo=" + periodoLetivo +
                '}';
    }
}
