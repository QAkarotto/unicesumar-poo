package br.edu.sistemaacademico.domain;

public class Turma {

    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da turma é obrigatório e não pode ser vazio.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina da turma é obrigatória.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Período letivo da turma é obrigatório.");
        }
        this.codigo = codigo.trim();
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
        return "Turma [" + codigo + "] " + disciplina.getNome() + " - " + periodoLetivo;
    }
}