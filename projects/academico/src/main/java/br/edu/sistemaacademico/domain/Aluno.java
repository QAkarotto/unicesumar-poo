package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
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
        return nome + " (" + ra + ")";
    }
}