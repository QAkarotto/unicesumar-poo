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
        if (ano < 1900 || ano > 2100) {
            throw new IllegalArgumentException("Ano deve estar entre 1900 e 2100");
        }
    }

    private void validarSemestre(Semestre semestre) {
        if (semestre == null) {
            throw new IllegalArgumentException("Semestre não pode ser nulo");
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
        return "PeriodoLetivo{" +
                "ano=" + ano +
                ", semestre=" + semestre.getDescricao() +
                '}';
    }
}
