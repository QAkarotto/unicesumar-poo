package br.edu.sistemaacademico.domain;

public class PeriodoLetivo {
    private int ano;
    private String semestre;

    public PeriodoLetivo(int ano, String semestre) {
        this.ano = ano;
        this.semestre = semestre;
    }

    public int getAno() {
        return ano;
    }

    public String getSemestre() {
        return semestre;
    }
}