public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(
            int ano,
            Semestre semestre) {

        if (ano < 2000) {
            throw new IllegalArgumentException(
                    "Ano letivo invalido."
            );
        }

        if (semestre == null) {
            throw new IllegalArgumentException(
                    "Semestre obrigatorio."
            );
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

        return ano + "/" + semestre;
    }
}