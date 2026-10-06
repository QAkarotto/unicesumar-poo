import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;

    private final List<Matricula> matriculas;

    public OfertaDisciplina(
            Disciplina disciplina,
            Turma turma) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina obrigatoria."
            );
        }

        if (turma == null) {
            throw new IllegalArgumentException(
                    "Turma obrigatoria."
            );
        }

        this.disciplina = disciplina;
        this.turma = turma;

        this.matriculas = new ArrayList<>();
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    /*
     * Consulta as matrículas desta oferta.
     */
    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    /*
     * Realiza matrícula de um aluno.
     */
    public Matricula matricular(Aluno aluno) {

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno nao pode ser nulo."
            );
        }

        /*
         * REGRA:
         * aluno aprovado anteriormente na disciplina
         * não pode cursar novamente.
         */
        if (aluno.foiAprovado(disciplina)) {

            throw new IllegalStateException(
                    "Aluno ja foi aprovado nesta disciplina."
            );
        }

        /*
         * REGRA:
         * aluno não pode ter duas matrículas
         * na mesma oferta.
         */
        for (Matricula matricula : matriculas) {

            if (matricula.getAluno() == aluno) {

                throw new IllegalStateException(
                        "Aluno ja possui matricula nesta oferta."
                );
            }
        }

        /*
         * Número gerado automaticamente.
         */
        String numero =
                "MAT-" + (matriculas.size() + 1);

        Matricula matricula =
                new Matricula(
                        numero,
                        aluno,
                        this
                );

        matriculas.add(matricula);

        /*
         * A matrícula também entra
         * no histórico do aluno.
         */
        aluno.adicionarMatricula(matricula);

        return matricula;
    }

    @Override
    public String toString() {

        return disciplina.getDescricao()
                + " - "
                + turma.getCodigo();
    }
}