package br.edu.sistemaacademico.domain;

/**
 * Representa um período letivo (ano + semestre).
 *
 * <p>Ano e semestre juntos identificam um período letivo específico: não
 * faz sentido "mudar de período" mantendo o mesmo objeto, então a classe é
 * totalmente imutável. O semestre só aceita os valores definidos no enum
 * {@link Semestre}, o que já elimina, por construção, qualquer semestre
 * inválido.</p>
 */
public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano do período letivo deve ser positivo.");
        }
        if (semestre == null) {
            throw new IllegalArgumentException("Semestre do período letivo é obrigatório.");
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
        return ano + "/" + (semestre == Semestre.PRIMEIRO ? "1" : "2");
    }
}
