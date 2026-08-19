package br.edu.sistemaacademico.domain;

public class Turma {
    private String codigo;
    private Disciplina disciplina;
    private PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.equals("")) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("O período letivo é obrigatório.");
        }
        this.codigo = codigo;
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
    }
    public String getCodigo() {
        return this.codigo;
    }
    public Disciplina getDisciplina() {
        return this.disciplina;
    }
    public PeriodoLetivo getPeriodoLetivo() {
        return this.periodoLetivo;
    }

    @Override
    public String toString() {
        return "Turma " + this.codigo + " - " + this.disciplina.getNome() + " [" + this.periodoLetivo + "]";
    }
}