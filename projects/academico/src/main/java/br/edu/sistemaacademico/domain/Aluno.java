package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {

    private String identificador;
    private String nome;
    private String email;

    private List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificador, String nome, String email) {

        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException(
                    "Identificador do aluno é obrigatório"
            );
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "Nome do aluno é obrigatório"
            );
        }

        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException(
                    "Email do aluno é inválido"
            );
        }

        this.identificador = identificador;
    private final String ra;
    private final String nome;
    private String email;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String ra, String nome, String email) {
        this.ra = ra;
        this.nome = nome;
        this.email = email;
    }

    public String getIdentificador() {
        return identificador;
    public String getRa() {
        return ra;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void adicionarMatricula(Matricula matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "Matrícula é obrigatória"
            );
        }

        if (!matriculas.contains(matricula)) {
            matriculas.add(matricula);
        }
    }

    public List<Matricula> getMatriculas() {
        return new ArrayList<>(matriculas);
    }

    public boolean jaFoiAprovadoNaDisciplina(Disciplina disciplina) {

        for (Matricula matricula : matriculas) {

            if (matricula.getOferta().getDisciplina().equals(disciplina)
                    && matricula.getResultado() == ResultadoAcademico.APROVADO) {
                return true;
            }
        }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }


    void registrarMatricula(Matricula matricula) {
        matriculas.add(matricula);
    }

    boolean jaFoiAprovadoEm(Disciplina disciplina) {
        for (Matricula matricula : matriculas) {
            boolean mesmaDisciplina = matricula.getOferta().getDisciplina().equals(disciplina);
            boolean aprovado = matricula.getResultado() == ResultadoAcademico.APROVADO;
            if (mesmaDisciplina && aprovado) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return nome;
    }
}
        return nome + " (" + ra + ")";
    }
}
