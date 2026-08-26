package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private final String identificador;
    private final String nome;
    private String email;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificador, String nome, String email) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("O identificador acadêmico é obrigatório.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }

        validarEmail(email);

        this.identificador = identificador;
        this.nome = nome;
        this.email = email;
    }

    public String getIdentificador() {
        return identificador;
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


    // Usado pela OfertaDisciplina ao concluir uma matrícula com sucesso, o aluno é quem mantém seu próprio histórico.

    void registrarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("A matrícula é obrigatória.");
        }

        matriculas.add(matricula);
    }

    public List<Matricula> getMatriculas() {
        List<Matricula> copia = new ArrayList<>();

        for (Matricula matricula : matriculas) {
            copia.add(matricula);
        }

        return copia;
    }


    // O aluno é quem tem as informações necessárias para responder se já foi aprovado em determinada disciplina.

    public boolean foiAprovadoEm(Disciplina disciplina) {
        for (Matricula matricula : matriculas) {
            boolean mesmaDisciplina = matricula.getOferta()
                    .getDisciplina()
                    .equals(disciplina);

            boolean aprovado = matricula.getResultado() == ResultadoAcademico.APROVADO;

            if (mesmaDisciplina && aprovado) {
                return true;
            }
        }

        return false;
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("O e-mail informado é inválido.");
        }
    }

    @Override
    public String toString() {
        return "Identificador: " + getIdentificador() +
                "\nNome: " + getNome() +
                "\nE-mail: " + getEmail();
    }
}