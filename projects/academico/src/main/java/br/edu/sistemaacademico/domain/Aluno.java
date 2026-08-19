package br.edu.sistemaacademico.domain;

public class Aluno {

    // final = definido uma única vez, no construtor, e nunca mais muda.
    private final String identificadorAcademico;

    // Sem final: podem mudar, mas SEMPRE passando pelas regras da classe.
    private String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {
        this.identificadorAcademico = validarTexto(identificadorAcademico, "Identificador acadêmico");
        this.nome = validarTexto(nome, "Nome do aluno");
        this.email = validarEmail(email);
    }

    // ---------- Métodos de acesso ----------

    public String getIdentificadorAcademico() {
        return identificadorAcademico;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    // ---------- Métodos de alteração ----------

    public void setNome(String nome) {
        // Valida ANTES de atribuir: se lançar exceção, o valor antigo fica intacto.
        this.nome = validarTexto(nome, "Nome do aluno");
    }

    public void setEmail(String email) {
        this.email = validarEmail(email);
    }

    // ---------- Regras privadas ----------

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório e não pode ser vazio.");
        }
        return valor.trim();
    }

    private static String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório e não pode ser vazio.");
        }
        var limpo = email.trim();
        // Formato mínimo: algo + @ + algo + . + algo, sem espaços.
        if (!limpo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("E-mail inválido: " + limpo);
        }
        return limpo;
    }

    @Override
    public String toString() {
        return "Aluno [" + identificadorAcademico + "] " + nome + " <" + email + ">";
    }
}