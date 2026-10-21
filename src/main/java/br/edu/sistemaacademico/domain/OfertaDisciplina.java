package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;

    private final List<Matricula> matriculas =
            new ArrayList<>();

    public OfertaDisciplina(
            Disciplina disciplina,
            Turma turma) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina é obrigatória.");
        }

        if (turma == null) {
            throw new IllegalArgumentException(
                    "A turma é obrigatória.");
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
                    "O aluno é obrigatório.");
        }

        for (Matricula matricula : matriculas) {

            if (matricula.getAluno().equals(aluno)) {
                throw new IllegalArgumentException(
                        "O aluno já está matriculado nesta oferta.");
            }
        }

        if (aluno.possuiAprovacaoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno já foi aprovado nesta disciplina.");
        }

        Matricula matricula =
                new Matricula(aluno, this);

        matriculas.add(matricula);

        aluno.adicionarAoHistorico(matricula);

        return matricula;
    }

    @Override
    public String toString() {
        return turma.getCodigo()
                + " - "
                + disciplina;
    }
}
