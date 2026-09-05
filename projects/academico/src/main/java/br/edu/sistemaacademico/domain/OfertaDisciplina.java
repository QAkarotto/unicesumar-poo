package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Disciplina disciplina, Turma turma) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma é obrigatória.");
        }
        this.disciplina = disciplina;
        this.turma = turma;
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório.");
        }

        var jaMatriculado = matriculas.stream()
                .anyMatch(matricula -> matricula.getAluno().equals(aluno));
        if (jaMatriculado) {
            throw new IllegalStateException("Aluno já matriculado nesta oferta.");
        }

        if (aluno.jaFoiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado em " + disciplina + " e não pode se matricular novamente.");
        }

        var matricula = new Matricula(aluno, this);
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    @Override
    public String toString() {
        return disciplina + " (" + turma.getCodigo() + ")";
    }
}