package br.edu.sistemaacademico.domain;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        validarTexto(identificadorAcademico, "O identificador acadêmico é obrigatório.");
        validarTexto(nome, "O nome do aluno é obrigatório.");
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

    public void alterarNome(String nome) {
        validarTexto(nome, "O nome do aluno é obrigatório.");
        this.nome = nome;
    }

    public void alterarEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    private void validarTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
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
        return "Aluno{" +
                "identificadorAcademico='" + identificadorAcademico + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}