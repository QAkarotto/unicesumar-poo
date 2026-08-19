package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {

    private int ano;
    private Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano inválido.");
        }

        if (semestre == null) {
            throw new IllegalArgumentException("Semestre é obrigatório.");
        }

        this.ano = ano;
        this.semestre = semestre;
    }

    public int getAno() {
        return ano;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    @Override
    public String toString() {
        return "Período: " + ano + " | Semestre: " + semestre;
    } // João Pedro Hulchak Kazmierzak RA: 25141620-2 e Hiuri Luciano dos Santos RA: 25208360-2
}