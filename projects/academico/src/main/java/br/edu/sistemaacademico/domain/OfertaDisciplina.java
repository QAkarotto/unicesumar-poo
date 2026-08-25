package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;

    private final List<Matricula> matriculas =
            new ArrayList<>();

    OfertaDisciplina(
            Disciplina disciplina,
            Turma turma
    ) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula."
            );
        }

        if (turma == null) {
            throw new IllegalArgumentException(
                    "A turma não pode ser nula."
            );
        }

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

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno não pode ser nulo."
            );
        }

        if (aluno.possuiAprovacao(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já aprovado nesta disciplina."
            );
        }

        for (Matricula matricula : matriculas) {
            if (matricula.getAluno().equals(aluno)) {
                throw new IllegalStateException(
                        "Aluno já possui matrícula nesta oferta."
                );
            }
        }

        Matricula matricula =
                new Matricula(aluno, this);

        matriculas.add(matricula);
        aluno.adicionarMatricula(matricula);

        return matricula;
    }

    @Override
    public String toString() {
        return disciplina.getCodigo()
                + " - "
                + disciplina.getNome()
                + " - Turma "
                + turma.getCodigo();
    }
}