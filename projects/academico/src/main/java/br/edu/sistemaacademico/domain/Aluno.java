package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Aluno {
    private final String ra;
    private final String nome;
    private final String email;
    private final List<Matricula> matriculas;

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

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public boolean jaFoiAprovadoEm(Disciplina disciplina) {
        if (disciplina == null) return false;
        return matriculas.stream()
                .anyMatch(m -> m.getOfertaDisciplina().getDisciplina().equals(disciplina)
                        && m.getResultado() == ResultadoAcademico.APROVADO);
    }

    public void registrarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("Matrícula não pode ser nula.");
        }
        this.matriculas.add(matricula);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno aluno)) return false;
        return Objects.equals(ra, aluno.ra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ra);
    }

    @Override
    public String toString() {
        return ra + " - " + nome;
    }
}