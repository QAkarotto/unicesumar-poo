package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {

    private final String ra;
    private final String nome;
    private String email;

    private final List<Matricula> matriculas;

    public Aluno(String ra, String nome, String email) {
        validarTexto(ra, "RA");
        validarTexto(nome, "Nome");
        validarTexto(email, "E-mail");

        this.ra = ra;
        this.nome = nome;
        this.email = email;
        this.matriculas = new ArrayList<>();
    }

    public String getRa() {
        return ra;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validarTexto(email, "E-mail");
        this.email = email;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public boolean possuiAprovacaoEm(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula."
            );
        }

        for (Matricula matricula : matriculas) {
            if (matricula.getDisciplina().getCodigo()
                    .equals(disciplina.getCodigo())
                    && matricula.getResultado()
                    == ResultadoAcademico.APROVADO) {

                return true;
            }
        }

        return false;
    }

    public void adicionarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException(
                    "A matrícula não pode ser nula."
            );
        }

        if (matricula.getAluno() != this) {
            throw new IllegalArgumentException(
                    "A matrícula não pertence a este aluno."
            );
        }

        matriculas.add(matricula);
    }

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser nulo ou vazio."
            );
        }
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "ra='" + ra + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
