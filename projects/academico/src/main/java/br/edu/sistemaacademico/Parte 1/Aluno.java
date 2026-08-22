import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private String identificadorAcademico;
    private String nome;
    private String email;
    private List<Matricula> matriculas;

    public Aluno(String identificadorAcademico, String nome, String email) {
        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
        this.matriculas = new ArrayList<>();
    }

    public String getIdentificadorAcademico() {
        return identificadorAcademico;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    // O próprio Aluno controla suas matrículas.
    public void adicionarMatricula(Matricula matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException(
                "A matrícula não pode ser nula."
            );
        }

        if (matriculas.contains(matricula)) {
            throw new IllegalArgumentException(
                "Essa matrícula já foi adicionada."
            );
        }

        matriculas.add(matricula);
    }

    public void mostrarDados() {

        System.out.println("\n===== DADOS DO ALUNO =====");
        System.out.println(
            "ID acadêmico: " + identificadorAcademico
        );
        System.out.println(
            "Nome: " + nome
        );
        System.out.println(
            "E-mail: " + email
        );
    }

    public void mostrarMatriculas() {

        System.out.println(
            "\n===== MINHAS MATRÍCULAS ====="
        );

        if (matriculas.isEmpty()) {
            System.out.println(
                "Você não possui matrículas."
            );
            return;
        }

        for (Matricula matricula : matriculas) {
            matricula.mostrarDados();
        }
    }
}