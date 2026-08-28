
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

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser vazio."
            );
        }
    }

    private static void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "E-mail não pode ser vazio."
            );
        }

        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
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
