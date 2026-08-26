package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    // final = definido uma única vez, no construtor, e nunca mais muda.
    private final String identificadorAcademico;

    // Sem final: podem mudar, mas SEMPRE passando pelas regras da classe.
    private String nome;
    private String email;

    // O aluno é o dono do próprio histórico acadêmico.
    private final List<Matricula> matriculas = new ArrayList<>();

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

    /*
     * Cópia protegida: quem consulta o histórico não consegue alterá-lo por fora.
     */
    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    // ---------- Métodos de alteração ----------

    public void setNome(String nome) {
        // Valida ANTES de atribuir: se lançar exceção, o valor antigo fica intacto.
        this.nome = validarTexto(nome, "Nome do aluno");
    }

    public void setEmail(String email) {
        this.email = validarEmail(email);
    }

    // ---------- Colaboração com a oferta ----------

    /*
     * Visibilidade de pacote: só as classes do domínio registram matrícula no aluno.
     * A matrícula sempre nasce a partir de uma OfertaDisciplina.
     */
    void registrarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("Matrícula é obrigatória.");
        }
        if (matricula.getAluno() != this) {
            throw new IllegalArgumentException("A matrícula pertence a outro aluno.");
        }
        if (matriculas.contains(matricula)) {
            throw new IllegalStateException("Matrícula já registrada no histórico do aluno.");
        }
        matriculas.add(matricula);
    }

    /*
     * O aluno é quem sabe responder se já foi aprovado em uma disciplina,
     * porque é ele quem guarda o histórico.
     */
    public boolean foiAprovadoEm(Disciplina disciplina) {
        if (disciplina == null) {
            return false;
        }
        return matriculas.stream()
                .anyMatch(matricula -> matricula.foiAprovadaEm(disciplina));
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