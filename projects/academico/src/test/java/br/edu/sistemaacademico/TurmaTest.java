import java.util.ArrayList;
import java.util.List;

public class Turma {

    private String codigo;
    private Disciplina disciplina;
    private PeriodoLetivo periodoLetivo;

    private List<Disciplina> disciplinas;

    public Turma(
            String codigo,
            Disciplina disciplina,
            PeriodoLetivo periodoLetivo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O código da turma é obrigatório."
            );
        }

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina é obrigatória."
            );
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "O período letivo é obrigatório."
            );
        }

        this.codigo = codigo;
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;

        this.disciplinas = new ArrayList<Disciplina>();

        // Adiciona a primeira disciplina da turma
        this.disciplinas.add(disciplina);
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

    public List<Disciplina> getDisciplinas() {
        return new ArrayList<Disciplina>(disciplinas);
    }

    public void adicionarDisciplina(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula."
            );
        }

        // Impede disciplina duplicada
        for (Disciplina existente : disciplinas) {

            if (existente.getCodigo()
                    .equalsIgnoreCase(disciplina.getCodigo())) {

                throw new IllegalArgumentException(
                        "A disciplina já está cadastrada nesta turma."
                );
            }
        }

        disciplinas.add(disciplina);
    }

    public void mostrarDados() {

        System.out.println(
                "\nTurma: " + codigo
        );

        System.out.println(
                "Período: " + periodoLetivo
        );

        System.out.println(
                "Disciplinas:"
        );

        for (Disciplina disciplina : disciplinas) {

            System.out.println(
                    "- " + disciplina.getNome()
            );
        }
    }
}