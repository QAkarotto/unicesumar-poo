package br.edu.sistemaacademico.domain;

public class Aluno {

    private final String identificadorAcademico;
    private final String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException("Identificador acadêmico não pode ser nulo ou vazio.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do aluno não pode ser nulo ou vazio.");
        }
        this.identificadorAcademico = identificadorAcademico.trim();
        this.nome = nome.trim();
        this.email = validarEmail(email);
    }

    public void setEmail(String email) {
        this.email = validarEmail(email);
    }

    private String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail do aluno não pode ser nulo ou vazio.");
        }
        String trimmed = email.trim();
        if (!trimmed.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("E-mail inválido: \"" + trimmed + "\".");
        }
        return trimmed;
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

    @Override
    public String toString() {
        return "Aluno{id='" + identificadorAcademico + "', nome='" + nome + "', email='" + email + "'}";
    }
}