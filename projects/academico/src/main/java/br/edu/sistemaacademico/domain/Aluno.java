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
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.setEmail(email); // Reutiliza a validação do setter para garantir consistência
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("E-mail inválido. Informe um formato válido (ex: nome@dominio.com).");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return nome + " (RA: " + identificadorAcademico + ") - " + email;
    }
}