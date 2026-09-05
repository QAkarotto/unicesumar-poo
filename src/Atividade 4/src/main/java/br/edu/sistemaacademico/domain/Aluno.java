package br.edu.sistemaacademico.domain;

public class Aluno {
    private final String identificadorAcademico;
    private final String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException("Identificador acadêmico não pode ser vazio");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        validarEmail(email);

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
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
        validarEmail(novoEmail);
        this.email = novoEmail;
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser vazio");
        }
        if (!email.contains("@") || email.indexOf("@") == 0 || email.indexOf("@") == email.length() - 1) {
            throw new IllegalArgumentException("E-mail deve ter um formato válido");
        }
    }

    @Override
    public String toString() {
        return String.format("Aluno(RA=%s, nome=%s, email=%s)", identificadorAcademico, nome, email);
    }
}
