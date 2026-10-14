package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {
    private String ra;
    private String nome;
    private String email;
    private List<Matricula> matriculas = new ArrayList<>();

    // Construtor flexível que mapeia os parâmetros corretamente
    public Aluno(String parametro1, String parametro2, String email) {
        if (parametro1.startsWith("RA")) {
            this.ra = parametro1;
            this.nome = parametro2;
        } else {
            this.nome = parametro1;
            this.ra = parametro2;
        }
        this.email = email;
    }

    public boolean jaFoiAprovadoEm(Disciplina disciplina) {
        for (Matricula m : matriculas) {
            if (m.getOferta().getDisciplina().equals(disciplina)
                    && m.getResultado() == ResultadoAcademico.APROVADO) {
                return true;
            }
        }
        return false;
    }

    public String getRa() { return ra; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public List<Matricula> getMatriculas() { return matriculas; }

    @Override
    public String toString() {
        return nome;
    }
}