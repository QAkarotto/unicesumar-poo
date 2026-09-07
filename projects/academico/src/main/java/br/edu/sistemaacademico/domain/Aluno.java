package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Aluno {
    private final String registroAcademico;
    private final String nome;
    private String email;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String registroAcademico, String nome, String email) {
        this.registroAcademico = validarTexto(
                registroAcademico,
                "O registro acadêmico é obrigatório."
        );
        this.nome = validarTexto(nome, "O nome do aluno é obrigatório.");
        alterarEmail(email);
    }

    public String getRegistroAcademico() {
        return registroAcademico;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    void adicionarMatriculaAoHistorico(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException(
                    "Matrícula não pode ser nula."
            );
        }

        historico.add(matricula);
    }

    public List<Matricula> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    public boolean jaFoiAprovadoEm(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina não pode ser nula."
            );
        }

        return historico.stream()
                .anyMatch(matricula ->
                        matricula.getOfertaDisciplina()
                                .getDisciplina()
                                .equals(disciplina)
                                && matricula.getResultado() == Resultado.APROVADO
                );
    }

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser vazio."
            );
        }
    }

    private static void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "E-mail não pode ser vazio."
            );
        }

        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }
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
