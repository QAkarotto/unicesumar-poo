package br.edu.sistemaacademico.domain;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        this.identificadorAcademico = validarObrigatorio(
                identificadorAcademico,
                "Identificador acadêmico"
        );
        this.nome = validarObrigatorio(nome, "Nome");
        this.email = validarEmail(email);
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
        this.nome = validarObrigatorio(nome, "Nome");
    }

    public void alterarEmail(String email) {
        this.email = validarEmail(email);
    }

    private static String validarObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser nulo ou vazio."
            );
        }

        return valor;
    }

    private static String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "E-mail não pode ser nulo ou vazio."
            );
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }

        return email;
    }

    @Override
    public String toString() {
        // Até o Goku precisa manter seu estado válido antes de uma batalha.
        return "Aluno{" +
                "identificadorAcademico='" + identificadorAcademico + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}