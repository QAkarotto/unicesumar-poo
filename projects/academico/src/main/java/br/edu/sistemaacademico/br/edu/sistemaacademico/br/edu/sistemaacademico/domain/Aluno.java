package br.edu.sistemaacademico.domain;

public class Aluno {

    private final String identificador;
    private final String nome;
    private String email;

    public Aluno(String identificador, String nome, String email) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("O identificador acadêmico é obrigatório.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }

        validarEmail(email);

        this.identificador = identificador;
        this.nome = nome;
        this.email = email;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("O e-mail informado é inválido.");
        }
    }

    @Override
    public String toString() {
        return "Identificador: " + identificador +
                "\nNome: " + nome +
                "\nE-mail: " + email;
    }
}