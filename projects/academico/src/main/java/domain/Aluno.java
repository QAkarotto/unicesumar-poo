package br.edu.sistemaacademico.domain;

public class Aluno {
    private final String identificadorAcademico;
    private String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador acadêmico não pode ser nulo ou vazio.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
    }

    private boolean isEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    // Getters
    public String getIdentificadorAcademico() { return identificadorAcademico; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }

    // Setters com validação
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    public void setEmail(String email) {
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "Aluno: " + identificadorAcademico + " - " + nome + " (" + email + ")";
    }
}