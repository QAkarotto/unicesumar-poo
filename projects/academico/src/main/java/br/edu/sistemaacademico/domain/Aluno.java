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
        alterarEmail(email);
    }

    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    void validarNovaMatricula(OfertaDisciplina oferta) {
        boolean jaAprovado = matriculas.stream()
                .anyMatch(matricula -> matricula.foiAprovadoEm(oferta.getDisciplina()));

        if (jaAprovado) {
            throw new IllegalStateException(
                    "O aluno já foi aprovado nesta disciplina."
            );
        }
    }

    void registrarMatricula(Matricula matricula) {
        matriculas.add(matricula);
    }

    private void alterarEmail(String email) {
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("O e-mail do aluno é inválido.");
        }
        this.email = email.trim();
    }

    private static String validarTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor.trim();
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Aluno aluno)) {
            return false;
        }
        return registroAcademico.equals(aluno.registroAcademico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registroAcademico);
    }

    @Override
    public String toString() {
        return registroAcademico + " - " + nome;
    }
}
