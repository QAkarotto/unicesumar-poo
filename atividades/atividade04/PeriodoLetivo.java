package br.edu.sistemaacademico.domain;

/**
 * Representa um período letivo (ano + semestre).
 *
 * É um objeto de valor: ano e semestre são definidos na criação
 * e não mudam depois, pois um período letivo diferente é,
 * conceitualmente, outro período.
 */
public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano do período letivo deve ser positivo.");
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
        String numeroSemestre = semestre == Semestre.PRIMEIRO ? "1" : "2";
        return ano + "/" + numeroSemestre;
    }
}
