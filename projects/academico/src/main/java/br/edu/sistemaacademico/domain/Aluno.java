package br.edu.sistemaacademico.domain;

public class Aluno {

    private final String identificadorAcademico;
    private final String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        validarTexto(identificadorAcademico, "Identificador acadêmico");
        validarTexto(nome, "Nome");
        validarEmail(email);

        this.identificadorAcademico = identificadorAcademico.trim();
        this.nome = nome.trim();
        this.email = email.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email.trim();
    }

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser nulo ou vazio.");
        }
    }

    private static void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser nulo ou vazio.");
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
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