package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {

    private final String ra;
    private final String nome;
    private final String email;

    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String ra, String nome, String email) {
        if (ra == null || ra.isBlank()) {
            throw new IllegalArgumentException("RA é obrigatório.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }

        this.ra = ra;
        this.nome = nome;
        this.email = email;
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

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public void adicionarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("Matrícula não pode ser nula.");
        }

        if (!matriculas.contains(matricula)) {
            matriculas.add(matricula);
        }
    }

    public boolean possuiAprovacaoNaDisciplina(Disciplina disciplina) {
        return matriculas.stream()
                .anyMatch(matricula ->
                        matricula.getOferta().getDisciplina().equals(disciplina)
                                && matricula.getResultado() == ResultadoAcademico.APROVADO
                );
    }

    @Override
    public String toString() {
        return nome + " (" + ra + ")";
    }
}