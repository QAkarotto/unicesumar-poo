package br.edu.sistemaacademico.domain;

public class Turma {
    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da turma é obrigatório.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("Turma precisa estar vinculada a uma disciplina.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Turma precisa ter um período letivo definido.");
        }

        this.codigo = codigo.trim();
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
    }

    @Override
    public String toString() {
        return String.format("Turma %s | %s | %s", codigo, disciplina, periodoLetivo);
    }
}