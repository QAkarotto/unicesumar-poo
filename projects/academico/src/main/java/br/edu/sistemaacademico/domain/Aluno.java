package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Aluno {
    private final String identificadorAcademico;
    private String nome;
    private String email;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador acadêmico é obrigatório.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        validarEmail(email);
        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
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
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        this.nome = nome;
    }

    public void alterarEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    void adicionarMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
    }

    boolean possuiAprovacaoEm(Disciplina disciplina) {
        return matriculas.stream()
                .anyMatch(m -> m.getDisciplina().equals(disciplina)
                        && m.getResultado() == ResultadoAcademico.APROVADO);
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno)) return false;
        Aluno aluno = (Aluno) o;
        return identificadorAcademico.equals(aluno.identificadorAcademico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificadorAcademico);
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "identificadorAcademico='" + identificadorAcademico + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
