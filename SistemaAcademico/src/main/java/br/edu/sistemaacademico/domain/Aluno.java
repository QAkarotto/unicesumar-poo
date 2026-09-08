package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Aluno {

    private final String registroAcademico;
    private final String nome;
    private final String email;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String registroAcademico, String nome, String email) {
        if (registroAcademico == null || registroAcademico.trim().isEmpty()) {
            throw new IllegalArgumentException("O registro acadêmico é obrigatório.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("O e-mail do aluno é inválido.");
        }
        this.registroAcademico = registroAcademico.trim();
        this.nome = nome.trim();
        this.email = email.trim();
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

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public boolean foiAprovadoEm(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        for (Matricula matricula : matriculas) {
            if (matricula.refereSeA(disciplina) && matricula.isAprovada()) {
                return true;
            }
        }
        return false;
    }

    public List<Matricula> getMatriculasEmCurso() {
        List<Matricula> emCurso = new ArrayList<>();
        for (Matricula matricula : matriculas) {
            if (matricula.isEmCurso()) {
                emCurso.add(matricula);
            }
        }
        return Collections.unmodifiableList(emCurso);
    }

    void registrarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("A matrícula é obrigatória.");
        }
        if (!this.equals(matricula.getAluno())) {
            throw new IllegalStateException("A matrícula não pertence a este aluno.");
        }
        matriculas.add(matricula);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Aluno)) {
            return false;
        }
        Aluno outro = (Aluno) obj;
        return registroAcademico.equalsIgnoreCase(outro.registroAcademico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registroAcademico.toLowerCase());
    }

    @Override
    public String toString() {
        return registroAcademico + " - " + nome;
    }
}