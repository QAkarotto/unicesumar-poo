package br.edu.sistemaacademico.domain;

/**
 * Representa um aluno do sistema acadêmico.
 *
 * Identificador acadêmico e nome são definidos na criação e não mudam
 * durante a vida do objeto (não fazem sentido mudar sem virar "outro aluno").
 * O e-mail é a única informação que pode ser alterada, mas sempre passando
 * pela mesma validação usada na criação.
 */
public class Aluno {

    private final String identificadorAcademico;
    private final String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException("Identificador acadêmico é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do aluno é obrigatório.");
        }

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        // Reaproveita a validação do setter também na criação,
        // assim a regra de e-mail válido existe em um único lugar.
        this.setEmail(email);
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

    /**
     * Altera o e-mail do aluno. Se o valor informado for inválido,
     * o e-mail atual é mantido (o objeto nunca fica inconsistente).
     */
    public void setEmail(String novoEmail) {
        if (!isEmailValido(novoEmail)) {
            throw new IllegalArgumentException("E-mail inválido: " + novoEmail);
        }
        this.email = novoEmail;
    }

    private boolean isEmailValido(String candidato) {
        if (candidato == null || candidato.isBlank()) {
            return false;
        }
        // Validação "minimamente válida": algo@algo.algo
        return candidato.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
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
