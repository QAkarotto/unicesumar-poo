package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Aluno {
    private String ra;
    private String nome;
    private String email;
    private List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String parametro1, String parametro2, String email) {
        if (ehRa(parametro1)) {
            this.ra = parametro1;
            this.nome = parametro2;
        } else if (ehRa(parametro2)) {
            this.nome = parametro1;
            this.ra = parametro2;
        } else {
            throw new IllegalArgumentException("RA inválido: deve começar com \"RA\"");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do aluno é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail do aluno é obrigatório");
        }
        this.email = email;
    }

    private static boolean ehRa(String valor) {
        return valor != null && valor.startsWith("RA") && valor.length() > 2;
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
    @JsonIgnore public List<Matricula> getMatriculas() { return matriculas; }

    @Override
    public String toString() {
        return nome;
    }
}