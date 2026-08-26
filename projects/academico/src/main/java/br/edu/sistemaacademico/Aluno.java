package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {
    private final String identificador;
    private final String nome;
    private String email;
    private final List<Matricula> matriculas;

    public Aluno(String identificador, String nome, String email) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("O identificador não pode ser nulo ou vazio.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }

        this.identificador = identificador;
        this.nome = nome;
        this.matriculas = new ArrayList<>();
        this.setEmail(email);
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
        this.email = email;
    }

    void adicionarMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
    }

    public boolean isAprovadoEm(Disciplina disciplina) {
        return matriculas.stream()
                .anyMatch(m -> m.getOfertaDisciplina().getDisciplina().equals(disciplina)
                        && m.getResultado() == ResultadoAcademico.APROVADO);
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return String.format("Aluno[%s - %s]", identificador, nome);
    } // João Pedro Hulchak Kazmierzak RA: 25141620-2 e Hiuri Luciano dos Santos RA: 25208360-2
}