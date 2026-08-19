package br.edu.sistemaacademico.domain;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        validarTexto(identificadorAcademico, "Identificador acadêmico");
        validarTexto(nome, "Nome");
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

    public String getEmail() {
        return email;
    }

    public void setNome(String nome) {
        validarTexto(nome, "Nome");
        this.nome = nome;
    }

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser vazio");
        }

        int arroba = email.indexOf("@");
        int ponto = email.lastIndexOf(".");

        if (arroba <= 0 || ponto <= arroba + 1 || ponto == email.length() - 1 || email.contains(" ")) {
            throw new IllegalArgumentException("E-mail inválido");
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
