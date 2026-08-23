package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    private final List<Matricula> matriculas;

    public Aluno(
            String identificadorAcademico,
            String nome,
            String email
    ) {
        if (identificadorAcademico == null
                || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException(
                    "Identificador acadêmico é obrigatório."
            );
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "Nome é obrigatório."
            );
        }

        if (email == null || email.isBlank()
                || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
        this.matriculas = new ArrayList<>();
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
        if (email == null || email.isBlank()
                || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }

        this.email = email;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public void adicionarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException(
                    "Matrícula é obrigatória."
            );
        }

        if (!matriculas.contains(matricula)) {
            matriculas.add(matricula);
        }
    }

    public boolean foiAprovadoNaDisciplina(
            Disciplina disciplina
    ) {
        if (disciplina == null) {
            return false;
        }

        return matriculas.stream()
                .anyMatch(matricula ->
                        matricula.foiAprovada()
                                && matricula
                                .getOfertaDisciplina()
                                .getDisciplina()
                                .equals(disciplina)
                );
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Aluno)) {
            return false;
        }

        Aluno outro = (Aluno) objeto;

        return identificadorAcademico.equals(
                outro.identificadorAcademico
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificadorAcademico);
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