package br.edu.sistemaacademico;

public class Turma {

    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina da turma é obrigatória.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("O período letivo da turma é obrigatório.");
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
        return String.format("Turma{codigo=%s, disciplina=%s, periodo=%s}",
                codigo, disciplina.getNome(), periodoLetivo);
    }
}