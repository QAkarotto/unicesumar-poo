package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(
            String identificadorAcademico,
            String nome,
            String email
    ) {
        this.identificadorAcademico =
                validarObrigatorio(
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

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    void adicionarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException(
                    "A matrícula não pode ser nula."
            );
        }

        matriculas.add(matricula);
    }

    public boolean possuiAprovacao(Disciplina disciplina) {
        if (disciplina == null) {
            return false;
        }

        return matriculas.stream()
                .anyMatch(matricula ->
                        matricula.getOfertaDisciplina()
                                .getDisciplina()
                                .equals(disciplina)
                                && matricula.getResultado()
                                == ResultadoAcademico.APROVADO
                );
    }

    private String validarObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser vazio."
            );
        }

        return valor.trim();
    }

    private String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "O e-mail não pode ser vazio."
            );
        }

        String emailNormalizado = email.trim();

        if (!emailNormalizado.matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
        )) {
            throw new IllegalArgumentException(
                    "O e-mail possui formato inválido."
            );
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