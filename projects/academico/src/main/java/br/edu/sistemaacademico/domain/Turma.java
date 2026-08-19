package br.edu.sistemaacademico.domain;

public class Turma {

    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        validarCodigo(codigo);
        validarDisciplina(disciplina);
        validarPeriodoLetivo(periodoLetivo);

        this.codigo = codigo;
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Código da turma não pode ser nulo ou vazio");
        }
    }

    private void validarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina não pode ser nula");
        }
    }

    private void validarPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "Período letivo não pode ser nulo");
        }
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
        return String.format(
                "Turma{codigo='%s', disciplina=%s, periodoLetivo=%s}",
                codigo,
                disciplina.getNome(),
                periodoLetivo
        );
    }
}