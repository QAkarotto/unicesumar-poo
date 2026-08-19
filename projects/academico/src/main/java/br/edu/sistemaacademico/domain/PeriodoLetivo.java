package br.edu.sistemaacademico.domain;

// Um período letivo é um valor fechado: ano e semestre nunca mudam.
// Para representar outro período basta criar um novo objeto.
public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        this.ano = validarAno(ano);
        this.semestre = validarSemestre(semestre);
    }

    public int getAno() {
        return ano;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    private static int validarAno(int ano) {
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano do período letivo deve ser positivo: " + ano);
        }
        return ano;
    }

    private static Semestre validarSemestre(Semestre semestre) {
        if (semestre == null) {
            throw new IllegalArgumentException("O semestre do período letivo é obrigatório.");
        }
        return semestre;
    }

    @Override
    public String toString() {
        return ano + " - " + semestre.getDescricao();
    }
}
