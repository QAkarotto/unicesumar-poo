package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Aluno {

    private final String identificadorAcademico;
    private final String nome;
    private String email;

    private final List<Matricula> historico = new ArrayList<>();

    private static final Pattern EMAIL_VALIDO =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public Aluno(String identificadorAcademico, String nome, String email) {
        this.identificadorAcademico = validarObrigatorio(
                identificadorAcademico,
                "O identificador acadêmico é obrigatório.");

        this.nome = validarObrigatorio(
                nome,
                "O nome do aluno é obrigatório.");

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

    public void alterarEmail(String novoEmail) {
        this.email = validarEmail(novoEmail);
    }

    public List<Matricula> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    public void adicionarAoHistorico(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException(
                    "A matrícula não pode ser nula.");
        }

        if (matricula.getAluno() != this) {
            throw new IllegalArgumentException(
                    "A matrícula não pertence a este aluno.");
        }

        if (!historico.contains(matricula)) {
            historico.add(matricula);
        }
    }

    public boolean possuiAprovacaoEm(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula.");
        }

        return historico.stream()
                .anyMatch(matricula ->
                        matricula.getResultado() == Resultado.APROVADO
                                && matricula.getOfertaDisciplina()
                                .getDisciplina()
                                .equals(disciplina));
    }

    private static String validarObrigatorio(
            String valor,
            String mensagem) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensagem);
        }

        return valor.trim();
    }

    private static String validarEmail(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O e-mail é obrigatório.");
        }

        String emailNormalizado = email.trim();

        if (!EMAIL_VALIDO.matcher(emailNormalizado).matches()) {
            throw new IllegalArgumentException(
                    "O e-mail informado é inválido.");
        }

        return emailNormalizado;
    }

    @Override
    public String toString() {
        return identificadorAcademico
                + " - "
                + nome
                + " - "
                + email;
    }
}