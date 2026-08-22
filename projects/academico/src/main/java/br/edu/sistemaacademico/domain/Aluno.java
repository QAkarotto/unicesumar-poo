package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Aluno {

    private final String identificadorAcademico;
    private final String nome;
    private String email;
    private final List<Matricula> matriculas;

    private static final Pattern PATTERN_EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Aluno(String identificadorAcademico, String nome, String email) {
        validarIdentificadorAcademico(identificadorAcademico);
        validarNome(nome);
        validarEmail(email);

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
        this.matriculas = new ArrayList<>();
    }

    private void validarIdentificadorAcademico(String identificadorAcademico) {
        if (identificadorAcademico == null || identificadorAcademico.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Identificador acadêmico não pode ser nulo ou vazio");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Nome do aluno não pode ser nulo ou vazio");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "E-mail do aluno não pode ser nulo ou vazio");
        }

        if (!PATTERN_EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException(
                    "E-mail fornecido possui formato inválido: " + email);
        }
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

    public void setEmail(String novoEmail) {
        validarEmail(novoEmail);
        this.email = novoEmail;
    }

    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    public void adicionarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException(
                    "Matrícula não pode ser nula");
        }

        matriculas.add(matricula);
    }

    public boolean jaFoiAprovadoEm(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina não pode ser nula");
        }

        for (Matricula matricula : matriculas) {
            if (matricula.getOfertaDisciplina()
                    .getDisciplina()
                    .getCodigo()
                    .equals(disciplina.getCodigo())
                    && matricula.getResultado()
                    == ResultadoAcademico.APROVADO) {

                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format(
                "Aluno{id='%s', nome='%s', email='%s'}",
                identificadorAcademico,
                nome,
                email
        );
    }
}