package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {
    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma é obrigatória.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória.");
        }
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório.");
        }

        boolean jaMatriculado = matriculas.stream()
                .anyMatch(m -> m.getAluno().equals(aluno));
        if (jaMatriculado) {
            throw new IllegalStateException(
                    "Aluno já está matriculado nesta oferta.");
        }

        if (aluno.possuiAprovacaoEm(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado em " + disciplina.getNome()
                            + " e não pode se matricular novamente.");
        }

        Matricula matricula = new Matricula(aluno, this);
        matriculas.add(matricula);
        aluno.adicionarMatricula(matricula);
        return matricula;
    }

    @Override
    public String toString() {
        return disciplina.toString();
    }
}