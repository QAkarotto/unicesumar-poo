package br.edu.sistemaacademico.domain;

import java.util.regex.Pattern;


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
