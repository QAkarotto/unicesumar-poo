public class PeriodoLetivo {

    private int ano;
    private Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        this.ano = ano;
        this.semestre = semestre;
    }

    public int getAno() {
        return ano;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void mostrarDados() {
        System.out.println(ano + " - " + semestre);
    }
}