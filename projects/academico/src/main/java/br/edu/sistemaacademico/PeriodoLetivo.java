package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {
    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano deve ser positivo.");
        }
        if (semestre == null) {
            throw new IllegalArgumentException("O semestre não pode ser nulo.");
        }

        this.ano = ano;
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return String.format("%d/%s", ano, semestre);
    } // João Pedro Hulchak Kazmierzak RA: 25141620-2 e Hiuri Luciano dos Santos RA: 25208360-2
}