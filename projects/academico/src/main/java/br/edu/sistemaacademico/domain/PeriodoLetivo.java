package br.edu.sistemaacademico.domain;

/**
 * Representa o Período Letivo (a linha do tempo, não a do Trunks do Futuro).
 */
public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano < 1900) {
            throw new IllegalArgumentException("Ano letivo inválido.");
        }
        if (semestre == null) {
            throw new IllegalArgumentException("O semestre é obrigatório.");
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
        return "PeriodoLetivo{" +
                "ano=" + ano +
                ", semestre=" + semestre +
                '}';
    }
}