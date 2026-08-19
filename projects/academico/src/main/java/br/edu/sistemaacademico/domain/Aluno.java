package br.edu.sistemaacademico.domain;

public class Aluno {
    private final String identificador;
    private String nome;
    private String email;

    public Aluno(String identificador, String nome, String email) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("Identificador acadêmico é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do aluno é obrigatório.");
        }

        this.identificador = identificador.trim();
        this.nome = nome.trim();
        this.setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser vazio.");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
        this.email = email.trim();
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", identificador, nome, email);
    }
}