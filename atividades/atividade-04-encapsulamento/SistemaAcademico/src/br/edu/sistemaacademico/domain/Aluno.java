package br.edu.sistemaacademico.domain;

public class Aluno {
    private final String id;
    private final String nome;
    private String email;

    public Aluno(String id, String nome, String email) {
        validarId(id);
        validarNome(nome);
        validarEmail(email);
        
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    private void validarId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do aluno não pode ser vazio");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do aluno não pode ser vazio");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email do aluno não pode ser vazio");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void alterarEmail(String novoEmail) {
        validarEmail(novoEmail);
        this.email = novoEmail;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
