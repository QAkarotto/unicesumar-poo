package br.edu.sistemaacademico.domain;

// O Goku também começou como aluno.
public class Aluno {
    

    private final String identificadorAcademico;
    private final String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException("O identificador acadêmico é obrigatório.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;

        setEmail(email);
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

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("O e-mail informado é inválido.");
        }

        this.email = email;
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