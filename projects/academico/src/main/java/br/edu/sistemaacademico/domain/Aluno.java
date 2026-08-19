package br.edu.sistemaacademico.domain;

public class Aluno {
    private final String identificadorAcademico;
    private String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador acadômico é obrigatório.");
        }
        validarNome(nome);
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

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !email.contains("@") || email.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }

    @Override
    public String toString() {
        return "Aluno [RA/ID=" + identificadorAcademico + ", Nome=" + nome + ", Email=" + email + "]";
    }
}