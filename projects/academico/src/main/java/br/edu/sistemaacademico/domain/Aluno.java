package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private final String ra;
    private final String nome;
    private String email;

    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String ra, String nome, String email) {
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

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        this.email = email;
    }

    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    void registrarMatricula(Matricula matricula) {
        matriculas.add(matricula);
    }

    boolean jaFoiAprovado(Disciplina disciplina) {
        return matriculas.stream()
                .anyMatch(matricula ->
                        matricula.getDisciplina().equals(disciplina)
                                && matricula.getResultado()
                                == ResultadoAcademico.APROVADO
                );
    }

    @Override
    public String toString() {
        return ra + " - " + nome;
    }
}