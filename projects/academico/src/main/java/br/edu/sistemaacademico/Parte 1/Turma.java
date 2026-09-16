public class Turma {

    private String codigo;
    private Disciplina disciplina;
    private PeriodoLetivo periodoLetivo;

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        this.codigo = codigo;
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public void mostrarDados() {
        System.out.println("\n===== TURMA =====");
        System.out.println("Código da turma: " + codigo);
        System.out.println("Disciplina: " + disciplina.getNome());
        System.out.println("Período: "
                + periodoLetivo.getAno()
                + " - "
                + periodoLetivo.getSemestre());
    }
}