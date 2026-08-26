import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {

    private final String matricula;
    private String nome;
    private String email;

    private final List<Matricula> matriculas;

    public Aluno(
            String matricula,
            String nome,
            String email) {

        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Matricula do aluno obrigatoria."
            );
        }

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Nome do aluno obrigatorio."
            );
        }

        if (email == null
                || !email.contains("@")
                || !email.contains(".")) {

            throw new IllegalArgumentException(
                    "Email invalido."
            );
        }

        this.matricula = matricula.trim();
        this.nome = nome.trim();
        this.email = email.trim();

        this.matriculas = new ArrayList<>();
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    /*
     * Histórico acadêmico do aluno.
     */
    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    /*
     * Adiciona uma matrícula ao histórico.
     */
    public void adicionarMatricula(Matricula matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "Matricula nao pode ser nula."
            );
        }

        matriculas.add(matricula);
    }

    /*
     * Verifica se o aluno já foi aprovado
     * na disciplina informada.
     */
    public boolean foiAprovado(Disciplina disciplina) {

        for (Matricula matricula : matriculas) {

            if (matricula.getOfertaDisciplina()
                    .getDisciplina()
                    .equals(disciplina)) {

                if (matricula.getResultado()
                        == ResultadoAcademico.APROVADO) {

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {

        return "Aluno: " + nome
                + " | Matricula: " + matricula
                + " | Email: " + email;
    }
}