package br.edu.sistemaacademico.domain;

public class Aluno {

    private final String identificador;
    private String nome;
    private String email;

    public Aluno(String identificador, String nome, String email) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("ID do aluno é obrigatório");
        }
        this.identificador = identificador;
        setNome(nome);
        setEmail(email);
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Forneça um email válido por gentileza.");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return  nome + " (RA: " + identificador + ", email: " + email + ")";
    }
}
