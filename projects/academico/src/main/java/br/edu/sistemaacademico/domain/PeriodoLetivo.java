package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        validarAno(ano);
        validarSemestre(semestre);

        this.ano = ano;
        this.semestre = semestre;
    }

    private void validarAno(int ano) {
        if (ano <= 0) {
            throw new IllegalArgumentException(
                    "Ano deve ser positivo, fornecido: " + ano);
        }
    }

    private void validarSemestre(Semestre semestre) {
        if (semestre == null) {
            throw new IllegalArgumentException(
                    "Semestre não pode ser nulo");
        }
    }

    public int getAno() {
        return ano;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    @Override
    public String toString() {
        return String.format(
                "PeriodoLetivo{ano=%d, semestre=%s}",
                ano,
                semestre
        );
    }
}