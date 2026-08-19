package br.edu.sistemaacademico.domain;

import java.util.regex.Pattern;

/**
 * Representa um aluno do sistema acadêmico.
 *
 * <p>O identificador acadêmico (RA) e o nome são definidos na criação do
 * aluno e não mudam depois, pois identificam a pessoa e não fazem sentido
 * como uma alteração trivial neste modelo. Já o e-mail pode ser corrigido
 * ou atualizado ao longo do tempo, então ele possui um método de alteração
 * próprio que reaplica as mesmas validações usadas na criação do objeto.</p>
 */
public class Aluno {

    private static final Pattern EMAIL_VALIDO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,}$");

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

    /**
     * Atualiza o e-mail do aluno.
     *
     * <p>O e-mail é validado antes de qualquer alteração de estado: se o
     * valor informado for nulo, vazio ou não tiver um formato minimamente
     * válido, uma exceção é lançada e o e-mail atual do aluno permanece
     * inalterado.</p>
     */
    public void setEmail(String novoEmail) {
        this.email = validarEmail(novoEmail);
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
