package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private String identificadorAcademico;
    private String nome;
    private String email;
    private List<Matricula> matriculas;

    public Aluno(
            String identificadorAcademico,
            String nome,
            String email
    ) {

        if (identificadorAcademico == null
                || identificadorAcademico.isBlank()) {

            throw new IllegalArgumentException(
                    "Identificador acadêmico é obrigatório."
            );
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "Nome é obrigatório."
            );
        }

        if (email == null
                || email.isBlank()
                || !email.contains("@")) {

            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
        this.matriculas = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        if (email == null
                || email.isBlank()
                || !email.contains("@")) {

            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }

        this.email = email;
    }

    public List<Matricula> getMatriculas() {
        return new ArrayList<>(matriculas);
    }

    public void adicionarMatricula(Matricula matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "Matrícula é obrigatória."
            );
        }

        matriculas.add(matricula);
    }

    public boolean jaFoiAprovado(Disciplina disciplina) {

        for (Matricula matricula : matriculas) {

            boolean mesmaDisciplina =
                    matricula.getOferta()
                            .getDisciplina()
                            .getCodigo()
                            .equals(disciplina.getCodigo());

            boolean aprovado =
                    matricula.getResultado()
                            == ResultadoAcademico.APROVADO;

            if (mesmaDisciplina && aprovado) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return identificadorAcademico
                + " "
                + nome
                + " "
                + email;
    }
}