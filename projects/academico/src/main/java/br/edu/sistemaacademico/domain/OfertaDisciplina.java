package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Disciplina disciplina, Turma turma) {
        this.disciplina = disciplina;
        this.turma = turma;
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

    public Matricula matricular(Aluno aluno) {
        if (aluno.jaFoiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado em " + disciplina.getNome() + " e não pode se matricular novamente."
            );
        }

        boolean jaMatriculado = matriculas.stream()
                .anyMatch(m -> m.getAluno().equals(aluno));
        if (jaMatriculado) {
            throw new IllegalArgumentException(
                    "Aluno já possui matrícula nesta oferta de " + disciplina.getNome() + "."
            );
        }

        Matricula matricula = new Matricula(aluno, this);
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
    }

    @Override
    public String toString() {
        return disciplina.getNome() + " - " + turma.getCodigo();
    }
}