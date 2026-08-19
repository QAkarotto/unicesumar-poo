package br.edu.sistemaacademico.domain;

public class Aluno {
    private String ra;
    private String nome;
    private String email;
    public Aluno(String ra, String nome, String email) {
        if (ra == null || ra.equals("")) {
            throw new IllegalArgumentException("O RA é obrigatório.");
        }
        if (nome == null || nome.equals("")) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        validarEmail(email);
        this.ra = ra;
        this.nome = nome;
        this.email = email;
    }
    private void validarEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }
    public String getRa() {
        return this.ra;
    }
    public String getNome() {
        return this.nome;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }
    @Override
    public String toString() {
        return this.ra + " - " + this.nome + " - " + this.email;
    }
}