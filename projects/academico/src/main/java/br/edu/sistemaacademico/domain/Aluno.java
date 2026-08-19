package br.edu.sistemaacademico.domain;

public class Aluno {

    private String identificadorAcademico;
    private String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        setIdentificadorAcademico(identificadorAcademico);
        setNome(nome);
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser nulo ou vazio.");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        this.email = email;
    }

    private void setIdentificadorAcademico(String identificadorAcademico) {
        if (identificadorAcademico == null || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException(
                    "Identificador acadêmico não pode ser nulo ou vazio."
            );
        }

        this.identificadorAcademico = identificadorAcademico;
    }

    private void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "Nome não pode ser nulo ou vazio."
            );
        }

        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "identificadorAcademico='" + identificadorAcademico + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}