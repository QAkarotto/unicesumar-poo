package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    private final List<Matricula> historico = new ArrayList<>();

    public Aluno(String identificadorAcademico, String nome, String email) {

        if (identificadorAcademico == null || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException(
                    "Identificador acadêmico é obrigatório."
            );
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "Nome é obrigatório."
            );
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

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "Nome não pode ser vazio."
            );
        }

        this.nome = nome;
    }

    public void alterarEmail(String email) {

        validarEmail(email);
        this.email = email;
    }

    private void validarEmail(String email) {

        if (email == null ||
                email.isBlank() ||
                !email.contains("@")) {

            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }
    }

    public List<Matricula> getHistorico() {
        return List.copyOf(historico);
    }

    public void adicionarMatricula(Matricula matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "Matrícula é obrigatória."
            );
        }

        if (!historico.contains(matricula)) {
            historico.add(matricula);
        }
    }

    public boolean possuiAprovacaoEm(Disciplina disciplina) {

        for (Matricula matricula : historico) {

            if (matricula.getOfertaDisciplina()
                    .getDisciplina()
                    .equals(disciplina)
                    && matricula.getResultado() == ResultadoAcademico.APROVADO) {

                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {

        return "Aluno: " +
                identificadorAcademico +
                " - " +
                nome +
                " - " +
                email;
    }
}