package br.edu.sistemaacademico.domain;

public class Aluno {

    private final String identificador;
    private String nome;
    private String email;

    public Aluno(String identificador, String nome, String email) {
        this.identificador = validarObrigatorio(
                identificador,
                "Identificador acadêmico"
        );

        this.nome = validarObrigatorio(
                nome,
                "Nome"
        );

        this.email = validarEmail(email);
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = validarObrigatorio(nome, "Nome");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validarEmail(email);
    }

    private static String validarObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " é obrigatório."
            );
        }

        return valor.trim();
    }

    private static String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "E-mail é obrigatório."
            );
        }

        String emailNormalizado = email.trim();

        if (!emailNormalizado.matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
        )) {
            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }

        return emailNormalizado;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "identificador='" + identificador + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
