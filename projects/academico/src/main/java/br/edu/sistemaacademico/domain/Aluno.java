package br.edu.sistemaacademico.domain;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

// Assim como Goku protege seu poder, o objeto protege seu estado.

    public Aluno(String identificadorAcademico, String nome, String email) {

        if (identificadorAcademico == null || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException("Identificador acadêmico é obrigatório.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }

        if (email == null || email.isBlank()
                || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        if (email == null || email.isBlank()
                || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        this.email = email;
    }

    @Override
    public String toString() {
        return identificadorAcademico + " " + nome + " " + email;
    }
}