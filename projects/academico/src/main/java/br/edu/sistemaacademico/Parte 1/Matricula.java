public class Matricula {

    private String codigo;
    private Aluno aluno;
    private Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        this.codigo = codigo;
        this.aluno = aluno;
        this.turma = turma;
    }

    public String getCodigo() {
        return codigo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    public void mostrarDados() {
        System.out.println("-----------------------------");
        System.out.println("Matrícula: " + codigo);
        System.out.println("Disciplina: " + turma.getDisciplina().getNome());
        System.out.println("Código da disciplina: "
                + turma.getDisciplina().getCodigo());
        System.out.println("Carga horária: "
                + turma.getDisciplina().getCargaHoraria()
                + " horas");
        System.out.println("Semestre: "
                + turma.getPeriodoLetivo().getAno()
                + " - "
                + turma.getPeriodoLetivo().getSemestre());
    }
}