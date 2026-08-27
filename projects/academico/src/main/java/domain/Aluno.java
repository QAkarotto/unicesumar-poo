package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    /**
     * Histórico de matrículas do aluno. É o próprio Aluno quem mantém
     * seu histórico e quem sabe responder se já foi aprovado em uma
     * determinada disciplina — essa informação não deve "vazar" para
     * outras classes decidirem sozinhas.
     */
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador acadêmico não pode ser nulo ou vazio.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
    }

    private boolean isEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    // Getters
    public String getIdentificadorAcademico() { return identificadorAcademico; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }

    // Setters com validação
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    public void setEmail(String email) {
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = email;
    }

    /**
     * Retorna o histórico de matrículas do aluno (somente leitura).
     */
    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    /**
     * Chamado por OfertaDisciplina no momento em que uma nova matrícula
     * é criada para este aluno, para que o histórico permaneça
     * consistente e centralizado no próprio Aluno.
     */
    void registrarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("A matrícula não pode ser nula.");
        }
        this.matriculas.add(matricula);
    }

    /**
     * Verifica, com base no próprio histórico, se o aluno já foi
     * aprovado em determinada disciplina (em qualquer turma ou
     * período letivo). Quem detém essa resposta é o Aluno, pois é ele
     * quem conhece seu histórico completo.
     */
    public boolean jaAprovadoEm(Disciplina disciplina) {
        if (disciplina == null) {
            return false;
        }
        return matriculas.stream()
                .anyMatch(m -> m.getResultado() == ResultadoAcademico.APROVADO
                        && disciplina.equals(m.getOfertaDisciplina().getDisciplina()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno aluno)) return false;
        return identificadorAcademico.equals(aluno.identificadorAcademico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificadorAcademico);
    }

    @Override
    public String toString() {
        return "Aluno: " + identificadorAcademico + " - " + nome + " (" + email + ")";
    }
}
