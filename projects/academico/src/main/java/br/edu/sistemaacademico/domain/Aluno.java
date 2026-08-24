package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Representa um aluno do sistema acadêmico.
 *
 * <p>Além dos dados cadastrais, o aluno mantém seu próprio histórico de
 * matrículas. É o aluno quem sabe se já foi aprovado em uma disciplina
 * (independentemente da turma ou período letivo em que isso aconteceu),
 * então essa verificação é responsabilidade dele, não de quem está tentando
 * matriculá-lo.</p>
 */
public class Aluno {

    private static final Pattern EMAIL_VALIDO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,}$");

    private final String identificadorAcademico;
    private final String nome;
    private String email;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException("Identificador acadêmico é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do aluno é obrigatório.");
        }

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = validarEmail(email);
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

 
    public void setEmail(String novoEmail) {
        this.email = validarEmail(novoEmail);
    }

    /**
     * Retorna o histórico de matrículas do aluno (uma cópia somente leitura,
     * para que ninguém de fora consiga adicionar ou remover matrículas sem
     * passar pelas regras de {@link OfertaDisciplina#matricular(Aluno)}).
     */
    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    /**
     * Registra uma matrícula no histórico do aluno.
     *
     * <p>Visibilidade de pacote: só quem está criando a matrícula
     * ({@link OfertaDisciplina}) pode chamar este método — o aluno não expõe
     * uma forma pública de manipular seu próprio histórico diretamente.</p>
     */
    void registrarMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
    }

    /**
     * Indica se o aluno já foi aprovado em determinada disciplina, em
     * qualquer turma ou período letivo. Usado para impedir uma nova
     * matrícula em uma disciplina já concluída com aprovação.
     */
    boolean jaFoiAprovadoEm(Disciplina disciplina) {
        return matriculas.stream()
                .anyMatch(matricula ->
                        matricula.getOfertaDisciplina().getDisciplina().equals(disciplina)
                                && matricula.getResultado() == ResultadoAcademico.APROVADO);
    }

    private static String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail do aluno é obrigatório.");
        }
        if (!EMAIL_VALIDO.matcher(email).matches()) {
            throw new IllegalArgumentException("E-mail do aluno possui formato inválido: " + email);
        }
        return email;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "identificadorAcademico='" + identificadorAcademico + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
