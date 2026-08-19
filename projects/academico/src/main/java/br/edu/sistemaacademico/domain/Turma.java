package br.edu.sistemaacademico.domain;

// Trocar a disciplina ou o período de uma turma criaria outra turma,
// então nada aqui pode ser alterado depois da criação.
public class Turma {

    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        this.codigo = validarCodigo(codigo);
        this.disciplina = validarDisciplina(disciplina);
        this.periodoLetivo = validarPeriodoLetivo(periodoLetivo);
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

    private static String validarCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }
        return codigo.strip();
    }

    private static Disciplina validarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A turma precisa de uma disciplina.");
        }
        return disciplina;
    }

    private static PeriodoLetivo validarPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("A turma precisa de um período letivo.");
        }
        return periodoLetivo;
    }

    @Override
    public String toString() {
        return codigo + " | " + disciplina.getNome() + " | " + periodoLetivo;
    }
}
