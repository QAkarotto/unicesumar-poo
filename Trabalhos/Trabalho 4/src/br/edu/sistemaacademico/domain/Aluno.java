package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {
    private String nome;
    private String id;
    private String email;
    private List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String nome, String id, String email) {
        this.nome = nome;
        this.id = id;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void adicionarMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
    }

    public boolean jaFoiAprovadoEm(Disciplina disciplinaProcurada) {
        for (Matricula matricula : matriculas) {
            // Comparando String de forma tradicional com .equals()
            if (matricula.getOferta().getDisciplina().equals(disciplinaProcurada)
                    && matricula.getResultado().equals(ResultadoAcademico.APROVADO)) {
                return true;
            }
        }
        return false;
    }
}